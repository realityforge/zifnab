package zifnab.hdf.test;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.testng.annotations.Test;
import zifnab.AbstractTest;
import zifnab.hdf.DataComment;
import zifnab.hdf.DataDocument;
import zifnab.hdf.DataElement;
import zifnab.hdf.DataFile;
import zifnab.hdf.DataNode;
import zifnab.hdf.DataParseException;
import zifnab.hdf.SourceLocation;
import static org.testng.Assert.*;

public class ParserTest
  extends AbstractTest
{
  @Test
  public void parseSingleElementDocument()
    throws Exception
  {
    final Path file = createTempDataFile();
    writeContent( file, "planet Mars\n" );

    final DataDocument document = DataFile.read( file ).getDocument();

    final List<DataNode> children = document.getChildren();
    assertEquals( children.size(), 1 );

    assertChildLocation( children, 0, file, 1, 0 );
    final DataElement element0 = ensureChildIsElement( children, 0, null, "planet", "Mars" );
    assertTrue( element0.getChildren().isEmpty() );
  }

  @Test
  public void parseQuotedToken()
    throws Exception
  {
    final Path file = createTempDataFile();
    writeContent( file, "planet \"Mars\" \"The glorious red planet\"\n" );

    final DataDocument document = DataFile.read( file ).getDocument();

    final List<DataNode> children = document.getChildren();
    assertEquals( children.size(), 1 );

    assertChildLocation( children, 0, file, 1, 0 );
    final DataElement element0 = ensureChildIsElement( children, 0, null, "planet", "Mars", "The glorious red planet" );
    assertTrue( element0.getChildren().isEmpty() );
  }

  @Test
  public void parseQuotedTokenMissingLastQuote()
    throws Exception
  {
    final Path file = createTempDataFile();
    writeContent( file, "planet \"The glorious red planet\n" );

    final DataParseException exception = expectThrows( DataParseException.class, () -> DataFile.read( file ) );

    assertEquals( exception.getMessage(), "Quoted token missing closing \"" );
    assertLocation( exception.getLocation(), file, 1, 31 );
  }

  @Test
  public void parseBackTickedTokenMissingLastBackTick()
    throws Exception
  {
    final Path file = createTempDataFile();
    writeContent( file, "planet `The \"glorious\" red planet\n" );

    final DataParseException exception = expectThrows( DataParseException.class, () -> DataFile.read( file ) );

    assertEquals( exception.getMessage(), "Quoted token missing closing `" );
    assertLocation( exception.getLocation(), file, 1, 33 );
  }

  @Test
  public void parseBadIndentLevel()
    throws Exception
  {
    final Path file = createTempDataFile();
    writeContent( file,
                  "planet Mars\n" +
                  "\t\tname Mars\n" );

    final DataParseException exception = expectThrows( DataParseException.class, () -> DataFile.read( file ) );

    assertEquals( exception.getMessage(), "Invalid file. Moved from indent level 0 to indent level 2" );
    assertLocation( exception.getLocation(), file, 2, 2 );
  }

  @Test
  public void parseIndentBelowComment()
    throws Exception
  {
    final Path file = createTempDataFile();
    writeContent( file,
                  "# Some Comment\n" +
                  "\tname Mars\n" );

    final DataParseException exception = expectThrows( DataParseException.class, () -> DataFile.read( file ) );

    assertEquals( exception.getMessage(), "Attempted to define node below a comment" );
    assertLocation( exception.getLocation(), file, 2, 1 );
  }

  @Test
  public void parseWitIndentAtTopLevel()
    throws Exception
  {
    final Path file = createTempDataFile();
    writeContent( file,
                  "\n\n\n\n\n\tplanet Mars\n" );

    final DataParseException exception = expectThrows( DataParseException.class, () -> DataFile.read( file ) );

    assertEquals( exception.getMessage(), "Attempted to define node without a parent node" );
    assertLocation( exception.getLocation(), file, 6, 1 );
  }

  @Test
  public void parseTokensWithLeadingAndTrailingSpaces()
    throws Exception
  {
    final Path file = createTempDataFile();
    writeContent( file, "   planet      Mars  \n" );

    final DataDocument document = DataFile.read( file ).getDocument();

    final List<DataNode> children = document.getChildren();
    assertEquals( children.size(), 1 );

    assertChildLocation( children, 0, file, 1, 0 );
    final DataElement element0 = ensureChildIsElement( children, 0, null, "planet", "Mars" );
    assertTrue( element0.getChildren().isEmpty() );
  }

  @Test
  public void parseSkipsLinesThatOnlyContainWhitespace()
    throws Exception
  {
    final Path file = createTempDataFile();
    writeContent( file, "planet Mars\n" +
                        "\t\t\n" +
                        "\tname \"The red planet\"\n" );

    final DataDocument document = DataFile.read( file ).getDocument();

    final List<DataNode> children = document.getChildren();
    assertEquals( children.size(), 1 );

    assertChildLocation( children, 0, file, 1, 0 );
    final DataElement mars = ensureChildIsElement( children, 0, null, "planet", "Mars" );
    final List<DataNode> marsChildren = mars.getChildren();
    assertEquals( marsChildren.size(), 1 );
    assertChildLocation( marsChildren, 0, file, 3, 1 );
    final DataElement name = ensureChildIsElement( marsChildren, 0, mars, "name", "The red planet" );
    assertTrue( name.getChildren().isEmpty() );
  }

  @Test
  public void parseIndentWithNoContent()
    throws Exception
  {
    final Path file = createTempDataFile();
    writeContent( file, "planet Mars\n" +
                        "\t\n" +
                        "\tname \"God of War\"\n" );

    final DataDocument document = DataFile.read( file ).getDocument();

    final List<DataNode> children = document.getChildren();
    assertEquals( children.size(), 1 );

    assertChildLocation( children, 0, file, 1, 0 );
    final DataElement mars = ensureChildIsElement( children, 0, null, "planet", "Mars" );
    final List<DataNode> marsChildren = mars.getChildren();
    assertEquals( marsChildren.size(), 1 );
    final DataElement name = ensureChildIsElement( marsChildren, 0, mars, "name", "God of War" );
    assertEquals( name.getChildren().size(), 0 );
  }

  @Test
  public void parseBackTickedToken()
    throws Exception
  {
    final Path file = createTempDataFile();
    writeContent( file, "planet Mars `The \"glorious\" red planet`\n" );

    final DataDocument document = DataFile.read( file ).getDocument();

    final List<DataNode> children = document.getChildren();
    assertEquals( children.size(), 1 );

    assertChildLocation( children, 0, file, 1, 0 );
    final DataElement element0 =
      ensureChildIsElement( children, 0, null, "planet", "Mars", "The \"glorious\" red planet" );
    assertTrue( element0.getChildren().isEmpty() );
  }

  @Test
  public void parseSingleCommentDocument()
    throws Exception
  {
    final Path file = createTempDataFile();
    writeContent( file, "# Some comment\n" );

    final DataDocument document = DataFile.read( file ).getDocument();

    final List<DataNode> children = document.getChildren();
    assertEquals( children.size(), 1 );
    assertChildLocation( children, 0, file, 1, 0 );
    assertChildIsComment( children, 0, null, "Some comment" );
  }

  @Test
  public void parseSquashedComment()
    throws Exception
  {
    final Path file = createTempDataFile();
    writeContent( file, "#Some comment\n" );

    final DataDocument document = DataFile.read( file ).getDocument();

    final List<DataNode> children = document.getChildren();
    assertEquals( children.size(), 1 );
    assertChildLocation( children, 0, file, 1, 0 );
    assertChildIsComment( children, 0, null, "Some comment" );
  }

  @Test
  public void parseMultipleNodeDocument()
    throws Exception
  {
    final Path file = createTempDataFile();
    writeContent( file,
                  "# Birthplace of man\n" +
                  "planet Earth\n" +
                  "\n" +
                  "# The red planet\n" +
                  "planet Mars\n" );

    final DataDocument document = DataFile.read( file ).getDocument();

    final List<DataNode> children = document.getChildren();
    assertEquals( children.size(), 4 );

    assertChildLocation( children, 0, file, 1, 0 );
    assertChildIsComment( children, 0, null, "Birthplace of man" );

    assertChildLocation( children, 1, file, 2, 0 );
    final DataElement element1 = ensureChildIsElement( children, 1, null, "planet", "Earth" );
    assertTrue( element1.getChildren().isEmpty() );

    assertChildLocation( children, 2, file, 4, 0 );
    assertChildIsComment( children, 2, null, "The red planet" );

    assertChildLocation( children, 3, file, 5, 0 );
    final DataElement element3 = ensureChildIsElement( children, 3, null, "planet", "Mars" );
    assertTrue( element3.getChildren().isEmpty() );
  }

  @Test
  public void parseNestedElementDocument()
    throws Exception
  {
    final Path file = createTempDataFile();
    writeContent( file,
                  "# Birthplace of man\n" +
                  "planet Earth\n" +
                  "\tname Earth\n" +
                  "\ttribute 10000\n" +
                  "\t\tthreshold 20000\n" +
                  "\t\t# Ouch!\n" +
                  "\t\tfleet \"Large Republic\" 50\n" +
                  "\tattributes \"near earth\" urban tourism\n" +
                  "\tlandscape land/city3\n" );

    final DataDocument document = DataFile.read( file ).getDocument();

    final List<DataNode> children = document.getChildren();
    assertEquals( children.size(), 2 );
    assertChildLocation( children, 0, file, 1, 0 );
    assertChildIsComment( children, 0, null, "Birthplace of man" );

    assertChildLocation( children, 1, file, 2, 0 );
    final DataElement earth = ensureChildIsElement( children, 1, null, "planet", "Earth" );
    final List<DataNode> earthChildren = earth.getChildren();
    assertEquals( earthChildren.size(), 4 );

    assertChildLocation( earthChildren, 0, file, 3, 1 );
    assertChildIsLeafElement( earthChildren, 0, earth, "name", "Earth" );

    assertChildLocation( earthChildren, 1, file, 4, 1 );
    final DataElement tribute = ensureChildIsElement( earthChildren, 1, earth, "tribute", "10000" );

    assertChildLocation( earthChildren, 2, file, 8, 1 );
    assertChildIsLeafElement( earthChildren, 2, earth, "attributes", "near earth", "urban", "tourism" );

    assertChildLocation( earthChildren, 3, file, 9, 1 );
    assertChildIsLeafElement( earthChildren, 3, earth, "landscape", "land/city3" );

    final List<DataNode> tributeChildren = tribute.getChildren();
    assertEquals( tributeChildren.size(), 3 );

    assertChildLocation( tributeChildren, 0, file, 5, 2 );
    assertChildIsLeafElement( tributeChildren, 0, tribute, "threshold", "20000" );

    assertChildLocation( tributeChildren, 1, file, 6, 2 );
    assertChildIsComment( tributeChildren, 1, tribute, "Ouch!" );

    assertChildLocation( tributeChildren, 2, file, 7, 2 );
    assertChildIsLeafElement( tributeChildren, 2, tribute, "fleet", "Large Republic", "50" );
  }

  @Nonnull
  private DataElement ensureChildIsElement( @Nonnull final List<DataNode> children,
                                            final int index,
                                            @Nullable final DataElement parent,
                                            @Nonnull final String... tokens )
  {
    final DataNode node0 = children.get( index );
    assertTrue( node0 instanceof DataElement );
    final DataElement element0 = (DataElement) node0;
    assertEquals( element0.getParent(), parent );
    assertEquals( element0.getTokens(), Arrays.asList( tokens ) );
    return element0;
  }

  private void assertChildIsLeafElement( @Nonnull final List<DataNode> children,
                                         final int index,
                                         @Nullable final DataElement parent,
                                         @Nonnull final String... tokens )
  {
    final DataElement element = ensureChildIsElement( children, index, parent, tokens );
    assertTrue( element.getChildren().isEmpty() );
  }

  private void assertChildLocation( @Nonnull final List<DataNode> children,
                                    final int index,
                                    @Nonnull final Path file,
                                    final int lineNumber,
                                    final int columnNumber )
  {
    assertLocation( children.get( index ), file, lineNumber, columnNumber );
  }

  private void assertLocation( @Nonnull final DataNode node0,
                               @Nonnull final Path file,
                               final int lineNumber,
                               final int columnNumber )
  {
    final SourceLocation location = node0.getLocation();
    assertNotNull( location );
    assertLocation( location, file, lineNumber, columnNumber );
  }

  private void assertLocation( @Nonnull final SourceLocation location,
                               @Nonnull final Path file,
                               final int lineNumber,
                               final int columnNumber )
  {
    assertEquals( location.getFilename(), file.toString() );
    assertEquals( location.getLineNumber(), lineNumber );
    assertEquals( location.getColumnNumber(), columnNumber );
  }

  private void assertChildIsComment( @Nonnull final List<DataNode> children,
                                     final int index,
                                     @Nullable final DataElement parent,
                                     @Nonnull final String comment )
  {
    final DataNode node0 = children.get( index );
    assertTrue( node0 instanceof DataComment );
    final DataComment comment0 = (DataComment) node0;
    assertComment( comment0, parent, comment );
  }

  private void assertComment( @Nonnull final DataComment node,
                              @Nullable final DataElement parent,
                              @Nonnull final String comment )
  {
    assertEquals( node.getParent(), parent );
    assertEquals( node.getComment(), comment );
  }
}
