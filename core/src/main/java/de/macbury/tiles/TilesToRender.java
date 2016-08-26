package de.macbury.tiles;

import com.badlogic.gdx.utils.Array;
import de.macbury.geo.Tile;

/**
 * This class helps rendering {@link TileInstance}
 */
public class TilesToRender extends Array<TileInstance> {

  /**
   * Find what tiles are going to be rendered
   * @param pool
   * @param visibleTileProvider
   */
  public void begin(TileCachePool pool, VisibleTileProvider visibleTileProvider) {
    for (Tile visibleTile : visibleTileProvider.getVisible()) {
      add(pool.get(visibleTile));
    }
  }

  /**
   * Clear tiles to render
   */
  public void end() {
    clear();
  }
}
