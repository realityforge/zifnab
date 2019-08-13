package zifnab.config.test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import zifnab.AbstractTest;
import zifnab.config.TradeConfig;
import zifnab.hdf.DataAccessException;
import zifnab.hdf.DataDocument;
import zifnab.hdf.DataElement;
import zifnab.hdf.DataFile;
import zifnab.hdf.SourceLocation;
import static org.testng.Assert.*;

public class TradeConfigTest
  extends AbstractTest
{
  @Test
  public void mutateCommodities()
  {
    final TradeConfig trade = new TradeConfig();

    final String commodity1 = randomString();
    final String commodity2 = randomString();

    assertEquals( trade.getCommodities().size(), 0 );

    final TradeConfig.Commodity commodity1Instance = trade.addSpecialCommodity( commodity1 );

    assertEquals( trade.getCommodities().size(), 1 );
    assertTrue( trade.getCommodities().contains( commodity1Instance ) );
    assertNotNull( trade.findCommodityByName( commodity1 ) );
    assertNull( trade.findCommodityByName( commodity2 ) );

    final TradeConfig.Commodity commodity2Instance =
      trade.addCommodity( commodity2, randomPositiveInt(), randomPositiveInt() );

    assertEquals( trade.getCommodities().size(), 2 );
    assertTrue( trade.getCommodities().contains( commodity1Instance ) );
    assertTrue( trade.getCommodities().contains( commodity2Instance ) );
    assertNotNull( trade.findCommodityByName( commodity1 ) );
    assertNotNull( trade.findCommodityByName( commodity2 ) );

    assertTrue( trade.removeCommodity( commodity2Instance ) );

    assertEquals( trade.getCommodities().size(), 1 );
    assertTrue( trade.getCommodities().contains( commodity1Instance ) );
    assertNotNull( trade.findCommodityByName( commodity1 ) );
    assertNull( trade.findCommodityByName( commodity2 ) );

    // Already deleted so this is a noop
    assertFalse( trade.removeCommodity( commodity2 ) );

    assertEquals( trade.getCommodities().size(), 1 );
    assertTrue( trade.getCommodities().contains( commodity1Instance ) );
    assertNotNull( trade.findCommodityByName( commodity1 ) );
    assertNull( trade.findCommodityByName( commodity2 ) );

    // Already deleted so this is a noop
    assertTrue( trade.removeCommodity( commodity1 ) );

    assertEquals( trade.getCommodities().size(), 0 );
    assertNull( trade.findCommodityByName( commodity1 ) );
    assertNull( trade.findCommodityByName( commodity2 ) );
  }

  @Test
  public void parseStandardCommodityTrade()
    throws Exception
  {
    final String data =
      "trade\n" +
      "\tcommodity \"Food\" 100 600\n" +
      "\t\t\"apple cakes\"\n" +
      "\t\t\"burgers\"\n" +
      "\t\t\"cake\"\n";

    final TradeConfig trade = parseConfig( data );

    assertEquals( trade.getCommodities().size(), 1 );

    assertNull( trade.findCommodityByName( "NoExisto" ) );

    final TradeConfig.Commodity commodity = trade.findCommodityByName( "Food" );
    assertNotNull( commodity );

    assertEquals( commodity.getName(), "Food" );
    assertEquals( commodity.getLow(), (Integer) 100 );
    assertEquals( commodity.getHigh(), (Integer) 600 );
    assertFalse( commodity.isSpecial() );

    final Set<String> items = commodity.getItems();
    assertEquals( items.size(), 3 );
    assertTrue( items.contains( "apple cakes" ) );
    assertTrue( items.contains( "burgers" ) );
    assertTrue( items.contains( "cake" ) );
  }

  @Test
  public void parseSpecialCommodityTrade()
    throws Exception
  {
    final String data =
      "trade\n" +
      "\tcommodity Toys\n" +
      "\t\tLego\n" +
      "\t\tPuzzles\n" +
      "\t\tTransformers\n";

    final TradeConfig trade = parseConfig( data );

    assertEquals( trade.getCommodities().size(), 1 );

    assertNull( trade.findCommodityByName( "NoExisto" ) );

    final TradeConfig.Commodity commodity = trade.findCommodityByName( "Toys" );
    assertNotNull( commodity );

    assertEquals( commodity.getName(), "Toys" );
    assertNull( commodity.getLow() );
    assertNull( commodity.getHigh() );
    assertTrue( commodity.isSpecial() );

    final Set<String> items = commodity.getItems();
    assertEquals( items.size(), 3 );
    assertTrue( items.contains( "Lego" ) );
    assertTrue( items.contains( "Puzzles" ) );
    assertTrue( items.contains( "Transformers" ) );
  }

  @Test
  public void parseMultipleCommodities()
    throws Exception
  {
    final String data =
      "trade\n" +
      "\tcommodity \"Food\" 100 600\n" +
      "\t\t\"apple cakes\"\n" +
      "\tcommodity Toys\n" +
      "\t\tLego\n";

    final TradeConfig trade = parseConfig( data );

    assertEquals( trade.getCommodities().size(), 2 );

    assertNotNull( trade.findCommodityByName( "Toys" ) );
    assertNotNull( trade.findCommodityByName( "Food" ) );
  }

  @Test
  public void parseWithBadKey()
  {
    final String data =
      "trade\n" +
      "\tbeep\n" +
      "\tcommodity \"Food\" 100 600\n" +
      "\t\t\"apple cakes\"\n";

    final DataAccessException exception =
      expectThrows( DataAccessException.class, () -> parseConfig( data ) );

    assertEquals( exception.getMessage(), "Unexpected data element named 'beep'" );
    final SourceLocation location = exception.getLocation();
    assertNotNull( location );
    assertEquals( location.getLineNumber(), 2 );
    assertEquals( location.getColumnNumber(), 1 );
  }

  @Test
  public void parseInvalidProperty()
  {
    final String data =
      "trade\n" +
      "\tcommodity \"Food\" 100\n" +
      "\t\t\"apple cakes\"\n";

    final DataAccessException exception =
      expectThrows( DataAccessException.class, () -> parseConfig( data ) );

    assertEquals( exception.getMessage(),
                  "Data element named 'commodity' expected to contain tokens with a count matching one of [2, 4] but contains 3 tokens" );
    final SourceLocation location = exception.getLocation();
    assertNotNull( location );
    assertEquals( location.getLineNumber(), 2 );
    assertEquals( location.getColumnNumber(), 1 );
  }

  @Test
  public void parseInvalidItem()
  {
    final String data =
      "trade\n" +
      "\tcommodity \"Food\" 100 600\n" +
      "\t\t\"apple cakes\" x2\n";

    final DataAccessException exception =
      expectThrows( DataAccessException.class, () -> parseConfig( data ) );

    assertEquals( exception.getMessage(),
                  "Data element named 'apple cakes' expected to contain 1 tokens but contains 2 tokens" );
    final SourceLocation location = exception.getLocation();
    assertNotNull( location );
    assertEquals( location.getLineNumber(), 3 );
    assertEquals( location.getColumnNumber(), 2 );
  }

  @Test
  public void encodeForComplete()
    throws Exception
  {
    final String data =
      "trade\n" +
      "\tcommodity Food 100 600\n" +
      "\t\t\"apple cakes\"\n" +
      "\t\tburgers\n" +
      "\t\tcake\n" +
      "\tcommodity Toys\n" +
      "\t\tLego\n" +
      "\t\tPuzzles\n" +
      "\t\tTransformers\n";

    assertEncodedFormMatchesInputData( parseConfig( data ), data );
  }

  @Test
  public void encodeWillReorderElements()
    throws Exception
  {
    final String data =
      "trade\n" +
      "\tcommodity Toys\n" +
      "\t\tLego\n" +
      "\t\tPuzzles\n" +
      "\t\tTransformers\n" +
      "\tcommodity \"Food\" 100 600\n" +
      "\t\t\"apple cakes\"\n" +
      "\t\t\"burgers\"\n" +
      "\t\t\"cake\"\n";

    final String expected =
      "trade\n" +
      "\tcommodity Food 100 600\n" +
      "\t\t\"apple cakes\"\n" +
      "\t\tburgers\n" +
      "\t\tcake\n" +
      "\tcommodity Toys\n" +
      "\t\tLego\n" +
      "\t\tPuzzles\n" +
      "\t\tTransformers\n";

    assertEncodedFormMatchesInputData( parseConfig( data ), expected );
  }

  @Nonnull
  private TradeConfig parseConfig( @Nonnull final String data )
    throws Exception
  {
    final DataElement element = asDataElement( data );
    assertTrue( TradeConfig.matches( element ) );
    return TradeConfig.from( element );
  }

  @SuppressWarnings( "SameParameterValue" )
  private void assertEncodedFormMatchesInputData( @Nonnull final TradeConfig config,
                                                  @Nonnull final String inputData )
    throws IOException
  {
    final DataDocument document = new DataDocument();
    final DataElement element = TradeConfig.encode( document, config );

    assertNotNull( element );
    assertEquals( element.getName(), "trade" );

    final Path file = createTempDataFile();
    new DataFile( file, document ).write();
    final String encodedData = new String( Files.readAllBytes( file ), StandardCharsets.UTF_8 );
    assertEquals( encodedData, inputData );
  }
}
