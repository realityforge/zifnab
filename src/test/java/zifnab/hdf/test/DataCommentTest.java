package zifnab.hdf.test;

import java.io.IOException;
import java.nio.file.Path;
import javax.annotation.Nonnull;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import zifnab.AbstractTest;
import zifnab.hdf.DataComment;
import zifnab.hdf.DataDocument;
import zifnab.hdf.DataElement;
import zifnab.hdf.DataFile;
import zifnab.hdf.DataNode;
import zifnab.hdf.SourceLocation;
import static org.testng.Assert.*;

public class DataCommentTest
  extends AbstractTest
{
  @Test
  public void constructTopLevel()
  {
    final String comment = ValueUtil.randomString();
    final DataComment element = new DataComment( null, comment );

    assertNull( element.getParent() );
    assertEquals( element.getComment(), comment );
  }

  @Test
  public void constructWithSourceLocation()
  {
    final String comment = ValueUtil.randomString();
    final SourceLocation location =
      new SourceLocation( ValueUtil.randomString(),
                          Math.abs( ValueUtil.randomInt() ),
                          Math.abs( ValueUtil.randomInt() ) );
    final DataComment element = new DataComment( location, null, comment );

    assertNull( element.getParent() );
    assertEquals( element.getComment(), comment );
    assertEquals( element.getLocation(), location );
  }

  @Test
  public void constructNested()
  {
    final DataElement parent = new DataElement( null, "planet", "AK5" );

    final String comment = ValueUtil.randomString();
    final DataComment child = new DataComment( parent, comment );

    assertEquals( child.getParent(), parent );
    assertEquals( child.getComment(), comment );
  }

  @Test
  public void writeAtTopLevel()
    throws Exception
  {
    final String comment = "Hello darkness, my old friend!";
    final DataComment node = new DataComment( null, comment );

    final String output = writeElement( node );
    assertEquals( output, "# Hello darkness, my old friend!\n" );
  }

  @Test
  public void writeWithMultipleLayers()
    throws Exception
  {
    final DataComment comment1 = new DataComment( null, "The humanitarian mission!" );
    final DataElement element1 = new DataElement( null, "mission", "Drought Relief" );
    new DataComment( element1, "The name of the mission as presented to user" );
    new DataElement( element1, "name", "Drought relief to <planet>" );
    final DataElement offer = new DataElement( element1, "to", "offer" );
    new DataComment( offer, "This happens 1 in 10 times?" );
    new DataElement( offer, "random", "<", "10" );

    final DataComment comment2 = new DataComment( null, "The safe planet" );
    final DataComment comment3 = new DataComment( null, "... or so I hear" );
    final DataElement element2 = new DataElement( null, "planet", "Dune" );

    final String output = writeElement( comment1, element1, comment2, comment3, element2 );
    assertEquals( output,
                  "# The humanitarian mission!\n" +
                  "mission \"Drought Relief\"\n" +
                  "\t# The name of the mission as presented to user\n" +
                  "\tname \"Drought relief to <planet>\"\n" +
                  "\tto offer\n" +
                  "\t\t# This happens 1 in 10 times?\n" +
                  "\t\trandom < 10\n" +
                  "\n" +
                  "# The safe planet\n" +
                  "# ... or so I hear\n" +
                  "planet Dune\n" );
  }

  @Nonnull
  private String writeElement( @Nonnull final DataNode... nodes )
    throws IOException
  {
    final Path file = createTempDataFile();
    final DataDocument document = new DataDocument();
    for ( final DataNode node : nodes )
    {
      document.append( node );
    }
    new DataFile( file, document ).write();
    return readContent( file );
  }
}
