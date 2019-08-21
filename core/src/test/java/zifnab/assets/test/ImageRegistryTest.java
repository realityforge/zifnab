package zifnab.assets.test;

import gir.io.FileUtil;
import java.nio.file.Files;
import java.nio.file.Path;
import org.testng.annotations.Test;
import zifnab.AbstractTest;
import zifnab.assets.Image;
import zifnab.assets.ImageRegistry;
import static org.testng.Assert.*;

public class ImageRegistryTest
  extends AbstractTest
{
  @Test
  public void basicOperation()
  {
    final ImageRegistry registry = new ImageRegistry();

    assertEquals( registry.getImages().size(), 0 );
    assertEquals( registry.getCategories().size(), 0 );
    assertFalse( registry.getCategories().contains( "outfit" ) );
    assertFalse( registry.getCategories().contains( "planet" ) );
    assertEquals( registry.findImagesByCategory( "outfit" ).size(), 0 );
    assertEquals( registry.findImagesByCategory( "planet" ).size(), 0 );
    assertNull( registry.findImageByKey( "outfit/fast button" ) );
    assertNull( registry.findImageByKey( "outfit/racing stripes" ) );
    assertNull( registry.findImageByKey( "planet/red1" ) );

    final Image image1 = registry.addImage( "outfit", "fast button", "outfit/fast button" );
    assertEquals( image1.getName(), "fast button" );
    assertEquals( image1.getCategory(), "outfit" );
    assertEquals( image1.getKey(), "outfit/fast button" );

    assertEquals( registry.getImages().size(), 1 );
    assertTrue( registry.getImages().contains( image1 ) );
    assertEquals( registry.getCategories().size(), 1 );
    assertTrue( registry.getCategories().contains( "outfit" ) );
    assertFalse( registry.getCategories().contains( "planet" ) );
    assertEquals( registry.findImagesByCategory( "outfit" ).size(), 1 );
    assertEquals( registry.findImagesByCategory( "planet" ).size(), 0 );
    assertEquals( registry.findImageByKey( "outfit/fast button" ), image1 );
    assertEquals( registry.findImage( "outfit", "fast button" ), image1 );
    assertNull( registry.findImageByKey( "outfit/racing stripes" ) );
    assertNull( registry.findImageByKey( "planet/red1" ) );

    final Image image2 = registry.addImage( "outfit", "racing stripes", "outfit/racing stripes" );
    assertEquals( image2.getName(), "racing stripes" );
    assertEquals( image2.getCategory(), "outfit" );
    assertEquals( image2.getKey(), "outfit/racing stripes" );

    assertEquals( registry.getImages().size(), 2 );
    assertTrue( registry.getImages().contains( image1 ) );
    assertTrue( registry.getImages().contains( image2 ) );
    assertEquals( registry.getCategories().size(), 1 );
    assertTrue( registry.getCategories().contains( "outfit" ) );
    assertFalse( registry.getCategories().contains( "planet" ) );
    assertEquals( registry.findImagesByCategory( "outfit" ).size(), 2 );
    assertEquals( registry.findImagesByCategory( "planet" ).size(), 0 );
    assertEquals( registry.findImageByKey( "outfit/fast button" ), image1 );
    assertEquals( registry.findImage( "outfit", "fast button" ), image1 );
    assertEquals( registry.findImageByKey( "outfit/racing stripes" ), image2 );
    assertEquals( registry.findImage( "outfit", "racing stripes" ), image2 );
    assertNull( registry.findImageByKey( "planet/red1" ) );
    assertNull( registry.findImage( "planet", "red1" ) );

    final Image image3 = registry.addImage( "planet", "red1", "planet/red1" );
    assertEquals( image3.getName(), "red1" );
    assertEquals( image3.getCategory(), "planet" );
    assertEquals( image3.getKey(), "planet/red1" );

    assertEquals( registry.getImages().size(), 3 );
    assertTrue( registry.getImages().contains( image1 ) );
    assertTrue( registry.getImages().contains( image2 ) );
    assertTrue( registry.getImages().contains( image3 ) );
    assertEquals( registry.getCategories().size(), 2 );
    assertTrue( registry.getCategories().contains( "outfit" ) );
    assertTrue( registry.getCategories().contains( "planet" ) );
    assertEquals( registry.findImagesByCategory( "outfit" ).size(), 2 );
    assertEquals( registry.findImagesByCategory( "planet" ).size(), 1 );
    assertEquals( registry.findImageByKey( "outfit/fast button" ), image1 );
    assertEquals( registry.findImage( "outfit", "fast button" ), image1 );
    assertEquals( registry.findImageByKey( "outfit/racing stripes" ), image2 );
    assertEquals( registry.findImage( "outfit", "racing stripes" ), image2 );
    assertEquals( registry.findImageByKey( "planet/red1" ), image3 );
    assertEquals( registry.findImage( "planet", "red1" ), image3 );

    assertTrue( registry.removeImage( image3 ) );

    assertEquals( registry.getImages().size(), 2 );
    assertTrue( registry.getImages().contains( image1 ) );
    assertTrue( registry.getImages().contains( image2 ) );
    assertFalse( registry.getImages().contains( image3 ) );
    assertEquals( registry.getCategories().size(), 1 );
    assertTrue( registry.getCategories().contains( "outfit" ) );
    assertFalse( registry.getCategories().contains( "planet" ) );
    assertEquals( registry.findImagesByCategory( "outfit" ).size(), 2 );
    assertEquals( registry.findImagesByCategory( "planet" ).size(), 0 );
    assertEquals( registry.findImageByKey( "outfit/fast button" ), image1 );
    assertEquals( registry.findImage( "outfit", "fast button" ), image1 );
    assertEquals( registry.findImageByKey( "outfit/racing stripes" ), image2 );
    assertEquals( registry.findImage( "outfit", "racing stripes" ), image2 );
    assertNull( registry.findImageByKey( "planet/red1" ) );
    assertNull( registry.findImage( "planet", "red1" ) );

    // Can not delete twice
    assertFalse( registry.removeImage( image3 ) );

    assertEquals( registry.getImages().size(), 2 );
    assertTrue( registry.getImages().contains( image1 ) );
    assertTrue( registry.getImages().contains( image2 ) );
    assertFalse( registry.getImages().contains( image3 ) );
    assertEquals( registry.getCategories().size(), 1 );
    assertTrue( registry.getCategories().contains( "outfit" ) );
    assertFalse( registry.getCategories().contains( "planet" ) );
    assertEquals( registry.findImagesByCategory( "outfit" ).size(), 2 );
    assertEquals( registry.findImagesByCategory( "planet" ).size(), 0 );
    assertEquals( registry.findImageByKey( "outfit/fast button" ), image1 );
    assertEquals( registry.findImage( "outfit", "fast button" ), image1 );
    assertEquals( registry.findImageByKey( "outfit/racing stripes" ), image2 );
    assertEquals( registry.findImage( "outfit", "racing stripes" ), image2 );
    assertNull( registry.findImageByKey( "planet/red1" ) );
    assertNull( registry.findImage( "planet", "red1" ) );

    assertTrue( registry.removeImage( image2 ) );

    assertEquals( registry.getImages().size(), 1 );
    assertTrue( registry.getImages().contains( image1 ) );
    assertFalse( registry.getImages().contains( image2 ) );
    assertFalse( registry.getImages().contains( image3 ) );
    assertEquals( registry.getCategories().size(), 1 );
    assertTrue( registry.getCategories().contains( "outfit" ) );
    assertFalse( registry.getCategories().contains( "planet" ) );
    assertEquals( registry.findImagesByCategory( "outfit" ).size(), 1 );
    assertEquals( registry.findImagesByCategory( "planet" ).size(), 0 );
    assertEquals( registry.findImageByKey( "outfit/fast button" ), image1 );
    assertEquals( registry.findImage( "outfit", "fast button" ), image1 );
    assertNull( registry.findImageByKey( "outfit/racing stripes" ) );
    assertNull( registry.findImage( "outfit", "racing stripes" ) );
    assertNull( registry.findImageByKey( "planet/red1" ) );
    assertNull( registry.findImage( "planet", "red1" ) );

    assertFalse( registry.removeImage( image2 ) );

    assertEquals( registry.getImages().size(), 1 );
    assertTrue( registry.getImages().contains( image1 ) );
    assertFalse( registry.getImages().contains( image2 ) );
    assertFalse( registry.getImages().contains( image3 ) );
    assertEquals( registry.getCategories().size(), 1 );
    assertTrue( registry.getCategories().contains( "outfit" ) );
    assertFalse( registry.getCategories().contains( "planet" ) );
    assertEquals( registry.findImagesByCategory( "outfit" ).size(), 1 );
    assertEquals( registry.findImagesByCategory( "planet" ).size(), 0 );
    assertEquals( registry.findImageByKey( "outfit/fast button" ), image1 );
    assertEquals( registry.findImage( "outfit", "fast button" ), image1 );
    assertNull( registry.findImageByKey( "outfit/racing stripes" ) );
    assertNull( registry.findImage( "outfit", "racing stripes" ) );
    assertNull( registry.findImageByKey( "planet/red1" ) );
    assertNull( registry.findImage( "planet", "red1" ) );

    assertTrue( registry.removeImage( image1 ) );

    assertEquals( registry.getImages().size(), 0 );
    assertFalse( registry.getImages().contains( image1 ) );
    assertFalse( registry.getImages().contains( image2 ) );
    assertFalse( registry.getImages().contains( image3 ) );
    assertEquals( registry.getCategories().size(), 0 );
    assertFalse( registry.getCategories().contains( "outfit" ) );
    assertFalse( registry.getCategories().contains( "planet" ) );
    assertEquals( registry.findImagesByCategory( "outfit" ).size(), 0 );
    assertEquals( registry.findImagesByCategory( "planet" ).size(), 0 );
    assertNull( registry.findImageByKey( "outfit/fast button" ) );
    assertNull( registry.findImage( "outfit", "fast button" ) );
    assertNull( registry.findImageByKey( "outfit/racing stripes" ) );
    assertNull( registry.findImage( "outfit", "racing stripes" ) );
    assertNull( registry.findImageByKey( "planet/red1" ) );
    assertNull( registry.findImage( "planet", "red1" ) );
  }

  @Test
  public void addImage_ErrorsOnDuplicates()
  {
    final ImageRegistry registry = new ImageRegistry();

    assertEquals( registry.getCategories().size(), 0 );

    registry.addImage( "outfit", "fast button", "outfit/fast button" );

    assertEquals( registry.getCategories().size(), 1 );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> registry.addImage( "outfit", "fast button", "outfit/fast button" ) );
    assertEquals( exception.getMessage(), "Asset with key 'outfit/fast button' already registered." );
  }

  @Test
  public void populateRegistryFromDirectory_emptyDirectory()
    throws Exception
  {
    final Path dir = FileUtil.createLocalTempDir();

    final ImageRegistry registry = new ImageRegistry();

    assertEquals( registry.getImages().size(), 0 );

    ImageRegistry.populateRegistryFromDirectory( registry, dir );

    assertEquals( registry.getImages().size(), 0 );
  }

  @Test
  public void populateRegistryFromDirectory_singleJpg()
    throws Exception
  {
    final Path dir = FileUtil.createLocalTempDir();

    Files.createDirectory( dir.resolve( "outfits" ) );
    FileUtil.write( dir.resolve( "outfits" ).resolve( "buckle.jpg" ), new byte[ 0 ] );

    final ImageRegistry registry = new ImageRegistry();

    assertEquals( registry.getImages().size(), 0 );

    ImageRegistry.populateRegistryFromDirectory( registry, dir );

    assertEquals( registry.getImages().size(), 1 );
    final Image image = registry.findImage( "outfits", "buckle" );
    assertNotNull( image );
    assertEquals( image.getCategory(), "outfits" );
    assertEquals( image.getName(), "buckle" );
    assertEquals( image.getKey(), "outfits/buckle" );
  }

  @Test
  public void populateRegistryFromDirectory_station()
    throws Exception
  {
    final Path dir = FileUtil.createLocalTempDir();

    Files.createDirectory( dir.resolve( "planet" ) );
    FileUtil.write( dir.resolve( "planet" ).resolve( "stationBeta.jpg" ), new byte[ 0 ] );
    FileUtil.write( dir.resolve( "planet" ).resolve( "red.jpg" ), new byte[ 0 ] );

    final ImageRegistry registry = new ImageRegistry();

    assertEquals( registry.getImages().size(), 0 );

    ImageRegistry.populateRegistryFromDirectory( registry, dir );

    assertEquals( registry.getImages().size(), 2 );
    final Image image1 = registry.findImage( "planet", "red" );
    assertNotNull( image1 );
    assertEquals( image1.getCategory(), "planet" );
    assertEquals( image1.getName(), "red" );
    assertEquals( image1.getKey(), "planet/red" );

    final Image image2 = registry.findImage( "station", "stationBeta" );
    assertNotNull( image2 );
    assertEquals( image2.getCategory(), "station" );
    assertEquals( image2.getName(), "stationBeta" );
    assertEquals( image2.getKey(), "planet/stationBeta" );
  }

  @Test
  public void populateRegistryFromDirectory_singlePng()
    throws Exception
  {
    final Path dir = FileUtil.createLocalTempDir();

    Files.createDirectory( dir.resolve( "outfits" ) );
    FileUtil.write( dir.resolve( "outfits" ).resolve( "buckle.png" ), new byte[ 0 ] );

    final ImageRegistry registry = new ImageRegistry();

    assertEquals( registry.getImages().size(), 0 );

    ImageRegistry.populateRegistryFromDirectory( registry, dir );

    assertEquals( registry.getImages().size(), 1 );
    final Image image = registry.findImage( "outfits", "buckle" );
    assertNotNull( image );
    assertEquals( image.getCategory(), "outfits" );
    assertEquals( image.getName(), "buckle" );
    assertEquals( image.getKey(), "outfits/buckle" );
  }

  @Test
  public void populateRegistryFromDirectory_nestedCategory()
    throws Exception
  {
    final Path dir = FileUtil.createLocalTempDir();

    Files.createDirectory( dir.resolve( "asteroid" ) );
    Files.createDirectory( dir.resolve( "asteroid" ).resolve( "gold" ) );
    FileUtil.write( dir.resolve( "asteroid" ).resolve( "gold" ).resolve( "spin.png" ), new byte[ 0 ] );

    final ImageRegistry registry = new ImageRegistry();

    assertEquals( registry.getImages().size(), 0 );

    ImageRegistry.populateRegistryFromDirectory( registry, dir );

    assertEquals( registry.getImages().size(), 1 );
    final Image image = registry.findImage( "asteroid/gold", "spin" );
    assertNotNull( image );
    assertEquals( image.getCategory(), "asteroid/gold" );
    assertEquals( image.getName(), "spin" );
    assertEquals( image.getKey(), "asteroid/gold/spin" );
  }

  @Test
  public void populateRegistryFromDirectory_animations_additiveBlend()
    throws Exception
  {
    final Path dir = FileUtil.createLocalTempDir();

    Files.createDirectory( dir.resolve( "asteroid" ) );
    Files.createDirectory( dir.resolve( "asteroid" ).resolve( "gold" ) );
    FileUtil.write( dir.resolve( "asteroid" ).resolve( "gold" ).resolve( "spin+1.png" ), new byte[ 0 ] );
    FileUtil.write( dir.resolve( "asteroid" ).resolve( "gold" ).resolve( "spin+2.png" ), new byte[ 0 ] );
    FileUtil.write( dir.resolve( "asteroid" ).resolve( "gold" ).resolve( "spin+3.png" ), new byte[ 0 ] );
    FileUtil.write( dir.resolve( "asteroid" ).resolve( "gold" ).resolve( "spin+4.png" ), new byte[ 0 ] );
    FileUtil.write( dir.resolve( "asteroid" ).resolve( "gold" ).resolve( "spin+5.png" ), new byte[ 0 ] );

    final ImageRegistry registry = new ImageRegistry();

    assertEquals( registry.getImages().size(), 0 );

    ImageRegistry.populateRegistryFromDirectory( registry, dir );

    assertEquals( registry.getImages().size(), 1 );
    final Image image = registry.findImage( "asteroid/gold", "spin" );
    assertNotNull( image );
    assertEquals( image.getCategory(), "asteroid/gold" );
    assertEquals( image.getName(), "spin" );
    assertEquals( image.getKey(), "asteroid/gold/spin" );
  }

  @Test
  public void populateRegistryFromDirectory_animations_halfAdditiveBlend()
    throws Exception
  {
    final Path dir = FileUtil.createLocalTempDir();

    Files.createDirectory( dir.resolve( "asteroid" ) );
    Files.createDirectory( dir.resolve( "asteroid" ).resolve( "gold" ) );
    FileUtil.write( dir.resolve( "asteroid" ).resolve( "gold" ).resolve( "spin~1.png" ), new byte[ 0 ] );
    FileUtil.write( dir.resolve( "asteroid" ).resolve( "gold" ).resolve( "spin~2.png" ), new byte[ 0 ] );
    FileUtil.write( dir.resolve( "asteroid" ).resolve( "gold" ).resolve( "spin~3.png" ), new byte[ 0 ] );
    FileUtil.write( dir.resolve( "asteroid" ).resolve( "gold" ).resolve( "spin~4.png" ), new byte[ 0 ] );
    FileUtil.write( dir.resolve( "asteroid" ).resolve( "gold" ).resolve( "spin~5.png" ), new byte[ 0 ] );

    final ImageRegistry registry = new ImageRegistry();

    assertEquals( registry.getImages().size(), 0 );

    ImageRegistry.populateRegistryFromDirectory( registry, dir );

    assertEquals( registry.getImages().size(), 1 );
    final Image image = registry.findImage( "asteroid/gold", "spin" );
    assertNotNull( image );
    assertEquals( image.getCategory(), "asteroid/gold" );
    assertEquals( image.getName(), "spin" );
    assertEquals( image.getKey(), "asteroid/gold/spin" );
  }

  @Test
  public void populateRegistryFromDirectory_animations_alphaBlend()
    throws Exception
  {
    final Path dir = FileUtil.createLocalTempDir();

    Files.createDirectory( dir.resolve( "asteroid" ) );
    Files.createDirectory( dir.resolve( "asteroid" ).resolve( "gold" ) );
    FileUtil.write( dir.resolve( "asteroid" ).resolve( "gold" ).resolve( "spin-1.png" ), new byte[ 0 ] );
    FileUtil.write( dir.resolve( "asteroid" ).resolve( "gold" ).resolve( "spin-2.png" ), new byte[ 0 ] );
    FileUtil.write( dir.resolve( "asteroid" ).resolve( "gold" ).resolve( "spin-3.png" ), new byte[ 0 ] );
    FileUtil.write( dir.resolve( "asteroid" ).resolve( "gold" ).resolve( "spin-4.png" ), new byte[ 0 ] );
    FileUtil.write( dir.resolve( "asteroid" ).resolve( "gold" ).resolve( "spin-5.png" ), new byte[ 0 ] );

    final ImageRegistry registry = new ImageRegistry();

    assertEquals( registry.getImages().size(), 0 );

    ImageRegistry.populateRegistryFromDirectory( registry, dir );

    assertEquals( registry.getImages().size(), 1 );
    final Image image = registry.findImage( "asteroid/gold", "spin" );
    assertNotNull( image );
    assertEquals( image.getCategory(), "asteroid/gold" );
    assertEquals( image.getName(), "spin" );
    assertEquals( image.getKey(), "asteroid/gold/spin" );
  }

  @Test
  public void populateRegistryFromDirectory_animations_preMultBlend()
    throws Exception
  {
    final Path dir = FileUtil.createLocalTempDir();

    Files.createDirectory( dir.resolve( "asteroid" ) );
    Files.createDirectory( dir.resolve( "asteroid" ).resolve( "gold" ) );
    FileUtil.write( dir.resolve( "asteroid" ).resolve( "gold" ).resolve( "spin=1.png" ), new byte[ 0 ] );
    FileUtil.write( dir.resolve( "asteroid" ).resolve( "gold" ).resolve( "spin=2.png" ), new byte[ 0 ] );
    FileUtil.write( dir.resolve( "asteroid" ).resolve( "gold" ).resolve( "spin=3.png" ), new byte[ 0 ] );
    FileUtil.write( dir.resolve( "asteroid" ).resolve( "gold" ).resolve( "spin=4.png" ), new byte[ 0 ] );
    FileUtil.write( dir.resolve( "asteroid" ).resolve( "gold" ).resolve( "spin=5.png" ), new byte[ 0 ] );

    final ImageRegistry registry = new ImageRegistry();

    assertEquals( registry.getImages().size(), 0 );

    ImageRegistry.populateRegistryFromDirectory( registry, dir );

    assertEquals( registry.getImages().size(), 1 );
    final Image image = registry.findImage( "asteroid/gold", "spin" );
    assertNotNull( image );
    assertEquals( image.getCategory(), "asteroid/gold" );
    assertEquals( image.getName(), "spin" );
    assertEquals( image.getKey(), "asteroid/gold/spin" );
  }

  @Test
  public void populateRegistryFromDirectory_nonImagePresent()
    throws Exception
  {
    final Path dir = FileUtil.createLocalTempDir();

    Files.createDirectory( dir.resolve( "outfits" ) );
    FileUtil.write( dir.resolve( "outfits" ).resolve( "buckle.txt" ), new byte[ 0 ] );
    FileUtil.write( dir.resolve( "outfits" ).resolve( "README" ), new byte[ 0 ] );

    final ImageRegistry registry = new ImageRegistry();

    assertEquals( registry.getImages().size(), 0 );

    ImageRegistry.populateRegistryFromDirectory( registry, dir );

    assertEquals( registry.getImages().size(), 0 );
  }

  @Test
  public void populateRegistryFromDirectory_multipleImages()
    throws Exception
  {
    final Path dir = FileUtil.createLocalTempDir();

    Files.createDirectory( dir.resolve( "outfits" ) );

    // Single jpg image
    FileUtil.write( dir.resolve( "outfits" ).resolve( "buckle.jpg" ), new byte[ 0 ] );

    // Non image
    FileUtil.write( dir.resolve( "outfits" ).resolve( "README.txt" ), new byte[ 0 ] );

    // Single png image nested
    Files.createDirectory( dir.resolve( "asteroid" ) );
    Files.createDirectory( dir.resolve( "asteroid" ).resolve( "gold" ) );
    FileUtil.write( dir.resolve( "asteroid" ).resolve( "gold" ).resolve( "spin.png" ), new byte[ 0 ] );

    // A image with blend
    Files.createDirectory( dir.resolve( "asteroid" ).resolve( "silver" ) );
    FileUtil.write( dir.resolve( "asteroid" ).resolve( "silver" ).resolve( "spin=1.png" ), new byte[ 0 ] );
    FileUtil.write( dir.resolve( "asteroid" ).resolve( "silver" ).resolve( "spin=2.png" ), new byte[ 0 ] );
    FileUtil.write( dir.resolve( "asteroid" ).resolve( "silver" ).resolve( "spin=3.png" ), new byte[ 0 ] );
    FileUtil.write( dir.resolve( "asteroid" ).resolve( "silver" ).resolve( "spin=4.png" ), new byte[ 0 ] );
    FileUtil.write( dir.resolve( "asteroid" ).resolve( "silver" ).resolve( "spin=5.png" ), new byte[ 0 ] );

    final ImageRegistry registry = new ImageRegistry();

    assertEquals( registry.getImages().size(), 0 );

    ImageRegistry.populateRegistryFromDirectory( registry, dir );

    assertEquals( registry.getImages().size(), 3 );
    assertNotNull( registry.findImage( "outfits", "buckle" ) );
    assertNotNull( registry.findImage( "asteroid/gold", "spin" ) );
    assertNotNull( registry.findImage( "asteroid/silver", "spin" ) );
  }
}
