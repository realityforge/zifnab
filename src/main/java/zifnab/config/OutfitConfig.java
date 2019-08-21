package zifnab.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import zifnab.hdf.DataDocument;
import zifnab.hdf.DataElement;

@SuppressWarnings( { "WeakerAccess", "unused" } )
public final class OutfitConfig
{
  @Nonnull
  private static final Set<String> VALID_CATEGORIES =
    Collections.unmodifiableSet( new HashSet<>( Arrays.asList( "Guns",
                                                               "Turrets",
                                                               "Secondary Weapons",
                                                               "Ammunition",
                                                               "Systems",
                                                               "Power",
                                                               "Engines",
                                                               "Hand to Hand",
                                                               "Special" ) ) );
  @Nonnull
  private String name;
  @Nullable
  private String pluralName;
  @Nullable
  private String category;
  @Nullable
  private String description;
  //TODO: Flare sprite should be a config which contains all the sprite data
  @Nullable
  private String flareSprite;
  @Nullable
  private String flareSound;
  @Nullable
  private String afterburnerEffect;
  @Nullable
  private String flotsamSprite;
  @Nullable
  private String thumbnail;
  @Nullable
  private String ammo;
  @Nullable
  private Weapon weapon;
  @Nonnull
  private final Set<String> licenses = new HashSet<>();
  //TODO: These arbitrary attributes can probably be extracted as actual properties
  @Nonnull
  private final Map<String, String> attributes = new HashMap<>();
  private int cost;
  private double mass;

  public OutfitConfig( @Nonnull final String name )
  {
    this.name = Objects.requireNonNull( name );
  }

  @Nonnull
  public static OutfitConfig from( @Nonnull final DataElement element )
  {
    return OutfitConfigParser.from( element );
  }

  @Nonnull
  public static DataElement encode( @Nonnull final DataDocument document, @Nonnull final OutfitConfig config )
  {
    return OutfitConfigEncoder.encode( document, config );
  }

  public static boolean matches( @Nonnull final DataElement element )
  {
    return OutfitConfigParser.matches( element );
  }

  @Nonnull
  public String getName()
  {
    return name;
  }

  @Nullable
  public String getPluralName()
  {
    return pluralName;
  }

  public void setPluralName( @Nullable final String pluralName )
  {
    this.pluralName = pluralName;
  }

  @Nullable
  public String getCategory()
  {
    return category;
  }

  public void setCategory( @Nullable final String category )
  {
    this.category = category;
  }

  @Nullable
  public String getDescription()
  {
    return description;
  }

  public void setDescription( @Nullable final String description )
  {
    this.description = description;
  }

  @Nullable
  public String getFlareSprite()
  {
    return flareSprite;
  }

  public void setFlareSprite( @Nullable final String flareSprite )
  {
    this.flareSprite = flareSprite;
  }

  @Nullable
  public String getFlareSound()
  {
    return flareSound;
  }

  public void setFlareSound( @Nullable final String flareSound )
  {
    this.flareSound = flareSound;
  }

  @Nullable
  public String getAfterburnerEffect()
  {
    return afterburnerEffect;
  }

  public void setAfterburnerEffect( @Nullable final String afterburnerEffect )
  {
    this.afterburnerEffect = afterburnerEffect;
  }

  @Nullable
  public String getFlotsamSprite()
  {
    return flotsamSprite;
  }

  public void setFlotsamSprite( @Nullable final String flotsamSprite )
  {
    this.flotsamSprite = flotsamSprite;
  }

  @Nullable
  public String getAmmo()
  {
    return ammo;
  }

  public void setAmmo( @Nullable final String ammo )
  {
    this.ammo = ammo;
  }

  @Nullable
  public Weapon getWeapon()
  {
    return weapon;
  }

  public void setWeapon( @Nullable final Weapon weapon )
  {
    this.weapon = weapon;
  }

  @Nullable
  public String getThumbnail()
  {
    return thumbnail;
  }

  public void setThumbnail( @Nullable final String thumbnail )
  {
    this.thumbnail = thumbnail;
  }

