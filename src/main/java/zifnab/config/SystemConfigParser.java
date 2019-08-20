package zifnab.config;

import java.util.List;
import javax.annotation.Nonnull;
import zifnab.hdf.DataAccessException;
import zifnab.hdf.DataElement;

final class SystemConfigParser
{
  private SystemConfigParser()
  {
  }

  @Nonnull
  static SystemConfig from( @Nonnull final DataElement element )
  {
    element.assertTokenCount( 2 );
    element.assertTokenName( "system" );
    final String name = element.getStringAt( 1 );
    final SystemConfig config = new SystemConfig( name );
    parseConfig( config, element.getChildElements() );
    return config;
  }

  static boolean matches( @Nonnull final DataElement element )
  {
    return "system".equals( element.getName() );
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
          element.assertTokenCount( 3 );
          config.setPos( new Position( element.getDoubleAt( 1 ), element.getDoubleAt( 2 ) ) );
          break;
        case "government":
          element.assertTokenCount( 2 );
          config.setGovernment( element.getStringAt( 1 ) );
          break;
        case "haze":
          element.assertTokenCount( 2 );
          config.setHaze( element.getStringAt( 1 ) );
          break;
        case "music":
          element.assertTokenCount( 2 );
          config.setMusic( element.getStringAt( 1 ) );
          break;
        case "habitable":
          element.assertTokenCount( 2 );
          config.setHabitable( element.getDoubleAt( 1 ) );
          break;
        case "belt":
          element.assertTokenCount( 2 );
          config.setBelt( element.getDoubleAt( 1 ) );
          break;
        case "link":
          element.assertTokenCount( 2 );
          config.addLink( element.getStringAt( 1 ) );
          break;
        case "asteroids":
          element.assertTokenCount( 4 );
          config.addAsteroid( element.getStringAt( 1 ), element.getIntAt( 2 ), element.getDoubleAt( 3 ) );
          break;
        case "minables":
          element.assertTokenCount( 4 );
          config.addMinable( element.getStringAt( 1 ), element.getIntAt( 2 ), element.getDoubleAt( 3 ) );
          break;
        case "trade":
          element.assertTokenCount( 3 );
          config.addTrade( element.getStringAt( 1 ), element.getIntAt( 2 ) );
          break;
        case "fleet":
          element.assertTokenCount( 3 );
          config.addFleet( element.getStringAt( 1 ), element.getIntAt( 2 ) );
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
    element.assertTokenCountRange( 1, 2 );
    final String name = 2 == element.getTokenCount() ? element.getStringAt( 1 ) : null;
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
          element.assertTokenCount( 2 );
          object.setSprite( element.getStringAt( 1 ) );
          break;
        case "distance":
          element.assertTokenCount( 2 );
          object.setDistance( element.getDoubleAt( 1 ) );
          break;
        case "period":
          element.assertTokenCount( 2 );
          object.setPeriod( element.getDoubleAt( 1 ) );
          break;
        case "offset":
          element.assertTokenCount( 2 );
          object.setOffset( element.getDoubleAt( 1 ) );
          break;
        case "object":
          object.addObject( parseObject( element ) );
          break;
        default:
          throw new DataAccessException( "Unexpected data element named '" + name + "'", element.getLocation() );
      }
    }
  }
}
