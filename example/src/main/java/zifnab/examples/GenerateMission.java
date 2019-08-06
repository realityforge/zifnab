package zifnab.examples;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Paths;
import javax.annotation.Nonnull;
import zifnab.hdf.DataComment;
import zifnab.hdf.DataDocument;
import zifnab.hdf.DataElement;
import zifnab.hdf.DataFile;

/**
 * Programatically generate a mission using the raw data API.
 */
public final class GenerateMission
{
  public static void main( @Nonnull final String[] args )
    throws IOException
  {
    final DataDocument document = new DataDocument();
    document.append( new DataComment( null, "The humanitarian mission!" ) );
    final DataElement element1 = new DataElement( null, "mission", "Drought Relief" );
    document.append( element1 );
    new DataComment( element1, "The name of the mission as presented to user" );
    new DataElement( element1, "name", "Drought relief to <planet>" );
    final DataElement offer = new DataElement( element1, "to", "offer" );
    new DataElement( offer, "random", "<", "10" );

    final DataFile dataFile = new DataFile( Paths.get( "ignored.txt" ), document );

    final StringWriter writer = new StringWriter();
    dataFile.writeTo( writer );

    System.out.println( writer.toString() );
  }
}
