package de.macbury.occlusion;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import de.macbury.geo.Tile;
import de.macbury.graphics.GeoPerspectiveCamera;

/**
 * Calculate using {@link de.macbury.graphics.GeoPerspectiveCamera} what {@link de.macbury.model.GeoTile} are visible
 */
public class VisibleTileProvider implements Disposable {
  private Pool<Tile> tilePool = new Pool<Tile>() {
    @Override
    protected Tile newObject() {
      return new Tile();
    }
  };
  /**
   * Visible tiles
   */
  private Array<Tile> visible = new Array<Tile>();

  /**
   * On this tile {@link GeoPerspectiveCamera} sits
   */
  private Tile originTile = new Tile();

  /**
   * Recalculate what is visible
   */
  public void update(GeoPerspectiveCamera camera) {
    // Clear pool and visible tiles
    tilePool.freeAll(visible);
    visible.clear();

    originTile.set(camera.geoPosition);

    // Calculate how many tiles are around player
    int tileAheadCount = MathUtils.ceil(camera.far / Tile.TILE_SIZE);
    for (int x = -tileAheadCount; x < tileAheadCount; x++) {
      for (int y = -tileAheadCount; y < tileAheadCount; y++) {
        Tile cursorTile = tilePool.obtain();
        cursorTile.setTilePosition(originTile.x + x, originTile.y + y);

        if (camera.frustum.boundsInFrustum(cursorTile.box)) {
          visible.add(cursorTile); // Tile is visible
        } else {
          tilePool.free(cursorTile); // Tile not visible so return it to pool
        }
      }
    }
  }

  /**
   * How many tiles are visible now
   * @return
   */
  public int getVisibleCount() {
    return visible.size;
  }

  @Override
  public void dispose() {
    tilePool.freeAll(visible);
    visible.clear();
  }

  /**
   * Return list of visible tiles
   * @return
   */
  public Array<Tile> getVisible() {
    return visible;
  }
}
