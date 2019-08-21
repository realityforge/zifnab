package zifnab.hdf;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Objects;
import javax.annotation.Nonnull;

public final class DataFile
{
  @Nonnull
  private final Path _path;
  @Nonnull
  private DataDocument _document;

  public DataFile( @Nonnull final Path path, @Nonnull final DataDocument document )
  {
    _path = Objects.requireNonNull( path );
    _document = Objects.requireNonNull( document );
  }

  @Nonnull
  public Path getPath()
  {
    return _path;
  }

  @Nonnull
  public DataDocument getDocument()
  {
    return _document;
  }

  @Nonnull
  public static DataFile read( @Nonnull final Path file )
    throws IOException, DataParseException
  {
    try ( final InputStream inputStream = new FileInputStream( file.toFile() ) )
    {
      try ( final Reader fileReader = new InputStreamReader( inputStream, StandardCharsets.UTF_8 ) )
      {
        try ( final LineNumberReader reader = new LineNumberReader( fileReader ) )
        {
          return new DataFile( file, new Parser( file.toString(), reader ).getDocument() );
        }
      }
    }
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
      writeTo( fileWriter );
    }
  }

  public void writeTo( @Nonnull final Writer writer )
    throws IOException
  {
    try ( final Writer bufferedWriter = new BufferedWriter( writer ) )
    {
      _document.write( bufferedWriter );
    }
  }
}