  public int getCost()
  {
    return cost;
  }

  public void setCost( final int cost )
  {
    this.cost = cost;
  }

  public double getMass()
  {
    return mass;
  }

  public void setMass( final double mass )
  {
    this.mass = mass;
  }

  @Nonnull
  public Set<String> getLicenses()
  {
    return Collections.unmodifiableSet( licenses );
  }

  public void addLicense( @Nonnull final String license )
  {
    licenses.add( license );
  }

  public boolean removeLicense( @Nonnull final String license )
  {
    return licenses.remove( license );
  }

  @Nonnull
  public Map<String, String> getAttributes()
  {
    return Collections.unmodifiableMap( attributes );
  }

  public void addAttribute( @Nonnull final String key, @Nonnull final String value )
  {
    attributes.put( key, value );
  }

  public boolean removeAttribute( @Nonnull final String key )
  {
    return null != attributes.remove( key );
  }

  public static final class Weapon
  {
    @Nullable
    private Boolean stream;
    @Nullable
    private Boolean cluster;
    @Nullable
    private Boolean safe;
    @Nullable
    private Boolean phasing;
    @Nullable
    private Boolean noDamageScaling;
    @Nullable
    private String sprite;
    @Nullable
    private String hardpointSprite;
    @Nullable
    private String sound;
    @Nullable
    private String ammo;
    @Nullable
    private String icon;
    // TODO: These effect maps can appear multiple times in data and that causes an increment.
    //  Should not the loader treat these as explicit sets and error if duplicates occur.
    @Nonnull
    private final Map<String, Integer> fireEffects = new HashMap<>();
    @Nonnull
    private final Map<String, Integer> liveEffects = new HashMap<>();
    @Nonnull
    private final Map<String, Integer> hitEffects = new HashMap<>();
    @Nonnull
    private final Map<String, Integer> dieEffects = new HashMap<>();
    @Nonnull
    private final Map<String, Integer> submunitions = new HashMap<>();
    private int lifetime;
    private int randomLifetime;
    private double reload;
    private double burstReload;
    private int burstCount;
    private int homing;
    private int missileStrength;
    private int antiMissile;
    private double velocity;
    private double randomVelocity;
    private double acceleration;
    private double drag;
    @Nullable
    private Position hardpointOffset;
    private double turn;
    private double inaccuracy;
    private double turretTurn;
    private double tracking;
    private double opticalTracking;
    private double infraredTracking;
    private double radarTracking;
    private double firingEnergy;
    private double firingForce;
    private double firingFuel;
    private double firingHeat;
    private double splitRange;
    private double triggerRadius;
    private double blastRadius;
    private double shieldDamage;
    private double hullDamage;
    private double fuelDamage;
    private double heatDamage;
    private double ionDamage;
    private double disruptionDamage;
    private double slowingDamage;
    private double hitForce;
    private double piercing;

    @Nullable
    public Boolean getStream()
    {
      return stream;
    }

    public void setStream( @Nullable final Boolean stream )
    {
      this.stream = stream;
    }

    @Nullable
    public Boolean getCluster()
    {
      return cluster;
    }

    public void setCluster( @Nullable final Boolean cluster )
    {
      this.cluster = cluster;
    }

    @Nullable
    public Boolean getSafe()
    {
      return safe;
    }

    public void setSafe( @Nullable final Boolean safe )
    {
      this.safe = safe;
    }

    @Nullable
    public Boolean getPhasing()
    {
      return phasing;
    }

    public void setPhasing( @Nullable final Boolean phasing )
    {
      this.phasing = phasing;
    }

    @Nullable
    public Boolean getNoDamageScaling()
    {
      return noDamageScaling;
    }

    public void setNoDamageScaling( @Nullable final Boolean noDamageScaling )
    {
      this.noDamageScaling = noDamageScaling;
    }

    @Nullable
    public String getSprite()
    {
      return sprite;
    }

    public void setSprite( @Nullable final String sprite )
    {
      this.sprite = sprite;
    }

    @Nullable
    public String getHardpointSprite()
    {
      return hardpointSprite;
    }

