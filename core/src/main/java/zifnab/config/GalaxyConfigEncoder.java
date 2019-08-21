package zifnab.config;

import javax.annotation.Nonnull;
import zifnab.hdf.DataDocument;
import zifnab.hdf.DataElement;

final class GalaxyConfigEncoder
{
  @Nonnull
  static DataElement encode( @Nonnull final DataDocument document, @Nonnull final GalaxyConfig galaxy )
  {
    final DataElement element = document.element( "galaxy", galaxy.getName() );
    encodeGalaxy( element, galaxy );
    return element;
  }

  private static void encodeGalaxy( @Nonnull final DataElement element, @Nonnull final GalaxyConfig galaxy )
  {
    final Position pos = galaxy.getPos();
    if ( null != pos )
    {
      element.element( "pos", String.valueOf( pos.getX() ), String.valueOf( pos.getY() ) );
    }

    final String sprite = galaxy.getSprite();
    if ( null != sprite )
    {
      element.element( "sprite", sprite );
    }
  }
}
