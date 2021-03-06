package zifnab.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import zifnab.hdf.DataDocument;
import zifnab.hdf.DataElement;

@SuppressWarnings( { "WeakerAccess", "unused" } )
public final class SystemConfig
{
  @Nonnull
  private final String name;
  @Nullable
  private Position pos;
  @Nullable
  private String government;
  @Nullable
  private String haze;
  @Nullable
  private String music;
  @Nullable
  private Double habitable;
  @Nullable
  private Double belt;
  @Nonnull
  private final Set<String> links = new HashSet<>();
  @Nonnull
  private final Map<String, Asteroid> asteroids = new HashMap<>();
  @Nonnull
  private final Map<String, Minable> minables = new HashMap<>();
  @Nonnull
  private final Map<String, Trade> trades = new HashMap<>();
  @Nonnull
  private final Map<String, Fleet> fleets = new HashMap<>();
  @Nonnull
  private final Collection<StellarObject> objects = new ArrayList<>();

  public SystemConfig( @Nonnull final String name )
  {
    this.name = Objects.requireNonNull( name );
  }

  @Nonnull
  public static SystemConfig from( @Nonnull final DataElement element )
  {
    return SystemConfigParser.from( element );
  }

  @Nonnull
  public static DataElement encode( @Nonnull final DataDocument document, @Nonnull final SystemConfig system )
  {
    return SystemConfigEncoder.encode( document, system );
  }

  public static boolean matches( @Nonnull final DataElement element )
  {
    return SystemConfigParser.matches( element );
  }

  @Nonnull
  public String getName()
  {
    return name;
  }

  @Nullable
  public Position getPos()
  {
    return pos;
  }

  public void setPos( @Nullable final Position pos )
  {
    this.pos = pos;
  }

  @Nullable
  public String getGovernment()
  {
    return government;
  }

  public void setGovernment( @Nullable final String government )
  {
    this.government = government;
  }

  @Nullable
  public String getHaze()
  {
    return haze;
  }

  public void setHaze( @Nullable final String haze )
  {
    this.haze = haze;
  }

  @Nullable
  public String getMusic()
  {
    return music;
  }

  public void setMusic( @Nullable final String music )
  {
    this.music = music;
  }

  @Nullable
  public Double getHabitable()
  {
    return habitable;
  }

  public void setHabitable( @Nullable final Double habitable )
  {
    this.habitable = habitable;
  }

  @Nullable
  public Double getBelt()
  {
    return belt;
  }

  public void setBelt( @Nullable final Double belt )
  {
    this.belt = belt;
  }

  @Nonnull
  public Set<String> getLinks()
  {
    return Collections.unmodifiableSet( links );
  }

  public boolean isLinked( @Nonnull final String link )
  {
    return links.contains( link );
  }

  public void addLink( @Nonnull final String link )
  {
    links.add( link );
  }

  public boolean removeLink( @Nonnull final String link )
  {
    return links.remove( link );
  }

  @Nonnull
  public Collection<Asteroid> getAsteroids()
  {
    return Collections.unmodifiableCollection( asteroids.values() );
  }

  @Nullable
  public Asteroid findAsteroidByName( @Nonnull final String name )
  {
    return asteroids.get( name );
  }

  @Nonnull
  public Asteroid addAsteroid( @Nonnull final String name, final int count, final double energy )
  {
    final Asteroid asteroid = new Asteroid( name, count, energy );
    asteroids.put( name, asteroid );
    return asteroid;
  }

  public boolean removeAsteroid( @Nonnull final String name )
  {
    return null != asteroids.remove( name );
  }

  public boolean removeAsteroid( @Nonnull final Asteroid asteroid )
  {
    final Asteroid existing = asteroids.get( asteroid.getName() );
    final boolean found = existing == asteroid;
    if ( found )
    {
      asteroids.remove( asteroid.getName() );
    }
    return found;
  }

  @Nonnull
  public Minable addMinable( @Nonnull final String name, final int count, final double energy )
  {
    final Minable minable = new Minable( name, count, energy );
    minables.put( name, minable );
    return minable;
  }

  @Nonnull
  public Collection<Minable> getMinables()
  {
    return Collections.unmodifiableCollection( minables.values() );
  }

  @Nullable
  public Minable findMinableByName( @Nonnull final String name )
  {
    return minables.get( name );
  }

  public boolean removeMinable( @Nonnull final String name )
  {
    return null != minables.remove( name );
  }

  public boolean removeMinable( @Nonnull final Minable minable )
  {
    final Minable existing = minables.get( minable.getName() );
    final boolean found = existing == minable;
    if ( found )
    {
      minables.remove( minable.getName() );
    }
    return found;
  }

  @Nonnull
  public Collection<Trade> getTrades()
  {
    return Collections.unmodifiableCollection( trades.values() );
  }

  @Nullable
  public Trade findTradeByName( @Nonnull final String name )
  {
    return trades.get( name );
  }

  @Nonnull
  public Trade addTrade( @Nonnull final String name, final int base )
  {
    final Trade trade = new Trade( name, base );
    addTrade( trade );
    return trade;
  }

