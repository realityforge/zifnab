package zifnab.config;

import javax.annotation.Nonnull;
import zifnab.hdf.DataDocument;
import zifnab.hdf.DataElement;

final class StarConfigEncoder
{
  @Nonnull
  static DataElement encode( @Nonnull final DataDocument document, @Nonnull final StarConfig star )
  {
    final DataElement element = document.element( "star", star.getName() );
    encodeStar( element, star );
    return element;
  }

  private static void encodeStar( @Nonnull final DataElement element, @Nonnull final StarConfig star )
  {
    final double power = star.getPower();
    if ( 0D != power )
    {
      element.element( "power", String.valueOf( power ) );
    }
    final double wind = star.getWind();
    if ( 0D != wind )
    {
      element.element( "wind", String.valueOf( wind ) );
    }
  }
}
