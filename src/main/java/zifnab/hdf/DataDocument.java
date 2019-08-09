package zifnab.hdf;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class DataDocument
{
  @Nullable
  private List<DataNode> _children;

  @Nonnull
  public List<DataNode> getChildren()
  {
    return null == _children ? Collections.emptyList() : Collections.unmodifiableList( _children );
  }

  @Nonnull
  public List<DataElement> getChildElements()
  {
    return null == _children ?
           Collections.emptyList() :
           Collections.unmodifiableList( _children.stream()
                                           .filter( e -> e instanceof DataElement )
                                           .map( e -> (DataElement) e )
                                           .collect( Collectors.toList() ) );
  }

  public void append( @Nonnull final DataNode child )
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
