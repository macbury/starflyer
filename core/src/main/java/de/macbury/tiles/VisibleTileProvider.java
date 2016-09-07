package de.macbury.tiles;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import de.macbury.geo.MercatorProjection;
import de.macbury.geo.Tile;
import de.macbury.geo.core.GeoPoint;
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
  private final Tile originTile = new Tile();
  private final GeoPoint originPoint = new GeoPoint();

  private final BoundingBox tileBox  = new BoundingBox();
  private final Vector3 tileStartVec = new Vector3();
  private final Vector3 tileEndVec   = new Vector3();
  private final Vector3 cameraPos   = new Vector3();
  /**
   * Recalculate what is visible
   */
  public void update(Vector3 cameraWorldCenterPosition, GeoPerspectiveCamera camera) {
    // Clear pool and visible tiles
    tilePool.freeAll(visible);
    visible.clear();

    cameraPos.set(cameraWorldCenterPosition).add(camera.normalOrDebugPosition());

    MercatorProjection.unproject(cameraPos, originPoint);
    originTile.set(originPoint);

    // Calculate how many tiles are around player
    int tileAheadCount = MathUtils.ceil(camera.far / Tile.TILE_SIZE);
    for (int x = -tileAheadCount; x <= tileAheadCount; x++) {
      for (int y = -tileAheadCount; y <= tileAheadCount; y++) {

        tileStartVec.set(x, y, 0).scl(Tile.TILE_SIZE);
        tileEndVec.set(Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.MAX_ELEVATION).add(tileStartVec);
        tileBox.set(tileStartVec, tileEndVec);

        if (camera.boundsInFrustum(tileBox)) {
          Tile cursorTile = tilePool.obtain();
          cursorTile.setTilePosition(originTile.x + x, originTile.y + y);
          cursorTile.box.set(tileBox); //TODO remove this, only for debug testing!!!
          visible.add(cursorTile);
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

  public boolean isVisible(int tileX, int tileY) {
    for (Tile tile: visible) {
      if (tile.x == tileX && tile.y == tileY) {
        return true;
      }
    }
    return false;
  }
}
