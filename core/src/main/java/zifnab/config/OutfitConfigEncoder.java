package zifnab.config;

import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import zifnab.hdf.DataDocument;
import zifnab.hdf.DataElement;

final class OutfitConfigEncoder
{
  @Nonnull
  static DataElement encode( @Nonnull final DataDocument document, @Nonnull final OutfitConfig outfit )
  {
    final DataElement element = document.element( "outfit", outfit.getName() );
    encodeOutfit( element, outfit );
    return element;
  }

  private static void encodeOutfit( @Nonnull final DataElement element, @Nonnull final OutfitConfig outfit )
  {
    final String pluralName = outfit.getPluralName();
    if ( null != pluralName )
    {
      element.element( "plural", pluralName );
    }
    final String category = outfit.getCategory();
    if ( null != category )
    {
      element.element( "category", category );
    }
    final int cost = outfit.getCost();
    if ( 0 != cost )
    {
      element.element( "cost", String.valueOf( cost ) );
    }
    final String ammo = outfit.getAmmo();
    if ( null != ammo )
    {
      element.element( "ammo", ammo );
    }
    final String thumbnail = outfit.getThumbnail();
    if ( null != thumbnail )
    {
      element.element( "thumbnail", thumbnail );
    }
    final double mass = outfit.getMass();
    if ( 0 != mass )
    {
      element.element( "mass", String.valueOf( mass ) );
    }
    final SpriteConfig flareSprite = outfit.getFlareSprite();
    if ( null != flareSprite )
    {
      SpriteConfig.encode( element, "flare sprite", flareSprite );
    }
    final String flareSound = outfit.getFlareSound();
    if ( null != flareSound )
    {
      element.element( "flare sound", flareSound );
    }
    final String afterburnerEffect = outfit.getAfterburnerEffect();
    if ( null != afterburnerEffect )
    {
      element.element( "afterburner effect", afterburnerEffect );
    }
    final String flotsamSprite = outfit.getFlotsamSprite();
    if ( null != flotsamSprite )
    {
      element.element( "flotsam sprite", flotsamSprite );
    }
    final Set<String> licenses = outfit.getLicenses();
    if ( !licenses.isEmpty() )
    {
      final DataElement licensesElement = element.element( "licenses" );
      licenses.stream().sorted().forEachOrdered( licensesElement::element );
    }
    final Map<String, String> attributes = outfit.getAttributes();
    attributes.keySet().stream().sorted().forEachOrdered( key -> element.element( key, attributes.get( key ) ) );

    final OutfitConfig.Weapon weapon = outfit.getWeapon();
    if ( null != weapon )
    {
      encodeWeapon( element.element( "weapon" ), weapon );
    }

    final String description = outfit.getDescription();
    if ( null != description )
    {
      for ( final String desc : description.split( "\n" ) )
      {
        element.element( "description", desc );
      }
    }
  }

