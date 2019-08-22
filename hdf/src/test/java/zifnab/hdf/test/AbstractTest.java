package zifnab.hdf.test;

import gir.Gir;
import gir.io.FileUtil;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import javax.annotation.Nonnull;
import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestResult;
import zifnab.hdf.SourceLocation;
import static org.testng.Assert.*;

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
  final Path createTempDataFile()
    throws IOException
  {
    return Files.createTempFile( "zifnab", ".txt" );
  }

  @Nonnull
  final String readContent( @Nonnull final Path file )
    throws IOException
  {
    return new String( Files.readAllBytes( file ), StandardCharsets.UTF_8 );
  }

  final void writeContent( @Nonnull final Path path, @Nonnull final String content )
    throws IOException
  {
    Files.write( path, content.getBytes( StandardCharsets.UTF_8 ) );
  }

  @Nonnull
  final SourceLocation randomSourceLocation()
  {
    return new SourceLocation( randomString(), randomPositiveInt(), randomPositiveInt() );
  }

  @Nonnull
  private Random getRandom()
  {
    return _random;
  }

  private int randomPositiveInt()
  {
    return Math.abs( getRandom().nextInt() );
  }

  @Nonnull
  final String randomString()
  {
    final StringBuilder sb = new StringBuilder();
    for ( int i = 0; i < 12; i++ )
    {
      sb.append( STRING_CHARACTERS.charAt( getRandom().nextInt( STRING_CHARACTERS.length() ) ) );
    }
    return sb.toString();
  }
}
