package zifnab.config;

import java.util.Collection;
import java.util.Comparator;
import javax.annotation.Nonnull;
import zifnab.hdf.DataDocument;
import zifnab.hdf.DataElement;

final class SystemConfigEncoder
{
  @Nonnull
  static DataElement encode( @Nonnull final DataDocument document, @Nonnull final SystemConfig system )
  {
    final DataElement element = document.element( "system", system.getName() );
    encodeSystem( element, system );
    return element;
  }

  private static void encodeSystem( @Nonnull final DataElement element, @Nonnull final SystemConfig system )
  {
    final Position pos = system.getPos();
    if ( null != pos )
    {
      element.element( "pos", String.valueOf( pos.getX() ), String.valueOf( pos.getY() ) );
    }

    final String government = system.getGovernment();
    if ( null != government )
    {
      element.element( "government", government );
    }

    final String haze = system.getHaze();
    if ( null != haze )
    {
      element.element( "haze", haze );
    }

    final String music = system.getMusic();
    if ( null != music )
    {
      element.element( "music", music );
    }

    final Double habitable = system.getHabitable();
    if ( null != habitable )
    {
      element.element( "habitable", String.valueOf( habitable ) );
    }

    final Double belt = system.getBelt();
    if ( null != belt )
    {
      element.element( "belt", String.valueOf( belt ) );
    }

    system.getLinks().stream().sorted().forEachOrdered( link -> element.element( "link", String.valueOf( link ) ) );

    system.getAsteroids()
      .stream()
      .sorted( Comparator.comparing( SystemConfig.Asteroid::getName ) )
      .forEachOrdered( asteroid -> element.element( "asteroids",
                                                    asteroid.getName(),
                                                    String.valueOf( asteroid.getCount() ),
                                                    String.valueOf( asteroid.getEnergy() ) ) );

    system.getMinables()
      .stream()
      .sorted( Comparator.comparing( SystemConfig.Minable::getName ) )
      .forEachOrdered( minable -> element.element( "minables",
                                                   minable.getName(),
                                                   String.valueOf( minable.getCount() ),
                                                   String.valueOf( minable.getEnergy() ) ) );

    system.getTrades()
      .stream()
      .sorted( Comparator.comparing( SystemConfig.Trade::getName ) )
      .forEachOrdered( trade -> element.element( "trade",
                                                 trade.getName(),
                                                 String.valueOf( trade.getBase() ) ) );

    system.getFleets()
      .stream()
      .sorted( Comparator.comparing( SystemConfig.Fleet::getName ) )
      .forEachOrdered( fleet -> element.element( "fleet",
                                                 fleet.getName(),
                                                 String.valueOf( fleet.getPeriod() ) ) );

    encodeObjects( element, system.getObjects() );
  }

  private static void encodeObjects( @Nonnull final DataElement element,
                                     @Nonnull final Collection<SystemConfig.StellarObject> objects )
  {
    objects
      .stream()
      .sorted( Comparator
                 .comparing( SystemConfig.StellarObject::getDistance )
                 .thenComparing( SystemConfig.StellarObject::getOffset ) )
      .forEachOrdered( object -> encodeObject( element, object ) );

  }

  private static void encodeObject( @Nonnull final DataElement parent,
                                    @Nonnull final SystemConfig.StellarObject object )
  {
    final String name = object.getName();
    final DataElement element = null != name ? parent.element( "object", name ) : parent.element( "object" );

    final SpriteConfig sprite = object.getSprite();
    if ( null != sprite )
    {
      SpriteConfig.encode( element, "sprite", sprite );
    }

    element.element( "distance", String.valueOf( object.getDistance() ) );

    element.element( "period", String.valueOf( object.getPeriod() ) );

    final double offset = object.getOffset();
    if ( 0.0D != offset )
    {
      element.element( "offset", String.valueOf( offset ) );
    }

    encodeObjects( element, object.getObjects() );
  }
}
