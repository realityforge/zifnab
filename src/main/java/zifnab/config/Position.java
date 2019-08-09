package zifnab.config;

import javax.annotation.Nonnull;

public final class Position
{
  @Nonnull
  public static final Position ORIGIN = new Position( 0, 0 );
  private final double x;
  private final double y;

  public Position( final double x, final double y )
  {
    this.x = x;
    this.y = y;
  }

  public double getX()
  {
    return x;
  }

  public double getY()
  {
    return y;
  }
}