  public void addTrade( @Nonnull final Trade trade )
  {
    trades.put( trade.getName(), trade );
  }

  public boolean removeTrade( @Nonnull final String name )
  {
    return null != trades.remove( name );
  }

  public boolean removeTrade( @Nonnull final Trade trade )
  {
    final Trade existing = trades.get( trade.getName() );
    final boolean found = existing == trade;
    if ( found )
    {
      trades.remove( trade.getName() );
    }
    return found;
  }

  @Nonnull
  public Collection<Fleet> getFleets()
  {
    return Collections.unmodifiableCollection( fleets.values() );
  }

  @Nullable
  public Fleet findFleetByName( @Nonnull final String name )
  {
    return fleets.get( name );
  }

  @Nonnull
  public Fleet addFleet( @Nonnull final String name, final int period )
  {
    final Fleet fleet = new Fleet( name, period );
    fleets.put( name, fleet );
    return fleet;
  }

  public boolean removeFleet( @Nonnull final String name )
  {
    return null != fleets.remove( name );
  }

  public boolean removeFleet( @Nonnull final Fleet fleet )
  {
    final Fleet existing = fleets.get( fleet.getName() );
    final boolean found = existing == fleet;
    if ( found )
    {
      fleets.remove( fleet.getName() );
    }
    return found;
  }

  @Nonnull
  public Collection<StellarObject> getObjects()
  {
    return Collections.unmodifiableCollection( objects );
  }

  @Nullable
  public StellarObject findObjectByName( @Nonnull final String name )
  {
    return objects.stream().filter( o -> name.equals( o.getName() ) ).findAny().orElse( null );
  }

  public void addObject( @Nonnull final StellarObject object )
  {
    objects.add( object );
  }

  public boolean removeObject( @Nonnull final StellarObject object )
  {
    return objects.remove( object );
  }

  public static final class Trade
  {
    @Nonnull
    private final String name;
    private final int base;

    Trade( @Nonnull final String name, final int base )
    {
      assert base > 0;
      this.name = Objects.requireNonNull( name );
      this.base = base;
    }

    @Nonnull
    public String getName()
    {
      return name;
    }

    public int getBase()
    {
      return base;
    }
  }

  public static final class Fleet
  {
    @Nonnull
    private final String name;
    private final int period;

    Fleet( @Nonnull final String name, final int period )
    {
      assert period > 0;
      this.name = Objects.requireNonNull( name );
      this.period = period;
    }

    @Nonnull
    public String getName()
    {
      return name;
    }

    public int getPeriod()
    {
      return period;
    }
  }

  public static final class Asteroid
  {
    @Nonnull
    private final String name;
    private final int count;
    private final double energy;

    Asteroid( @Nonnull final String name, final int count, final double energy )
    {
      assert count > 0;
      assert energy > 0;
      this.name = Objects.requireNonNull( name );
      this.count = count;
      this.energy = energy;
    }

    @Nonnull
    public String getName()
    {
      return name;
    }

    public int getCount()
    {
      return count;
    }

    public double getEnergy()
    {
      return energy;
    }
  }

  public static final class Minable
  {
    @Nonnull
    private final String name;
    private final int count;
    private final double energy;

    Minable( @Nonnull final String name, final int count, final double energy )
    {
      assert count > 0;
      assert energy > 0;
      this.name = Objects.requireNonNull( name );
      this.count = count;
      this.energy = energy;
    }

    @Nonnull
    public String getName()
    {
      return name;
    }

    public int getCount()
    {
      return count;
    }

    public double getEnergy()
    {
      return energy;
    }
  }

  public static final class StellarObject
  {
    @Nullable
    private final String name;
    @Nullable
    private SpriteConfig sprite;
    private double distance;
    private double period;
    private double offset;
    @Nonnull
    private final List<StellarObject> objects = new ArrayList<>();

    public StellarObject( @Nullable final String name )
    {
      this.name = name;
    }

    @Nullable
    public String getName()
    {
      return name;
    }

    @Nullable
    public SpriteConfig getSprite()
    {
      return sprite;
    }

    public void setSprite( @Nullable final SpriteConfig sprite )
    {
      this.sprite = sprite;
    }

    public double getDistance()
    {
      return distance;
    }

    public void setDistance( final double distance )
    {
      this.distance = distance;
    }

    public double getPeriod()
    {
      return period;
    }

    public void setPeriod( final double period )
    {
      this.period = period;
    }

    public double getOffset()
    {
      return offset;
    }

    public void setOffset( final double offset )
    {
      this.offset = offset;
    }

    @Nonnull
    public List<StellarObject> getObjects()
    {
      return Collections.unmodifiableList( objects );
    }

    @Nullable
    public StellarObject findObjectByName( @Nonnull final String name )
    {
      return objects.stream().filter( o -> name.equals( o.getName() ) ).findAny().orElse( null );
    }

    public void addObject( @Nonnull final StellarObject object )
    {
      assert this != object;
      objects.add( object );
    }

    public boolean removeObject( @Nonnull final StellarObject object )
    {
      return objects.remove( object );
    }
  }
}
