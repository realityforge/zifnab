package zifnab.config;

import java.util.Objects;
import javax.annotation.Nonnull;
import zifnab.hdf.DataElement;

public final class SpriteConfig
{
  @Nonnull
  private final String name;
  private double frameRate;
  private double frameTime;
  private double delay;
  private double startFrame;
  private boolean randomStartFrame;
  private boolean noRepeat;
  private boolean rewind;

  SpriteConfig( @Nonnull final String name )
  {
    this.name = Objects.requireNonNull( name );
  }

  @Nonnull
  public static SpriteConfig from( @Nonnull final DataElement element )
  {
    return SpriteConfigParser.from( element );
  }

  @Nonnull
  public static DataElement encode( @Nonnull final DataElement parentElement,
                                    @Nonnull final String name,
                                    @Nonnull final SpriteConfig sprite )
  {
    return SpriteConfigEncoder.encode( parentElement, name, sprite );
  }

  @Nonnull
  public String getName()
  {
    return name;
  }

  public double getFrameRate()
  {
    return frameRate;
  }

  public void setFrameRate( final double frameRate )
  {
    assert frameRate >= 0D;
    this.frameRate = frameRate;
  }

  public double getFrameTime()
  {
    return frameTime;
  }

  public void setFrameTime( final double frameTime )
  {
    assert frameTime >= 0D;
    this.frameTime = frameTime;
  }

  public double getDelay()
  {
    return delay;
  }

  public void setDelay( final double delay )
  {
    assert delay >= 0D;
    this.delay = delay;
  }

  public double getStartFrame()
  {
    return startFrame;
  }

  public void setStartFrame( final double startFrame )
  {
    assert startFrame >= 0D;
    this.startFrame = startFrame;
  }

  public boolean isRandomStartFrame()
  {
    return randomStartFrame;
  }

  public void setRandomStartFrame( final boolean randomStartFrame )
  {
    this.randomStartFrame = randomStartFrame;
  }

  public boolean isNoRepeat()
  {
    return noRepeat;
  }

  public void setNoRepeat( final boolean noRepeat )
  {
    this.noRepeat = noRepeat;
  }

  public boolean isRewind()
  {
    return rewind;
  }

  public void setRewind( final boolean rewind )
  {
    this.rewind = rewind;
  }
}
