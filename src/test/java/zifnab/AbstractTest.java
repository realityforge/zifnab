package zifnab;

import gir.Gir;
import gir.io.FileUtil;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;
import javax.annotation.Nonnull;
import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestResult;
import zifnab.hdf.DataDocument;
import zifnab.hdf.DataElement;
import zifnab.hdf.DataFile;
import zifnab.hdf.DataParseException;
import zifnab.hdf.SourceLocation;
import static org.testng.Assert.*;

@SuppressWarnings( { "WeakerAccess", "SameParameterValue" } )
public abstract class AbstractTest
  implements IHookable
{
  /// Used when generating random string
  @Nonnull
  private static final String STRING_CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
  @Nonnull
  private final Random _random = new Random();

  @Override
  public void run( final IHookCallBack callBack, final ITestResult testResult )
  {
    try
    {
      Gir.go( () -> FileUtil.inTempDir( () -> callBack.runTestMethod( testResult ) ) );
    }
    catch ( final Exception e )
    {
      assertNull( e );
    }
  }

  @Nonnull
  protected final Path createTempDataFile()
    throws IOException
  {
    return Files.createTempFile( "zifnab", ".txt" );
  }

  @Nonnull
  protected final String readContent( @Nonnull final Path file )
    throws IOException
  {
    return new String( Files.readAllBytes( file ), StandardCharsets.UTF_8 );
  }

  protected final void writeContent( @Nonnull final Path path, @Nonnull final String content )
    throws IOException
  {
    Files.write( path, content.getBytes( StandardCharsets.UTF_8 ) );
  }

  @Nonnull
  protected final DataElement asDataElement( @Nonnull final String data )
    throws IOException, DataParseException
  {
    final List<DataElement> elements = asDataDocument( data ).getChildElements();
    assertEquals( elements.size(), 1 );
    return elements.get( 0 );
  }

  @Nonnull
  protected final DataDocument asDataDocument( @Nonnull final String data )
    throws IOException, DataParseException
  {
    final Path file = createTempDataFile();
    writeContent( file, data );
    return DataFile.read( file ).getDocument();
  }

  @Nonnull
  protected final SourceLocation randomSourceLocation()
  {
    return new SourceLocation( randomString(), randomPositiveInt(), randomPositiveInt() );
  }

  @Nonnull
  protected final Random getRandom()
  {
    return _random;
  }

  protected final int randomPositiveInt()
  {
    return Math.abs( randomInt() );
  }

  protected final int randomInt()
  {
    return getRandom().nextInt();
  }

  protected final double randomPositiveDouble()
  {
    return Math.abs( randomDouble() );
  }

  protected final double randomDouble()
  {
    return getRandom().nextDouble();
  }

  @Nonnull
  protected final String randomString()
  {
    return randomString( 12 );
  }

  @Nonnull
  protected final String randomString( final int length )
  {
    final StringBuilder sb = new StringBuilder();
    for ( int i = 0; i < length; i++ )
    {
      sb.append( STRING_CHARACTERS.charAt( getRandom().nextInt( STRING_CHARACTERS.length() ) ) );
    }
    return sb.toString();
  }
}
