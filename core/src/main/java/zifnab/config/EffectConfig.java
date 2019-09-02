package zifnab.config;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import zifnab.hdf.DataDocument;
import zifnab.hdf.DataElement;

public final class EffectConfig
{
  @Nonnull
  private final String name;
  @Nullable
  private SpriteConfig _sprite;
  @Nullable
  private String sound;
  private int lifetime;
  private double velocityScale;
  private double randomVelocity;
  private double randomAngle;
  private double randomSpin;
  private double randomFrameRate;

  public EffectConfig( @Nonnull final String name )
  {
    this.name = Objects.requireNonNull( name );
  }

  @Nonnull
  public static EffectConfig from( @Nonnull final DataElement element )
  {
    return EffectConfigParser.from( element );
  }

  @Nonnull
  public static DataElement encode( @Nonnull final DataDocument document, @Nonnull final EffectConfig effect )
  {
    return EffectConfigEncoder.encode( document, effect );
  }

  public static boolean matches( @Nonnull final DataElement element )
  {
    return EffectConfigParser.matches( element );
  }

  @Nonnull
  public String getName()
  {
    return name;
  }

  @Nullable
  public SpriteConfig getSprite()
  {
    return _sprite;
  }

  public void setSprite( @Nullable final SpriteConfig sprite )
  {
    _sprite = sprite;
  }

  @Nullable
  public String getSound()
  {
    return sound;
  }

  public void setSound( @Nullable final String sound )
  {
    this.sound = sound;
  }

  public int getLifetime()
  {
    return lifetime;
  }

  public void setLifetime( final int lifetime )
  {
    this.lifetime = lifetime;
  }

  public double getVelocityScale()
  {
    return velocityScale;
  }

  public void setVelocityScale( final double velocityScale )
  {
    this.velocityScale = velocityScale;
  }

  public double getRandomVelocity()
  {
    return randomVelocity;
  }

  public void setRandomVelocity( final double randomVelocity )
  {
    this.randomVelocity = randomVelocity;
  }

  public double getRandomAngle()
  {
    return randomAngle;
  }

  public void setRandomAngle( final double randomAngle )
  {
    this.randomAngle = randomAngle;
  }

  public double getRandomSpin()
  {
    return randomSpin;
  }

  public void setRandomSpin( final double randomSpin )
  {
    this.randomSpin = randomSpin;
  }

  public double getRandomFrameRate()
  {
    return randomFrameRate;
  }

  public void setRandomFrameRate( final double randomFrameRate )
  {
    this.randomFrameRate = randomFrameRate;
  }
}
