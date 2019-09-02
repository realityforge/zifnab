package zifnab;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import zifnab.assets.Image;
import zifnab.assets.ImageRegistry;
import zifnab.config.EffectConfig;
import zifnab.config.GalaxyConfig;
import zifnab.config.OutfitConfig;
import zifnab.config.StarConfig;
import zifnab.config.SystemConfig;
import zifnab.config.TradeConfig;
import zifnab.hdf.DataElement;
import zifnab.hdf.DataFile;
import zifnab.hdf.DataParseException;
import static org.testng.Assert.*;

public class DataFileDataLoadTest
  extends AbstractTest
{
  @Test
  public void loadAllData()
    throws Exception
  {
    final List<Path> files =
      Files.walk( endlessSkyDir().resolve( "data" ) )
        .filter( Files::isRegularFile )
        .filter( file -> file.toString().endsWith( ".txt" ) )
        .collect( Collectors.toList() );

    final Set<String> attributeKeys = new HashSet<>();

    boolean failed = false;
    for ( final Path file : files )
    {
      System.out.println( "Processing: " + file );
      try
      {
        final DataFile dataFile = DataFile.read( file );
        for ( final DataElement element : dataFile.getDocument().getChildElements() )
        {
          if ( GalaxyConfig.matches( element ) )
          {
            final GalaxyConfig config = GalaxyConfig.from( element );
            System.out.println( "Loaded " + config.getName() + " galaxy." );
          }
          else if ( SystemConfig.matches( element ) )
          {
            final SystemConfig config = SystemConfig.from( element );
            System.out.println( "Loaded " + config.getName() + " system." );
          }
          else if ( TradeConfig.matches( element ) )
          {
            final TradeConfig config = TradeConfig.from( element );
            System.out.println( "Loaded " + config.getCommodities().size() + " commodities." );
          }
          else if ( OutfitConfig.matches( element ) )
          {
            final OutfitConfig config = OutfitConfig.from( element );
            System.out.println( "Loaded " + config.getName() + " outfit." );

            attributeKeys.addAll( config.getAttributes().keySet() );
          }
          else if ( StarConfig.matches( element ) )
          {
            final StarConfig config = StarConfig.from( element );
            System.out.println( "Loaded " + config.getName() + " star." );
          }
          else if ( EffectConfig.matches( element ) )
          {
            final EffectConfig config = EffectConfig.from( element );
            System.out.println( "Loaded " + config.getName() + " effect." );
          }
        }
      }
      catch ( final DataParseException e )
      {
        failed = true;
        System.out.println( e.getLocation().getFilename() + ":" + e.getLocation().getLineNumber() +
                            " : Error processing file: " + e.getMessage() );
      }
    }

    assertFalse( failed );

    for ( final String attributeKey : attributeKeys )
    {
      System.out.println( "Outfit Attribute Key = " + attributeKey );
    }
  }

  @Test
  public void populateRegistryFromDirectory()
    throws Exception
  {
    final ImageRegistry registry = new ImageRegistry();
    ImageRegistry.populateRegistryFromDirectory( registry, endlessSkyDir().resolve( "images" ) );
    for ( final String category : registry.getCategories() )
    {
      final Collection<Image> images = registry.findImagesByCategory( category );
      System.out.println( "Category: " + category + " (" + images.size() + " images)" );
      for ( final Image image : images )
      {
        System.out.println( "  " + image.getName() );
      }
    }
  }

  @Nonnull
  private Path endlessSkyDir()
  {
    final String endlessSkyHome = System.getProperty( "zifnab.endless_sky_dir" );
    assertNotNull( endlessSkyHome,
                   "Must specify zifnab.endless_sky_dir system property to point at Endless Sky directory" );
    return Paths.get( endlessSkyHome );
  }
}