    public void setHardpointSprite( @Nullable final String hardpointSprite )
    {
      this.hardpointSprite = hardpointSprite;
    }

    @Nullable
    public String getSound()
    {
      return sound;
    }

    public void setSound( @Nullable final String sound )
    {
      this.sound = sound;
    }

    @Nullable
    public String getAmmo()
    {
      return ammo;
    }

    public void setAmmo( @Nullable final String ammo )
    {
      this.ammo = ammo;
    }

    @Nullable
    public String getIcon()
    {
      return icon;
    }

    public void setIcon( @Nullable final String icon )
    {
      this.icon = icon;
    }

    public int getLifetime()
    {
      return lifetime;
    }

    public void setLifetime( final int lifetime )
    {
      this.lifetime = lifetime;
    }

    public int getRandomLifetime()
    {
      return randomLifetime;
    }

    public void setRandomLifetime( final int randomLifetime )
    {
      this.randomLifetime = randomLifetime;
    }

    public double getReload()
    {
      return reload;
    }

    public void setReload( final double reload )
    {
      this.reload = reload;
    }

    public double getBurstReload()
    {
      return burstReload;
    }

    public void setBurstReload( final double burstReload )
    {
      this.burstReload = burstReload;
    }

    public int getBurstCount()
    {
      return burstCount;
    }

    public void setBurstCount( final int burstCount )
    {
      this.burstCount = burstCount;
    }

    public int getHoming()
    {
      return homing;
    }

    public void setHoming( final int homing )
    {
      this.homing = homing;
    }

    public int getMissileStrength()
    {
      return missileStrength;
    }

    public void setMissileStrength( final int missileStrength )
    {
      this.missileStrength = missileStrength;
    }

    public int getAntiMissile()
    {
      return antiMissile;
    }

    public void setAntiMissile( final int antiMissile )
    {
      this.antiMissile = antiMissile;
    }

    public double getVelocity()
    {
      return velocity;
    }

    public void setVelocity( final double velocity )
    {
      this.velocity = velocity;
    }

    public double getRandomVelocity()
    {
      return randomVelocity;
    }

    public void setRandomVelocity( final double randomVelocity )
    {
      this.randomVelocity = randomVelocity;
    }

    public double getAcceleration()
    {
      return acceleration;
    }

    public void setAcceleration( final double acceleration )
    {
      this.acceleration = acceleration;
    }

    public double getDrag()
    {
      return drag;
    }

    public void setDrag( final double drag )
    {
      this.drag = drag;
    }

    @Nullable
    public Position getHardpointOffset()
    {
      return hardpointOffset;
    }

    public void setHardpointOffset( @Nullable final Position hardpointOffset )
    {
      this.hardpointOffset = hardpointOffset;
    }

    public double getTurn()
    {
      return turn;
    }

    public void setTurn( final double turn )
    {
      this.turn = turn;
    }

    public double getInaccuracy()
    {
      return inaccuracy;
    }

    public void setInaccuracy( final double inaccuracy )
    {
      this.inaccuracy = inaccuracy;
    }

    public double getTurretTurn()
    {
      return turretTurn;
    }

    public void setTurretTurn( final double turretTurn )
    {
      this.turretTurn = turretTurn;
    }

    public double getTracking()
    {
      return tracking;
    }

    public void setTracking( final double tracking )
    {
      this.tracking = tracking;
    }

    public double getOpticalTracking()
    {
      return opticalTracking;
    }

    public void setOpticalTracking( final double opticalTracking )
    {
      this.opticalTracking = opticalTracking;
    }

    public double getInfraredTracking()
    {
      return infraredTracking;
    }

    public void setInfraredTracking( final double infraredTracking )
    {
      this.infraredTracking = infraredTracking;
    }

    public double getRadarTracking()
    {
      return radarTracking;
    }

    public void setRadarTracking( final double radarTracking )
    {
      this.radarTracking = radarTracking;
    }

    public double getFiringEnergy()
    {
      return firingEnergy;
    }

    public void setFiringEnergy( final double firingEnergy )
    {
      this.firingEnergy = firingEnergy;
    }

