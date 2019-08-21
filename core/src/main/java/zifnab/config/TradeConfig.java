package zifnab.config;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import zifnab.hdf.DataDocument;
import zifnab.hdf.DataElement;

public final class TradeConfig
{
  @Nonnull
  private final Map<String, Commodity> commodities = new HashMap<>();

  @Nonnull
  public static TradeConfig from( @Nonnull final DataElement element )
  {
    return TradeConfigParser.from( element );
  }

  @Nonnull
  public static DataElement encode( @Nonnull final DataDocument document, @Nonnull final TradeConfig trade )
  {
    return TradeConfigEncoder.encode( document, trade );
  }

  public static boolean matches( @Nonnull final DataElement element )
  {
    return TradeConfigParser.matches( element );
  }

  @Nonnull
  public Collection<Commodity> getCommodities()
  {
    return Collections.unmodifiableCollection( commodities.values() );
  }

  @Nullable
  public Commodity findCommodityByName( @Nonnull final String name )
  {
    return commodities.get( name );
  }

  @Nonnull
  public Commodity addSpecialCommodity( @Nonnull final String name )
  {
    return addCommodity( new Commodity( name, null, null ) );
  }

  @Nonnull
  public Commodity addCommodity( @Nonnull final String name, @Nonnull final Integer low, @Nonnull final Integer high )
  {
    return addCommodity( new Commodity( name, Objects.requireNonNull( low ), Objects.requireNonNull( high ) ) );
  }

  @Nonnull
  private Commodity addCommodity( @Nonnull final Commodity commodity )
  {
    commodities.put( commodity.getName(), commodity );
    return commodity;
  }

  public boolean removeCommodity( @Nonnull final String name )
  {
    return null != commodities.remove( name );
  }

  public boolean removeCommodity( @Nonnull final Commodity commodity )
  {
    final Commodity existing = commodities.get( commodity.getName() );
    final boolean found = existing == commodity;
    if ( found )
    {
      commodities.remove( commodity.getName() );
    }
    return found;
  }

  public static final class Commodity
  {
    @Nonnull
    private final String name;
    @Nullable
    private Integer low;
    @Nullable
    private Integer high;
    @Nonnull
    private final Set<String> items = new HashSet<>();

    Commodity( @Nonnull final String name, @Nullable final Integer low, @Nullable final Integer high )
    {
      assert ( null == low && null == high ) || ( null != low && null != high );
      this.name = Objects.requireNonNull( name );
      this.low = low;
      this.high = high;
    }

    @Nonnull
    public String getName()
    {
      return name;
    }

    public boolean isSpecial()
    {
      return null == low;
    }

    @Nullable
    public Integer getLow()
    {
      return low;
    }

    public void setLow( @Nullable final Integer low )
    {
      this.low = low;
    }

    @Nullable
    public Integer getHigh()
    {
      return high;
    }

    public void setHigh( @Nullable final Integer high )
    {
      this.high = high;
    }

    @Nonnull
    public Set<String> getItems()
    {
      return Collections.unmodifiableSet( items );
    }

    public void addItem( @Nonnull final String item )
    {
      items.add( item );
    }

    public boolean removeItem( @Nonnull final String item )
    {
      return items.remove( item );
    }
  }
}
