package de.macbury.tests.tiles;

import de.macbury.geo.Tile;
import de.macbury.geo.core.GeoPoint;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Created by macbury on 24.08.16.
 */
public class TestTile {

  @Test
  public void itShouldCalculateProperTilePositionForHomeLocation() {
    Tile tile = new Tile();
    GeoPoint point = new GeoPoint(50.093113, 20.059572);

    tile.set(point);

    Assert.assertEquals(36419, tile.x);
    Assert.assertEquals(22199, tile.y);

    Assert.assertEquals(50.09591722887199, tile.north);
    Assert.assertEquals(50.09239321093879, tile.south);

    Assert.assertEquals(20.06103515625, tile.east);
    Assert.assertEquals(20.0555419921875, tile.west);
  }

  @Test
  public void itShouldCalculateDiffrentTileForDiffrentZoom() {
    Tile tile = new Tile();
    GeoPoint point = new GeoPoint(50.093113, 20.059572);

    tile.set(point, 16);

    Assert.assertNotSame(72839, tile.x);
    Assert.assertNotSame(44399, tile.y);
  }

  @Test
  public void itShouldCalculateBoundingBoxForTile() {
    Tile tile = new Tile();
    tile.set(0,0);

    Assert.assertEquals(Tile.TILE_SIZE, tile.box.getWidth());
    Assert.assertEquals(Tile.TILE_SIZE, tile.box.getHeight());
    Assert.assertEquals(Tile.MAX_ELEVATION+10, tile.box.getDepth());

    tile.set(85.0511287798, 180);

    Assert.assertEquals(Tile.TILE_SIZE, tile.box.getHeight());
    Assert.assertEquals(Tile.TILE_SIZE, tile.box.getWidth());
    Assert.assertEquals(Tile.MAX_ELEVATION+10, tile.box.getDepth());

    tile.set(-85.0511287798, -180);

    Assert.assertEquals(Tile.TILE_SIZE, tile.box.getHeight());
    Assert.assertEquals(Tile.TILE_SIZE, tile.box.getWidth());
    Assert.assertEquals(Tile.MAX_ELEVATION+10, tile.box.getDepth());
  }
}
