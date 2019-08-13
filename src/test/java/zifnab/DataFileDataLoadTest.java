package zifnab;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import zifnab.assets.Image;
import zifnab.assets.ImageRegistry;
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

    boolean failed = false;
    for ( final Path file : files )
    {
      System.out.println( "Processing: " + file );
      try
      {
        final DataFile dataFile = DataFile.read( file );
        for ( final DataElement element : dataFile.getDocument().getChildElements() )
        {
          if ( SystemConfig.matches( element ) )
          {
            final SystemConfig systemConfig = SystemConfig.from( element );
            System.out.println( "Loaded " + systemConfig.getName() + " system." );
          }
          else if ( TradeConfig.matches( element ) )
          {
            final TradeConfig tradeConfig = TradeConfig.from( element );
            System.out.println( "Loaded " + tradeConfig.getCommodities().size() + " commodities." );
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
