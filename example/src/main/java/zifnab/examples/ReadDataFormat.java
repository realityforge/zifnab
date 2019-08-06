package zifnab.examples;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.annotation.Nonnull;
import zifnab.hdf.DataElement;
import zifnab.hdf.DataFile;
import zifnab.hdf.DataParseException;

/**
 * A simple example that parses files passed on the command line.
 */
public final class ReadDataFormat
{
  public static void main( @Nonnull final String[] args )
  {
    if ( 0 == args.length )
    {
      System.out.println( "Error: Please supply file names to parse" );
      System.exit( 1 );
    }
    else
    {
      for ( final String filename : args )
      {
        System.out.println( "Parsing " + filename + " ..." );
        try
        {
          final Path file = Paths.get( filename );
          final DataFile dataFile = DataFile.read( file );
          final long topLevelElements =
            dataFile.getDocument().getChildren().stream().filter( e -> e instanceof DataElement ).count();
          System.out.println( "Success - " + topLevelElements + " top-level elements found." );
        }
        catch ( final DataParseException e )
        {
          System.out.println( "Error: Failed to parse file - " + e );
          System.out.println( e.getLocation().toString() );
        }
        catch ( final IOException e )
        {
          System.out.println( "Error: Failed to read file - " + e );
        }
      }
    }
  }
}
