package zifnab.hdf;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DataDocument
{
  @Nullable
  private List<DataNode> _children;

  @Nonnull
  public List<DataNode> getChildren()
  {
    return null == _children ? Collections.emptyList() : Collections.unmodifiableList( _children );
  }

  public void append( @Nonnull final DataElement child )
  {
    if ( null == _children )
    {
      _children = new ArrayList<>();
    }
    _children.add( Objects.requireNonNull( child ) );
  }

  void write( @Nonnull final Writer writer )
    throws IOException
  {
    boolean lastChildWasAnElement = false;
    for ( final DataNode child : getChildren() )
    {
      if ( lastChildWasAnElement )
      {
        writer.write( "\n" );
      }
      child.write( writer, 0 );
      lastChildWasAnElement = child instanceof DataElement;
    }
  }
}
