package zifnab.hdf;

import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

final class Parser
{
  @Nonnull
  private final DataDocument _document = new DataDocument();
  @Nonnull
  private final String _location;
  @Nonnull
  private final LineNumberReader _reader;
  /**
   * The parent node if any.
   */
  @Nullable
  private DataElement _parentNode;
  /**
   * The last line parsed.
   */
  @Nullable
  private DataNode _lastNode;
  /**
   * The current indent level. This is the number of tabs that previous line
   * contained. The indent level can increase by 1 per line or decrease by any
   * amount (as long as it remains positive).
   */
  private int _indent = 0;
  /**
   * The current column in current line. It is automatically increased each time a character is read.
   */
  private int _column;
  /**
   * The current line being parsed.
   */
  private String _line;

  @Nonnull
  Parser( @Nonnull final String location, @Nonnull final LineNumberReader reader )
    throws IOException, DataParseException
  {
    _location = Objects.requireNonNull( location );
    _reader = Objects.requireNonNull( reader );
    parseDocument();
  }

  @Nonnull
  DataDocument getDocument()
  {
    return _document;
  }

  private void parseDocument()
    throws IOException, DataParseException
  {
    while ( null != ( _line = readLine() ) )
    {
      parseLine();
    }
  }

  private void parseLine()
    throws DataParseException
  {
    if ( !_line.isEmpty() )
    {
      _column = 0;
      parseIndentation();
      if ( _line.length() <= _column )
      {
        return;
      }
      else if ( '#' == _line.charAt( _column ) )
      {
        final SourceLocation location = newLocation();
        final String comment;
        if ( _line.length() > _column + 1 && ' ' == _line.charAt( _column + 1 ) )
        {
          comment = _line.substring( _column + 2 );
        }
        else
        {
          comment = _line.substring( _column + 1 );
        }
        final DataComment node = new DataComment( location, _parentNode, comment );
        _lastNode = node;
        if ( null == _parentNode )
        {
          _document.append( node );
        }
      }
      else
      {
        final SourceLocation location = newLocation();
        final List<String> tokens = parseTokens();
        final DataElement node = new DataElement( location, _parentNode, tokens );
        _lastNode = node;
        if ( null == _parentNode )
        {
          _document.append( node );
        }
      }
    }
  }

  @Nonnull
  private List<String> parseTokens()
    throws DataParseException
  {
    final List<String> tokens = new ArrayList<>();
    String token;
    while ( null != ( token = parseToken() ) )
    {
      tokens.add( token );
    }

    return tokens;
  }

  @Nullable
  private String parseToken()
    throws DataParseException
  {
    stripWhiteSpace();
    if ( _line.length() == _column )
    {
      return null;
    }
    else if ( '`' == _line.charAt( _column ) )
    {
      return parseBackTickedToken();
    }
    else if ( '"' == _line.charAt( _column ) )
    {
      return parseQuotedToken();
    }
    else
    {
      return parseRawToken();
    }
  }

  @Nonnull
  private String parseBackTickedToken()
    throws DataParseException
  {
    assert '`' == _line.charAt( _column );

    // Skip the quote
    _column++;

    final StringBuilder sb = new StringBuilder();

    while ( _line.length() > _column && '`' != _line.charAt( _column ) )
    {
      sb.append( _line.charAt( _column ) );
      _column++;
    }

    if ( _line.length() == _column )
    {
      throw newDataParseException( "Quoted token missing closing `" );
    }

    // Skip the quote
    _column++;

    return sb.toString();
  }

  @Nonnull
  private String parseQuotedToken()
    throws DataParseException
  {
    assert '"' == _line.charAt( _column );

    // Skip the quote
    _column++;

    final StringBuilder sb = new StringBuilder();

    while ( _line.length() > _column && '"' != _line.charAt( _column ) )
    {
      sb.append( _line.charAt( _column ) );
      _column++;
    }

    if ( _line.length() == _column )
    {
      throw newDataParseException( "Quoted token missing closing \"" );
    }

    // Skip the quote
    _column++;

    return sb.toString();
  }

  @Nonnull
  private String parseRawToken()
  {
    final StringBuilder sb = new StringBuilder();

    while ( _line.length() > _column && ' ' != _line.charAt( _column ) )
    {
      sb.append( _line.charAt( _column ) );
      _column++;
    }

    return sb.toString();
  }

  private void stripWhiteSpace()
  {
    while ( _line.length() > _column && ' ' == _line.charAt( _column ) )
    {
      _column++;
    }
  }

  private void parseIndentation()
    throws DataParseException
  {
    int indent = 0;
    while ( _line.length() > _column && _line.charAt( _column ) == '\t' )
    {
      _column++;
      indent++;
    }
    if ( indent > _indent + 1 )
    {
      final String message = "Invalid file. Moved from indent level " + _indent + " to indent level " + indent;
      throw newDataParseException( message );
    }
    else if ( indent == _indent + 1 )
    {
      if ( null == _lastNode )
      {
        throw newDataParseException( "Attempted to define node without a parent node" );
      }
      else if ( !( _lastNode instanceof DataElement ) )
      {
        throw newDataParseException( "Attempted to define node below a comment" );
      }
      else
      {
        _parentNode = (DataElement) _lastNode;
      }
    }
    else if ( indent < _indent )
    {
      final int downSteps = _indent - indent;
      for ( int i = 0; i < downSteps; i++ )
      {
        assert null != _parentNode;
        _parentNode = _parentNode.getParent();
      }
    }
    _indent = indent;
  }

  @Nonnull
  private DataParseException newDataParseException( final String message )
  {
    return new DataParseException( message, newLocation() );
  }

  @Nonnull
  private SourceLocation newLocation()
  {
    return new SourceLocation( _location, _reader.getLineNumber(), _column );
  }

  @Nullable
  private String readLine()
    throws IOException
  {
    return _reader.readLine();
  }
}
