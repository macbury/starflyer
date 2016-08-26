package de.macbury.tiles.assembler;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import de.macbury.geo.Tile;
import de.macbury.geo.core.GeoPoint;
import de.macbury.model.GeoTile;

import java.util.concurrent.*;

/**
 * This module assemble {@link Model} for passed {@link de.macbury.model.GeoTile} in separate thread
 */
public class TileAssembler implements Disposable {
  private final ExecutorService executor;
  private final Pool<AssembleGeoTileTask> assembleGeoTileTaskPool = new Pool<AssembleGeoTileTask>() {
    @Override
    protected AssembleGeoTileTask newObject() {
      return new AssembleGeoTileTask();
    }
  };

  private final DelayedRemovalArray<AssembleGeoTileTask> runningTasks;

  public TileAssembler() {
    executor = Executors.newFixedThreadPool(2);
    runningTasks = new DelayedRemovalArray<AssembleGeoTileTask>();
  }

  //<editor-fold desc="Managing listeners">
  private Array<Listener> listeners = new Array<Listener>();

  public void addListener(Listener listener) {
    listeners.add(listener);
  }

  /**
   * Trigger {@link Listener#onGeoJsonModelAssemblyFinish(ModelInstance, GeoTile, TileAssembler)}
   * @param tile
   * @param result
   */
  private void trigger(GeoTile tile, ModelInstance result) {
    for (Listener listener : listeners) {
      listener.onGeoJsonModelAssemblyFinish(result, tile, this);
    }
  }

  //</editor-fold>

  /**
   * This will trigger all listeners with assembled {@link Model}. Run this on OpenGL thread
   */
  public void update() {
    runningTasks.begin(); {
      for (AssembleGeoTileTask task : runningTasks) {
        if (task.isDone()) {
          try {
            ModelInstance resultInstance = task.get();
            trigger(task.getGeoTile(), resultInstance);
          } catch (InterruptedException e) {
            e.printStackTrace();
          } catch (ExecutionException e) {
            e.printStackTrace();
          }
          assembleGeoTileTaskPool.free(task);
          runningTasks.removeValue(task, true);
        }
      }
    } runningTasks.end();

  }

  /**
   * Submit assembling of {@link GeoTile} to one of threads
   * @param geoTile
   */
  public void assemble(GeoTile geoTile) {
    AssembleGeoTileTask task = assembleGeoTileTaskPool.obtain();
    task.setGeoTile(geoTile);
    runningTasks.add(task);
    executor.submit(task);
  }

  @Override
  public void dispose() {
    listeners.clear();
    runningTasks.clear();
    executor.shutdown();
    assembleGeoTileTaskPool.clear();
  }

  public interface Listener {
    public void onGeoJsonModelAssemblyFinish(ModelInstance modelInstance, GeoTile geoTile, TileAssembler assembler);
  }
}
