package de.macbury.tiles;

import com.badlogic.gdx.graphics.g3d.Model;
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
public class TileInstance implements Disposable, RenderableProvider {
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
    Error
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
    if (state == State.Ready) {
      model.getRenderables(renderables, pool);
    }
  }
}
