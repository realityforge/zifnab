package zifnab.config;

import javax.annotation.Nonnull;
import zifnab.hdf.DataDocument;
import zifnab.hdf.DataElement;

final class EffectConfigEncoder
{
  @Nonnull
  static DataElement encode( @Nonnull final DataDocument document, @Nonnull final EffectConfig effect )
  {
    final DataElement element = document.element( "effect", effect.getName() );
    encodeEffect( element, effect );
    return element;
  }

  private static void encodeEffect( @Nonnull final DataElement element, @Nonnull final EffectConfig effect )
  {
    final SpriteConfig sprite = effect.getSprite();
    if ( null != sprite )
    {
      SpriteConfig.encode( element, "sprite", sprite );
    }
    final String sound = effect.getSound();
    if ( null != sound )
    {
      element.element( "sound", sound );
    }
    final int lifetime = effect.getLifetime();
    if ( 0 != lifetime )
    {
      element.element( "lifetime", String.valueOf( lifetime ) );
    }
    final double velocityScale = effect.getVelocityScale();
    if ( 0 != velocityScale )
    {
      element.element( "velocity scale", String.valueOf( velocityScale ) );
    }
    final double randomVelocity = effect.getRandomVelocity();
    if ( 0 != randomVelocity )
    {
      element.element( "random velocity", String.valueOf( randomVelocity ) );
    }
    final double randomAngle = effect.getRandomAngle();
    if ( 0 != randomAngle )
    {
      element.element( "random angle", String.valueOf( randomAngle ) );
    }
    final double randomSpin = effect.getRandomSpin();
    if ( 0 != randomSpin )
    {
      element.element( "random spin", String.valueOf( randomSpin ) );
    }
    final double randomFrameRate = effect.getRandomFrameRate();
    if ( 0 != randomFrameRate )
    {
      element.element( "random frame rate", String.valueOf( randomFrameRate ) );
    }
  }
}
