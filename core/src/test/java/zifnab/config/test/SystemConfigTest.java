package zifnab.config.test;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import zifnab.AbstractTest;
import zifnab.config.Position;
import zifnab.config.SpriteConfig;
import zifnab.config.SystemConfig;
import zifnab.hdf.DataAccessException;
import zifnab.hdf.DataDocument;
import zifnab.hdf.DataElement;
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
  public void mutateAsteroids()
  {
    final SystemConfig system = new SystemConfig( randomString() );

    assertTrue( system.getAsteroids().isEmpty() );

    final String asteroid1 = randomString();
    final String asteroid2 = randomString();
    final String asteroid3 = randomString();

    final SystemConfig.Asteroid asteroid1Instance =
      system.addAsteroid( asteroid1, randomPositiveInt(), randomPositiveDouble() );

    assertEquals( system.getAsteroids().size(), 1 );
    assertTrue( system.getAsteroids().contains( asteroid1Instance ) );
    assertNotNull( system.findAsteroidByName( asteroid1 ) );
    assertNull( system.findAsteroidByName( asteroid2 ) );
    assertNull( system.findAsteroidByName( asteroid3 ) );

    final SystemConfig.Asteroid asteroid2aInstance =
      system.addAsteroid( asteroid2, randomPositiveInt(), randomPositiveDouble() );

    assertEquals( system.getAsteroids().size(), 2 );
    assertTrue( system.getAsteroids().contains( asteroid1Instance ) );
    assertTrue( system.getAsteroids().contains( asteroid2aInstance ) );
    assertNotNull( system.findAsteroidByName( asteroid1 ) );
    assertNotNull( system.findAsteroidByName( asteroid2 ) );
    assertNull( system.findAsteroidByName( asteroid3 ) );

    // Add with same name is just an update
    final SystemConfig.Asteroid asteroid2bInstance =
      system.addAsteroid( asteroid2, randomPositiveInt(), randomPositiveDouble() );

    assertEquals( system.getAsteroids().size(), 2 );
    assertTrue( system.getAsteroids().contains( asteroid1Instance ) );
    assertFalse( system.getAsteroids().contains( asteroid2aInstance ) );
    assertTrue( system.getAsteroids().contains( asteroid2bInstance ) );
    assertNotNull( system.findAsteroidByName( asteroid1 ) );
    assertNotNull( system.findAsteroidByName( asteroid2 ) );
    assertNull( system.findAsteroidByName( asteroid3 ) );

    final SystemConfig.Asteroid asteroid2Instance = system.findAsteroidByName( asteroid2 );
    assertTrue( system.removeAsteroid( Objects.requireNonNull( asteroid2Instance ) ) );

    assertEquals( system.getAsteroids().size(), 1 );
    assertTrue( system.getAsteroids().contains( asteroid1Instance ) );
    assertFalse( system.getAsteroids().contains( asteroid2aInstance ) );
    assertFalse( system.getAsteroids().contains( asteroid2bInstance ) );
    assertNotNull( system.findAsteroidByName( asteroid1 ) );
    assertNull( system.findAsteroidByName( asteroid2 ) );
    assertNull( system.findAsteroidByName( asteroid3 ) );

    // Re-add asteroid with the same name but different instances
    system.addAsteroid( asteroid2, randomPositiveInt(), randomPositiveDouble() );

    assertEquals( system.getAsteroids().size(), 2 );
    assertNotNull( system.findAsteroidByName( asteroid1 ) );
    assertNotNull( system.findAsteroidByName( asteroid2 ) );
    assertNull( system.findAsteroidByName( asteroid3 ) );

    // Remove where asteroid name matches but asteroid instance does not is a no-op
    assertFalse( system.removeAsteroid( Objects.requireNonNull( asteroid2Instance ) ) );

    assertEquals( system.getAsteroids().size(), 2 );
    assertNotNull( system.findAsteroidByName( asteroid1 ) );
    assertNotNull( system.findAsteroidByName( asteroid2 ) );
    assertNull( system.findAsteroidByName( asteroid3 ) );

    assertTrue( system.removeAsteroid( asteroid2 ) );

    assertEquals( system.getAsteroids().size(), 1 );
    assertNotNull( system.findAsteroidByName( asteroid1 ) );
    assertNull( system.findAsteroidByName( asteroid2 ) );
    assertNull( system.findAsteroidByName( asteroid3 ) );
  }

  @Test
  public void mutateMinables()
  {
    final SystemConfig system = new SystemConfig( randomString() );

    assertTrue( system.getMinables().isEmpty() );

    final String minable1 = randomString();
    final String minable2 = randomString();
    final String minable3 = randomString();

    final SystemConfig.Minable minable1Instance =
      system.addMinable( minable1, randomPositiveInt(), randomPositiveDouble() );

    assertEquals( system.getMinables().size(), 1 );
    assertTrue( system.getMinables().contains( minable1Instance ) );
    assertNotNull( system.findMinableByName( minable1 ) );
    assertNull( system.findMinableByName( minable2 ) );
    assertNull( system.findMinableByName( minable3 ) );

    final SystemConfig.Minable minable2aInstance =
      system.addMinable( minable2, randomPositiveInt(), randomPositiveDouble() );

    assertEquals( system.getMinables().size(), 2 );
    assertTrue( system.getMinables().contains( minable1Instance ) );
    assertTrue( system.getMinables().contains( minable2aInstance ) );
    assertNotNull( system.findMinableByName( minable1 ) );
    assertNotNull( system.findMinableByName( minable2 ) );
    assertNull( system.findMinableByName( minable3 ) );

    // Add with same name is just an update
    final SystemConfig.Minable minable2bInstance =
      system.addMinable( minable2, randomPositiveInt(), randomPositiveDouble() );

    assertEquals( system.getMinables().size(), 2 );
    assertTrue( system.getMinables().contains( minable1Instance ) );
    assertFalse( system.getMinables().contains( minable2aInstance ) );
    assertTrue( system.getMinables().contains( minable2bInstance ) );
    assertNotNull( system.findMinableByName( minable1 ) );
    assertNotNull( system.findMinableByName( minable2 ) );
    assertNull( system.findMinableByName( minable3 ) );

    final SystemConfig.Minable minable2Instance = system.findMinableByName( minable2 );
    assertTrue( system.removeMinable( Objects.requireNonNull( minable2Instance ) ) );

    assertEquals( system.getMinables().size(), 1 );
    assertTrue( system.getMinables().contains( minable1Instance ) );
    assertFalse( system.getMinables().contains( minable2aInstance ) );
    assertFalse( system.getMinables().contains( minable2bInstance ) );
    assertNotNull( system.findMinableByName( minable1 ) );
    assertNull( system.findMinableByName( minable2 ) );
    assertNull( system.findMinableByName( minable3 ) );

    // Re-add minable with the same name but different instances
    system.addMinable( minable2, randomPositiveInt(), randomPositiveDouble() );

    assertEquals( system.getMinables().size(), 2 );
    assertNotNull( system.findMinableByName( minable1 ) );
    assertNotNull( system.findMinableByName( minable2 ) );
    assertNull( system.findMinableByName( minable3 ) );

    // Remove where minable name matches but minable instance does not is a no-op
    assertFalse( system.removeMinable( Objects.requireNonNull( minable2Instance ) ) );

    assertEquals( system.getMinables().size(), 2 );
    assertNotNull( system.findMinableByName( minable1 ) );
    assertNotNull( system.findMinableByName( minable2 ) );
    assertNull( system.findMinableByName( minable3 ) );

    assertTrue( system.removeMinable( minable2 ) );

    assertEquals( system.getMinables().size(), 1 );
    assertNotNull( system.findMinableByName( minable1 ) );
    assertNull( system.findMinableByName( minable2 ) );
    assertNull( system.findMinableByName( minable3 ) );
  }

  @Test
  public void mutateTrades()
  {
    final SystemConfig system = new SystemConfig( randomString() );

    assertTrue( system.getTrades().isEmpty() );

    final String trade1 = randomString();
    final String trade2 = randomString();
    final String trade3 = randomString();

    final SystemConfig.Trade trade1Instance = system.addTrade( trade1, randomPositiveInt() );

    assertEquals( system.getTrades().size(), 1 );
    assertTrue( system.getTrades().contains( trade1Instance ) );
    assertNotNull( system.findTradeByName( trade1 ) );
    assertNull( system.findTradeByName( trade2 ) );
    assertNull( system.findTradeByName( trade3 ) );

    final SystemConfig.Trade trade2aInstance = system.addTrade( trade2, randomPositiveInt() );

    assertEquals( system.getTrades().size(), 2 );
    assertTrue( system.getTrades().contains( trade1Instance ) );
    assertTrue( system.getTrades().contains( trade2aInstance ) );
    assertNotNull( system.findTradeByName( trade1 ) );
    assertNotNull( system.findTradeByName( trade2 ) );
    assertNull( system.findTradeByName( trade3 ) );

    // Add with same name is just an update
    final SystemConfig.Trade trade2bInstance = system.addTrade( trade2, randomPositiveInt() );

    assertEquals( system.getTrades().size(), 2 );
    assertTrue( system.getTrades().contains( trade1Instance ) );
    assertFalse( system.getTrades().contains( trade2aInstance ) );
    assertTrue( system.getTrades().contains( trade2bInstance ) );
    assertNotNull( system.findTradeByName( trade1 ) );
    assertNotNull( system.findTradeByName( trade2 ) );
    assertNull( system.findTradeByName( trade3 ) );

    final SystemConfig.Trade trade2Instance = system.findTradeByName( trade2 );
    assertTrue( system.removeTrade( Objects.requireNonNull( trade2Instance ) ) );

    assertEquals( system.getTrades().size(), 1 );
    assertTrue( system.getTrades().contains( trade1Instance ) );
    assertFalse( system.getTrades().contains( trade2aInstance ) );
    assertFalse( system.getTrades().contains( trade2bInstance ) );
    assertNotNull( system.findTradeByName( trade1 ) );
    assertNull( system.findTradeByName( trade2 ) );
    assertNull( system.findTradeByName( trade3 ) );

    // Re-add trade with the same name but different instances
    system.addTrade( trade2, randomPositiveInt() );

    assertEquals( system.getTrades().size(), 2 );
    assertNotNull( system.findTradeByName( trade1 ) );
    assertNotNull( system.findTradeByName( trade2 ) );
    assertNull( system.findTradeByName( trade3 ) );

    // Remove where trade name matches but trade instance does not is a no-op
    assertFalse( system.removeTrade( Objects.requireNonNull( trade2Instance ) ) );

    assertEquals( system.getTrades().size(), 2 );
    assertNotNull( system.findTradeByName( trade1 ) );
    assertNotNull( system.findTradeByName( trade2 ) );
    assertNull( system.findTradeByName( trade3 ) );

    assertTrue( system.removeTrade( trade2 ) );

    assertEquals( system.getTrades().size(), 1 );
    assertNotNull( system.findTradeByName( trade1 ) );
    assertNull( system.findTradeByName( trade2 ) );
    assertNull( system.findTradeByName( trade3 ) );
  }

  @Test
  public void mutateFleets()
  {
    final SystemConfig system = new SystemConfig( randomString() );

    assertTrue( system.getFleets().isEmpty() );

    final String fleet1 = randomString();
    final String fleet2 = randomString();
    final String fleet3 = randomString();

    final SystemConfig.Fleet fleet1Instance = system.addFleet( fleet1, randomPositiveInt() );

    assertEquals( system.getFleets().size(), 1 );
    assertTrue( system.getFleets().contains( fleet1Instance ) );
    assertNotNull( system.findFleetByName( fleet1 ) );
    assertNull( system.findFleetByName( fleet2 ) );
    assertNull( system.findFleetByName( fleet3 ) );

    final SystemConfig.Fleet fleet2aInstance = system.addFleet( fleet2, randomPositiveInt() );

    assertEquals( system.getFleets().size(), 2 );
    assertTrue( system.getFleets().contains( fleet1Instance ) );
    assertTrue( system.getFleets().contains( fleet2aInstance ) );
    assertNotNull( system.findFleetByName( fleet1 ) );
    assertNotNull( system.findFleetByName( fleet2 ) );
    assertNull( system.findFleetByName( fleet3 ) );

    // Add with same name is just an update
    final SystemConfig.Fleet fleet2bInstance = system.addFleet( fleet2, randomPositiveInt() );

    assertEquals( system.getFleets().size(), 2 );
    assertTrue( system.getFleets().contains( fleet1Instance ) );
    assertFalse( system.getFleets().contains( fleet2aInstance ) );
    assertTrue( system.getFleets().contains( fleet2bInstance ) );
    assertNotNull( system.findFleetByName( fleet1 ) );
    assertNotNull( system.findFleetByName( fleet2 ) );
    assertNull( system.findFleetByName( fleet3 ) );

    final SystemConfig.Fleet fleet2Instance = system.findFleetByName( fleet2 );
    assertTrue( system.removeFleet( Objects.requireNonNull( fleet2Instance ) ) );

    assertEquals( system.getFleets().size(), 1 );
    assertTrue( system.getFleets().contains( fleet1Instance ) );
    assertFalse( system.getFleets().contains( fleet2aInstance ) );
    assertFalse( system.getFleets().contains( fleet2bInstance ) );
    assertNotNull( system.findFleetByName( fleet1 ) );
    assertNull( system.findFleetByName( fleet2 ) );
    assertNull( system.findFleetByName( fleet3 ) );

    // Re-add fleet with the same name but different instances
    system.addFleet( fleet2, randomPositiveInt() );

    assertEquals( system.getFleets().size(), 2 );
    assertNotNull( system.findFleetByName( fleet1 ) );
    assertNotNull( system.findFleetByName( fleet2 ) );
    assertNull( system.findFleetByName( fleet3 ) );

    // Remove where fleet name matches but fleet instance does not is a no-op
    assertFalse( system.removeFleet( Objects.requireNonNull( fleet2Instance ) ) );

    assertEquals( system.getFleets().size(), 2 );
    assertNotNull( system.findFleetByName( fleet1 ) );
    assertNotNull( system.findFleetByName( fleet2 ) );
    assertNull( system.findFleetByName( fleet3 ) );

    assertTrue( system.removeFleet( fleet2 ) );

    assertEquals( system.getFleets().size(), 1 );
    assertNotNull( system.findFleetByName( fleet1 ) );
    assertNull( system.findFleetByName( fleet2 ) );
    assertNull( system.findFleetByName( fleet3 ) );
  }

  @Test
  public void mutateObjects()
  {
    final SystemConfig system = new SystemConfig( randomString() );

    assertTrue( system.getObjects().isEmpty() );

    final SystemConfig.StellarObject object1 = new SystemConfig.StellarObject( null );
    final SystemConfig.StellarObject object2 = new SystemConfig.StellarObject( randomString() );

    system.addObject( object1 );

    assertEquals( system.getObjects().size(), 1 );
    assertTrue( system.getObjects().contains( object1 ) );

    assertTrue( object1.getObjects().isEmpty() );

    object1.addObject( object2 );

    assertEquals( object1.getObjects().size(), 1 );
    assertTrue( object1.getObjects().contains( object2 ) );

    assertTrue( object1.removeObject( object2 ) );

    assertEquals( object1.getObjects().size(), 0 );

    assertFalse( object1.removeObject( object2 ) );

    assertEquals( object1.getObjects().size(), 0 );

    assertTrue( system.removeObject( object1 ) );

    assertEquals( system.getObjects().size(), 0 );

    assertFalse( system.removeObject( object1 ) );

    assertEquals( system.getObjects().size(), 0 );
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
      final SpriteConfig sprite1 = object1.getSprite();
      assertNotNull( sprite1 );
      assertEquals( sprite1.getName(), "star/g0" );
      assertEquals( object1.getDistance(), 40.3837D );
      assertEquals( object1.getPeriod(), 14.9791D );
      assertEquals( object1.getOffset(), 0D );
      assertTrue( object1.getObjects().isEmpty() );

      final SystemConfig.StellarObject object2 = system.findObjectByName( "Zen - 2" );
      assertNotNull( object2 );
      assertEquals( object2.getName(), "Zen - 2" );
      final SpriteConfig sprite2 = object2.getSprite();
      assertNotNull( sprite2 );
      assertEquals( sprite2.getName(), "star/k0" );
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
    final SpriteConfig zenSprite = zen.getSprite();
    assertNotNull( zenSprite );
    assertEquals( zenSprite.getName(), "star/k0" );
    assertEquals( zen.getDistance(), 89.6163D );
    assertEquals( zen.getPeriod(), 14.9791D );
    assertEquals( zen.getOffset(), 180D );
    assertEquals( zen.getObjects().size(), 1 );

    final SystemConfig.StellarObject zenMinor = zen.findObjectByName( "Zen Minor" );
    assertNotNull( zenMinor );
    assertEquals( zenMinor.getName(), "Zen Minor" );
    final SpriteConfig zenMinorSprite = zenMinor.getSprite();
    assertNotNull( zenMinorSprite );
    assertEquals( zenMinorSprite.getName(), "planet/luna" );
    assertEquals( zenMinor.getDistance(), 400.4D );
    assertEquals( zenMinor.getPeriod(), 29.3D );
    assertEquals( zenMinor.getOffset(), 0D );
    assertEquals( zenMinor.getObjects().size(), 1 );

    final SystemConfig.StellarObject whizzer = zenMinor.findObjectByName( "Whizzer" );
    assertNotNull( whizzer );
    assertEquals( whizzer.getName(), "Whizzer" );
    final SpriteConfig whizzerSprite = whizzer.getSprite();
    assertNotNull( whizzerSprite );
    assertEquals( whizzerSprite.getName(), "planet/blue" );
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
    final DataElement element = asDataElement( data );
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

    assertDocumentMatches( document, inputData );
  }
}
