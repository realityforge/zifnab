package zifnab.hdf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class DataElement
{
  @Nullable
  private final DataElement _parent;
  @Nonnull
  private final List<String> _tokens;
  @Nullable
  private List<DataElement> _children;

  public DataElement( @Nullable final DataElement parent, @Nonnull final String... tokens )
  {
    this( parent, Arrays.asList( tokens ) );
  }

  public DataElement( @Nullable final DataElement parent, @Nonnull final List<String> tokens )
  {
    assert !tokens.isEmpty();
    assert tokens.stream().noneMatch( line -> line.contains( "\n" ) );
    _parent = parent;
    _tokens = new ArrayList<>( Objects.requireNonNull( tokens ) );
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
  public List<String> getTokens()
  {
    return Collections.unmodifiableList( _tokens );
  }

  @Nonnull
  public List<DataElement> getChildren()
  {
    return null == _children ? Collections.emptyList() : Collections.unmodifiableList( _children );
  }

  private void append( @Nonnull final DataElement child )
  {
    if ( null == _children )
    {
      _children = new ArrayList<>();
    }
    _children.add( Objects.requireNonNull( child ) );
  }
}
