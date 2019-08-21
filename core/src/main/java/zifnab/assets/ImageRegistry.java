package zifnab.assets;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class ImageRegistry
{
  /**
   * Pattern that matches images that are part of animation.
   */
  private static final Pattern IMAGE_SET_PATTERN = Pattern.compile( "^(.*)([\\-~+=]\\d+)$" );
  // category => (name => Asset)
  @Nonnull
  private final Map<String, Map<String, Image>> imageMap = new HashMap<>();
  @Nonnull
  private final Map<String, Image> imagesByKey = new HashMap<>();

  public static void populateRegistryFromDirectory( @Nonnull final ImageRegistry registry, @Nonnull final Path path )
    throws IOException
  {
    for ( final Path file : getFilesInDirectory( path ) )
    {
      final String localFilename = path.relativize( file ).toString();
      final int dotIndex = localFilename.lastIndexOf( "." );
      if ( -1 == dotIndex )
      {
        continue;
      }
      final String extension = localFilename.substring( dotIndex + 1 ).toLowerCase();
      if ( !"png".equals( extension ) && !"jpg".equals( extension ) )
      {
        continue;
      }
      final String candidateKey = localFilename.substring( 0, dotIndex );
      final Matcher matcher = IMAGE_SET_PATTERN.matcher( candidateKey );
      final String key = matcher.matches() ? matcher.toMatchResult().group( 1 ) : candidateKey;
      if ( null == registry.findImageByKey( key ) )
      {
        final int separatorIndex = key.lastIndexOf( File.separator );
        final String name = -1 == separatorIndex ? key : key.substring( separatorIndex + 1 );
        final String category =
          key.startsWith( "planet/station" ) ?
          "station" :
          -1 == separatorIndex ? "" : key.substring( 0, separatorIndex );
        registry.addImage( category, name, key );
      }
    }
  }

  @Nonnull
  private static List<Path> getFilesInDirectory( @Nonnull final Path directory )
    throws IOException
  {
    return Files.walk( directory )
      .filter( Files::isRegularFile )
      .collect( Collectors.toList() );
  }

  @Nonnull
  public Set<String> getCategories()
  {
    return Collections.unmodifiableSet( imageMap.keySet() );
  }

  @Nonnull
  public Collection<Image> findImagesByCategory( @Nonnull final String category )
  {
    final Map<String, Image> categoryMap = imageMap.get( category );
    return null == categoryMap ? Collections.emptyList() : Collections.unmodifiableCollection( categoryMap.values() );
  }

  @Nullable
  public Image findImage( @Nonnull final String category, @Nonnull final String name )
  {
    final Map<String, Image> categoryMap = imageMap.get( category );
    return null == categoryMap ? null : categoryMap.get( name );
  }

  @Nullable
  public Image findImageByKey( @Nonnull final String key )
  {
    return imagesByKey.get( key );
  }

  @Nonnull
  public Collection<Image> getImages()
  {
    return Collections.unmodifiableCollection( imagesByKey.values() );
  }

  @Nonnull
  public Image addImage( @Nonnull final String category,
                         @Nonnull final String name,
                         @Nonnull final String key )
  {
    final Image image = new Image( name, category, key );
    final Map<String, Image> categoryMap = imageMap.computeIfAbsent( category, c -> new HashMap<>() );
    if ( categoryMap.containsKey( name ) )
    {
      throw new IllegalStateException( "Asset with key '" + key + "' already registered." );
    }
    categoryMap.put( name, image );
    imagesByKey.put( key, image );
    return image;
  }

  public boolean removeImage( @Nonnull final Image image )
  {
    final String category = image.getCategory();
    final Map<String, Image> categoryMap = imageMap.get( category );
    if ( null == categoryMap )
    {
      return false;
    }
    else
    {
      final String name = image.getName();
      final Image existing = categoryMap.get( name );
      if ( existing == image )
      {
        categoryMap.remove( name );
        imagesByKey.remove( image.getKey() );
        if ( categoryMap.isEmpty() )
        {
          imageMap.remove( category );
        }
        return true;
      }
      else
      {
        return false;
      }
    }
  }
}
