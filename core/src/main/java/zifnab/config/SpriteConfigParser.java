package zifnab.config;

import java.util.List;
import javax.annotation.Nonnull;
import zifnab.hdf.DataAccessException;
import zifnab.hdf.DataElement;

final class SpriteConfigParser
{
  private SpriteConfigParser()
  {
  }

  @Nonnull
  static SpriteConfig from( @Nonnull final DataElement element )
  {
    element.assertTokenCount( 2 );
    final String name = element.getStringAt( 1 );
    final SpriteConfig config = new SpriteConfig( name );
    parseConfig( config, element.getChildElements() );
    return config;
  }

  private static void parseConfig( @Nonnull final SpriteConfig config, @Nonnull final List<DataElement> elements )
  {
    for ( final DataElement element : elements )
    {
      final String name = element.getName();
      switch ( name )
      {
        case "frame rate":
          element.assertLeafNode();
          element.assertTokenCount( 2 );
          if ( 0D != config.getFrameTime() )
          {
            throw new DataAccessException( "Only one of 'frame time' or 'frame rate' may be specified",
                                           element.getLocation() );
          }
          config.setFrameRate( element.getDoubleAt( 1, 0D ) );
          break;
        case "frame time":
          element.assertLeafNode();
          element.assertTokenCount( 2 );
          if ( 0D != config.getFrameRate() )
          {
            throw new DataAccessException( "Only one of 'frame time' or 'frame rate' may be specified",
                                           element.getLocation() );
          }
          config.setFrameTime( element.getDoubleAt( 1, 0D ) );
          break;
        case "delay":
          element.assertLeafNode();
          element.assertTokenCount( 2 );
          config.setDelay( element.getDoubleAt( 1, 0D ) );
          break;
        case "start frame":
          element.assertLeafNode();
          element.assertTokenCount( 2 );
          config.setStartFrame( element.getDoubleAt( 1, 0D ) );
          break;
        case "random start frame":
          element.assertLeafNode();
          element.assertTokenCount( 1 );
          config.setRandomStartFrame( true );
          break;
        case "no repeat":
          element.assertLeafNode();
          element.assertTokenCount( 1 );
          config.setNoRepeat( true );
          break;
        case "rewind":
          element.assertLeafNode();
          element.assertTokenCount( 1 );
          config.setRewind( true );
          break;
        default:
          throw new DataAccessException( "Unexpected data element named '" + name + "'", element.getLocation() );
      }
    }
  }

}
