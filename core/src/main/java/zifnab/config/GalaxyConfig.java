package zifnab.config;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import zifnab.hdf.DataDocument;
import zifnab.hdf.DataElement;

public final class GalaxyConfig
{
  @Nonnull
  private final String name;
  @Nullable
  private Position pos;
  @Nullable
  private String sprite;

  public GalaxyConfig( @Nonnull final String name )
  {
    this.name = Objects.requireNonNull( name );
  }

  @Nonnull
  public static GalaxyConfig from( @Nonnull final DataElement element )
  {
    return GalaxyConfigParser.from( element );
  }

  @Nonnull
  public static DataElement encode( @Nonnull final DataDocument document, @Nonnull final GalaxyConfig galaxy )
  {
    return GalaxyConfigEncoder.encode( document, galaxy );
  }

  public static boolean matches( @Nonnull final DataElement element )
  {
    return GalaxyConfigParser.matches( element );
  }

  @Nonnull
  public String getName()
  {
    return name;
  }

  @Nullable
  public Position getPos()
  {
    return pos;
  }

  public void setPos( @Nullable final Position pos )
  {
    this.pos = pos;
  }

  @Nullable
  public String getSprite()
  {
    return sprite;
  }

  public void setSprite( @Nullable final String sprite )
  {
    this.sprite = sprite;
  }
}
