package zifnab.examples;

import java.io.IOException;
import java.nio.file.Paths;
import javax.annotation.Nonnull;
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
          DataFile.read( Paths.get( filename ) );
          System.out.println( "Success." );
        }
        catch ( IOException | DataParseException e )
        {
          System.out.println( "Error: " + e );
        }
      }
    }
    System.out.println( "args = " + args );
  }
}
