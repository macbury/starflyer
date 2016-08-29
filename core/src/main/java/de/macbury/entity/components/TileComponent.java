package de.macbury.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import de.macbury.geo.Tile;
import de.macbury.tiles.TileInstance;

/**
 * Holds information about tile
 */
public class TileComponent implements Component, Pool.Poolable {
  private TileInstance instance;

  @Override
  public void reset() {
    instance = null;
  }

  public TileInstance getInstance() {
    return instance;
  }

  public void setInstance(TileInstance instance) {
    this.instance = instance;
  }

  /**
   * Return true if match {@link Tile} position
   * @param tile
   * @return
   */
  public boolean is(Tile tile) {
    return instance != null && instance.tileX == tile.x && instance.tileY == tile.y;
  }
}
