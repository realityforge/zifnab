package zifnab.config.test;

import java.io.IOException;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import zifnab.AbstractTest;
import zifnab.config.EffectConfig;
import zifnab.config.SpriteConfig;
import zifnab.hdf.DataAccessException;
import zifnab.hdf.DataDocument;
import zifnab.hdf.DataElement;
import zifnab.hdf.SourceLocation;
import static org.testng.Assert.*;

public class EffectConfigTest
  extends AbstractTest
{
  @Test
  public void parseEffect()
    throws Exception
  {
    final String data =
      "effect \"bombardment impact\"\n" +
      "\tsprite \"effect/bombardment impact\"\n" +
      "\t\t\"frame rate\" 40\n" +
      "\t\"lifetime\" 7\n" +
      "\t\"random angle\" 40\n" +
      "\t\"random spin\" 20\n" +
      "\t\"random velocity\" 2\n" +
      "\t\"random frame rate\" 4\n" +
      "\t\"velocity scale\" -.2\n" +
      "\tsound boom\n";

    final EffectConfig effect = parseEffectConfig( data );

    assertEquals( effect.getName(), "bombardment impact" );
    assertEquals( effect.getLifetime(), 7 );
    assertEquals( effect.getRandomAngle(), 40D );
    assertEquals( effect.getRandomSpin(), 20D );
    assertEquals( effect.getRandomVelocity(), 2D );
    assertEquals( effect.getVelocityScale(), -0.2D );
    assertEquals( effect.getRandomFrameRate(), 4D );
    assertEquals( effect.getSound(), "boom" );

    final SpriteConfig sprite = effect.getSprite();
    assertNotNull( sprite );
    assertEquals( sprite.getName(), "effect/bombardment impact" );
    assertEquals( sprite.getFrameRate(), 40D );
  }

  @Test
  public void parseEffectMinimal()
    throws Exception
  {
    final String data =
      "effect \"bombardment impact\"\n" +
      "\tsprite \"effect/bombardment impact\"\n";

    final EffectConfig effect = parseEffectConfig( data );

    assertEquals( effect.getName(), "bombardment impact" );
    assertEquals( effect.getLifetime(), 0 );
    assertEquals( effect.getRandomAngle(), 0D );
    assertEquals( effect.getRandomSpin(), 0D );
    assertEquals( effect.getRandomVelocity(), 0D );
    assertEquals( effect.getVelocityScale(), 0D );
    assertEquals( effect.getRandomFrameRate(), 0D );
    assertNull( effect.getSound() );
    final SpriteConfig sprite = effect.getSprite();
    assertNotNull( sprite );
    assertEquals( sprite.getName(), "effect/bombardment impact" );
  }

  @Test
  public void parseWithUnknownKey()
  {
    final String data =
      "effect \"bombardment impact\"\n" +
      "\tsprite \"effect/bombardment impact\"\n" +
      "\tmeepmeep 180\n";

    final DataAccessException exception =
      expectThrows( DataAccessException.class, () -> parseEffectConfig( data ) );

    assertEquals( exception.getMessage(), "Unexpected data element named 'meepmeep'" );
    final SourceLocation location = exception.getLocation();
    assertNotNull( location );
    assertEquals( location.getLineNumber(), 3 );
    assertEquals( location.getColumnNumber(), 1 );
  }

  @Test
  public void parseInvalidProperty()
  {
    final String data =
      "effect \"Blue Zone\"\n" +
      "\t\"random angle\" XX\n";

    final DataAccessException exception =
      expectThrows( DataAccessException.class, () -> parseEffectConfig( data ) );

    assertEquals( exception.getMessage(),
                  "Token at index 1 for data element named 'random angle' has value 'XX' which is not an double" );
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
      "effect \"bombardment impact\"\n" +
      "\tsprite \"effect/bombardment impact\"\n" +
      "\t\t\"frame rate\" 40.0\n" +
      "\tsound boom\n" +
      "\tlifetime 7\n" +
      "\t\"velocity scale\" -0.2\n" +
      "\t\"random velocity\" 2.0\n" +
      "\t\"random angle\" 40.0\n" +
      "\t\"random spin\" 20.0\n" +
      "\t\"random frame rate\" 4.0\n";

    assertEncodedFormMatchesInputData( data );
  }

  @Test
  public void encodeForMinimal()
    throws Exception
  {
    final String data =
      "effect \"bombardment impact\"\n" +
      "\tsprite \"effect/bombardment impact\"\n";

    assertEncodedFormMatchesInputData( data );
  }

  @Test
  public void encodeWillReorderElementsAndNormalizeDoubles()
    throws Exception
  {
    final String data =
      "effect \"bombardment impact\"\n" +
      "\tsprite \"effect/bombardment impact\"\n" +
      "\t\t\"frame rate\" 40\n" +
      "\t\"lifetime\" 7\n" +
      "\t\"random angle\" 40\n" +
      "\t\"random spin\" 20\n" +
      "\t\"random velocity\" 2\n" +
      "\t\"random frame rate\" 4\n" +
      "\t\"velocity scale\" -.2\n" +
      "\tsound boom\n";

    final String expected =
      "effect \"bombardment impact\"\n" +
      "\tsprite \"effect/bombardment impact\"\n" +
      "\t\t\"frame rate\" 40.0\n" +
      "\tsound boom\n" +
      "\tlifetime 7\n" +
      "\t\"velocity scale\" -0.2\n" +
      "\t\"random velocity\" 2.0\n" +
      "\t\"random angle\" 40.0\n" +
      "\t\"random spin\" 20.0\n" +
      "\t\"random frame rate\" 4.0\n";

    assertEncodedFormMatchesInputData( parseEffectConfig( data ), expected );
  }

  private void assertEncodedFormMatchesInputData( @Nonnull final String data )
    throws Exception
  {
    assertEncodedFormMatchesInputData( parseEffectConfig( data ), data );
  }

  @Nonnull
  private EffectConfig parseEffectConfig( @Nonnull final String data )
    throws Exception
  {
    final DataElement element = asDataElement( data );
    assertTrue( EffectConfig.matches( element ) );
    return EffectConfig.from( element );
  }

  private void assertEncodedFormMatchesInputData( @Nonnull final EffectConfig effect,
                                                  @Nonnull final String inputData )
    throws IOException
  {
    final DataDocument document = new DataDocument();
    final DataElement element = EffectConfig.encode( document, effect );

    assertNotNull( element );
    assertEquals( element.getName(), "effect" );
    assertEquals( element.getStringAt( 1 ), effect.getName() );

    assertDocumentMatches( document, inputData );
  }
}
