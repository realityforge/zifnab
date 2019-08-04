package zifnab.hdf.test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import zifnab.AbstractTest;
import zifnab.hdf.DataElement;
import zifnab.hdf.DataFile;
import static org.testng.Assert.*;

public class DataFileTest
  extends AbstractTest
{
  @Test
  public void writeWithOneLayer()
    throws Exception
  {
    final DataElement root = new DataElement( null, Arrays.asList( "tip", "spike:" ) );

    final String output = writeElement( root );
    assertEquals( output, "tip spike:\n" );
  }

  @Test
  public void writeTokensWithSpaces()
    throws Exception
  {
    final DataElement root = new DataElement( null, Arrays.asList( "tip", "awesome spike:" ) );

    final String output = writeElement( root );
    assertEquals( output, "tip \"awesome spike:\"\n" );
  }

  @Test
  public void writeTokensWithQuotes()
    throws Exception
  {
    final DataElement root = new DataElement( null, "planet", "Air \"quotes\"" );

    final String output = writeElement( root );
    assertEquals( output, "planet `Air \"quotes\"`\n" );
  }

  @Test
  public void writeWithMultipleLayers()
    throws Exception
  {
    final DataElement root = new DataElement( null, "mission", "Drought Relief" );
    new DataElement( root, "name", "Drought relief to <planet>" );
    new DataElement( root, "job" );
    new DataElement( root, "repeat" );
    new DataElement( root,
                     "description",
                     "The farming world of <destination> is currently experiencing a drought. Deliver <cargo> to the planet by <date> for <payment>." );
    new DataElement( root, "cargo", "drought relief supplies", "25", "2", ".05" );
    new DataElement( root, "deadline" );
    final DataElement offer = new DataElement( root, "to", "offer" );
    new DataElement( offer, "random", "<", "10" );
    final DataElement source = new DataElement( root, "source" );
    new DataElement( source, "government", "Republic" );
    new DataElement( source, "not", "attributes", "farming" );

    final String output = writeElement( root );
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
  public void writeToDifferentFile()
    throws Exception
  {
    final DataElement root = new DataElement( null, Arrays.asList( "tip", "spike:" ) );

    final Path file1 = createTempDataFile();
    final Path file2 = createTempDataFile();
    createDataFile( root, file1 ).writeTo( file2 );
    final String output = readContent( file2 );
    assertEquals( output, "tip spike:\n" );
  }

  @Nonnull
  private String writeElement( @Nonnull final DataElement root )
    throws IOException
  {
    final Path file = createTempDataFile();
    createDataFile( root, file ).write();
    return readContent( file );
  }

  @Nonnull
  private DataFile createDataFile( @Nonnull final DataElement root, @Nonnull final Path file )
  {
    final DataFile dataFile = new DataFile( file, root );
    assertEquals( dataFile.getPath(), file );
    assertEquals( dataFile.getRoot(), root );
    return dataFile;
  }
}
