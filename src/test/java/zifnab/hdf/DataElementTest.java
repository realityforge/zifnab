package zifnab.hdf;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class DataElementTest
{
  @Test
  public void constructTopLevel()
  {
    final List<String> tokens = Arrays.asList( "planet", "Dune" );
    final DataElement element = new DataElement( null, tokens );

    assertNull( element.getParent() );
    assertEquals( element.getTokens(), tokens );
    assertTrue( element.getChildren().isEmpty() );
  }

  @Test
  public void constructNested()
  {
    final List<String> parentTokens = Arrays.asList( "planet", "AK5" );
    final DataElement parent = new DataElement( null, parentTokens );

    final List<String> childTokens = Arrays.asList( "name", "Akaron 5" );
    final DataElement child = new DataElement( parent, childTokens );

    assertNull( parent.getParent() );
    assertEquals( parent.getTokens(), parentTokens );
    assertEquals( parent.getChildren(), Collections.singletonList( child ) );

    assertEquals( child.getParent(), parent );
    assertEquals( child.getTokens(), childTokens );
    assertTrue( child.getChildren().isEmpty() );
  }
}
