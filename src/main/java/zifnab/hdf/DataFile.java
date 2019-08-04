package zifnab.hdf;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;

public final class DataFile
{
  @Nonnull
  private final Path _path;
  @Nonnull
  private DataElement _root;

  public DataFile( @Nonnull final Path path, @Nonnull final DataElement root )
  {
    _path = Objects.requireNonNull( path );
    _root = Objects.requireNonNull( root );
  }

  @Nonnull
  public Path getPath()
  {
    return _path;
  }

  @Nonnull
  public DataElement getRoot()
  {
    return _root;
  }

  public void write()
    throws IOException
  {
    writeTo( _path );
  }

  public void writeTo( @Nonnull final Path file )
    throws IOException
  {
    try ( final Writer fileWriter = new FileWriter( file.toFile() ) )
    {
      try ( final Writer writer = new BufferedWriter( fileWriter ) )
      {
        write( writer, _root, 0 );
      }
    }
  }

  private void write( @Nonnull final Writer writer, @Nonnull final DataElement element, final int depth )
    throws IOException
  {
    writeIndent( writer, depth );
    writeTokens( writer, element );
    writeChildren( writer, element, depth );
  }

  private void writeChildren( @Nonnull final Writer writer, @Nonnull final DataElement element, final int depth )
    throws IOException
  {
    for ( final DataElement child : element.getChildren() )
    {
      write( writer, child, depth + 1 );
    }
  }

  private void writeTokens( @Nonnull final Writer writer,
                            @Nonnull final DataElement element )
    throws IOException
  {
    final List<String> tokens = element.getTokens();
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

  private void writeIndent( @Nonnull final Writer writer, final int depth )
    throws IOException
  {
    for ( int i = 0; i < depth; i++ )
    {
      writer.write( "\t" );
    }
  }
}
