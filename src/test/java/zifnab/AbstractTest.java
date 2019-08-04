package zifnab;

import gir.Gir;
import gir.io.FileUtil;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.annotation.Nonnull;
import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestResult;
import static org.testng.Assert.*;

public abstract class AbstractTest
  implements IHookable
{
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

  @Nonnull
  protected final Path writeContent( @Nonnull final String path, @Nonnull final String content )
    throws IOException
  {
    FileUtil.write( path, content );
    return FileUtil.getCurrentDirectory().resolve( path );
  }
}
