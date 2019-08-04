package zifnab.hdf;

import java.io.IOException;
import java.io.Writer;
import javax.annotation.Nonnull;

public abstract class DataNode
{
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
