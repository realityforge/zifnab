package zifnab.hdf;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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

  public DataElement( @Nullable final DataElement parent, @Nonnull final String... tokens )
  {
    this( null, parent, Arrays.asList( tokens ) );
  }

  public DataElement( @Nullable final SourceLocation location,
                      @Nullable final DataElement parent,
                      @Nonnull final List<String> tokens )
  {
    super( location );
    assert !tokens.isEmpty();
    assert tokens.stream().noneMatch( line -> line.contains( "\n" ) );
    _parent = parent;
    _tokens = new ArrayList<>( Objects.requireNonNull( tokens ) );
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

  @Nonnull
  public List<DataNode> getChildren()
  {
    return null == _children ? Collections.emptyList() : Collections.unmodifiableList( _children );
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
