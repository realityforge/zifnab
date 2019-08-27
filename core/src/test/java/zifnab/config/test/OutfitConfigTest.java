package zifnab.config.test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import zifnab.AbstractTest;
import zifnab.config.OutfitConfig;
import zifnab.config.Position;
import zifnab.config.SpriteConfig;
import zifnab.hdf.DataAccessException;
import zifnab.hdf.DataDocument;
import zifnab.hdf.DataElement;
import zifnab.hdf.SourceLocation;
import static org.testng.Assert.*;

public class OutfitConfigTest
  extends AbstractTest
{
  @Test
  public void parseOutfit()
    throws Exception
  {
    final String data =
      "outfit \"Boom Gun\"\n" +
      "\tcategory \"Secondary Weapons\"\n" +
      "\tplural \"+Boom Guns+\"\n" +
      "\tcost 25000\n" +
      "\tthumbnail outfit/boom\n" +
      "\tammo boomers\n" +
      "\tmass 2\n" +
      "\tlicenses\n" +
      "\t\tCoalition\n" +
      "\t\t\"Ace Pilots\"\n" +
      "\t\"flare sound\" tinkle/tinkle\n" +
      "\t\"flare sprite\" \"effect/atomic flare/tiny\"\n" +
      "\t\t\"frame rate\" 14\n" +
      "\t\"outfit space\" -8\n" +
      "\t\"weapon capacity\" -8\n" +
      "\t\"afterburner thrust\" 29.0\n" +
      "\t\"afterburner fuel\" .1\n" +
      "\t\"afterburner energy\" 5.1\n" +
      "\t\"afterburner heat\" 12.0\n" +
      "\t\"afterburner effect\" \"ionic afterburner\"\n" +
      "\t\"flotsam sprite\" \"effect/flotsam aluminum\"\n" +
      "\t\"gun ports\" -1\n" +
      "\t\"boom round capacity\" 3000\n" +
      "\tweapon\n" +
      "\t\tsprite \"projectile/tinyflare\"\n" +
      "\t\t\t\"random start frame\"\n" +
      "\t\t\"hardpoint sprite\" \"hardpoint/pulse turret\"\n" +
      "\t\t\"hardpoint offset\" 7.\n" +
      "\t\t\n" +
      "\t\tsound \"explosion tiny\"\n" +
      "\t\tammo \"Gatling Gun Ammo\"\n" +
      "\t\ticon icon/gat\n" +
      "\t\tphasing\n" +
      "\t\tinaccuracy 2\n" +
      "\t\tacceleration 2.3\n" +
      "\t\tvelocity 16\n" +
      "\t\tdrag 1.2\n" +
      "\t\t`random velocity` 8\n" +
      "\t\tstream\n" +
      "\t\tsafe\n" +
      "\t\tcluster\n" +
      "\t\t`no damage scaling`\n" +
      "\t\tlifetime 1\n" +
      "\t\t`random lifetime` 2\n" +
      "\t\t\"anti-missile\" 5\n" +
      "\t\treload 3\n" +
      "\t\tturn `210`\n" +
      "\t\t`turret turn` 120\n" +
      "\t\thoming 3\n" +
      "\t\t\"missile strength\" 50\n" +
      "\t\t\"burst count\" 180\n" +
      "\t\t\"burst reload\" 1\n" +
      "\t\t\"firing energy\" .03\n" +
      "\t\t\"firing heat\" 2.5\n" +
      "\t\t\"firing fuel\" 0.4\n" +
      "\t\t\"firing force\" 2.1\n" +
      "\t\t\"infrared tracking\" 0.3\n" +
      "\t\t\"radar tracking\" 0.4\n" +
      "\t\t\"tracking\" .1\n" +
      "\t\t\"optical tracking\" 0.2\n" +
      "\t\t\"split range\" 600\n" +
      "\t\t\"disruption damage\" 6\n" +
      "\t\t\"slowing damage\" 16\n" +
      "\t\t\"trigger radius\" 50\n" +
      "\t\t\"blast radius\" 200\n" +
      "\t\t\"shield damage\" 700\n" +
      "\t\t\"hull damage\" 100\n" +
      "\t\t\"fuel damage\" 120\n" +
      "\t\t\"heat damage\" 22\n" +
      "\t\t\"ion damage\" 48\n" +
      "\t\t\"hit force\" 90\n" +
      "\t\t\"piercing\" .60\n" +
      "\t\t\"fire effect\" \"meteor fire\" 2\n" +
      "\t\t\"live effect\" \"rail sparks\" 5\n" +
      "\t\t\"fire effect\" \"meteor fire\" 2\n" +
      "\t\t\"die effect\" \"missile death\"\n" +
      "\t\t\"hit effect\" \"medium explosion\"\n" +
      "\t\tsubmunition gbullet\n" +
      "\t\t\"hit effect\" \"bullet impact\"\n" +
      "\tdescription `Boom Boom boom!`";

    final OutfitConfig outfit = parseOutfitConfig( data );

    assertEquals( outfit.getName(), "Boom Gun" );
    assertEquals( outfit.getPluralName(), "+Boom Guns+" );
    assertEquals( outfit.getCategory(), "Secondary Weapons" );
    assertEquals( outfit.getDescription(), "Boom Boom boom!" );
    assertEquals( outfit.getCost(), 25000 );
    assertEquals( outfit.getThumbnail(), "outfit/boom" );
    assertEquals( outfit.getMass(), 2.0D );
    final SpriteConfig flareSprite = outfit.getFlareSprite();
    assertNotNull( flareSprite );
    assertEquals( flareSprite.getName(), "effect/atomic flare/tiny" );
    assertEquals( flareSprite.getFrameRate(), 14D );
    assertEquals( outfit.getFlareSound(), "tinkle/tinkle" );
    assertEquals( outfit.getAfterburnerEffect(), "ionic afterburner" );
    assertEquals( outfit.getFlotsamSprite(), "effect/flotsam aluminum" );
    assertEquals( outfit.getAmmo(), "boomers" );
    assertEquals( outfit.getLicenses(), new HashSet<>( Arrays.asList( "Ace Pilots", "Coalition" ) ) );
    final Map<String, Double> attributes = outfit.getAttributes();
    assertEquals( attributes.size(), 8 );
    assertEquals( attributes.get( "afterburner thrust" ), 29D );
    assertEquals( attributes.get( "outfit space" ), -8D );
    assertEquals( attributes.get( "afterburner energy" ), 5.1D );
    assertEquals( attributes.get( "boom round capacity" ), 3000D );
    assertEquals( attributes.get( "afterburner fuel" ), 0.1D );
    assertEquals( attributes.get( "weapon capacity" ), -8D );
    assertEquals( attributes.get( "gun ports" ), -1D );
    assertEquals( attributes.get( "afterburner heat" ), 12D );
    final OutfitConfig.Weapon weapon = outfit.getWeapon();
    assertNotNull( weapon );
    assertEquals( weapon.getStream(), Boolean.TRUE );
    assertEquals( weapon.getCluster(), Boolean.TRUE );
    assertEquals( weapon.getSafe(), Boolean.TRUE );
    assertEquals( weapon.getPhasing(), Boolean.TRUE );
    assertEquals( weapon.getNoDamageScaling(), Boolean.TRUE );
    assertEquals( weapon.getAmmo(), "Gatling Gun Ammo" );
    assertEquals( weapon.getSound(), "explosion tiny" );
    assertEquals( weapon.getIcon(), "icon/gat" );
    final SpriteConfig sprite = weapon.getSprite();
    assertNotNull( sprite );
    assertEquals( sprite.getName(), "projectile/tinyflare" );
    assertTrue( sprite.isRandomStartFrame() );
    final SpriteConfig hardpointSprite = weapon.getHardpointSprite();
    assertNotNull( hardpointSprite );
    assertEquals( hardpointSprite.getName(), "hardpoint/pulse turret" );
    assertEquals( weapon.getLifetime(), 1 );
    assertEquals( weapon.getRandomLifetime(), 2 );
    assertEquals( weapon.getReload(), 3D );
    assertEquals( weapon.getBurstReload(), 1D );
    assertEquals( weapon.getBurstCount(), 180 );
    assertEquals( weapon.getHoming(), 3 );
    assertEquals( weapon.getMissileStrength(), 50 );
    assertEquals( weapon.getAntiMissile(), 5 );
    assertEquals( weapon.getVelocity(), 16D );
    assertEquals( weapon.getRandomVelocity(), 8D );
    assertEquals( weapon.getAcceleration(), 2.3D );
    assertEquals( weapon.getDrag(), 1.2D );
    assertEquals( weapon.getTurn(), 210D );
    assertEquals( weapon.getTurretTurn(), 120D );
    assertEquals( weapon.getInaccuracy(), 2D );
    assertEquals( weapon.getFiringEnergy(), 0.03D );
    assertEquals( weapon.getFiringForce(), 2.1D );
    assertEquals( weapon.getFiringFuel(), 0.4D );
    assertEquals( weapon.getFiringHeat(), 2.5D );
    assertEquals( weapon.getTracking(), 0.1D );
    assertEquals( weapon.getOpticalTracking(), 0.2D );
    assertEquals( weapon.getInfraredTracking(), 0.3D );
    assertEquals( weapon.getRadarTracking(), 0.4D );
    assertEquals( weapon.getSplitRange(), 600D );
    assertEquals( weapon.getTriggerRadius(), 50D );
    assertEquals( weapon.getBlastRadius(), 200D );
    assertEquals( weapon.getShieldDamage(), 700D );
    assertEquals( weapon.getHullDamage(), 100D );
    assertEquals( weapon.getFuelDamage(), 120D );
    assertEquals( weapon.getHeatDamage(), 22D );
    assertEquals( weapon.getIonDamage(), 48D );
    assertEquals( weapon.getDisruptionDamage(), 6D );
    assertEquals( weapon.getSlowingDamage(), 16D );
    assertEquals( weapon.getHitForce(), 90D );
    assertEquals( weapon.getPiercing(), 0.6D );
    final Map<String, Integer> fireEffects = weapon.getFireEffects();
    assertEquals( fireEffects.size(), 1 );
    assertEquals( fireEffects.get( "meteor fire" ), (Integer) 4 );
    final Map<String, Integer> liveEffects = weapon.getLiveEffects();
    assertEquals( liveEffects.size(), 1 );
    assertEquals( liveEffects.get( "rail sparks" ), (Integer) 5 );
    final Map<String, Integer> hitEffects = weapon.getHitEffects();
    assertEquals( hitEffects.size(), 2 );
    assertEquals( hitEffects.get( "medium explosion" ), (Integer) 1 );
    assertEquals( hitEffects.get( "bullet impact" ), (Integer) 1 );
    final Map<String, Integer> dieEffects = weapon.getDieEffects();
    assertEquals( dieEffects.size(), 1 );
    assertEquals( dieEffects.get( "missile death" ), (Integer) 1 );
    final Map<String, Integer> submunitions = weapon.getSubmunitions();
    assertEquals( submunitions.size(), 1 );
    assertEquals( submunitions.get( "gbullet" ), (Integer) 1 );

    final Position hardpointOffset = weapon.getHardpointOffset();
    assertNotNull( hardpointOffset );
    assertEquals( hardpointOffset.getX(), 0D );
    assertEquals( hardpointOffset.getY(), 7D );
  }

  @Test
  public void parseWithUnknownKey()
  {
    final String data =
      "outfit \"bang bang\"\n" +
      "\tmeepmeep\n";

    final DataAccessException exception =
      expectThrows( DataAccessException.class, () -> parseOutfitConfig( data ) );

    assertEquals( exception.getMessage(),
                  "Data element named 'meepmeep' expected to contain 2 tokens but contains 1 tokens" );
    final SourceLocation location = exception.getLocation();
    assertNotNull( location );
    assertEquals( location.getLineNumber(), 2 );
    assertEquals( location.getColumnNumber(), 1 );
  }

  @Test
  public void parseInvalidProperty()
  {
    final String data =
      "outfit \"Bang Stick\"\n" +
      "\tcost Monkey!\n";

    final DataAccessException exception =
      expectThrows( DataAccessException.class, () -> parseOutfitConfig( data ) );

    assertEquals( exception.getMessage(),
                  "Token at index 1 for data element named 'cost' has value 'Monkey!' which is not an integer" );
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
      "outfit \"Boom Gun\"\n" +
      "\tplural \"+Boom Guns+\"\n" +
      "\tcategory \"Secondary Weapons\"\n" +
      "\tcost 25000\n" +
      "\tammo boomers\n" +
      "\tthumbnail outfit/boom\n" +
      "\tmass 2.0\n" +
      "\t\"flare sprite\" \"effect/atomic flare/tiny\"\n" +
      "\t\"flare sound\" tinkle/tinkle\n" +
      "\t\"afterburner effect\" \"ionic afterburner\"\n" +
      "\t\"flotsam sprite\" \"effect/flotsam aluminum\"\n" +
      "\tlicenses\n" +
      "\t\t\"Ace Pilots\"\n" +
      "\t\tCoalition\n" +
      "\t\"afterburner energy\" 5.1\n" +
      "\t\"afterburner fuel\" 0.1\n" +
      "\t\"afterburner heat\" 12.0\n" +
      "\t\"afterburner thrust\" 29.0\n" +
      "\t\"boom round capacity\" 3000.0\n" +
      "\t\"gun ports\" -1.0\n" +
      "\t\"outfit space\" -8.0\n" +
      "\t\"weapon capacity\" -8.0\n" +
      "\tweapon\n" +
      "\t\tstream\n" +
      "\t\tcluster\n" +
      "\t\tsafe\n" +
      "\t\tphasing\n" +
      "\t\t\"no damage scaling\"\n" +
      "\t\tsprite projectile/tinyflare\n" +
      "\t\t\"hardpoint sprite\" \"hardpoint/pulse turret\"\n" +
      "\t\t\"hardpoint offset\" 7.0\n" +
      "\t\tsound \"explosion tiny\"\n" +
      "\t\tammo \"Gatling Gun Ammo\"\n" +
      "\t\ticon icon/gat\n" +
      "\t\tlifetime 1\n" +
      "\t\t\"random lifetime\" 2\n" +
      "\t\thoming 3\n" +
      "\t\t\"missile strength\" 50\n" +
      "\t\tanti-missile 5\n" +
      "\t\treload 3.0\n" +
      "\t\t\"burst reload\" 1.0\n" +
      "\t\t\"burst count\" 180\n" +
      "\t\tpiercing 0.6\n" +
      "\t\tvelocity 16.0\n" +
      "\t\t\"random velocity\" 8.0\n" +
      "\t\tacceleration 2.3\n" +
      "\t\tdrag 1.2\n" +
      "\t\tturn 210.0\n" +
      "\t\tinaccuracy 2.0\n" +
      "\t\t\"turret turn\" 120.0\n" +
      "\t\t\"firing energy\" 0.03\n" +
      "\t\t\"firing force\" 2.1\n" +
      "\t\t\"firing fuel\" 0.4\n" +
      "\t\t\"firing heat\" 2.5\n" +
      "\t\t\"split range\" 600.0\n" +
      "\t\t\"trigger radius\" 50.0\n" +
      "\t\t\"blast radius\" 200.0\n" +
      "\t\t\"shield damage\" 700.0\n" +
      "\t\t\"hull damage\" 100.0\n" +
      "\t\t\"fuel damage\" 120.0\n" +
      "\t\t\"heat damage\" 22.0\n" +
      "\t\t\"ion damage\" 48.0\n" +
      "\t\t\"disruption damage\" 6.0\n" +
      "\t\t\"slowing damage\" 16.0\n" +
      "\t\t\"hit force\" 90.0\n" +
      "\t\ttracking 0.1\n" +
      "\t\t\"optical tracking\" 0.2\n" +
      "\t\t\"infrared tracking\" 0.3\n" +
      "\t\t\"radar tracking\" 0.4\n" +
      "\t\t\"fire effect\" \"meteor fire\" 4\n" +
      "\t\t\"live effect\" \"rail sparks\" 5\n" +
      "\t\t\"hit effect\" \"bullet impact\"\n" +
      "\t\t\"hit effect\" \"medium explosion\"\n" +
      "\t\t\"die effect\" \"missile death\"\n" +
      "\t\tsubmunition gbullet\n" +
      "\tdescription \"Boom Boom boom!\"\n" +
      "\tdescription \"Put your hands in the air!\"\n";

    assertEncodedFormMatchesInputData( data );
  }

  @Test
  public void encodeWillReorderElementsAndNormalizeDoubles()
    throws Exception
  {
    final String data =
      "outfit \"Boom Gun\"\n" +
      "\tcategory \"Secondary Weapons\"\n" +
      "\tplural \"+Boom Guns+\"\n" +
      "\tcost 25000\n" +
      "\tthumbnail outfit/boom\n" +
      "\tammo boomers\n" +
      "\tmass 2\n" +
      "\tlicenses\n" +
      "\t\tCoalition\n" +
      "\t\t\"Ace Pilots\"\n" +
      "\t\"flare sound\" tinkle/tinkle\n" +
      "\t\"flare sprite\" \"effect/atomic flare/tiny\"\n" +
      "\t\t\"frame rate\" 14\n" +
      "\t\"outfit space\" -8\n" +
      "\t\"weapon capacity\" -8\n" +
      "\t\"afterburner thrust\" 29.0\n" +
      "\t\"afterburner fuel\" .1\n" +
      "\t\"afterburner energy\" 5.1\n" +
      "\t\"afterburner heat\" 12.0\n" +
      "\t\"afterburner effect\" \"ionic afterburner\"\n" +
      "\t\"flotsam sprite\" \"effect/flotsam aluminum\"\n" +
      "\t\"gun ports\" -1\n" +
      "\t\"boom round capacity\" 3000\n" +
      "\tweapon\n" +
      "\t\tsprite \"projectile/tinyflare\"\n" +
      "\t\t\t\"random start frame\"\n" +
      "\t\t\"hardpoint sprite\" \"hardpoint/pulse turret\"\n" +
      "\t\t\"hardpoint offset\" 7.\n" +
      "\t\t\n" +
      "\t\tsound \"explosion tiny\"\n" +
      "\t\tammo \"Gatling Gun Ammo\"\n" +
      "\t\ticon icon/gat\n" +
      "\t\tphasing\n" +
      "\t\tinaccuracy 2\n" +
      "\t\tacceleration 2.3\n" +
      "\t\tvelocity 16\n" +
      "\t\tdrag 1.2\n" +
      "\t\t`random velocity` 8\n" +
      "\t\tstream\n" +
      "\t\tsafe\n" +
      "\t\tcluster\n" +
      "\t\t`no damage scaling`\n" +
      "\t\tlifetime 1\n" +
      "\t\t`random lifetime` 2\n" +
      "\t\t\"anti-missile\" 5\n" +
      "\t\treload 3\n" +
      "\t\tturn `210`\n" +
      "\t\t`turret turn` 120\n" +
      "\t\thoming 3\n" +
      "\t\t\"missile strength\" 50\n" +
      "\t\t\"burst count\" 180\n" +
      "\t\t\"burst reload\" 1\n" +
      "\t\t\"firing energy\" .03\n" +
      "\t\t\"firing heat\" 2.5\n" +
      "\t\t\"firing fuel\" 0.4\n" +
      "\t\t\"firing force\" 2.1\n" +
      "\t\t\"infrared tracking\" 0.3\n" +
      "\t\t\"radar tracking\" 0.4\n" +
      "\t\t\"tracking\" .1\n" +
      "\t\t\"optical tracking\" 0.2\n" +
      "\t\t\"split range\" 600\n" +
      "\t\t\"disruption damage\" 6\n" +
      "\t\t\"slowing damage\" 16\n" +
      "\t\t\"trigger radius\" 50\n" +
      "\t\t\"blast radius\" 200\n" +
      "\t\t\"shield damage\" 700\n" +
      "\t\t\"hull damage\" 100\n" +
      "\t\t\"fuel damage\" 120\n" +
      "\t\t\"heat damage\" 22\n" +
      "\t\t\"ion damage\" 48\n" +
      "\t\t\"hit force\" 90\n" +
      "\t\t\"piercing\" .60\n" +
      "\t\t\"fire effect\" \"meteor fire\" 2\n" +
      "\t\t\"live effect\" \"rail sparks\" 5\n" +
      "\t\t\"fire effect\" \"meteor fire\" 2\n" +
      "\t\t\"die effect\" \"missile death\"\n" +
      "\t\t\"hit effect\" \"medium explosion\"\n" +
      "\t\tsubmunition gbullet\n" +
      "\t\t\"hit effect\" \"bullet impact\"\n" +
      "\tdescription `Boom Boom boom!`\n" +
      "\tdescription `Put your hands in the air!`\n";

    final String expected =
      "outfit \"Boom Gun\"\n" +
      "\tplural \"+Boom Guns+\"\n" +
      "\tcategory \"Secondary Weapons\"\n" +
      "\tcost 25000\n" +
      "\tammo boomers\n" +
      "\tthumbnail outfit/boom\n" +
      "\tmass 2.0\n" +
      "\t\"flare sprite\" \"effect/atomic flare/tiny\"\n" +
      "\t\t\"frame rate\" 14.0\n" +
      "\t\"flare sound\" tinkle/tinkle\n" +
      "\t\"afterburner effect\" \"ionic afterburner\"\n" +
      "\t\"flotsam sprite\" \"effect/flotsam aluminum\"\n" +
      "\tlicenses\n" +
      "\t\t\"Ace Pilots\"\n" +
      "\t\tCoalition\n" +
      "\t\"afterburner energy\" 5.1\n" +
      "\t\"afterburner fuel\" 0.1\n" +
      "\t\"afterburner heat\" 12.0\n" +
      "\t\"afterburner thrust\" 29.0\n" +
      "\t\"boom round capacity\" 3000.0\n" +
      "\t\"gun ports\" -1.0\n" +
      "\t\"outfit space\" -8.0\n" +
      "\t\"weapon capacity\" -8.0\n" +
      "\tweapon\n" +
      "\t\tstream\n" +
      "\t\tcluster\n" +
      "\t\tsafe\n" +
      "\t\tphasing\n" +
      "\t\t\"no damage scaling\"\n" +
      "\t\tsprite projectile/tinyflare\n" +
      "\t\t\t\"random start frame\"\n" +
      "\t\t\"hardpoint sprite\" \"hardpoint/pulse turret\"\n" +
      "\t\t\"hardpoint offset\" 7.0\n" +
      "\t\tsound \"explosion tiny\"\n" +
      "\t\tammo \"Gatling Gun Ammo\"\n" +
      "\t\ticon icon/gat\n" +
      "\t\tlifetime 1\n" +
      "\t\t\"random lifetime\" 2\n" +
      "\t\thoming 3\n" +
      "\t\t\"missile strength\" 50\n" +
      "\t\tanti-missile 5\n" +
      "\t\treload 3.0\n" +
      "\t\t\"burst reload\" 1.0\n" +
      "\t\t\"burst count\" 180\n" +
      "\t\tpiercing 0.6\n" +
      "\t\tvelocity 16.0\n" +
      "\t\t\"random velocity\" 8.0\n" +
      "\t\tacceleration 2.3\n" +
      "\t\tdrag 1.2\n" +
      "\t\tturn 210.0\n" +
      "\t\tinaccuracy 2.0\n" +
      "\t\t\"turret turn\" 120.0\n" +
      "\t\t\"firing energy\" 0.03\n" +
      "\t\t\"firing force\" 2.1\n" +
      "\t\t\"firing fuel\" 0.4\n" +
      "\t\t\"firing heat\" 2.5\n" +
      "\t\t\"split range\" 600.0\n" +
      "\t\t\"trigger radius\" 50.0\n" +
      "\t\t\"blast radius\" 200.0\n" +
      "\t\t\"shield damage\" 700.0\n" +
      "\t\t\"hull damage\" 100.0\n" +
      "\t\t\"fuel damage\" 120.0\n" +
      "\t\t\"heat damage\" 22.0\n" +
      "\t\t\"ion damage\" 48.0\n" +
      "\t\t\"disruption damage\" 6.0\n" +
      "\t\t\"slowing damage\" 16.0\n" +
      "\t\t\"hit force\" 90.0\n" +
      "\t\ttracking 0.1\n" +
      "\t\t\"optical tracking\" 0.2\n" +
      "\t\t\"infrared tracking\" 0.3\n" +
      "\t\t\"radar tracking\" 0.4\n" +
      "\t\t\"fire effect\" \"meteor fire\" 4\n" +
      "\t\t\"live effect\" \"rail sparks\" 5\n" +
      "\t\t\"hit effect\" \"bullet impact\"\n" +
      "\t\t\"hit effect\" \"medium explosion\"\n" +
      "\t\t\"die effect\" \"missile death\"\n" +
      "\t\tsubmunition gbullet\n" +
      "\tdescription \"Boom Boom boom!\"\n" +
      "\tdescription \"Put your hands in the air!\"\n";

    assertEncodedFormMatchesInputData( parseOutfitConfig( data ), expected );
  }

  @SuppressWarnings( "SameParameterValue" )
  private void assertEncodedFormMatchesInputData( @Nonnull final String data )
    throws Exception
  {
    assertEncodedFormMatchesInputData( parseOutfitConfig( data ), data );
  }

  @Nonnull
  private OutfitConfig parseOutfitConfig( @Nonnull final String data )
    throws Exception
  {
    final DataElement element = asDataElement( data );
    assertTrue( OutfitConfig.matches( element ) );
    return OutfitConfig.from( element );
  }

  private void assertEncodedFormMatchesInputData( @Nonnull final OutfitConfig outfit,
                                                  @Nonnull final String inputData )
    throws IOException
  {
    final DataDocument document = new DataDocument();
    final DataElement element = OutfitConfig.encode( document, outfit );

    assertNotNull( element );
    assertEquals( element.getName(), "outfit" );
    assertEquals( element.getStringAt( 1 ), outfit.getName() );

    assertDocumentMatches( document, inputData );
  }
}
