package zifnab.hdf;

import java.io.IOException;
import java.io.Writer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class DataNode
{
  @Nullable
  private final SourceLocation _location;

  protected DataNode( @Nullable final SourceLocation location )
  {
    _location = location;
  }

  @Nullable
  public SourceLocation getLocation()
  {
    return _location;
  }

  abstract void write( @Nonnull Writer writer, int depth )
    throws IOException;

  final void writeIndent( @Nonnull final Writer writer, final int depth )
    throws IOException
  {
    for ( int i = 0; i < depth; i++ )
    {
      writer.write( "\t" );
    }
  }
}
