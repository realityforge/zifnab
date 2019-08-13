package zifnab.config.test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import zifnab.AbstractTest;
import zifnab.config.GalaxyConfig;
import zifnab.config.Position;
import zifnab.hdf.DataAccessException;
import zifnab.hdf.DataDocument;
import zifnab.hdf.DataElement;
import zifnab.hdf.DataFile;
import zifnab.hdf.SourceLocation;
import static org.testng.Assert.*;

public class GalaxyConfigTest
  extends AbstractTest
{
  @Test
  public void parseGalaxy()
    throws Exception
  {
    final String data =
      "galaxy \"Dirt Belt\"\n" +
      "\tpos -515 260\n" +
      "\tsprite \"label/dirt belt\"\n";

    final GalaxyConfig galaxy = parseGalaxyConfig( data );

    assertEquals( galaxy.getName(), "Dirt Belt" );
    assertEquals( galaxy.getSprite(), "label/dirt belt" );

    final Position pos = galaxy.getPos();
    assertNotNull( pos );
    assertEquals( pos.getX(), -515D );
    assertEquals( pos.getY(), 260D );
  }

  @Test
  public void parseGalaxyMinimal()
    throws Exception
  {
    final String data =
      "galaxy \"Dirt Belt\"\n" +
      "\tpos -515 260\n";

    final GalaxyConfig galaxy = parseGalaxyConfig( data );

    assertEquals( galaxy.getName(), "Dirt Belt" );
    assertNull( galaxy.getSprite() );

    final Position pos = galaxy.getPos();
    assertNotNull( pos );
    assertEquals( pos.getX(), -515D );
    assertEquals( pos.getY(), 260D );
  }

  @Test
  public void parseWithUnknownKey()
  {
    final String data =
      "galaxy \"Dirt Belt\"\n" +
      "\tpos -515 260\n" +
      "\tsprite \"label/dirt belt\"\n" +
      "\tmeepmeep 180\n";

    final DataAccessException exception =
      expectThrows( DataAccessException.class, () -> parseGalaxyConfig( data ) );

    assertEquals( exception.getMessage(), "Unexpected data element named 'meepmeep'" );
    final SourceLocation location = exception.getLocation();
    assertNotNull( location );
    assertEquals( location.getLineNumber(), 4 );
    assertEquals( location.getColumnNumber(), 1 );
  }

  @Test
  public void parseInvalidProperty()
  {
    final String data =
      "galaxy \"Blue Zone\"\n" +
      "\tpos 50.5\n";

    final DataAccessException exception =
      expectThrows( DataAccessException.class, () -> parseGalaxyConfig( data ) );

    assertEquals( exception.getMessage(),
                  "Data element named 'pos' expected to contain 3 tokens but contains 2 tokens" );
    final SourceLocation location = exception.getLocation();
    assertNotNull( location );
    assertEquals( location.getLineNumber(), 2 );
    assertEquals( location.getColumnNumber(), 1 );
  }

  @Test
  public void encodeForComplete()
    throws Exception
  {
    final String data =
      "galaxy \"Dirt Belt\"\n" +
      "\tpos -515.0 260.0\n" +
      "\tsprite \"label/dirt belt\"\n";

    assertEncodedFormMatchesInputData( data );
  }

  @Test
  public void encodeForMinimal()
    throws Exception
  {
    final String data =
      "galaxy \"Red Zone\"\n" +
      "\tpos 2.1 3.5\n";

    assertEncodedFormMatchesInputData( data );
  }

  @Test
  public void encodeWillReorderElementsAndNormalizeDoubles()
    throws Exception
  {
    final String data =
      "galaxy \"Dirt Belt\"\n" +
      "\tsprite \"label/dirt belt\"\n" +
      "\tpos -515 260\n";

    final String expected =
      "galaxy \"Dirt Belt\"\n" +
      "\tpos -515.0 260.0\n" +
      "\tsprite \"label/dirt belt\"\n";

    assertEncodedFormMatchesInputData( parseGalaxyConfig( data ), expected );
  }

  private void assertEncodedFormMatchesInputData( @Nonnull final String data )
    throws Exception
  {
    assertEncodedFormMatchesInputData( parseGalaxyConfig( data ), data );
  }

  @Nonnull
  private GalaxyConfig parseGalaxyConfig( @Nonnull final String data )
    throws Exception
  {
    final List<DataElement> elements = asDataDocument( data ).getChildElements();
    assertEquals( elements.size(), 1 );

    final DataElement element = elements.get( 0 );
    assertTrue( GalaxyConfig.matches( element ) );
    return GalaxyConfig.from( element );
  }

  private void assertEncodedFormMatchesInputData( @Nonnull final GalaxyConfig galaxy,
                                                  @Nonnull final String inputData )
    throws IOException
  {
    final DataDocument document = new DataDocument();
    final DataElement element = GalaxyConfig.encode( document, galaxy );

    assertNotNull( element );
    assertEquals( element.getName(), "galaxy" );
    assertEquals( element.getStringAt( 1 ), galaxy.getName() );

    final Path file = createTempDataFile();
    new DataFile( file, document ).write();
    final String encodedData = new String( Files.readAllBytes( file ), StandardCharsets.UTF_8 );
    assertEquals( encodedData, inputData );
  }
}
