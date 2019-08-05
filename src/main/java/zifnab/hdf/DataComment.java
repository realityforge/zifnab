package zifnab.hdf;

import java.io.IOException;
import java.io.Writer;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class DataComment
  extends DataNode
{
  @Nullable
  private final DataElement _parent;
  @Nonnull
  private final String _comment;

  public DataComment( @Nullable final DataElement parent, @Nonnull final String comment )
  {
    this( null, parent, comment );
  }

  public DataComment( @Nullable final SourceLocation location,
                      @Nullable final DataElement parent,
                      @Nonnull final String comment )
  {
    super( location );
    assert !comment.contains( "\n" );
    _parent = parent;
    _comment = Objects.requireNonNull( comment );
    if ( null != _parent )
    {
      _parent.append( this );
    }
  }

  @Nullable
  public DataElement getParent()
  {
    return _parent;
  }

  @Nonnull
  public String getComment()
  {
    return _comment;
  }

  @Override
  void write( @Nonnull final Writer writer, final int depth )
    throws IOException
  {
    writeIndent( writer, depth );
    writer.write( "# " );
    writer.write( _comment );
    writer.write( '\n' );
  }
}