  private static void encodeWeapon( @Nonnull final DataElement element, @Nonnull final OutfitConfig.Weapon config )
  {
    if ( Boolean.TRUE == config.getStream() )
    {
      element.element( "stream" );
    }
    if ( Boolean.TRUE == config.getCluster() )
    {
      element.element( "cluster" );
    }
    if ( Boolean.TRUE == config.getSafe() )
    {
      element.element( "safe" );
    }
    if ( Boolean.TRUE == config.getPhasing() )
    {
      element.element( "phasing" );
    }
    if ( Boolean.TRUE == config.getNoDamageScaling() )
    {
      element.element( "no damage scaling" );
    }
    final SpriteConfig sprite = config.getSprite();
    if ( null != sprite )
    {
      SpriteConfig.encode( element, "sprite", sprite );
    }
    final SpriteConfig hardpointSprite = config.getHardpointSprite();
    if ( null != hardpointSprite )
    {
      SpriteConfig.encode( element, "hardpoint sprite", hardpointSprite );
    }
    final Position hardpointOffset = config.getHardpointOffset();
    if ( null != hardpointOffset )
    {
      if ( 0D == hardpointOffset.getX() )
      {
        element.element( "hardpoint offset", String.valueOf( hardpointOffset.getY() ) );
      }
      else
      {
        element.element( "hardpoint offset",
                         String.valueOf( hardpointOffset.getX() ),
                         String.valueOf( hardpointOffset.getY() ) );
      }
    }
    final String sound = config.getSound();
    if ( null != sound )
    {
      element.element( "sound", sound );
    }
    final String ammo = config.getAmmo();
    if ( null != ammo )
    {
      element.element( "ammo", ammo );
    }
    final String icon = config.getIcon();
    if ( null != icon )
    {
      element.element( "icon", icon );
    }
    final int lifetime = config.getLifetime();
    if ( 0 != lifetime )
    {
      element.element( "lifetime", String.valueOf( lifetime ) );
    }
    final int randomLifetime = config.getRandomLifetime();
    if ( 0 != randomLifetime )
    {
      element.element( "random lifetime", String.valueOf( randomLifetime ) );
    }
    final int homing = config.getHoming();
    if ( 0 != homing )
    {
      element.element( "homing", String.valueOf( homing ) );
    }
    final int missileStrength = config.getMissileStrength();
    if ( 0 != missileStrength )
    {
      element.element( "missile strength", String.valueOf( missileStrength ) );
    }
    final int antiMissile = config.getAntiMissile();
    if ( 0 != antiMissile )
    {
      element.element( "anti-missile", String.valueOf( antiMissile ) );
    }
    final double reload = config.getReload();
    if ( 0D != reload )
    {
      element.element( "reload", String.valueOf( reload ) );
    }
    final double burstReload = config.getBurstReload();
    if ( 0D != burstReload )
    {
      element.element( "burst reload", String.valueOf( burstReload ) );
    }
    final int burstCount = config.getBurstCount();
    if ( 0 != burstCount )
    {
      element.element( "burst count", String.valueOf( burstCount ) );
    }
    final double piercing = config.getPiercing();
    if ( 0D != piercing )
    {
      element.element( "piercing", String.valueOf( piercing ) );
    }
    final double velocity = config.getVelocity();
    if ( 0D != velocity )
    {
      element.element( "velocity", String.valueOf( velocity ) );
    }
    final double randomVelocity = config.getRandomVelocity();
    if ( 0D != randomVelocity )
    {
      element.element( "random velocity", String.valueOf( randomVelocity ) );
    }
    final double acceleration = config.getAcceleration();
    if ( 0D != acceleration )
    {
      element.element( "acceleration", String.valueOf( acceleration ) );
    }
    final double drag = config.getDrag();
    if ( 0D != drag )
    {
      element.element( "drag", String.valueOf( drag ) );
    }
    final double turn = config.getTurn();
    if ( 0D != turn )
    {
      element.element( "turn", String.valueOf( turn ) );
    }
    final double inaccuracy = config.getInaccuracy();
    if ( 0D != inaccuracy )
    {
      element.element( "inaccuracy", String.valueOf( inaccuracy ) );
    }
    final double turretTurn = config.getTurretTurn();
    if ( 0D != turretTurn )
    {
      element.element( "turret turn", String.valueOf( turretTurn ) );
    }
    final double firingEnergy = config.getFiringEnergy();
    if ( 0D != firingEnergy )
    {
      element.element( "firing energy", String.valueOf( firingEnergy ) );
    }
    final double firingForce = config.getFiringForce();
    if ( 0D != firingForce )
    {
      element.element( "firing force", String.valueOf( firingForce ) );
    }
    final double firingFuel = config.getFiringFuel();
    if ( 0D != firingFuel )
    {
      element.element( "firing fuel", String.valueOf( firingFuel ) );
    }
    final double firingHeat = config.getFiringHeat();
    if ( 0D != firingHeat )
    {
      element.element( "firing heat", String.valueOf( firingHeat ) );
    }
    final double splitRange = config.getSplitRange();
    if ( 0D != splitRange )
    {
      element.element( "split range", String.valueOf( splitRange ) );
    }
    final double triggerRadius = config.getTriggerRadius();
    if ( 0D != triggerRadius )
    {
      element.element( "trigger radius", String.valueOf( triggerRadius ) );
    }
    final double blastRadius = config.getBlastRadius();
    if ( 0D != blastRadius )
    {
      element.element( "blast radius", String.valueOf( blastRadius ) );
    }
    final double shieldDamage = config.getShieldDamage();
    if ( 0D != shieldDamage )
    {
      element.element( "shield damage", String.valueOf( shieldDamage ) );
    }
    final double hullDamage = config.getHullDamage();
    if ( 0D != hullDamage )
    {
      element.element( "hull damage", String.valueOf( hullDamage ) );
    }
    final double fuelDamage = config.getFuelDamage();
    if ( 0D != fuelDamage )
    {
      element.element( "fuel damage", String.valueOf( fuelDamage ) );
    }
    final double heatDamage = config.getHeatDamage();
    if ( 0D != heatDamage )
    {
      element.element( "heat damage", String.valueOf( heatDamage ) );
    }
    final double ionDamage = config.getIonDamage();
    if ( 0D != ionDamage )
    {
      element.element( "ion damage", String.valueOf( ionDamage ) );
    }
    final double disruptionDamage = config.getDisruptionDamage();
    if ( 0D != disruptionDamage )
    {
      element.element( "disruption damage", String.valueOf( disruptionDamage ) );
    }
    final double slowingDamage = config.getSlowingDamage();
    if ( 0D != slowingDamage )
    {
      element.element( "slowing damage", String.valueOf( slowingDamage ) );
    }
    final double hitForce = config.getHitForce();
    if ( 0D != hitForce )
    {
      element.element( "hit force", String.valueOf( hitForce ) );
    }
    final double tracking = config.getTracking();
    if ( 0D != tracking )
    {
      element.element( "tracking", String.valueOf( tracking ) );
    }
    final double opticalTracking = config.getOpticalTracking();
    if ( 0D != opticalTracking )
    {
      element.element( "optical tracking", String.valueOf( opticalTracking ) );
    }
    final double infraredTracking = config.getInfraredTracking();
    if ( 0D != infraredTracking )
    {
      element.element( "infrared tracking", String.valueOf( infraredTracking ) );
    }
    final double radarTracking = config.getRadarTracking();
    if ( 0D != radarTracking )
    {
      element.element( "radar tracking", String.valueOf( radarTracking ) );
    }
    final Map<String, Integer> fireEffect = config.getFireEffects();
    fireEffect.keySet().stream().sorted().forEachOrdered( effect -> {
      final int count = fireEffect.get( effect );
      if ( 0 != count )
      {
        if ( 1 == count )
        {
          element.element( "fire effect", effect );
        }
        else
        {
          element.element( "fire effect", effect, String.valueOf( count ) );
        }
      }
    } );
    final Map<String, Integer> liveEffect = config.getLiveEffects();
    liveEffect.keySet().stream().sorted().forEachOrdered( effect -> {
      final int count = liveEffect.get( effect );
      if ( 0 != count )
      {
        if ( 1 == count )
        {
          element.element( "live effect", effect );
        }
        else
        {
          element.element( "live effect", effect, String.valueOf( count ) );
        }
      }
    } );
    final Map<String, Integer> hitEffect = config.getHitEffects();
    hitEffect.keySet().stream().sorted().forEachOrdered( effect -> {
      final int count = hitEffect.get( effect );
      if ( 0 != count )
      {
        if ( 1 == count )
        {
          element.element( "hit effect", effect );
        }
        else
        {
          element.element( "hit effect", effect, String.valueOf( count ) );
        }
      }
    } );
    final Map<String, Integer> dieEffect = config.getDieEffects();
    dieEffect.keySet().stream().sorted().forEachOrdered( effect -> {
      final int count = dieEffect.get( effect );
      if ( 0 != count )
      {
        if ( 1 == count )
        {
          element.element( "die effect", effect );
        }
        else
        {
          element.element( "die effect", effect, String.valueOf( count ) );
        }
      }
    } );

    final Map<String, Integer> submunition = config.getSubmunitions();
    submunition.keySet().stream().sorted().forEachOrdered( effect -> {
      final int count = submunition.get( effect );
      if ( 0 != count )
      {
        if ( 1 == count )
        {
          element.element( "submunition", effect );
        }
        else
        {
          element.element( "submunition", effect, String.valueOf( count ) );
        }
      }
    } );
  }
}
