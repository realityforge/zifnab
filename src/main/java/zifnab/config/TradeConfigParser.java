package zifnab.config;

import java.util.List;
import javax.annotation.Nonnull;
import zifnab.hdf.DataAccessException;
import zifnab.hdf.DataElement;

final class TradeConfigParser
{
  private TradeConfigParser()
  {
  }

  @Nonnull
  static TradeConfig from( @Nonnull final DataElement element )
  {
    element.assertTokenCount( 1 );
    element.assertTokenName( "trade" );
    final TradeConfig config = new TradeConfig();
    parseConfig( config, element.getChildElements() );
    return config;
  }

  static boolean matches( @Nonnull final DataElement element )
  {
    return "trade".equals( element.getName() );
  }

  private static void parseConfig( @Nonnull final TradeConfig config, @Nonnull final List<DataElement> elements )
  {
    for ( final DataElement element : elements )
    {
      final String name = element.getName();
      if ( "commodity".equals( name ) )
      {
        parseCommodity( config, element );
      }
      else
      {
        throw new DataAccessException( "Unexpected data element named '" + name + "'", element.getLocation() );
      }
    }
  }

  private static void parseCommodity( @Nonnull final TradeConfig config, @Nonnull final DataElement element )
  {
    element.assertTokenCounts( 2, 4 );
    final TradeConfig.Commodity commodity =
      2 == element.getTokenCount() ?
      config.addSpecialCommodity( element.getStringAt( 1 ) ) :
      config.addCommodity( element.getStringAt( 1 ),
                           element.getIntAt( 2 ),
                           element.getIntAt( 3 ) );
    final List<DataElement> children = element.getChildElements();
    for ( final DataElement child : children )
    {
      child.assertTokenCount( 1 );
      commodity.addItem( child.getName() );
    }
  }
}
