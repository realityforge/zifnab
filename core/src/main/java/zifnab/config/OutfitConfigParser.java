package zifnab.config;

import java.util.List;
import javax.annotation.Nonnull;
import zifnab.hdf.DataAccessException;
import zifnab.hdf.DataElement;

final class OutfitConfigParser
{
  private OutfitConfigParser()
  {
  }

  @Nonnull
  static OutfitConfig from( @Nonnull final DataElement element )
  {
    element.assertTokenCount( 2 );
    element.assertTokenName( "outfit" );
    final String name = element.getStringAt( 1 );
    final OutfitConfig config = new OutfitConfig( name );
    parseConfig( config, element.getChildElements() );
    return config;
  }

  static boolean matches( @Nonnull final DataElement element )
  {
    return "outfit".equals( element.getName() );
  }

  private static void parseConfig( @Nonnull final OutfitConfig config, @Nonnull final List<DataElement> elements )
  {
    for ( final DataElement element : elements )
    {
      final String name = element.getName();
      switch ( name )
      {
        case "plural":
          element.assertLeafNode();
          element.assertTokenCount( 2 );
          config.setPluralName( element.getStringAt( 1 ) );
          break;
        case "category":
          element.assertLeafNode();
          element.assertTokenCount( 2 );
          config.setCategory( element.getStringAt( 1 ) );
          break;
        case "cost":
          element.assertLeafNode();
          element.assertTokenCount( 2 );
          config.setCost( element.getIntAt( 1 ) );
          break;
        case "ammo":
          element.assertLeafNode();
          element.assertTokenCount( 2 );
          config.setAmmo( element.getStringAt( 1 ) );
          break;
        case "thumbnail":
          element.assertLeafNode();
          element.assertTokenCount( 2 );
          config.setThumbnail( element.getStringAt( 1 ) );
          break;
        case "mass":
          element.assertLeafNode();
          element.assertTokenCount( 2 );
          config.setMass( element.getDoubleAt( 1 ) );
          break;
        case "flare sprite":
          element.assertTokenCount( 2 );
          config.setFlareSprite( SpriteConfig.from( element ) );
          break;
        case "flare sound":
          element.assertLeafNode();
          element.assertTokenCount( 2 );
          config.setFlareSound( element.getStringAt( 1 ) );
          break;
        case "afterburner effect":
          element.assertLeafNode();
          element.assertTokenCount( 2 );
          config.setAfterburnerEffect( element.getStringAt( 1 ) );
          break;
        case "flotsam sprite":
          element.assertLeafNode();
          element.assertTokenCount( 2 );
          config.setFlotsamSprite( element.getStringAt( 1 ) );
          break;
        case "licenses":
          parseLicenses( config, element );
          break;
        case "weapon":
          parseWeapon( config, element );
          break;
        case "description":
          parseDescription( config, element );
          break;
        default:
          // Must be one of the arbitrary attributes
          element.assertLeafNode();
          element.assertTokenCount( 2 );
          config.addAttribute( element.getStringAt( 0 ), element.getStringAt( 1 ) );
      }
    }
  }

  private static void parseWeapon( @Nonnull final OutfitConfig config, @Nonnull final DataElement element )
  {
    element.assertTokenCount( 1 );
    final OutfitConfig.Weapon weapon = new OutfitConfig.Weapon();
    parseConfig( weapon, element.getChildElements() );
    config.setWeapon( weapon );
  }

