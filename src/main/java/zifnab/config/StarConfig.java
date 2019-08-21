package zifnab.config;

import java.util.Objects;
import javax.annotation.Nonnull;
import zifnab.hdf.DataDocument;
import zifnab.hdf.DataElement;

public final class StarConfig
{
  @Nonnull
  private final String name;
  private double power;
  private double wind;

  public StarConfig( @Nonnull final String name )
  {
    this.name = Objects.requireNonNull( name );
  }

  @Nonnull
  public static StarConfig from( @Nonnull final DataElement element )
  {
    return StarConfigParser.from( element );
  }

  @Nonnull
  public static DataElement encode( @Nonnull final DataDocument document, @Nonnull final StarConfig star )
  {
    return StarConfigEncoder.encode( document, star );
  }

  public static boolean matches( @Nonnull final DataElement element )
  {
    return StarConfigParser.matches( element );
  }

  @Nonnull
  public String getName()
  {
    return name;
  }

  public double getPower()
  {
    return power;
  }

  public void setPower( final double power )
  {
    this.power = power;
  }

  public double getWind()
  {
    return wind;
  }

  public void setWind( final double wind )
  {
    this.wind = wind;
  }
}
