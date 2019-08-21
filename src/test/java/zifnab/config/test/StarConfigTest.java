package zifnab.config.test;

import java.io.IOException;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import zifnab.AbstractTest;
import zifnab.config.StarConfig;
import zifnab.hdf.DataAccessException;
import zifnab.hdf.DataDocument;
import zifnab.hdf.DataElement;
import zifnab.hdf.SourceLocation;
import static org.testng.Assert.*;

public class StarConfigTest
  extends AbstractTest
{
  @Test
  public void parseStar()
    throws Exception
  {
    final String data =
      "star \"star/b5\"\n" +
      "\tpower 2.5\n" +
      "\twind 0.54\n";

    final StarConfig star = parseStarConfig( data );

    assertEquals( star.getName(), "star/b5" );
    assertEquals( star.getPower(), 2.5D );
    assertEquals( star.getWind(), 0.54D );
  }

  @Test
  public void parseWithUnknownKey()
  {
    final String data =
      "star \"star/b5\"\n" +
      "\tpower 2.5\n" +
      "\twind 0.54\n" +
      "\tmeepmeep 180\n";

    final DataAccessException exception =
      expectThrows( DataAccessException.class, () -> parseStarConfig( data ) );

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
      "star \"star/b5\"\n" +
      "\tpower X\n" +
      "\twind 0.54\n";

    final DataAccessException exception =
      expectThrows( DataAccessException.class, () -> parseStarConfig( data ) );

    assertEquals( exception.getMessage(),
                  "Token at index 1 for data element named 'power' has value 'X' which is not an double" );
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
      "star star/b5\n" +
      "\tpower 2.5\n" +
      "\twind 0.54\n";

    assertEncodedFormMatchesInputData( data );
  }

  @Test
  public void encodeForMinimal()
    throws Exception
  {
    final String data =
      "star star/blue\n";

    assertEncodedFormMatchesInputData( data );
  }

  @Test
  public void encodeWillReorderElementsAndNormalizeDoubles()
    throws Exception
  {
    final String data =
      "star \"star/b5\"\n" +
      "\tpower 2.5\n" +
      "\twind .54\n";

    final String expected =
      "star star/b5\n" +
      "\tpower 2.5\n" +
      "\twind 0.54\n";

    assertEncodedFormMatchesInputData( parseStarConfig( data ), expected );
  }

  private void assertEncodedFormMatchesInputData( @Nonnull final String data )
    throws Exception
  {
    assertEncodedFormMatchesInputData( parseStarConfig( data ), data );
  }

  @Nonnull
  private StarConfig parseStarConfig( @Nonnull final String data )
    throws Exception
  {
    final DataElement element = asDataElement( data );
    assertTrue( StarConfig.matches( element ) );
    return StarConfig.from( element );
  }

  private void assertEncodedFormMatchesInputData( @Nonnull final StarConfig star,
                                                  @Nonnull final String inputData )
    throws IOException
  {
    final DataDocument document = new DataDocument();
    final DataElement element = StarConfig.encode( document, star );

    assertNotNull( element );
    assertEquals( element.getName(), "star" );
    assertEquals( element.getStringAt( 1 ), star.getName() );

    assertDocumentMatches( document, inputData );
  }
}
