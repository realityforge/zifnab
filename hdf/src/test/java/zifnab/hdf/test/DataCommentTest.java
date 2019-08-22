package zifnab.hdf.test;

import java.io.IOException;
import java.nio.file.Path;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import zifnab.hdf.DataComment;
import zifnab.hdf.DataDocument;
import zifnab.hdf.DataElement;
import zifnab.hdf.DataFile;
import zifnab.hdf.SourceLocation;
import static org.testng.Assert.*;

public class DataCommentTest
  extends AbstractTest
{
  @Test
  public void constructTopLevel()
  {
    final DataDocument document = new DataDocument();
    final String comment = randomString();
    final DataComment element = document.comment( comment );

    assertNull( element.getParent() );
    assertEquals( element.getComment(), comment );
  }

  @Test
  public void constructWithSourceLocation()
  {
    final DataDocument document = new DataDocument();
    final String comment = randomString();
    final SourceLocation location = randomSourceLocation();
    final DataComment element = document.comment( location, comment );

    assertNull( element.getParent() );
    assertEquals( element.getComment(), comment );
    assertEquals( element.getLocation(), location );
  }

  @Test
  public void constructNested()
  {
    final DataDocument document = new DataDocument();
    final DataElement parent = document.element( "planet", "AK5" );

    final String comment = randomString();
    final DataComment child = parent.comment( comment );

    assertEquals( child.getParent(), parent );
    assertEquals( child.getComment(), comment );
  }

  @Test
  public void constructNestedWithSourceLocation()
  {
    final DataDocument document = new DataDocument();
    final DataElement parent = document.element( "planet", "AK5" );

    final String comment = randomString();
    final SourceLocation location = randomSourceLocation();
    final DataComment element = parent.comment( location, comment );

    assertEquals( element.getParent(), parent );
    assertEquals( element.getComment(), comment );
    assertEquals( element.getLocation(), location );
  }

  @Test
  public void writeAtTopLevel()
    throws Exception
  {
    final DataDocument document = new DataDocument();

    document.comment( "Hello darkness, my old friend!" );

    final String output = writeElement( document );
    assertEquals( output, "# Hello darkness, my old friend!\n" );
  }

  @Test
  public void writeEmptyComment()
    throws Exception
  {
    final DataDocument document = new DataDocument();

    document.comment( "" );

    final String output = writeElement( document );
    assertEquals( output, "#\n" );
  }

  @Test
  public void writeWithMultipleLayers()
    throws Exception
  {
    final DataDocument document = new DataDocument();
    document.comment( "The humanitarian mission!" );
    final DataElement element1 = document.element( "mission", "Drought Relief" );
    element1.comment( "The name of the mission as presented to user" );
    element1.element( "name", "Drought relief to <planet>" );
    final DataElement offer = element1.element( "to", "offer" );
    offer.comment( "This happens 1 in 10 times?" );
    offer.element( "random", "<", "10" );

    document.comment( "The safe planet" );
    document.comment( "... or so I hear" );
    document.element( "planet", "Dune" );

    final String output = writeElement( document );
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
  private String writeElement( @Nonnull final DataDocument document )
    throws IOException
  {
    final Path file = createTempDataFile();
    new DataFile( file, document ).write();
    return readContent( file );
  }
}
