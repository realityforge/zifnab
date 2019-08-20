package zifnab.config;

import java.util.List;
import javax.annotation.Nonnull;
import zifnab.hdf.DataAccessException;
import zifnab.hdf.DataElement;

final class GalaxyConfigParser
{
  private GalaxyConfigParser()
  {
  }

  @Nonnull
  static GalaxyConfig from( @Nonnull final DataElement element )
  {
    element.assertTokenCount( 2 );
    element.assertTokenName( "galaxy" );
    final String name = element.getStringAt( 1 );
    final GalaxyConfig config = new GalaxyConfig( name );
    parseConfig( config, element.getChildElements() );
    return config;
  }

  static boolean matches( @Nonnull final DataElement element )
  {
    return "galaxy".equals( element.getName() );
  }

  private static void parseConfig( @Nonnull final GalaxyConfig config, @Nonnull final List<DataElement> elements )
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
        case "sprite":
          element.assertTokenCount( 2 );
          config.setSprite( element.getStringAt( 1 ) );
          break;
        default:
          throw new DataAccessException( "Unexpected data element named '" + name + "'", element.getLocation() );
      }
    }
  }
}
