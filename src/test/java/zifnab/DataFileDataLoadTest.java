package zifnab;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import org.testng.annotations.Test;
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
      Files.walk( Paths.get( "/Users/peter/Code/realityforge/endless-sky/data" ) )
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
}
