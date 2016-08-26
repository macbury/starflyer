package de.macbury.tiles;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import de.macbury.model.GeoTile;

/**
 * This module assemble {@link Model} for passed {@link de.macbury.model.GeoTile} in separate thread
 */
public class TileAssembler implements Disposable {
  private Array<Listener> listeners = new Array<Listener>();

  public void addListener(Listener listener) {
    listeners.add(listener);
  }

  /**
   * This will trigger all listeners with assembled {@link Model}. Run this on OpenGL thread
   */
  public void update() {

  }

  public void assemble(GeoTile geoTile) {

  }

  @Override
  public void dispose() {

  }

  public interface Listener {
    public void onGeoJsonModelAssemblyFinish(ModelInstance modelInstance, GeoTile geoTile, TileAssembler assembler);
  }
}
