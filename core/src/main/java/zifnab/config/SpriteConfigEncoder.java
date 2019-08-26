package zifnab.config;

import javax.annotation.Nonnull;
import zifnab.hdf.DataElement;

final class SpriteConfigEncoder
{
  @Nonnull
  static DataElement encode( @Nonnull final DataElement parentElement,
                             @Nonnull final String name,
                             @Nonnull final SpriteConfig sprite )
  {
    final DataElement element = parentElement.element( name, sprite.getName() );
    encodeSprite( element, sprite );
    return element;
  }

  private static void encodeSprite( @Nonnull final DataElement element, @Nonnull final SpriteConfig sprite )
  {
    final double frameRate = sprite.getFrameRate();
    if ( 0D < frameRate )
    {
      element.element( "frame rate", String.valueOf( frameRate ) );
    }

    final double frameTime = sprite.getFrameTime();
    if ( 0D < frameTime )
    {
      element.element( "frame time", String.valueOf( frameTime ) );
    }

    final double delay = sprite.getDelay();
    if ( 0D < delay )
    {
      element.element( "delay", String.valueOf( delay ) );
    }

    final double startFrame = sprite.getStartFrame();
    if ( 0D < startFrame )
    {
      element.element( "start frame", String.valueOf( startFrame ) );
    }

    if ( sprite.isRandomStartFrame() )
    {
      element.element( "random start frame" );
    }
    if ( sprite.isNoRepeat() )
    {
      element.element( "no repeat" );
    }
    if ( sprite.isRewind() )
    {
      element.element( "rewind" );
    }
  }
}
