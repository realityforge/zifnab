package zifnab.hdf;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class DataElement
  extends DataNode
{
  @Nullable
  private final DataElement _parent;
  @Nonnull
  private final List<String> _tokens;
  @Nullable
  private List<DataNode> _children;

  DataElement( @Nullable final SourceLocation location,
               @Nullable final DataElement parent,
               @Nonnull final String... tokens )
  {
    super( location );
    _parent = parent;
    _tokens = Collections.unmodifiableList( Arrays.asList( Objects.requireNonNull( tokens ) ) );
    assert !_tokens.isEmpty();
    assert _tokens.stream().noneMatch( line -> line.contains( "\n" ) );
    if ( null != _parent )
    {
      _parent.append( this );
    }
  }

  @Nullable
  public DataElement getParent()
  {
    return _parent;
  }

  @Nonnull
  public String getName()
  {
    return Objects.requireNonNull( _tokens.get( 0 ) );
  }

  @Nonnull
  public List<String> getTokens()
  {
    return Collections.unmodifiableList( _tokens );
  }

  public int getTokenCount()
  {
    return _tokens.size();
  }

  public void assertTokenName( @Nonnull final String name )
  {
    if ( !name.equals( getName() ) )
    {
      final String message = "Data element named '" + getName() + "' expected to be named '" + name + "'";
      throw new DataAccessException( message, getLocation() );
    }
  }

  public void assertTokenCount( final int length )
  {
    final int size = getTokenCount();
    if ( length != size )
    {
      final String message =
        "Data element named '" + getName() + "' expected to contain " + length + " tokens " +
        "but contains " + size + " tokens";
      throw new DataAccessException( message, getLocation() );
    }
  }

  public void assertTokenCounts( @Nonnull final Integer... tokenCounts )
  {
    final int size = getTokenCount();
    for ( final int tokenCount : tokenCounts )
    {
      if ( size == tokenCount )
      {
        return;
      }
    }
    final String message =
      "Data element named '" + getName() + "' expected to contain tokens with a count matching one of " +
      Arrays.asList( tokenCounts ) + " but contains " + size + " tokens";
    throw new DataAccessException( message, getLocation() );
  }

  public void assertTokenCountRange( final int minLength, final int maxLength )
  {
    final int size = getTokenCount();
    if ( size < minLength || size > maxLength )
    {
      final String message =
        "Data element named '" + getName() + "' expected to contain between " + minLength + " and " + maxLength +
        " tokens but contains " + size + " tokens";
      throw new DataAccessException( message, getLocation() );
    }
  }

  @Nonnull
  public String getStringAt( final int index )
  {
    if ( index >= getTokenCount() )
    {
      final String message =
        "Data element named '" + getName() + "' does not contain a token at index " + index;
      throw new DataAccessException( message, getLocation() );
    }
    else
    {
      return Objects.requireNonNull( _tokens.get( index ) );
    }
  }

  public int getIntAt( final int index )
  {
    final String token = getStringAt( index );
    try
    {
      return Integer.parseInt( token );
    }
    catch ( final NumberFormatException e )
    {
      throw new DataAccessException( "Token at index " + index + " for data element named '" + getName() +
                                     "' has value '" + token + "' which is not an integer", getLocation() );
    }
  }

  public double getDoubleAt( final int index )
  {
    final String token = getStringAt( index );
    try
    {
      return Double.parseDouble( token );
    }
    catch ( final NumberFormatException e )
    {
      throw new DataAccessException( "Token at index " + index + " for data element named '" + getName() +
                                     "' has value '" + token + "' which is not an double", getLocation() );
    }
  }

  @Nonnull
  public List<DataNode> getChildren()
  {
    return null == _children ? Collections.emptyList() : Collections.unmodifiableList( _children );
  }

  @Nonnull
  public List<DataElement> getChildElements()
  {
    return null == _children ?
           Collections.emptyList() :
           Collections.unmodifiableList( _children.stream()
                                           .filter( e -> e instanceof DataElement )
                                           .map( e -> (DataElement) e )
                                           .collect( Collectors.toList() ) );
  }

  @Nonnull
  public DataElement element( @Nonnull final String... tokens )
  {
    return element( null, tokens );
  }

  @Nonnull
  public DataElement element( @Nullable final SourceLocation location, @Nonnull final String... tokens )
  {
    return new DataElement( location, this, tokens );
  }

  @Nonnull
  public DataComment comment( @Nonnull final String comment )
  {
    return comment( null, comment );
  }

  @Nonnull
  public DataComment comment( @Nullable final SourceLocation location, @Nonnull final String comment )
  {
    return new DataComment( location, this, comment );
  }

  void append( @Nonnull final DataNode child )
  {
    if ( null == _children )
    {
      _children = new ArrayList<>();
    }
    _children.add( Objects.requireNonNull( child ) );
  }

  @Override
  void write( @Nonnull final Writer writer, final int depth )
    throws IOException
  {
    writeSelf( writer, depth );
    writeChildren( writer, depth );
  }

  private void writeSelf( @Nonnull final Writer writer, final int depth )
    throws IOException
  {
    writeIndent( writer, depth );
    writeTokens( writer );
  }

  private void writeChildren( @Nonnull final Writer writer, final int depth )
    throws IOException
  {
    for ( final DataNode child : getChildren() )
    {
      child.write( writer, depth + 1 );
    }
  }

  private void writeTokens( @Nonnull final Writer writer )
    throws IOException
  {
    final List<String> tokens = getTokens();
    assert !tokens.isEmpty();
    boolean first = true;
    for ( final String token : tokens )
    {
      if ( !first )
      {
        writer.write( ' ' );
      }
      first = false;
      writeToken( writer, token );
    }
    writer.write( '\n' );
  }

  private void writeToken( @Nonnull final Writer writer, @Nonnull final String token )
    throws IOException
  {
    if ( token.isEmpty() )
    {
      writer.write( "\"\"" );
    }
    else
    {
      final boolean tokenContainsQuote = token.contains( "\"" );
      if ( tokenContainsQuote )
      {
        writer.write( '`' );
        writer.write( token );
        writer.write( '`' );
      }
      else
      {
        final boolean tokenContainsSpace = token.contains( " " );
        if ( tokenContainsSpace )
        {
          writer.write( '"' );
          writer.write( token );
          writer.write( '"' );
        }
        else
        {
          writer.write( token );
        }
      }
    }
  }
}
