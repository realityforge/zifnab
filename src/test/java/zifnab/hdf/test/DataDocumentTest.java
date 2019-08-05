package zifnab.hdf.test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import zifnab.AbstractTest;
import zifnab.hdf.DataDocument;
import zifnab.hdf.DataElement;
import zifnab.hdf.DataFile;
import static org.testng.Assert.*;

public class DataDocumentTest
  extends AbstractTest
{
  @Test
  public void construct()
  {
    final DataDocument document = new DataDocument();

    assertTrue( document.getChildren().isEmpty() );
  }

  @Test
  public void append()
  {
    final DataDocument document = new DataDocument();

    assertTrue( document.getChildren().isEmpty() );

    final DataElement element1 = new DataElement( null, "planet", "Dune" );
    final DataElement element2 = new DataElement( null, "planet", "Mars" );
    document.append( element1 );
    document.append( element2 );

    assertEquals( document.getChildren(), Arrays.asList( element1, element2 ) );
  }

  @Test
  public void write()
    throws Exception
  {
    final DataDocument document = new DataDocument();

    assertTrue( document.getChildren().isEmpty() );

    final DataElement element1 = new DataElement( null, "planet", "Dune" );
    final DataElement element2 = new DataElement( null, "planet", "Mars" );
    document.append( element1 );
    document.append( element2 );

    final String output = writeDocument( document );
    assertEquals( output, "planet Dune\n" +
                          "\n" +
                          "planet Mars\n" );
  }

  @Nonnull
  private String writeDocument( @Nonnull final DataDocument document )
    throws IOException
  {
    final Path file = createTempDataFile();
    new DataFile( file, document ).write();
    return readContent( file );
  }
}
