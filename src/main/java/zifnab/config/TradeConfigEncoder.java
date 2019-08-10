package zifnab.config;

import java.util.Comparator;
import javax.annotation.Nonnull;
import zifnab.hdf.DataDocument;
import zifnab.hdf.DataElement;

final class TradeConfigEncoder
{
  @Nonnull
  static DataElement encode( @Nonnull final DataDocument document, @Nonnull final TradeConfig trade )
  {
    final DataElement element = document.element( "trade" );
    encodeTrade( element, trade );
    return element;
  }

  private static void encodeTrade( @Nonnull final DataElement element, @Nonnull final TradeConfig trade )
  {
    trade.getCommodities()
      .stream()
      .filter( c -> !c.isSpecial() )
      .sorted( Comparator.comparing( TradeConfig.Commodity::getName ) )
      .forEachOrdered( commodity -> encodeCommodity( element, commodity ) );

    trade.getCommodities()
      .stream()
      .filter( TradeConfig.Commodity::isSpecial )
      .sorted( Comparator.comparing( TradeConfig.Commodity::getName ) )
      .forEachOrdered( commodity -> encodeCommodity( element, commodity ) );
  }

  private static void encodeCommodity( @Nonnull final DataElement parent,
                                       @Nonnull final TradeConfig.Commodity commodity )
  {
    final DataElement element =
      commodity.isSpecial() ?
      parent.element( "commodity", commodity.getName() ) :
      parent.element( "commodity",
                      commodity.getName(),
                      String.valueOf( commodity.getLow() ),
                      String.valueOf( commodity.getHigh() ) );
    commodity.getItems().stream().sorted().forEachOrdered( element::element );
  }
}
