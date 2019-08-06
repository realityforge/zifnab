package zifnab.hdf.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import zifnab.AbstractTest;
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
    final DataElement mission = new DataElement( null, "mission", "Drought Relief" );
    new DataElement( mission, "name", "Drought relief to <planet>" );
    new DataElement( mission, "job" );
    new DataElement( mission, "repeat" );
    new DataElement( mission,
                     "description",
                     "The farming world of <destination> is currently experiencing a drought. Deliver <cargo> to the planet by <date> for <payment>." );
    new DataElement( mission, "cargo", "drought relief supplies", "25", "2", ".05" );
    new DataElement( mission, "deadline" );
    final DataElement offer = new DataElement( mission, "to", "offer" );
    new DataElement( offer, "random", "<", "10" );
    final DataElement source = new DataElement( mission, "source" );
    new DataElement( source, "government", "Republic" );
    new DataElement( source, "not", "attributes", "farming" );

    final DataDocument document = new DataDocument();
    document.append( mission );

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
    final DataElement element1 = new DataElement( null, "tip", "spike:" );
    final DataElement element2 = new DataElement( null, "planet", "Mars" );
    final DataElement element3 = new DataElement( null, "planet", "Luna" );

    final Path file = createTempDataFile();
    final DataDocument document = new DataDocument();
    document.append( element1 );
    document.append( element2 );
    document.append( element3 );
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
    final DataElement element = new DataElement( null, "tip", "spike:" );

    final Path file = createTempDataFile();
    final DataDocument document = new DataDocument();
    document.append( element );
    createDataFile( document, file ).write();
    final String output = readContent( file );
    assertEquals( output, "tip spike:\n" );
  }

  @Test
  public void writeToDifferentFile()
    throws Exception
  {
    final DataElement element = new DataElement( null, "tip", "spike:" );

    final Path file1 = createTempDataFile();
    final Path file2 = createTempDataFile();
    final DataDocument document = new DataDocument();
    document.append( element );
    createDataFile( document, file1 ).writeTo( file2 );
    final String output = readContent( file2 );
    assertEquals( output, "tip spike:\n" );
  }

  @Test
  public void writeToInMemoryWriter()
    throws Exception
  {
    final DataElement element = new DataElement( null, "tip", "spike:" );

    final DataDocument document = new DataDocument();
    document.append( element );
    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    createDataFile( document, createTempDataFile() ).writeTo( new OutputStreamWriter( baos, StandardCharsets.UTF_8 ) );
    final String output = new String( baos.toByteArray(), StandardCharsets.UTF_8 );
    assertEquals( output, "tip spike:\n" );
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
