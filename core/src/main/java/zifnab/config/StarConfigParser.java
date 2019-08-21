package zifnab.config;

import java.util.List;
import javax.annotation.Nonnull;
import zifnab.hdf.DataAccessException;
import zifnab.hdf.DataElement;

final class StarConfigParser
{
  private StarConfigParser()
  {
  }

  @Nonnull
  static StarConfig from( @Nonnull final DataElement element )
  {
    element.assertTokenCount( 2 );
    element.assertTokenName( "star" );
    final String name = element.getStringAt( 1 );
    final StarConfig config = new StarConfig( name );
    parseConfig( config, element.getChildElements() );
    return config;
  }

  static boolean matches( @Nonnull final DataElement element )
  {
    return "star".equals( element.getName() );
  }

  private static void parseConfig( @Nonnull final StarConfig config, @Nonnull final List<DataElement> elements )
  {
    for ( final DataElement element : elements )
    {
      final String name = element.getName();
      switch ( name )
      {
        case "power":
          element.assertLeafNode();
          config.setPower( element.getDoubleAt( 1 ) );
          break;
        case "wind":
          element.assertLeafNode();
          element.assertTokenCount( 2 );
          config.setWind( element.getDoubleAt( 1 ) );
          break;
        default:
          throw new DataAccessException( "Unexpected data element named '" + name + "'", element.getLocation() );
      }
    }
  }
}