  private static void parseConfig( @Nonnull final OutfitConfig.Weapon config,
                                   @Nonnull final List<DataElement> elements )
  {
    for ( final DataElement element : elements )
    {
      final String name = element.getName();
      switch ( name )
      {
        case "stream":
          element.assertTokenCount( 1 );
          config.setStream( Boolean.TRUE );
          break;
        case "cluster":
          element.assertTokenCount( 1 );
          config.setCluster( Boolean.TRUE );
          break;
        case "safe":
          element.assertTokenCount( 1 );
          config.setSafe( Boolean.TRUE );
          break;
        case "phasing":
          element.assertTokenCount( 1 );
          config.setPhasing( Boolean.TRUE );
          break;
        case "no damage scaling":
          element.assertTokenCount( 1 );
          config.setNoDamageScaling( Boolean.TRUE );
          break;
        case "sprite":
          config.setSprite( SpriteConfig.from( element ) );
          break;
        case "hardpoint sprite":
          config.setHardpointSprite( SpriteConfig.from( element ) );
          break;
        case "sound":
          element.assertTokenCount( 2 );
          config.setSound( element.getStringAt( 1 ) );
          break;
        case "ammo":
          element.assertTokenCount( 2 );
          config.setAmmo( element.getStringAt( 1 ) );
          break;
        case "icon":
          element.assertTokenCount( 2 );
          config.setIcon( element.getStringAt( 1 ) );
          break;
        case "lifetime":
          element.assertTokenCount( 2 );
          config.setLifetime( element.getIntAt( 1, 0 ) );
          break;
        case "random lifetime":
          element.assertTokenCount( 2 );
          config.setRandomLifetime( element.getIntAt( 1, 0 ) );
          break;
        case "reload":
          element.assertTokenCount( 2 );
          config.setReload( element.getDoubleAt( 1, 1D ) );
          break;
        case "burst reload":
          element.assertTokenCount( 2 );
          config.setBurstReload( element.getDoubleAt( 1, 1D ) );
          break;
        case "burst count":
          element.assertTokenCount( 2 );
          config.setBurstCount( element.getIntAt( 1, 1 ) );
          break;
        case "piercing":
          element.assertTokenCount( 2 );
          config.setPiercing( element.getDoubleAt( 1, 0D, 1D ) );
          break;
        case "homing":
          element.assertTokenCount( 2 );
          config.setHoming( element.getIntAt( 1 ) );
          break;
        case "missile strength":
          element.assertTokenCount( 2 );
          config.setMissileStrength( element.getIntAt( 1, 0 ) );
          break;
        case "anti-missile":
          element.assertTokenCount( 2 );
          config.setAntiMissile( element.getIntAt( 1, 0 ) );
          break;
        case "velocity":
          element.assertTokenCount( 2 );
          config.setVelocity( element.getDoubleAt( 1 ) );
          break;
        case "random velocity":
          element.assertTokenCount( 2 );
          config.setRandomVelocity( element.getDoubleAt( 1 ) );
          break;
        case "acceleration":
          element.assertTokenCount( 2 );
          config.setAcceleration( element.getDoubleAt( 1 ) );
          break;
        case "drag":
          element.assertTokenCount( 2 );
          config.setDrag( element.getDoubleAt( 1 ) );
          break;
        case "hardpoint offset":
          element.assertTokenCountRange( 2, 3 );
          final int tokenCount = element.getTokenCount();
          final double x = 2 == tokenCount ? 0D : element.getDoubleAt( 1 );
          final double y = element.getDoubleAt( 2 == tokenCount ? 1 : 2 );
          config.setHardpointOffset( new Position( x, y ) );
          break;
        case "turn":
          element.assertTokenCount( 2 );
          config.setTurn( element.getDoubleAt( 1 ) );
          break;
        case "inaccuracy":
          element.assertTokenCount( 2 );
          config.setInaccuracy( element.getDoubleAt( 1 ) );
          break;
        case "turret turn":
          element.assertTokenCount( 2 );
          config.setTurretTurn( element.getDoubleAt( 1 ) );
          break;
        case "firing energy":
          element.assertTokenCount( 2 );
          config.setFiringEnergy( element.getDoubleAt( 1 ) );
          break;
        case "firing force":
          element.assertTokenCount( 2 );
          config.setFiringForce( element.getDoubleAt( 1 ) );
          break;
        case "firing fuel":
          element.assertTokenCount( 2 );
          config.setFiringFuel( element.getDoubleAt( 1 ) );
          break;
        case "firing heat":
          element.assertTokenCount( 2 );
          config.setFiringHeat( element.getDoubleAt( 1 ) );
          break;
        case "split range":
          element.assertTokenCount( 2 );
          config.setSplitRange( element.getDoubleAt( 1, 0D ) );
          break;
        case "trigger radius":
          element.assertTokenCount( 2 );
          config.setTriggerRadius( element.getDoubleAt( 1, 0D ) );
          break;
        case "blast radius":
          element.assertTokenCount( 2 );
          config.setBlastRadius( element.getDoubleAt( 1, 0D ) );
          break;
        case "shield damage":
          element.assertTokenCount( 2 );
          config.setShieldDamage( element.getDoubleAt( 1 ) );
          break;
        case "hull damage":
          element.assertTokenCount( 2 );
          config.setHullDamage( element.getDoubleAt( 1 ) );
          break;
        case "fuel damage":
          element.assertTokenCount( 2 );
          config.setFuelDamage( element.getDoubleAt( 1 ) );
          break;
        case "heat damage":
          element.assertTokenCount( 2 );
          config.setHeatDamage( element.getDoubleAt( 1 ) );
          break;
        case "ion damage":
          element.assertTokenCount( 2 );
          config.setIonDamage( element.getDoubleAt( 1 ) );
          break;
        case "disruption damage":
          element.assertTokenCount( 2 );
          config.setDisruptionDamage( element.getDoubleAt( 1 ) );
          break;
        case "slowing damage":
          element.assertTokenCount( 2 );
          config.setSlowingDamage( element.getDoubleAt( 1 ) );
          break;
        case "hit force":
          element.assertTokenCount( 2 );
          config.setHitForce( element.getDoubleAt( 1 ) );
          break;
        case "tracking":
          element.assertTokenCount( 2 );
          config.setTracking( element.getDoubleAt( 1, 0D, 1D ) );
          break;
        case "optical tracking":
          element.assertTokenCount( 2 );
          config.setOpticalTracking( element.getDoubleAt( 1, 0D, 1D ) );
          break;
        case "infrared tracking":
          element.assertTokenCount( 2 );
          config.setInfraredTracking( element.getDoubleAt( 1, 0D, 1D ) );
          break;
        case "radar tracking":
          element.assertTokenCount( 2 );
          config.setRadarTracking( element.getDoubleAt( 1, 0D, 1D ) );
          break;
        case "fire effect":
          element.assertTokenCountRange( 2, 3 );
          config.incrementFireEffect( element.getStringAt( 1 ),
                                      2 == element.getTokenCount() ? 1 : element.getIntAt( 2, 0 ) );
          break;
        case "live effect":
          element.assertTokenCountRange( 2, 3 );
          config.incrementLiveEffect( element.getStringAt( 1 ),
                                      2 == element.getTokenCount() ? 1 : element.getIntAt( 2, 0 ) );
          break;
        case "hit effect":
          element.assertTokenCountRange( 2, 3 );
          config.incrementHitEffect( element.getStringAt( 1 ),
                                     2 == element.getTokenCount() ? 1 : element.getIntAt( 2, 0 ) );
          break;
        case "die effect":
          element.assertTokenCountRange( 2, 3 );
          config.incrementDieEffect( element.getStringAt( 1 ),
                                     2 == element.getTokenCount() ? 1 : element.getIntAt( 2, 0 ) );
          break;
        case "submunition":
          element.assertTokenCountRange( 2, 3 );
          config.incrementSubmunition( element.getStringAt( 1 ),
                                       2 == element.getTokenCount() ? 1 : element.getIntAt( 2, 0 ) );
          break;
        default:
          throw new DataAccessException( "Unexpected data element named '" + name + "'", element.getLocation() );
      }
    }
  }

  private static void parseDescription( @Nonnull final OutfitConfig config, @Nonnull final DataElement element )
  {
    element.assertTokenName( "description" );
    element.assertTokenCount( 2 );
    final String existing = config.getDescription();
    config.setDescription( ( null == existing ? "" : existing + "\n" ) + element.getStringAt( 1 ) );
  }

  private static void parseLicenses( @Nonnull final OutfitConfig config, @Nonnull final DataElement element )
  {
    element.assertTokenName( "licenses" );
    element.assertTokenCount( 1 );
    for ( final DataElement license : element.getChildElements() )
    {
      license.assertTokenCount( 1 );
      config.addLicense( license.getStringAt( 0 ) );
    }
  }
}
