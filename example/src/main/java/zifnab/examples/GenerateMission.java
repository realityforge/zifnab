package zifnab.examples;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Paths;
import javax.annotation.Nonnull;
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
    document.comment( "The humanitarian mission!" );
    final DataElement element1 = document.element( "mission", "Drought Relief" );
    element1.comment( "The name of the mission as presented to user" );
    element1.element( "name", "Drought relief to <planet>" );
    final DataElement offer = element1.element( "to", "offer" );
    offer.element( "random", "<", "10" );

    final DataFile dataFile = new DataFile( Paths.get( "ignored.txt" ), document );

    final StringWriter writer = new StringWriter();
    dataFile.writeTo( writer );

    System.out.println( writer.toString() );
  }
}
