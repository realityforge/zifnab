package zifnab.config.test;

import java.io.IOException;
import java.util.List;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import zifnab.AbstractTest;
import zifnab.config.SpriteConfig;
import zifnab.hdf.DataAccessException;
import zifnab.hdf.DataDocument;
import zifnab.hdf.DataElement;
import zifnab.hdf.SourceLocation;
import static org.testng.Assert.*;

public class SpriteConfigTest
  extends AbstractTest
{
  @Test
  public void parseSprite()
    throws Exception
  {
    final String data =
      "testwrapper\n" +
      "\tsprite \"effect/puff\"\n" +
      "\t\t\"no repeat\"\n" +
      "\t\t\"rewind\"\n" +
      "\t\t\"random start frame\"\n" +
      "\t\t\"delay\" 2\n" +
      "\t\t\"start frame\" 30.5\n" +
      "\t\t\"frame rate\" 10\n";

    final SpriteConfig sprite = parseSpriteConfig( data );

    assertEquals( sprite.getName(), "effect/puff" );
    assertEquals( sprite.getDelay(), 2D );
    assertEquals( sprite.getStartFrame(), 30.5D );
    assertEquals( sprite.getFrameRate(), 10D );
    assertEquals( sprite.getFrameTime(), 0D );
    assertTrue( sprite.isNoRepeat() );
    assertTrue( sprite.isRandomStartFrame() );
    assertTrue( sprite.isRewind() );
  }

  @Test
  public void parseSprite_frameTime()
    throws Exception
  {
    final String data =
      "testwrapper\n" +
      "\tsprite \"effect/puff\"\n" +
      "\t\t\"frame time\" 10\n";

    final SpriteConfig sprite = parseSpriteConfig( data );

    assertEquals( sprite.getName(), "effect/puff" );
    assertEquals( sprite.getDelay(), 0D );
    assertEquals( sprite.getStartFrame(), 0D );
    assertEquals( sprite.getFrameRate(), 0D );
    assertEquals( sprite.getFrameTime(), 10D );
    assertFalse( sprite.isNoRepeat() );
    assertFalse( sprite.isRandomStartFrame() );
    assertFalse( sprite.isRewind() );
  }

  @Test
  public void parseWithUnknownKey()
  {
    final String data =
      "testwrapper\n" +
      "\tsprite \"effect/puff\"\n" +
      "\t\tmeepmeep 180\n";

    final DataAccessException exception =
      expectThrows( DataAccessException.class, () -> parseSpriteConfig( data ) );

    assertEquals( exception.getMessage(), "Unexpected data element named 'meepmeep'" );
    final SourceLocation location = exception.getLocation();
    assertNotNull( location );
    assertEquals( location.getLineNumber(), 3 );
    assertEquals( location.getColumnNumber(), 2 );
  }

  @Test
  public void parseInvalidProperty()
  {
    final String data =
      "testwrapper\n" +
      "\tsprite \"effect/puff\"\n" +
      "\t\trewind 50.5\n";

    final DataAccessException exception =
      expectThrows( DataAccessException.class, () -> parseSpriteConfig( data ) );

    assertEquals( exception.getMessage(),
                  "Data element named 'rewind' expected to contain 1 tokens but contains 2 tokens" );
    final SourceLocation location = exception.getLocation();
    assertNotNull( location );
    assertEquals( location.getLineNumber(), 3 );
    assertEquals( location.getColumnNumber(), 2 );
  }

  @Test
  public void roundtripComplete()
    throws Exception
  {
    final String data =
      "testwrapper\n" +
      "\tsprite effect/puff\n" +
      "\t\t\"frame rate\" 10.0\n" +
      "\t\tdelay 2.0\n" +
      "\t\t\"start frame\" 30.5\n" +
      "\t\t\"random start frame\"\n" +
      "\t\t\"no repeat\"\n" +
      "\t\trewind\n";

    assertEncodedFormMatchesInputData( data );
  }

  @Test
  public void roundtripMinimal()
    throws Exception
  {
    final String data =
      "testwrapper\n" +
      "\tsprite effect/puff\n";

    assertEncodedFormMatchesInputData( data );
  }

  @Test
  public void encodeForMinimal()
    throws Exception
  {
    final String data =
      "testwrapper\n" +
      "\tsprite effect/puff\n";

    assertEncodedFormMatchesInputData( data );
  }

  @Test
  public void encodeWillReorderElementsAndNormalizeDoubles()
    throws Exception
  {
    final String data =
      "testwrapper\n" +
      "\tsprite \"effect/puff\"\n" +
      "\t\t\"no repeat\"\n" +
      "\t\t\"rewind\"\n" +
      "\t\t\"random start frame\"\n" +
      "\t\t\"delay\" 2\n" +
      "\t\t\"start frame\" 30.5\n" +
      "\t\t\"frame rate\" 10\n";

    final String expected =
      "testwrapper\n" +
      "\tsprite effect/puff\n" +
      "\t\t\"frame rate\" 10.0\n" +
      "\t\tdelay 2.0\n" +
      "\t\t\"start frame\" 30.5\n" +
      "\t\t\"random start frame\"\n" +
      "\t\t\"no repeat\"\n" +
      "\t\trewind\n";

    assertEncodedFormMatchesInputData( parseSpriteConfig( data ), expected );
  }

  private void assertEncodedFormMatchesInputData( @Nonnull final String data )
    throws Exception
  {
    assertEncodedFormMatchesInputData( parseSpriteConfig( data ), data );
  }

  @Nonnull
  private SpriteConfig parseSpriteConfig( @Nonnull final String data )
    throws Exception
  {
    final DataElement element = asDataElement( data );
    final List<DataElement> elements = element.getChildElements();
    assertEquals( elements.size(), 1 );
    return SpriteConfig.from( elements.get( 0 ) );
  }

  private void assertEncodedFormMatchesInputData( @Nonnull final SpriteConfig sprite,
                                                  @Nonnull final String inputData )
    throws IOException
  {
    final DataDocument document = new DataDocument();

    final DataElement element = SpriteConfig.encode( document.element( "testwrapper" ), "sprite", sprite );

    assertNotNull( element );
    assertEquals( element.getName(), "sprite" );
    assertEquals( element.getStringAt( 1 ), sprite.getName() );

    assertDocumentMatches( document, inputData );
  }
}
