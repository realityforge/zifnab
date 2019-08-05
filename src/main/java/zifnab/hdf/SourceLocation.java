package zifnab.hdf;

import java.util.Objects;
import javax.annotation.Nonnull;

public final class SourceLocation
{
  public static final int UNSPECIFIED = -1;
  @Nonnull
  private final String _filename;
  private final int _lineNumber;
  private final int _columnNumber;

  public SourceLocation( @Nonnull final String filename, final int lineNumber, final int columnNumber )
  {
    _filename = Objects.requireNonNull( filename );
    _lineNumber = lineNumber;
    _columnNumber = columnNumber;
  }

  @Nonnull
  public String getFilename()
  {
    return _filename;
  }

  public int getLineNumber()
  {
    return _lineNumber;
  }

  public int getColumnNumber()
  {
    return _columnNumber;
  }

  @Override
  public String toString()
  {
    return _filename +
           ( UNSPECIFIED == _lineNumber ? "" :
             ":" + _lineNumber + ( UNSPECIFIED == _columnNumber ? "" : ":" + _columnNumber ) );
  }
}
