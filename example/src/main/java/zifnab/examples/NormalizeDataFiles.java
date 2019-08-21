package zifnab.examples;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import zifnab.hdf.DataFile;
import zifnab.hdf.DataParseException;

/**
 * A simple tool that reads all files from specified data directory and saves them again, thus normalizing them.
 */
public final class NormalizeDataFiles
{
  public static void main( @Nonnull final String[] args )
  {
    if ( 1 != args.length )
    {
      System.out.println( "Error: Please supply a single argument specifying the data directory to normalize" );
      System.exit( 1 );
    }
    else
    {
      for ( final Path file : collectDataFiles( args[ 0 ] ) )
      {
        normalizeFile( file );
      }
    }
  }

  @Nonnull
  private static List<Path> collectDataFiles( @Nonnull final String dirName )
  {
    try
    {
      return Files.walk( Paths.get( dirName ) )
        .filter( Files::isRegularFile )
        .filter( file -> file.toString().endsWith( ".txt" ) )
        .collect( Collectors.toList() );
    }
    catch ( final IOException e )
    {
      System.out.println( "Error: Failed to scan directory " + dirName + " - " + e );
      System.exit( 1 );
      throw new Error();
    }
  }

  private static void normalizeFile( final Path file )
  {
    System.out.println( "Normalizing " + file + " ..." );
    boolean parseComplete = false;
    try
    {
      final DataFile dataFile = DataFile.read( file );
      parseComplete = true;
      dataFile.write();
    }
    catch ( final DataParseException e )
    {
      System.out.println( "Error: Failed to parse file - " + e );
      System.out.println( e.getLocation().toString() );
    }
    catch ( final IOException e )
    {
      System.out.println( "Error: Failed to " + ( parseComplete ? "write" : "read" ) + " file - " + e );
    }
  }
}
