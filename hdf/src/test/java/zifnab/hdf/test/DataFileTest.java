package zifnab.hdf.test;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import zifnab.hdf.DataDocument;
import zifnab.hdf.DataElement;
import zifnab.hdf.DataFile;
import static org.testng.Assert.*;

public class DataFileTest
  extends AbstractTest
{
  @Test
  public void writeComplexRepresentation()
    throws Exception
  {
    final DataDocument document = new DataDocument();
    final DataElement mission = document.element( "mission", "Drought Relief" );
    mission.element( "name", "Drought relief to <planet>" );
    mission.element( "job" );
    mission.element( "repeat" );
    mission.element( "description",
                     "The farming world of <destination> is currently experiencing a drought. Deliver <cargo> to the planet by <date> for <payment>." );
    mission.element( "cargo", "drought relief supplies", "25", "2", ".05" );
    mission.element( "deadline" );
    final DataElement offer = mission.element( "to", "offer" );
    offer.element( "random", "<", "10" );
    final DataElement source = mission.element( "source" );
    source.element( "government", "Republic" );
    source.element( "not", "attributes", "farming" );

    final String output = writeElement( document );
    assertEquals( output,
                  "mission \"Drought Relief\"\n" +
                  "\tname \"Drought relief to <planet>\"\n" +
                  "\tjob\n" +
                  "\trepeat\n" +
                  "\tdescription \"The farming world of <destination> is currently experiencing a drought. Deliver <cargo> to the planet by <date> for <payment>.\"\n" +
                  "\tcargo \"drought relief supplies\" 25 2 .05\n" +
                  "\tdeadline\n" +
                  "\tto offer\n" +
                  "\t\trandom < 10\n" +
                  "\tsource\n" +
                  "\t\tgovernment Republic\n" +
                  "\t\tnot attributes farming\n" );
  }

  @Test
  public void write_multipleRootElements()
    throws Exception
  {
    final DataDocument document = new DataDocument();
    document.element( "tip", "spike:" );
    document.element( "planet", "Mars" );
    document.element( "planet", "Luna" );

    final Path file = createTempDataFile();
    createDataFile( document, file ).write();
    final String output = readContent( file );
    assertEquals( output, "tip spike:\n" +
                          "\n" +
                          "planet Mars\n" +
                          "\n" +
                          "planet Luna\n" );
  }

  @Test
  public void write_defaultFile()
    throws Exception
  {
    final DataDocument document = new DataDocument();
    document.element( "tip", "spike:" );

    final Path file = createTempDataFile();
    createDataFile( document, file ).write();
    final String output = readContent( file );
    assertEquals( output, "tip spike:\n" );
  }

  @Test
  public void writeToDifferentFile()
    throws Exception
  {
    final DataDocument document = new DataDocument();
    document.element( "tip", "spike:" );

    final Path file1 = createTempDataFile();
    final Path file2 = createTempDataFile();
    createDataFile( document, file1 ).writeTo( file2 );
    final String output = readContent( file2 );
    assertEquals( output, "tip spike:\n" );
  }

  @Test
  public void writeToInMemoryWriter()
    throws Exception
  {
    final DataDocument document = new DataDocument();
    document.element( "tip", "spike:" );

    final StringWriter sw = new StringWriter();
    createDataFile( document, createTempDataFile() ).writeTo( sw );
    assertEquals( sw.toString(), "tip spike:\n" );
  }

  @Nonnull
  private String writeElement( @Nonnull final DataDocument document )
    throws IOException
  {
    final Path file = createTempDataFile();
    createDataFile( document, file ).write();
    return readContent( file );
  }

  @Nonnull
  private DataFile createDataFile( @Nonnull final DataDocument document, @Nonnull final Path file )
  {
    final DataFile dataFile = new DataFile( file, document );
    assertEquals( dataFile.getPath(), file );
    assertEquals( dataFile.getDocument(), document );
    return dataFile;
  }
}
