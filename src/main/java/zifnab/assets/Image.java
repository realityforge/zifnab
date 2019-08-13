package zifnab.assets;

import java.util.Objects;
import javax.annotation.Nonnull;

public final class Image
{
  @Nonnull
  private final String name;
  @Nonnull
  private final String category;
  /**
   * The key is the string used when referencing asset from within the data files.
   * It is the relative filename from asset directory without the file extension.
   */
  @Nonnull
  private final String key;

  public Image( @Nonnull final String name, @Nonnull final String category, @Nonnull final String key )
  {
    this.name = Objects.requireNonNull( name );
    this.category = Objects.requireNonNull( category );
    this.key = Objects.requireNonNull( key );
  }

  @Nonnull
  public String getName()
  {
    return name;
  }

  @Nonnull
  public String getCategory()
  {
    return category;
  }

  @Nonnull
  public String getKey()
  {
    return key;
  }
}
