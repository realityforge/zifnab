package zifnab.config;

import java.util.List;
import javax.annotation.Nonnull;
import zifnab.hdf.DataAccessException;
import zifnab.hdf.DataElement;

final class EffectConfigParser
{
  private EffectConfigParser()
  {
  }

  @Nonnull
  static EffectConfig from( @Nonnull final DataElement element )
  {
    element.assertTokenCount( 2 );
    element.assertTokenName( "effect" );
    final EffectConfig config = new EffectConfig( element.getStringAt( 1 ) );
    parseConfig( config, element.getChildElements() );
    return config;
  }

  static boolean matches( @Nonnull final DataElement element )
  {
    return "effect".equals( element.getName() );
  }

  private static void parseConfig( @Nonnull final EffectConfig config, @Nonnull final List<DataElement> elements )
  {
    for ( final DataElement element : elements )
    {
      final String name = element.getName();
      switch ( name )
      {
        case "sprite":
          config.setSprite( SpriteConfig.from( element ) );
          break;
        case "sound":
          element.assertLeafNode();
          element.assertTokenCount( 2 );
          config.setSound( element.getStringAt( 1 ) );
          break;
        case "lifetime":
          element.assertLeafNode();
          element.assertTokenCount( 2 );
          config.setLifetime( element.getIntAt( 1 ) );
          break;
        case "velocity scale":
          element.assertLeafNode();
          element.assertTokenCount( 2 );
          config.setVelocityScale( element.getDoubleAt( 1 ) );
          break;
        case "random velocity":
          element.assertLeafNode();
          element.assertTokenCount( 2 );
          config.setRandomVelocity( element.getDoubleAt( 1 ) );
          break;
        case "random angle":
          element.assertLeafNode();
          element.assertTokenCount( 2 );
          config.setRandomAngle( element.getDoubleAt( 1 ) );
          break;
        case "random spin":
          element.assertLeafNode();
          element.assertTokenCount( 2 );
          config.setRandomSpin( element.getDoubleAt( 1 ) );
          break;
        case "random frame rate":
          element.assertLeafNode();
          element.assertTokenCount( 2 );
          config.setRandomFrameRate( element.getDoubleAt( 1 ) );
          break;
        default:
          throw new DataAccessException( "Unexpected data element named '" + name + "'", element.getLocation() );
      }
    }
  }
}