    public double getFiringForce()
    {
      return firingForce;
    }

    public void setFiringForce( final double firingForce )
    {
      this.firingForce = firingForce;
    }

    public double getFiringFuel()
    {
      return firingFuel;
    }

    public void setFiringFuel( final double firingFuel )
    {
      this.firingFuel = firingFuel;
    }

    public double getFiringHeat()
    {
      return firingHeat;
    }

    public void setFiringHeat( final double firingHeat )
    {
      this.firingHeat = firingHeat;
    }

    public double getSplitRange()
    {
      return splitRange;
    }

    public void setSplitRange( final double splitRange )
    {
      this.splitRange = splitRange;
    }

    public double getTriggerRadius()
    {
      return triggerRadius;
    }

    public void setTriggerRadius( final double triggerRadius )
    {
      this.triggerRadius = triggerRadius;
    }

    public double getBlastRadius()
    {
      return blastRadius;
    }

    public void setBlastRadius( final double blastRadius )
    {
      this.blastRadius = blastRadius;
    }

    public double getShieldDamage()
    {
      return shieldDamage;
    }

    public void setShieldDamage( final double shieldDamage )
    {
      this.shieldDamage = shieldDamage;
    }

    public double getHullDamage()
    {
      return hullDamage;
    }

    public void setHullDamage( final double hullDamage )
    {
      this.hullDamage = hullDamage;
    }

    public double getFuelDamage()
    {
      return fuelDamage;
    }

    public void setFuelDamage( final double fuelDamage )
    {
      this.fuelDamage = fuelDamage;
    }

    public double getHeatDamage()
    {
      return heatDamage;
    }

    public void setHeatDamage( final double heatDamage )
    {
      this.heatDamage = heatDamage;
    }

    public double getIonDamage()
    {
      return ionDamage;
    }

    public void setIonDamage( final double ionDamage )
    {
      this.ionDamage = ionDamage;
    }

    public double getDisruptionDamage()
    {
      return disruptionDamage;
    }

    public void setDisruptionDamage( final double disruptionDamage )
    {
      this.disruptionDamage = disruptionDamage;
    }

    public double getSlowingDamage()
    {
      return slowingDamage;
    }

    public void setSlowingDamage( final double slowingDamage )
    {
      this.slowingDamage = slowingDamage;
    }

    public double getHitForce()
    {
      return hitForce;
    }

    public void setHitForce( final double hitForce )
    {
      this.hitForce = hitForce;
    }

    public double getPiercing()
    {
      return piercing;
    }

    public void setPiercing( final double piercing )
    {
      this.piercing = piercing;
    }

    @Nonnull
    public Map<String, Integer> getFireEffects()
    {
      return Collections.unmodifiableMap( fireEffects );
    }

    public void incrementFireEffect( @Nonnull final String name, final int value )
    {
      //TODO: Should verify that value is positive and/or that effect value never goes below 1
      fireEffects.merge( name, value, Integer::sum );
    }

    @Nonnull
    public Map<String, Integer> getLiveEffects()
    {
      return Collections.unmodifiableMap( liveEffects );
    }

    public void incrementLiveEffect( @Nonnull final String name, final int value )
    {
      liveEffects.merge( name, value, Integer::sum );
    }

    @Nonnull
    public Map<String, Integer> getHitEffects()
    {
      return Collections.unmodifiableMap( hitEffects );
    }

    public void incrementHitEffect( @Nonnull final String name, final int value )
    {
      hitEffects.merge( name, value, Integer::sum );
    }

    @Nonnull
    public Map<String, Integer> getDieEffects()
    {
      return Collections.unmodifiableMap( dieEffects );
    }

    public void incrementDieEffect( @Nonnull final String name, final int value )
    {
      dieEffects.merge( name, value, Integer::sum );
    }

    @Nonnull
    public Map<String, Integer> getSubmunitions()
    {
      return Collections.unmodifiableMap( submunitions );
    }

    public void incrementSubmunition( @Nonnull final String name, final int value )
    {
      submunitions.merge( name, value, Integer::sum );
    }
  }
}
