package de.macbury.tiles;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import de.macbury.geo.Tile;
import de.macbury.model.GeoTile;
import de.macbury.tiles.assembler.TileAssembler;
import de.macbury.tiles.downloaders.AbstractGeoTileDownloader;

/**
 * Manage all loaded {@link TileInstance}.
 */
public class TileCachePool implements Disposable, AbstractGeoTileDownloader.Listener, de.macbury.tiles.assembler.TileAssembler.Listener {
  public final static int MAX_POOL_SIZE = 100;
  private final Array<TileInstance> instances;
  private final AbstractGeoTileDownloader downloader;
  private final TileAssembler assembler;

  public TileCachePool(AbstractGeoTileDownloader downloader, de.macbury.tiles.assembler.TileAssembler assembler) {
    this.downloader = downloader;
    this.assembler  = assembler;
    instances       = new Array<TileInstance>();

    assembler.addListener(this);
    downloader.addListener(this);
  }

  /**
   * Update assigned {@link de.macbury.tiles.assembler.TileAssembler} and {@link AbstractGeoTileDownloader}. Run this on OpenGL thread.
   */
  public void update() {
    assembler.update();
  }

  /**
   * Get tile for location, otherwise initialize it
   * @param x tile location
   * @param y tile location
   * @return
   */
  public TileInstance get(int x, int y) {
    TileInstance instance = find(x,y);

    if (instance != null)
      return instance;
    return build(x,y);
  }

  /**
   * Only find {@link TileInstance}
   * @param x
   * @param y
   * @return instance or null
   */
  protected TileInstance find(int x, int y) {
    for (TileInstance instance : instances) {
      if (instance.isAt(x,y)) {
        return instance;
      }
    }

    return null;
  }

  /**
   * Build new {@link TileInstance} and retrieve its information from remote service, then assemble it
   * @param x
   * @param y
   * @return
   */
  private TileInstance build(int x, int y) {
    TileInstance instance = new TileInstance(x, y);
    instances.add(instance);
    trim(x,y);
    downloader.retrieve(x,y);
    return instance;
  }

  /**
   * If number of instances exceed max pool size remove the farthest away tiles from memory
   * @param x
   * @param y
   */
  private void trim(int x, int y) {
    int toRemove = instances.size - MAX_POOL_SIZE;
    if (toRemove > 0) {
      instances.sort();
    }
  }

  public TileInstance get(Tile tile) {
    return get(tile.x, tile.y);
  }

  /**
   * Dispose all {@link TileInstance}
   */
  @Override
  public void dispose() {
    for (TileInstance instance: instances) {
      instance.dispose();
    }
    instances.clear();
  }

  /**
   * Assemble mesh for fetched {@link GeoTile}
   * @param geoTile
   * @param tileDownloader
   */
  @Override
  public void onGeoTileFetch(GeoTile geoTile, AbstractGeoTileDownloader tileDownloader) {
    TileInstance instance = find(geoTile.x, geoTile.y);
    if (instance != null) {
      instance.geoTile = geoTile;
      instance.state = TileInstance.State.Assembling;
      assembler.assemble(geoTile);
    }
  }

  /**
   * After assembling finishes assign model to {@link TileInstance} and set status to {@link de.macbury.tiles.TileInstance.State#Ready}, If {@link TileInstance} was
   * removed before that then dispose generated model
   * @param modelInstance
   * @param geoTile
   * @param assembler
   */
  @Override
  public void onGeoJsonModelAssemblyFinish(ModelInstance modelInstance, GeoTile geoTile, de.macbury.tiles.assembler.TileAssembler assembler) {
    TileInstance instance = find(geoTile.x, geoTile.y);
    if (instance == null) { // instance was removed before assembling was finished, dispose this model
      modelInstance.model.dispose();
    } else {
      instance.state = TileInstance.State.Ready;
      instance.model = modelInstance;
    }
  }

  /**
   * Return number of {@link TileInstance} allocated in pool
   * @return
   */
  public int getSize() {
    return instances.size;
  }

  /**
   * Return all created {@link TileInstance}
   * @return
   */
  public Array<TileInstance> all() {
    return instances;
  }
}
