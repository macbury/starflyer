package de.macbury.tiles;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import de.macbury.model.GeoTile;

/**
 * Main tile component for rendering. It have model and information about tile.
 */
public class TileInstance implements Disposable, RenderableProvider, Comparable<TileInstance> {

  public enum State {
    /**
     * Tile is downloading its {@link de.macbury.geo.core.GeoJSON} from server
     */
    Downloading,
    /**
     * Another thread now is assembling mesh for this tile
     */
    Assembling,
    /**
     * This is ready to render
     */
    Ready,
    /**
     * There was a error while downloading/assembling phase
     */
    Error,
    /**
     * This tile is going to be disposed
     */
    Unloaded
  }
  /**
   * Downloaded tile information. At first it can be null
   */
  public GeoTile geoTile;
  /**
   * Model of tile. At first it can be null
   */
  public ModelInstance model;
  /**
   * Tile coordinates
   */
  public int tileX;
  public int tileY;

  public State state;

  public TileInstance(int x, int y) {
    this.tileX = x;
    this.tileY = y;
    this.geoTile = new GeoTile();
    geoTile.setId(x,y);
    state = State.Downloading;
  }

  @Override
  public void dispose() {
    state = TileInstance.State.Unloaded;
    if (model != null)
      this.model.model.dispose();
    if (geoTile != null)
      geoTile = null;
    this.model = null;
  }

  /**
   * Return true if passed tile coords equals this {@link TileInstance}
   * @param x
   * @param y
   * @return
   */
  public boolean isAt(int x, int y) {
    return tileX == x && tileY == y;
  }

  @Override
  public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
    if (isReady()) {
      model.getRenderables(renderables, pool);
    }
  }

  public boolean isReady() {
    return this.state == State.Ready;
  }

  @Override
  public int compareTo(TileInstance o) {
    return dst(o.tileX, o.tileY);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    TileInstance instance = (TileInstance) o;

    if (tileX != instance.tileX) return false;
    return tileY == instance.tileY;

  }

  @Override
  public int hashCode() {
    int result = tileX;
    result = 31 * result + tileY;
    return result;
  }

  public int dst(int tx, int ty) {
    final int x_d = tx - this.tileX;
    final int y_d = ty - this.tileY;
    return (int)Math.sqrt(x_d * x_d + y_d * y_d);
  }

  @Override
  public String toString() {
    return "TileInstance{" +
            "tileX=" + tileX +
            ", tileY=" + tileY +
            ", state=" + state +
            '}';
  }
}
