package zifnab.config;

import java.util.List;
import javax.annotation.Nonnull;
import zifnab.hdf.DataAccessException;
import zifnab.hdf.DataElement;

final class SystemConfigParser
{
  private static final String NAME = "system";

  private SystemConfigParser()
  {
  }

  @Nonnull
  static SystemConfig from( @Nonnull final DataElement element )
  {
    element.assertTokenCount( 2 );
    element.assertTokenName( NAME );
    final String name = element.getStringAt( 1 );
    final SystemConfig config = new SystemConfig( name );
    parseConfig( config, element.getChildElements() );
    return config;
  }

  static boolean matches( @Nonnull final DataElement element )
  {
    return NAME.equals( element.getName() );
  }

  private static void parseConfig( @Nonnull final SystemConfig config,
                                   @Nonnull final List<DataElement> elements )
  {
    for ( final DataElement element : elements )
    {
      final String name = element.getName();
      switch ( name )
      {
        case "pos":
          parsePos( config, element );
          break;
        case "government":
          parseGovernment( config, element );
          break;
        case "haze":
          parseHaze( config, element );
          break;
        case "music":
          parseMusic( config, element );
          break;
        case "habitable":
          parseHabitable( config, element );
          break;
        case "belt":
          parseBelt( config, element );
          break;
        case "link":
          parseLink( config, element );
          break;
        case "asteroids":
          parseAsteroids( config, element );
          break;
        case "minables":
          parseMinables( config, element );
          break;
        case "trade":
          parseTrade( config, element );
          break;
        case "fleet":
          parseFleet( config, element );
          break;
        case "object":
          config.addObject( parseObject( element ) );
          break;
        default:
          throw new DataAccessException( "Unexpected data element named '" + name + "'", element.getLocation() );
      }
    }
  }

  @Nonnull
  private static SystemConfig.StellarObject parseObject( @Nonnull final DataElement element )
  {
    element.assertTokenName( "object" );
    element.assertTokenCount( 1, 2 );
    final String name = 2 == element.getTokens().size() ? element.getStringAt( 1 ) : null;
    final SystemConfig.StellarObject object = new SystemConfig.StellarObject( name );
    parseConfig( object, element.getChildElements() );
    return object;
  }

  private static void parseConfig( @Nonnull final SystemConfig.StellarObject object,
                                   @Nonnull final List<DataElement> elements )
  {
    for ( final DataElement element : elements )
    {
      final String name = element.getName();
      switch ( name )
      {
        case "sprite":
          parseObjectSprite( object, element );
          break;
        case "distance":
          parseObjectDistance( object, element );
          break;
        case "period":
          parseObjectPeriod( object, element );
          break;
        case "offset":
          parseObjectOffset( object, element );
          break;
        case "object":
          object.addObject( parseObject( element ) );
          break;
        default:
          throw new DataAccessException( "Unexpected data element named '" + name + "'", element.getLocation() );
      }
    }
  }

  private static void parseObjectSprite( @Nonnull final SystemConfig.StellarObject object,
                                         @Nonnull final DataElement element )
  {
    element.assertTokenName( "sprite" );
    element.assertTokenCount( 2 );
    object.setSprite( element.getStringAt( 1 ) );
  }

  private static void parseObjectDistance( @Nonnull final SystemConfig.StellarObject object,
                                           @Nonnull final DataElement element )
  {
    element.assertTokenName( "distance" );
    element.assertTokenCount( 2 );
    object.setDistance( element.getDoubleAt( 1 ) );
  }

  private static void parseObjectPeriod( @Nonnull final SystemConfig.StellarObject object,
                                         @Nonnull final DataElement element )
  {
    element.assertTokenName( "period" );
    element.assertTokenCount( 2 );
    object.setPeriod( element.getDoubleAt( 1 ) );
  }

  private static void parseObjectOffset( @Nonnull final SystemConfig.StellarObject object,
                                         @Nonnull final DataElement element )
  {
    element.assertTokenName( "offset" );
    element.assertTokenCount( 2 );
    object.setOffset( element.getDoubleAt( 1 ) );
  }

  private static void parseFleet( @Nonnull final SystemConfig config, @Nonnull final DataElement element )
  {
    element.assertTokenName( "fleet" );
    element.assertTokenCount( 3 );
    config.addFleet( element.getStringAt( 1 ), element.getIntAt( 2 ) );
  }

  private static void parseTrade( @Nonnull final SystemConfig config, @Nonnull final DataElement element )
  {
    element.assertTokenName( "trade" );
    element.assertTokenCount( 3 );
    config.addTrade( element.getStringAt( 1 ), element.getIntAt( 2 ) );
  }

  private static void parseMinables( @Nonnull final SystemConfig config, @Nonnull final DataElement element )
  {
    element.assertTokenName( "minables" );
    element.assertTokenCount( 4 );
    config.addMinable( element.getStringAt( 1 ), element.getIntAt( 2 ), element.getDoubleAt( 3 ) );
  }

  private static void parseAsteroids( @Nonnull final SystemConfig config, @Nonnull final DataElement element )
  {
    element.assertTokenName( "asteroids" );
    element.assertTokenCount( 4 );
    config.addAsteroid( element.getStringAt( 1 ), element.getIntAt( 2 ), element.getDoubleAt( 3 ) );
  }

  private static void parseLink( @Nonnull final SystemConfig config, @Nonnull final DataElement element )
  {
    element.assertTokenName( "link" );
    element.assertTokenCount( 2 );
    config.addLink( element.getStringAt( 1 ) );
  }

  private static void parseBelt( @Nonnull final SystemConfig config, @Nonnull final DataElement element )
  {
    element.assertTokenName( "belt" );
    element.assertTokenCount( 2 );
    config.setBelt( element.getDoubleAt( 1 ) );
  }

  private static void parseHabitable( @Nonnull final SystemConfig config, @Nonnull final DataElement element )
  {
    element.assertTokenName( "habitable" );
    element.assertTokenCount( 2 );
    config.setHabitable( element.getDoubleAt( 1 ) );
  }

  private static void parseGovernment( @Nonnull final SystemConfig config, @Nonnull final DataElement element )
  {
    element.assertTokenName( "government" );
    element.assertTokenCount( 2 );
    config.setGovernment( element.getStringAt( 1 ) );
  }

  private static void parseHaze( @Nonnull final SystemConfig config, @Nonnull final DataElement element )
  {
    element.assertTokenName( "haze" );
    element.assertTokenCount( 2 );
    config.setHaze( element.getStringAt( 1 ) );
  }

  private static void parseMusic( @Nonnull final SystemConfig config, @Nonnull final DataElement element )
  {
    element.assertTokenName( "music" );
    element.assertTokenCount( 2 );
    config.setMusic( element.getStringAt( 1 ) );
  }

  private static void parsePos( @Nonnull final SystemConfig config, @Nonnull final DataElement element )
  {
    element.assertTokenName( "pos" );
    element.assertTokenCount( 3 );
    config.setPos( new Position( element.getDoubleAt( 1 ), element.getDoubleAt( 2 ) ) );
  }
}
