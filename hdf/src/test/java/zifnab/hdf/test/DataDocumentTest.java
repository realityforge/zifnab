package zifnab.hdf.test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import zifnab.hdf.DataComment;
import zifnab.hdf.DataDocument;
import zifnab.hdf.DataElement;
import zifnab.hdf.DataFile;
import zifnab.hdf.SourceLocation;
import static org.testng.Assert.*;

public class DataDocumentTest
  extends AbstractTest
{
  @Test
  public void construct()
  {
    final DataDocument document = new DataDocument();

    assertTrue( document.getChildren().isEmpty() );
    assertTrue( document.getChildElements().isEmpty() );
  }

  @Test
  public void append()
  {
    final DataDocument document = new DataDocument();

    assertTrue( document.getChildren().isEmpty() );

    final DataElement element1 = document.element( "planet", "Dune" );
    final DataComment comment1 = document.comment( "some random comment" );
    final DataElement element2 = document.element( "planet", "Mars" );

    assertEquals( document.getChildren(), Arrays.asList( element1, comment1, element2 ) );
    assertEquals( document.getChildElements(), Arrays.asList( element1, element2 ) );
  }

  @Test
  public void element()
  {
    final DataDocument document = new DataDocument();

    assertTrue( document.getChildren().isEmpty() );

    final SourceLocation location = new SourceLocation( "", 1, 0 );
    final DataElement element = document.element( location, "My header license comment" );

    assertEquals( element.getLocation(), location );
    assertEquals( document.getChildren(), Collections.singletonList( element ) );
    assertEquals( document.getChildElements(), Collections.singletonList( element ) );
  }

  @Test
  public void comment()
  {
    final DataDocument document = new DataDocument();

    assertTrue( document.getChildren().isEmpty() );

    final SourceLocation location = new SourceLocation( "", 1, 0 );
    final DataComment comment =
      document.comment( location, "My header license comment" );

    assertEquals( comment.getLocation(), location );
    assertEquals( document.getChildren(), Collections.singletonList( comment ) );
    assertEquals( document.getChildElements(), Collections.emptyList() );
  }

  @Test
  public void write()
    throws Exception
  {
    final DataDocument document = new DataDocument();

    assertTrue( document.getChildren().isEmpty() );

    document.element( "planet", "Dune" );
    document.comment( "The red planet" );
    document.element( "planet", "Mars" );

    final String output = writeDocument( document );
    assertEquals( output, "planet Dune\n" +
                          "\n" +
                          "# The red planet\n" +
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
