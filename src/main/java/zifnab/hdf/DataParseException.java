package zifnab.hdf;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class DataParseException
  extends Exception
{
  @Nonnull
  private final SourceLocation _location;

  public DataParseException( @Nonnull final String message, @Nonnull final SourceLocation location )
  {
    super( Objects.requireNonNull( message ) );
    _location = Objects.requireNonNull( location );
  }

  public DataParseException( @Nonnull final String message,
                             @Nullable final Throwable cause,
                             @Nonnull final SourceLocation location )
  {
    super( Objects.requireNonNull( message ), cause );
    _location = Objects.requireNonNull( location );
  }

  @Nonnull
  public SourceLocation getLocation()
  {
    return _location;
  }
}
