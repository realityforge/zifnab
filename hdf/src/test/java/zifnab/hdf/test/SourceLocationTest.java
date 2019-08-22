package zifnab.hdf.test;

import org.testng.annotations.Test;
import zifnab.hdf.SourceLocation;
import static org.testng.Assert.*;

public class SourceLocationTest
  extends AbstractTest
{
  @Test
  public void constructWithNoLineNumber()
  {
    final String filename = "/mydir/somefile.txt";
    final int lineNumber = SourceLocation.UNSPECIFIED;
    final int columnNumber = SourceLocation.UNSPECIFIED;
    final SourceLocation location = new SourceLocation( filename, lineNumber, columnNumber );

    assertEquals( location.getFilename(), filename );
    assertEquals( location.getLineNumber(), lineNumber );
    assertEquals( location.getColumnNumber(), columnNumber );
    assertEquals( location.toString(), filename );
  }

  @Test
  public void constructWithNoColumnNumber()
  {
    final String filename = "/mydir/somefile.txt";
    final int lineNumber = 23;
    final int columnNumber = SourceLocation.UNSPECIFIED;
    final SourceLocation location = new SourceLocation( filename, lineNumber, columnNumber );

    assertEquals( location.getFilename(), filename );
    assertEquals( location.getLineNumber(), lineNumber );
    assertEquals( location.getColumnNumber(), columnNumber );
    assertEquals( location.toString(), "/mydir/somefile.txt:23" );
  }

  @Test
  public void constructWithAllFields()
  {
    final String filename = "/mydir/somefile.txt";
    final int lineNumber = 23;
    final int columnNumber = 42;
    final SourceLocation location = new SourceLocation( filename, lineNumber, columnNumber );

    assertEquals( location.getFilename(), filename );
    assertEquals( location.getLineNumber(), lineNumber );
    assertEquals( location.getColumnNumber(), columnNumber );
    assertEquals( location.toString(), "/mydir/somefile.txt:23:42" );
  }
}
