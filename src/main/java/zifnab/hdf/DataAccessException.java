package zifnab.hdf;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class DataAccessException
  extends RuntimeException
{
  @Nullable
  private final SourceLocation _location;

  public DataAccessException( @Nonnull final String message, @Nullable final SourceLocation location )
  {
    super( Objects.requireNonNull( message ) );
    _location = location;
  }

  @Nullable
  public SourceLocation getLocation()
  {
    return _location;
  }
}
