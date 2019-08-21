package zifnab.hdf.test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import zifnab.AbstractTest;
import zifnab.hdf.DataAccessException;
import zifnab.hdf.DataComment;
import zifnab.hdf.DataDocument;
import zifnab.hdf.DataElement;
import zifnab.hdf.DataFile;
import zifnab.hdf.SourceLocation;
import static org.testng.Assert.*;

public class DataElementTest
  extends AbstractTest
{
  @Test
  public void constructTopLevel()
  {
    final DataDocument document = new DataDocument();
    final DataElement element = document.element( "planet", "Dune" );

    assertNull( element.getParent() );
    assertNull( element.getLocation() );
    assertEquals( element.getName(), "planet" );
    assertEquals( element.getTokens(), Arrays.asList( "planet", "Dune" ) );
    assertTrue( element.getChildren().isEmpty() );
    assertTrue( element.getChildElements().isEmpty() );
  }

  @Test
  public void constructWithSourceLocation()
  {
    final SourceLocation location = randomSourceLocation();

    final DataDocument document = new DataDocument();
    final DataElement element = document.element( location, "planet", "Dune" );

    assertNull( element.getParent() );
    assertEquals( element.getLocation(), location );
    assertEquals( element.getName(), "planet" );
    assertEquals( element.getTokens(), Arrays.asList( "planet", "Dune" ) );
    assertTrue( element.getChildren().isEmpty() );
    assertTrue( element.getChildElements().isEmpty() );
  }

  @Test
  public void constructWithVarargsCtor()
  {
    final DataDocument document = new DataDocument();
    final DataElement element = document.element( "planet", "Dune" );

    assertNull( element.getParent() );
    assertNull( element.getLocation() );
    assertEquals( element.getName(), "planet" );
    assertEquals( element.getTokens(), Arrays.asList( "planet", "Dune" ) );
    assertTrue( element.getChildren().isEmpty() );
    assertTrue( element.getChildElements().isEmpty() );
  }

  @Test
  public void constructNested()
  {
    final DataDocument document = new DataDocument();
    final DataElement parent = document.element( "planet", "AK5" );

    final DataElement child = parent.element( "name", "Akaron 5" );

    assertNull( parent.getParent() );
    assertNull( parent.getLocation() );
    assertEquals( parent.getName(), "planet" );
    assertEquals( parent.getTokens(), Arrays.asList( "planet", "AK5" ) );
    assertEquals( parent.getChildren(), Collections.singletonList( child ) );
    assertEquals( parent.getChildElements(), Collections.singletonList( child ) );

    assertEquals( child.getParent(), parent );
    assertNull( child.getLocation() );
    assertEquals( child.getName(), "name" );
    assertEquals( child.getTokens(), Arrays.asList( "name", "Akaron 5" ) );
    assertTrue( child.getChildren().isEmpty() );
    assertTrue( child.getChildElements().isEmpty() );
  }

  @Test
  public void constructNestedWithSourceLocation()
  {
    final DataDocument document = new DataDocument();
    final DataElement parent = document.element( "planet", "AK5" );

    final SourceLocation location = randomSourceLocation();
    final DataElement element = parent.element( location, "planet", "Dune" );

    assertEquals( element.getParent(), parent );
    assertEquals( element.getLocation(), location );
    assertEquals( element.getName(), "planet" );
    assertEquals( element.getTokens(), Arrays.asList( "planet", "Dune" ) );
    assertTrue( element.getChildren().isEmpty() );
    assertTrue( element.getChildElements().isEmpty() );
  }

  @Test
  public void constructNestedWithComments()
  {
    final DataDocument document = new DataDocument();
    final DataElement parent = document.element( "planet", "AK5" );

    final DataComment comment = parent.comment( "The home planet of the queen in waiting" );
    final DataElement child = parent.element( "name", "Akaron 5" );

    assertNull( parent.getParent() );
    assertNull( parent.getLocation() );
    assertEquals( parent.getName(), "planet" );
    assertEquals( parent.getTokens(), Arrays.asList( "planet", "AK5" ) );
    assertEquals( parent.getChildren(), Arrays.asList( comment, child ) );
    assertEquals( parent.getChildElements(), Collections.singletonList( child ) );

    assertEquals( comment.getParent(), parent );
    assertNull( comment.getLocation() );
    assertEquals( comment.getComment(), "The home planet of the queen in waiting" );

    assertEquals( child.getParent(), parent );
    assertNull( child.getLocation() );
    assertEquals( child.getName(), "name" );
    assertEquals( child.getTokens(), Arrays.asList( "name", "Akaron 5" ) );
    assertTrue( child.getChildren().isEmpty() );
    assertTrue( child.getChildElements().isEmpty() );
  }

  @Test
  public void writeWithOneLayer()
    throws Exception
  {
    final DataDocument document = new DataDocument();
    document.element( "tip", "spike:" );

    final String output = writeElement( document );
    assertEquals( output, "tip spike:\n" );
  }

  @Test
  public void writeTokensWithSpaces()
    throws Exception
  {
    final DataDocument document = new DataDocument();
    document.element( "tip", "awesome spike:" );

    final String output = writeElement( document );
    assertEquals( output, "tip \"awesome spike:\"\n" );
  }

  @Test
  public void writeTokensWithTabs()
    throws Exception
  {
    final DataDocument document = new DataDocument();
    document.element( "description", "\tAnd then the magic happened" );

    final String output = writeElement( document );
    assertEquals( output, "description \"\tAnd then the magic happened\"\n" );
  }

  @Test
  public void writeTokensWithLeadingTab()
    throws Exception
  {
    final DataDocument document = new DataDocument();
    document.element( "\tAnd so the story goes..." );

    final String output = writeElement( document );
    assertEquals( output, "\"\tAnd so the story goes...\"\n" );
  }

  @Test
  public void writeTokensWithQuotes()
    throws Exception
  {
    final DataDocument document = new DataDocument();
    document.element( "planet", "Air \"quotes\"" );

    final String output = writeElement( document );
    assertEquals( output, "planet `Air \"quotes\"`\n" );
  }

  @Test
  public void writeEmptyToken()
    throws Exception
  {
    final DataDocument document = new DataDocument();
    document.element( "planet", "" );

    final String output = writeElement( document );
    assertEquals( output, "planet \"\"\n" );
  }

  @Test
  public void writeWithMultipleLayers()
    throws Exception
  {
    final DataDocument document = new DataDocument();
    final DataElement element = document.element( "mission", "Drought Relief" );
    element.element( "name", "Drought relief to <planet>" );
    element.element( "job" );
    element.element( "repeat" );
    element.element( "description",
                     "The farming world of <destination> is currently experiencing a drought. Deliver <cargo> to the planet by <date> for <payment>." );
    element.element( "cargo", "drought relief supplies", "25", "2", ".05" );
    element.element( "deadline" );
    final DataElement offer = element.element( "to", "offer" );
    offer.element( "random", "<", "10" );
    final DataElement source = element.element( "source" );
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
  public void assertTokenName()
  {
    final DataDocument document = new DataDocument();
    final DataElement element = document.element( "planet", "Dune" );

    element.assertTokenName( "planet" );
  }

  @Test
  public void assertTokenName_errorWithNoLocation()
  {
    final DataDocument document = new DataDocument();
    final DataElement element = document.element( "planet", "Dune" );

    final DataAccessException exception =
      expectThrows( DataAccessException.class, () -> element.assertTokenName( "Dune" ) );

    assertEquals( exception.getMessage(), "Data element named 'planet' expected to be named 'Dune'" );
    assertNull( exception.getLocation() );
  }

  @Test
  public void assertTokenName_errorWithLocation()
  {
    final SourceLocation location = new SourceLocation( "file.txt", 1, 0 );
    final DataDocument document = new DataDocument();
    final DataElement element = document.element( location, "planet", "Dune" );

    final DataAccessException exception =
      expectThrows( DataAccessException.class, () -> element.assertTokenName( "Dune" ) );

    assertEquals( exception.getMessage(), "Data element named 'planet' expected to be named 'Dune'" );
    assertEquals( exception.getLocation(), location );
  }

  @Test
  public void assertLeafNode()
  {
    final DataDocument document = new DataDocument();
    final DataElement element = document.element( "planet", "Dune" );

    element.assertLeafNode();
  }

  @Test
  public void assertLeafNode_whenChildrenPresent()
  {
    final SourceLocation location = new SourceLocation( "file.txt", 1, 0 );
    final DataDocument document = new DataDocument();
    final DataElement element = document.element( location, "planet", "Dune" );
    element.element( "name", "The Red Planet" );

    final DataAccessException exception =
      expectThrows( DataAccessException.class, element::assertLeafNode );

    assertEquals( exception.getMessage(),
                  "Data element named 'planet' expected to have 0 children but has 1 children" );
    assertEquals( exception.getLocation(), location );
  }

  @Test
  public void assertTokenCount()
  {
    final DataDocument document = new DataDocument();
    final DataElement element = document.element( "planet", "Dune" );
    element.element( "name", "The Red Planet" );

    element.assertTokenCount( 2 );
  }

  @Test
  public void assertTokenCount_error()
  {
    final SourceLocation location = new SourceLocation( "file.txt", 1, 0 );
    final DataDocument document = new DataDocument();
    final DataElement element = document.element( location, "planet", "Dune" );
    element.element( "name", "The Red Planet" );

    final DataAccessException exception =
      expectThrows( DataAccessException.class, () -> element.assertTokenCount( 0 ) );

    assertEquals( exception.getMessage(),
                  "Data element named 'planet' expected to contain 0 tokens but contains 2 tokens" );
    assertEquals( exception.getLocation(), location );
  }

  @Test
  public void assertTokenCount_error_emptyLocation()
  {
    final DataDocument document = new DataDocument();
    final DataElement element = document.element( "planet", "Dune" );
    element.element( "name", "The Red Planet" );

    final DataAccessException exception =
      expectThrows( DataAccessException.class, () -> element.assertTokenCount( 0 ) );

    assertEquals( exception.getMessage(),
                  "Data element named 'planet' expected to contain 0 tokens but contains 2 tokens" );
    assertNull( exception.getLocation() );
  }

  @Test
  public void assertTokenCounts()
  {
    final DataDocument document = new DataDocument();
    final DataElement element = document.element( "planet", "Dune" );
    final DataElement child = element.element( "name", "The Red Planet" );

    element.assertTokenCounts( 1, 2 );
    child.assertTokenCounts( 0, 1, 2, 3, 4 );
  }

  @Test
  public void assertTokenCounts_error()
  {
    final DataDocument document = new DataDocument();
    final SourceLocation location = new SourceLocation( "file.txt", 1, 0 );
    final DataElement element = document.element( location, "planet", "Dune" );
    element.element( "name", "The Red Planet" );

    final DataAccessException exception =
      expectThrows( DataAccessException.class, () -> element.assertTokenCounts( 1, 3 ) );

    assertEquals( exception.getMessage(),
                  "Data element named 'planet' expected to contain tokens with a count matching one of [1, 3] but contains 2 tokens" );
    assertEquals( exception.getLocation(), location );
  }

  @Test
  public void assertTokenCountRange()
  {
    final DataDocument document = new DataDocument();
    final DataElement element = document.element( "planet", "Dune" );
    final DataElement child = element.element( "name", "The Red Planet" );

    element.assertTokenCountRange( 1, 2 );
    child.assertTokenCountRange( 0, 4 );
  }

  @Test
  public void assertTokenCountRange_error()
  {
    final DataDocument document = new DataDocument();
    final SourceLocation location = new SourceLocation( "file.txt", 1, 0 );
    final DataElement element = document.element( location, "planet", "Dune" );
    element.element( "name", "The Red Planet" );

    final DataAccessException exception =
      expectThrows( DataAccessException.class, () -> element.assertTokenCountRange( 0, 1 ) );

    assertEquals( exception.getMessage(),
                  "Data element named 'planet' expected to contain between 0 and 1 tokens but contains 2 tokens" );
    assertEquals( exception.getLocation(), location );
  }

  @Test
  public void assertTokenCountRange_error_emptyLocation()
  {
    final DataDocument document = new DataDocument();
    final DataElement element = document.element( "planet", "Dune" );
    element.element( "name", "The Red Planet" );

    final DataAccessException exception =
      expectThrows( DataAccessException.class, () -> element.assertTokenCountRange( 0, 1 ) );

    assertEquals( exception.getMessage(),
                  "Data element named 'planet' expected to contain between 0 and 1 tokens but contains 2 tokens" );
    assertNull( exception.getLocation() );
  }

  @Test
  public void getStringAt()
  {
    final DataDocument document = new DataDocument();
    final DataElement element = document.element( "planet", "Dune" );

    assertEquals( element.getStringAt( 0 ), "planet" );
    assertEquals( element.getStringAt( 1 ), "Dune" );
  }

  @Test
  public void getStringAt_badIndex()
  {
    final DataDocument document = new DataDocument();
    final DataElement element = document.element( "planet", "Dune" );

    final DataAccessException exception =
      expectThrows( DataAccessException.class, () -> element.getStringAt( 2 ) );

    assertEquals( exception.getMessage(),
                  "Data element named 'planet' does not contain a token at index 2" );
    assertNull( exception.getLocation() );
  }

  @Test
  public void getIntAt()
  {
    final DataDocument document = new DataDocument();
    final DataElement element = document.element( "trade", "sugar", "230" );

    assertEquals( element.getIntAt( 2 ), 230 );
  }

  @Test
  public void getIntAt_badIndex()
  {
    final SourceLocation location = new SourceLocation( "file.txt", 1, 0 );
    final DataDocument document = new DataDocument();
    final DataElement element = document.element( location, "planet", "Dune" );

    final DataAccessException exception =
      expectThrows( DataAccessException.class, () -> element.getIntAt( 2 ) );

    assertEquals( exception.getMessage(),
                  "Data element named 'planet' does not contain a token at index 2" );
    assertEquals( exception.getLocation(), location );
  }

  @Test
  public void getIntAt_badType()
  {
    final SourceLocation location = new SourceLocation( "file.txt", 1, 0 );
    final DataDocument document = new DataDocument();
    final DataElement element = document.element( location, "planet", "Dune" );

    final DataAccessException exception =
      expectThrows( DataAccessException.class, () -> element.getIntAt( 1 ) );

    assertEquals( exception.getMessage(),
                  "Token at index 1 for data element named 'planet' has value 'Dune' which is not an integer" );
    assertEquals( exception.getLocation(), location );
  }

  @Test
  public void getIntAt_belowMin()
  {
    final SourceLocation location = new SourceLocation( "file.txt", 1, 0 );
    final DataDocument document = new DataDocument();
    final DataElement element = document.element( location, "age", "1" );

    final DataAccessException exception =
      expectThrows( DataAccessException.class, () -> element.getIntAt( 1, 18 ) );

    assertEquals( exception.getMessage(),
                  "Token at index 1 for data element named 'age' has value '1' which is below the expected minimum value 18" );
    assertEquals( exception.getLocation(), location );
  }

  @Test
  public void getDoubleAt()
  {
    final DataDocument document = new DataDocument();
    final DataElement element = document.element( "trade", "sugar", "23.5" );

    assertEquals( element.getDoubleAt( 2 ), 23.5D );
  }

  @Test
  public void getDoubleAt_badIndex()
  {
    final SourceLocation location = new SourceLocation( "file.txt", 1, 0 );
    final DataDocument document = new DataDocument();
    final DataElement element = document.element( location, "planet", "Dune" );

    final DataAccessException exception =
      expectThrows( DataAccessException.class, () -> element.getDoubleAt( 2 ) );

    assertEquals( exception.getMessage(),
                  "Data element named 'planet' does not contain a token at index 2" );
    assertEquals( exception.getLocation(), location );
  }

  @Test
  public void getDoubleAt_badType()
  {
    final SourceLocation location = new SourceLocation( "file.txt", 1, 0 );
    final DataDocument document = new DataDocument();
    final DataElement element = document.element( location, "planet", "Dune" );

    final DataAccessException exception =
      expectThrows( DataAccessException.class, () -> element.getDoubleAt( 1 ) );

    assertEquals( exception.getMessage(),
                  "Token at index 1 for data element named 'planet' has value 'Dune' which is not an double" );
    assertEquals( exception.getLocation(), location );
  }

  @Test
  public void getDoubleAt_belowMinimum()
  {
    final SourceLocation location = new SourceLocation( "file.txt", 1, 0 );
    final DataDocument document = new DataDocument();
    final DataElement element = document.element( location, "speed", "-2" );

    final DataAccessException exception =
      expectThrows( DataAccessException.class, () -> element.getDoubleAt( 1, 0D ) );

    assertEquals( exception.getMessage(),
                  "Token at index 1 for data element named 'speed' has value '-2.0' which is below the expected minimum value 0.0" );
    assertEquals( exception.getLocation(), location );
  }

  @Test
  public void getDoubleAt_aboveMinimum()
  {
    final SourceLocation location = new SourceLocation( "file.txt", 1, 0 );
    final DataDocument document = new DataDocument();
    final DataElement element = document.element( location, "chance", "2" );

    final DataAccessException exception =
      expectThrows( DataAccessException.class, () -> element.getDoubleAt( 1, 0D, 1D ) );

    assertEquals( exception.getMessage(),
                  "Token at index 1 for data element named 'chance' has value '2.0' which is above the expected maximum value 0.0" );
    assertEquals( exception.getLocation(), location );
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
