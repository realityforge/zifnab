package zifnab.hdf.test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import org.realityforge.guiceyloops.shared.ValueUtil;
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
    final DataElement element = new DataElement( null, "planet", "Dune" );

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
    final SourceLocation location =
      new SourceLocation( ValueUtil.randomString(),
                          Math.abs( ValueUtil.randomInt() ),
                          Math.abs( ValueUtil.randomInt() ) );
    final List<String> tokens = Arrays.asList( "planet", "Dune" );
    final DataElement element = new DataElement( location, null, tokens );

    assertNull( element.getParent() );
    assertEquals( element.getLocation(), location );
    assertEquals( element.getName(), "planet" );
    assertEquals( element.getTokens(), tokens );
    assertTrue( element.getChildren().isEmpty() );
    assertTrue( element.getChildElements().isEmpty() );
  }

  @Test
  public void constructWithVarargsCtor()
  {
    final DataElement element = new DataElement( null, "planet", "Dune" );

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
    final DataElement parent = new DataElement( null, "planet", "AK5" );

    final DataElement child = new DataElement( parent, "name", "Akaron 5" );

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
  public void constructNestedWithComments()
  {
    final DataElement parent = new DataElement( null, "planet", "AK5" );

    final DataComment comment = new DataComment( parent, "The home planet of the queen in waiting" );
    final DataElement child = new DataElement( parent, "name", "Akaron 5" );

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
    final DataElement root = new DataElement( null, "tip", "spike:" );

    final String output = writeElement( root );
    assertEquals( output, "tip spike:\n" );
  }

  @Test
  public void writeTokensWithSpaces()
    throws Exception
  {
    final DataElement root = new DataElement( null, "tip", "awesome spike:" );

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
  public void writeEmptyToken()
    throws Exception
  {
    final DataElement root = new DataElement( null, "planet", "" );

    final String output = writeElement( root );
    assertEquals( output, "planet \"\"\n" );
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
  public void assertTokenName()
  {
    final DataElement element = new DataElement( null, "planet", "Dune" );

    element.assertTokenName( "planet" );
  }

  @Test
  public void assertTokenName_errorWithNoLocation()
  {
    final DataElement element = new DataElement( null, "planet", "Dune" );

    final DataAccessException exception =
      expectThrows( DataAccessException.class, () -> element.assertTokenName( "Dune" ) );

    assertEquals( exception.getMessage(), "Data element named 'planet' expected to be named 'Dune'" );
    assertNull( exception.getLocation() );
  }

  @Test
  public void assertTokenName_errorWithLocation()
  {
    final SourceLocation location = new SourceLocation( "file.txt", 1, 0 );
    final DataElement element = new DataElement( location, null, "planet", "Dune" );

    final DataAccessException exception =
      expectThrows( DataAccessException.class, () -> element.assertTokenName( "Dune" ) );

    assertEquals( exception.getMessage(), "Data element named 'planet' expected to be named 'Dune'" );
    assertEquals( exception.getLocation(), location );
  }

  @Test
  public void assertTokenCount()
  {
    final DataElement element = new DataElement( null, "planet", "Dune" );
    new DataElement( element, "name", "The Red Planet" );

    element.assertTokenCount( 2 );
  }

  @Test
  public void assertTokenCount_error()
  {
    final SourceLocation location = new SourceLocation( "file.txt", 1, 0 );
    final DataElement element = new DataElement( location, null, "planet", "Dune" );
    new DataElement( element, "name", "The Red Planet" );

    final DataAccessException exception =
      expectThrows( DataAccessException.class, () -> element.assertTokenCount( 0 ) );

    assertEquals( exception.getMessage(), "Data element named 'planet' expected to contain 0 tokens but contains 2 tokens" );
    assertEquals( exception.getLocation(), location );
  }

  @Test
  public void assertTokenCount_error_emptyLocation()
  {
    final DataElement element = new DataElement( null, "planet", "Dune" );
    new DataElement( element, "name", "The Red Planet" );

    final DataAccessException exception =
      expectThrows( DataAccessException.class, () -> element.assertTokenCount( 0 ) );

    assertEquals( exception.getMessage(), "Data element named 'planet' expected to contain 0 tokens but contains 2 tokens" );
    assertNull( exception.getLocation() );
  }

  @Nonnull
  private String writeElement( @Nonnull final DataElement root )
    throws IOException
  {
    final Path file = createTempDataFile();
    final DataDocument document = new DataDocument();
    document.append( root );
    new DataFile( file, document ).write();
    return readContent( file );
  }
}
