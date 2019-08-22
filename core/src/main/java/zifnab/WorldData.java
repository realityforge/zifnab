package zifnab;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nonnull;
import zifnab.assets.ImageRegistry;
import zifnab.config.GalaxyConfig;
import zifnab.config.OutfitConfig;
import zifnab.config.StarConfig;
import zifnab.config.SystemConfig;
import zifnab.config.TradeConfig;
import zifnab.hdf.DataDocument;
import zifnab.hdf.DataElement;

@SuppressWarnings( "UnusedReturnValue" )
public final class WorldData
{
  @Nonnull
  private final Set<GalaxyConfig> galaxies = new HashSet<>();
  @Nonnull
  private final Collection<GalaxyConfig> roGalaxies = Collections.unmodifiableCollection( galaxies );
  @Nonnull
  private final Set<SystemConfig> systems = new HashSet<>();
  @Nonnull
  private final Collection<SystemConfig> roSystems = Collections.unmodifiableCollection( systems );
  @Nonnull
  private final Set<StarConfig> stars = new HashSet<>();
  @Nonnull
  private final Collection<StarConfig> roStars = Collections.unmodifiableCollection( stars );
  @Nonnull
  private final Set<TradeConfig> trades = new HashSet<>();
  @Nonnull
  private final Collection<TradeConfig> roTrades = Collections.unmodifiableCollection( trades );
  @Nonnull
  private final Set<OutfitConfig> outfits = new HashSet<>();
  @Nonnull
  private final Collection<OutfitConfig> roOutfits = Collections.unmodifiableCollection( outfits );
  @Nonnull
  private final ImageRegistry imageRegistry = new ImageRegistry();

  public boolean addGalaxy( @Nonnull final GalaxyConfig config )
  {
    return galaxies.add( config );
  }

  public boolean removeGalaxy( @Nonnull final GalaxyConfig config )
  {
    return galaxies.remove( config );
  }

  @Nonnull
  public Collection<GalaxyConfig> getGalaxies()
  {
    return roGalaxies;
  }

  public boolean addSystem( @Nonnull final SystemConfig config )
  {
    return systems.add( config );
  }

  public boolean removeSystem( @Nonnull final SystemConfig config )
  {
    return systems.remove( config );
  }

  @Nonnull
  public Collection<SystemConfig> getSystems()
  {
    return roSystems;
  }

  public boolean addStar( @Nonnull final StarConfig config )
  {
    return stars.add( config );
  }

  public boolean removeStar( @Nonnull final StarConfig config )
  {
    return stars.remove( config );
  }

  @Nonnull
  public Collection<StarConfig> getStars()
  {
    return roStars;
  }

  public boolean addTrade( @Nonnull final TradeConfig config )
  {
    return trades.add( config );
  }

  public boolean removeTrade( @Nonnull final TradeConfig config )
  {
    return trades.remove( config );
  }

  @Nonnull
  public Collection<TradeConfig> getTrades()
  {
    return roTrades;
  }

  public boolean addOutfit( @Nonnull final OutfitConfig config )
  {
    return outfits.add( config );
  }

  public boolean removeOutfit( @Nonnull final OutfitConfig config )
  {
    return outfits.remove( config );
  }

  @Nonnull
  public Collection<OutfitConfig> getOutfits()
  {
    return roOutfits;
  }

  @Nonnull
  public ImageRegistry getImageRegistry()
  {
    return imageRegistry;
  }

  public void importDocument( @Nonnull final DataDocument document )
  {
    for ( final DataElement element : document.getChildElements() )
    {
      if ( GalaxyConfig.matches( element ) )
      {
        addGalaxy( GalaxyConfig.from( element ) );
      }
      else if ( SystemConfig.matches( element ) )
      {
        addSystem( SystemConfig.from( element ) );
      }
      else if ( StarConfig.matches( element ) )
      {
        addStar( StarConfig.from( element ) );
      }
      else if ( TradeConfig.matches( element ) )
      {
        addTrade( TradeConfig.from( element ) );
      }
      else if ( OutfitConfig.matches( element ) )
      {
        addOutfit( OutfitConfig.from( element ) );
      }
    }
  }
}
