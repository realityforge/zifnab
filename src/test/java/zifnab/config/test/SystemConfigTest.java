package zifnab.config.test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import zifnab.AbstractTest;
import zifnab.config.Position;
import zifnab.config.SystemConfig;
import zifnab.hdf.DataAccessException;
import zifnab.hdf.DataDocument;
import zifnab.hdf.DataElement;
import zifnab.hdf.DataFile;
import zifnab.hdf.SourceLocation;
import static org.testng.Assert.*;

public class SystemConfigTest
  extends AbstractTest
{
  @Test
  public void mutateLinks()
  {
    final SystemConfig system = new SystemConfig( randomString() );

    assertTrue( system.getLinks().isEmpty() );

    final String system1 = randomString();
    final String system2 = randomString();
    final String system3 = randomString();

    system.addLink( system1 );

    assertEquals( system.getLinks().size(), 1 );
    assertTrue( system.getLinks().contains( system1 ) );
    assertFalse( system.getLinks().contains( system2 ) );
    assertFalse( system.getLinks().contains( system3 ) );
    assertTrue( system.isLinked( system1 ) );
    assertFalse( system.isLinked( system2 ) );
    assertFalse( system.isLinked( system3 ) );

    system.addLink( system2 );

    assertEquals( system.getLinks().size(), 2 );
    assertTrue( system.getLinks().contains( system1 ) );
    assertTrue( system.getLinks().contains( system2 ) );
    assertFalse( system.getLinks().contains( system3 ) );
    assertTrue( system.isLinked( system1 ) );
    assertTrue( system.isLinked( system2 ) );
    assertFalse( system.isLinked( system3 ) );

    // Duplicate link is a no-op
    system.addLink( system2 );

    assertEquals( system.getLinks().size(), 2 );
    assertTrue( system.getLinks().contains( system1 ) );
    assertTrue( system.getLinks().contains( system2 ) );
    assertFalse( system.getLinks().contains( system3 ) );
    assertTrue( system.isLinked( system1 ) );
    assertTrue( system.isLinked( system2 ) );
    assertFalse( system.isLinked( system3 ) );

    assertTrue( system.removeLink( system2 ) );

    assertEquals( system.getLinks().size(), 1 );
    assertTrue( system.getLinks().contains( system1 ) );
    assertFalse( system.getLinks().contains( system2 ) );
    assertFalse( system.getLinks().contains( system3 ) );
    assertTrue( system.isLinked( system1 ) );
    assertFalse( system.isLinked( system2 ) );
    assertFalse( system.isLinked( system3 ) );

    assertFalse( system.removeLink( system2 ) );

    assertEquals( system.getLinks().size(), 1 );
    assertTrue( system.getLinks().contains( system1 ) );
    assertFalse( system.getLinks().contains( system2 ) );
    assertFalse( system.getLinks().contains( system3 ) );
    assertTrue( system.isLinked( system1 ) );
    assertFalse( system.isLinked( system2 ) );
    assertFalse( system.isLinked( system3 ) );
  }

  @Test
  public void parseCompletedSystem()
    throws Exception
  {
    final String data =
      "system \"Blue Zone\"\n" +
      "\tpos -100 50.5\n" +
      "\tgovernment Coalition\n" +
      "\tmusic dance123\n" +
      "\thabitable 1200.68\n" +
      "\tbelt 1090\n" +
      "\thaze redHaze\n" +
      "\tlink \"Other Place\"\n" +
      "\tlink \"Home System\"\n" +
      "\tasteroids \"small rock\" 2 4.1454\n" +
      "\tasteroids \"medium rock\" 28 3.2928\n" +
      "\tminables gold 17 3.14953\n" +
      "\tminables titanium 46 3.06136\n" +
      "\ttrade Clothing 202\n" +
      "\ttrade Electronics 723\n" +
      "\tfleet \"Small Arach\" 800\n" +
      "\tfleet Heliarch 400\n" +
      "\tobject\n" +
      "\t\tsprite star/g0\n" +
      "\t\tdistance 40.3837\n" +
      "\t\tperiod 14.9791\n" +
      "\tobject \"Zen - 2\"\n" +
      "\t\tsprite star/k0\n" +
      "\t\tdistance 89.6163\n" +
      "\t\tperiod 14.9791\n" +
      "\t\toffset 180";

    final SystemConfig system = parseSystemConfig( data );

    assertEquals( system.getName(), "Blue Zone" );
    assertEquals( system.getGovernment(), "Coalition" );
    assertEquals( system.getMusic(), "dance123" );
    assertEquals( system.getHabitable(), 1200.68D );
    assertEquals( system.getBelt(), 1090D );
    assertEquals( system.getHaze(), "redHaze" );

    final Position pos = system.getPos();
    assertNotNull( pos );
    assertEquals( pos.getX(), -100D );
    assertEquals( pos.getY(), 50.5D );

    final Set<String> links = system.getLinks();
    assertEquals( links.size(), 2 );
    assertTrue( links.contains( "Other Place" ) );
    assertTrue( links.contains( "Home System" ) );

    // Asteroids
    {
      assertEquals( system.getAsteroids().size(), 2 );

      final SystemConfig.Asteroid asteroid1 = system.findAsteroidByName( "small rock" );
      assertNotNull( asteroid1 );
      assertEquals( asteroid1.getName(), "small rock" );
      assertEquals( asteroid1.getCount(), 2 );
      assertEquals( asteroid1.getEnergy(), 4.1454D );

      final SystemConfig.Asteroid asteroid2 = system.findAsteroidByName( "medium rock" );
      assertNotNull( asteroid2 );
      assertEquals( asteroid2.getName(), "medium rock" );
      assertEquals( asteroid2.getCount(), 28 );
      assertEquals( asteroid2.getEnergy(), 3.2928D );
    }

    // Minables
    {
      assertEquals( system.getMinables().size(), 2 );

      final SystemConfig.Minable minable1 = system.findMinableByName( "gold" );
      assertNotNull( minable1 );
      assertEquals( minable1.getName(), "gold" );
      assertEquals( minable1.getCount(), 17 );
      assertEquals( minable1.getEnergy(), 3.14953D );

      final SystemConfig.Minable minable2 = system.findMinableByName( "titanium" );
      assertNotNull( minable2 );
      assertEquals( minable2.getName(), "titanium" );
      assertEquals( minable2.getCount(), 46 );
      assertEquals( minable2.getEnergy(), 3.06136D );

      assertNull( system.findMinableByName( "noExisto" ) );
    }

    // Trades
    {
      assertEquals( system.getTrades().size(), 2 );

      final SystemConfig.Trade trade1 = system.findTradeByName( "Clothing" );
      assertNotNull( trade1 );
      assertEquals( trade1.getName(), "Clothing" );
      assertEquals( trade1.getBase(), 202 );

      final SystemConfig.Trade trade2 = system.findTradeByName( "Electronics" );
      assertNotNull( trade2 );
      assertEquals( trade2.getName(), "Electronics" );
      assertEquals( trade2.getBase(), 723 );

      assertNull( system.findTradeByName( "NoExisto" ) );
    }

    // Fleets
    {
      assertEquals( system.getFleets().size(), 2 );

      final SystemConfig.Fleet fleet1 = system.findFleetByName( "Small Arach" );
      assertNotNull( fleet1 );
      assertEquals( fleet1.getName(), "Small Arach" );
      assertEquals( fleet1.getPeriod(), 800 );

      final SystemConfig.Fleet fleet2 = system.findFleetByName( "Heliarch" );
      assertNotNull( fleet2 );
      assertEquals( fleet2.getName(), "Heliarch" );
      assertEquals( fleet2.getPeriod(), 400 );

      assertNull( system.findFleetByName( "NoExisto" ) );
    }

    // Stellar Objects
    {
      assertEquals( system.getObjects().size(), 2 );

      final SystemConfig.StellarObject object1 =
        system.getObjects().stream().filter( o -> null == o.getName() ).findAny().orElse( null );
      assertNotNull( object1 );
      assertNull( object1.getName() );
      assertEquals( object1.getSprite(), "star/g0" );
      assertEquals( object1.getDistance(), 40.3837D );
      assertEquals( object1.getPeriod(), 14.9791D );
      assertEquals( object1.getOffset(), 0D );
      assertTrue( object1.getObjects().isEmpty() );

      final SystemConfig.StellarObject object2 = system.findObjectByName( "Zen - 2" );
      assertNotNull( object2 );
      assertEquals( object2.getName(), "Zen - 2" );
      assertEquals( object2.getSprite(), "star/k0" );
      assertEquals( object2.getDistance(), 89.6163D );
      assertEquals( object2.getPeriod(), 14.9791D );
      assertEquals( object2.getOffset(), 180D );
      assertTrue( object2.getObjects().isEmpty() );
    }
  }

  @Test
  public void parseMinimalSystem()
    throws Exception
  {
    final String data =
      "system \"Red Zone\"\n" +
      "\tpos 2.1 3.5\n";

    final SystemConfig system = parseSystemConfig( data );

    assertEquals( system.getName(), "Red Zone" );
    assertNull( system.getGovernment() );
    assertNull( system.getMusic() );
    assertNull( system.getHabitable() );
    assertNull( system.getBelt() );
    assertNull( system.getHaze() );

    final Position pos = system.getPos();
    assertNotNull( pos );
    assertEquals( pos.getX(), 2.1D );
    assertEquals( pos.getY(), 3.5D );

    assertTrue( system.getLinks().isEmpty() );
    assertTrue( system.getAsteroids().isEmpty() );
    assertTrue( system.getTrades().isEmpty() );
    assertTrue( system.getFleets().isEmpty() );
    assertTrue( system.getObjects().isEmpty() );
  }

  @Test
  public void parseWithNestedStellarObjects()
    throws Exception
  {
    final String data =
      "system \"Blue Zone\"\n" +
      "\tpos -100 50.5\n" +
      "\tobject Zen\n" +
      "\t\tsprite star/k0\n" +
      "\t\tdistance 89.6163\n" +
      "\t\tperiod 14.9791\n" +
      "\t\toffset 180\n" +
      "\t\tobject `Zen Minor`\n" +
      "\t\t\tsprite planet/luna\n" +
      "\t\t\tdistance 400.4\n" +
      "\t\t\tperiod 29.3\n" +
      "\t\t\tobject Whizzer\n" +
      "\t\t\t\tsprite planet/blue\n" +
      "\t\t\t\tdistance 50.1\n" +
      "\t\t\t\tperiod 4.2\n";

    final SystemConfig system = parseSystemConfig( data );

    assertEquals( system.getName(), "Blue Zone" );

    // Stellar Objects
    assertEquals( system.getObjects().size(), 1 );

    final SystemConfig.StellarObject zen = system.findObjectByName( "Zen" );
    assertNotNull( zen );
    assertEquals( zen.getName(), "Zen" );
    assertEquals( zen.getSprite(), "star/k0" );
    assertEquals( zen.getDistance(), 89.6163D );
    assertEquals( zen.getPeriod(), 14.9791D );
    assertEquals( zen.getOffset(), 180D );
    assertEquals( zen.getObjects().size(), 1 );

    final SystemConfig.StellarObject zenMinor = zen.findObjectByName( "Zen Minor" );
    assertNotNull( zenMinor );
    assertEquals( zenMinor.getName(), "Zen Minor" );
    assertEquals( zenMinor.getSprite(), "planet/luna" );
    assertEquals( zenMinor.getDistance(), 400.4D );
    assertEquals( zenMinor.getPeriod(), 29.3D );
    assertEquals( zenMinor.getOffset(), 0D );
    assertEquals( zenMinor.getObjects().size(), 1 );

    final SystemConfig.StellarObject whizzer = zenMinor.findObjectByName( "Whizzer" );
    assertNotNull( whizzer );
    assertEquals( whizzer.getName(), "Whizzer" );
    assertEquals( whizzer.getSprite(), "planet/blue" );
    assertEquals( whizzer.getDistance(), 50.1D );
    assertEquals( whizzer.getPeriod(), 4.2D );
    assertEquals( whizzer.getOffset(), 0D );
    assertTrue( whizzer.getObjects().isEmpty() );
  }

  @Test
  public void parseWhereStellarObjectHasUnknownKey()
  {
    final String data =
      "system \"Blue Zone\"\n" +
      "\tpos -100 50.5\n" +
      "\tobject Zen\n" +
      "\t\tsprite star/k0\n" +
      "\t\tdistance 89.6163\n" +
      "\t\tperiod 14.9791\n" +
      "\t\toffset 180\n" +
      "\t\tunknownkey 180\n";

    final DataAccessException exception =
      expectThrows( DataAccessException.class, () -> parseSystemConfig( data ) );

    assertEquals( exception.getMessage(), "Unexpected data element named 'unknownkey'" );
    final SourceLocation location = exception.getLocation();
    assertNotNull( location );
    assertEquals( location.getLineNumber(), 8 );
    assertEquals( location.getColumnNumber(), 2 );
  }

  @Test
  public void parseWhereSystemHasUnknownKey()
  {
    final String data =
      "system \"Blue Zone\"\n" +
      "\tpos -100 50.5\n" +
      "\tbadkey 180\n";

    final DataAccessException exception =
      expectThrows( DataAccessException.class, () -> parseSystemConfig( data ) );

    assertEquals( exception.getMessage(), "Unexpected data element named 'badkey'" );
    final SourceLocation location = exception.getLocation();
    assertNotNull( location );
    assertEquals( location.getLineNumber(), 3 );
    assertEquals( location.getColumnNumber(), 1 );
  }

  @Test
  public void parseInvalidProperty()
  {
    final String data =
      "system \"Blue Zone\"\n" +
      "\tpos 50.5\n";

    final DataAccessException exception =
      expectThrows( DataAccessException.class, () -> parseSystemConfig( data ) );

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
      "system \"Blue Zone\"\n" +
      "\tpos -100.0 50.5\n" +
      "\tgovernment Coalition\n" +
      "\thaze redHaze\n" +
      "\tmusic dance123\n" +
      "\thabitable 1200.68\n" +
      "\tbelt 1090.0\n" +
      "\tlink \"Home System\"\n" +
      "\tlink \"Other Place\"\n" +
      "\tasteroids \"medium rock\" 28 3.2928\n" +
      "\tasteroids \"small rock\" 2 4.1454\n" +
      "\tminables gold 17 3.14953\n" +
      "\tminables titanium 46 3.06136\n" +
      "\ttrade Clothing 202\n" +
      "\ttrade Electronics 723\n" +
      "\tfleet Heliarch 400\n" +
      "\tfleet \"Small Arach\" 800\n" +
      "\tobject\n" +
      "\t\tsprite star/g0\n" +
      "\t\tdistance 40.3837\n" +
      "\t\tperiod 14.9791\n" +
      "\tobject \"Zen - 2\"\n" +
      "\t\tsprite star/k0\n" +
      "\t\tdistance 89.6163\n" +
      "\t\tperiod 14.9791\n" +
      "\t\toffset 180.0\n";

    assertEncodedFormMatchesInputData( data );
  }

  @Test
  public void encodeForMinimal()
    throws Exception
  {
    final String data =
      "system \"Red Zone\"\n" +
      "\tpos 2.1 3.5\n";

    assertEncodedFormMatchesInputData( data );
  }

  @Test
  public void encodeWillReorderElementsAndNormalizeDoubles()
    throws Exception
  {
    final String data =
      "system \"Blue Zone\"\n" +
      "\tpos -100 50.5\n" +
      "\tgovernment Coalition\n" +
      "\tmusic dance123\n" +
      "\thabitable 1200.68\n" +
      "\tbelt 1090\n" +
      "\thaze redHaze\n" +
      "\tlink \"Other Place\"\n" +
      "\tlink \"Home System\"\n" +
      "\tasteroids \"small rock\" 2 4.1454\n" +
      "\tasteroids \"medium rock\" 28 3.2928\n" +
      "\tminables gold 17 3.14953\n" +
      "\tminables titanium 46 3.06136\n" +
      "\ttrade Clothing 202\n" +
      "\ttrade Electronics 723\n" +
      "\tfleet \"Small Arach\" 800\n" +
      "\tfleet Heliarch 400\n" +
      "\tobject\n" +
      "\t\tsprite star/g0\n" +
      "\t\tdistance 40.3837\n" +
      "\t\tperiod 14.9791\n" +
      "\tobject \"Zen - 2\"\n" +
      "\t\tsprite star/k0\n" +
      "\t\tdistance 89.6163\n" +
      "\t\tperiod 14.9791\n" +
      "\t\toffset 180";

    final String expected =
      "system \"Blue Zone\"\n" +
      "\tpos -100.0 50.5\n" +
      "\tgovernment Coalition\n" +
      "\thaze redHaze\n" +
      "\tmusic dance123\n" +
      "\thabitable 1200.68\n" +
      "\tbelt 1090.0\n" +
      "\tlink \"Home System\"\n" +
      "\tlink \"Other Place\"\n" +
      "\tasteroids \"medium rock\" 28 3.2928\n" +
      "\tasteroids \"small rock\" 2 4.1454\n" +
      "\tminables gold 17 3.14953\n" +
      "\tminables titanium 46 3.06136\n" +
      "\ttrade Clothing 202\n" +
      "\ttrade Electronics 723\n" +
      "\tfleet Heliarch 400\n" +
      "\tfleet \"Small Arach\" 800\n" +
      "\tobject\n" +
      "\t\tsprite star/g0\n" +
      "\t\tdistance 40.3837\n" +
      "\t\tperiod 14.9791\n" +
      "\tobject \"Zen - 2\"\n" +
      "\t\tsprite star/k0\n" +
      "\t\tdistance 89.6163\n" +
      "\t\tperiod 14.9791\n" +
      "\t\toffset 180.0\n";

    assertEncodedFormMatchesInputData( parseSystemConfig( data ), expected );
  }

  private void assertEncodedFormMatchesInputData( @Nonnull final String data )
    throws Exception
  {
    assertEncodedFormMatchesInputData( parseSystemConfig( data ), data );
  }

  @Nonnull
  private SystemConfig parseSystemConfig( @Nonnull final String data )
    throws Exception
  {
    final List<DataElement> elements = asDataDocument( data ).getChildElements();
    assertEquals( elements.size(), 1 );

    final DataElement element = elements.get( 0 );
    assertTrue( SystemConfig.matches( element ) );
    return SystemConfig.from( element );
  }

  private void assertEncodedFormMatchesInputData( @Nonnull final SystemConfig system,
                                                  @Nonnull final String inputData )
    throws IOException
  {
    final DataDocument document = new DataDocument();
    final DataElement element = SystemConfig.encode( document, system );

    assertNotNull( element );
    assertEquals( element.getName(), "system" );
    assertEquals( element.getStringAt( 1 ), system.getName() );

    final Path file = createTempDataFile();
    new DataFile( file, document ).write();
    final String encodedData = new String( Files.readAllBytes( file ), StandardCharsets.UTF_8 );
    assertEquals( encodedData, inputData );
  }
}
