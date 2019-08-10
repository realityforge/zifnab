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

  @Nonnull
  public DataElement element( @Nonnull final String... tokens )
  {
    return element( null, tokens );
  }

  @Nonnull
  public DataElement element( @Nullable final SourceLocation location, @Nonnull final String... tokens )
  {
    final DataElement element = new DataElement( location, null, tokens );
    append( element );
    return element;
  }

  @Nonnull
  public DataComment comment( @Nonnull final String comment )
  {
    return comment( null, comment );
  }

  @Nonnull
  public DataComment comment( @Nullable final SourceLocation location, @Nonnull final String comment )
  {
    final DataComment node = new DataComment( location, null, comment );
    append( node );
    return node;
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
