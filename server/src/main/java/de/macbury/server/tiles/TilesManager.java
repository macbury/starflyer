package de.macbury.server.tiles;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import de.macbury.model.GeoTile;
import de.macbury.server.tiles.cache.ITileCache;
import de.macbury.server.tiles.mapzen.MapZenApi;
import de.macbury.server.tiles.mapzen.MapZenLayersResult;

/**
 * This manager manage downloading tiles as {@link de.macbury.geo.core.GeoJSON}, then transforming them into {@link de.macbury.model.GeoTile} and caching them.
 */
public class TilesManager implements Disposable {
  private final ITileCache tileCache;
  private final Array<Listener> listeners;

  public TilesManager(ITileCache tileCache) {
    this.tileCache = tileCache;
    this.listeners = new Array<Listener>();
  }

  /**
   * Enqueue tile retrieve. If it is stored in cache it will retrieve immediately. Otherwise it will retrieve {@link de.macbury.geo.core.GeoJSON} using {@link MapZenApi} and build {@link GeoTile}
   * @param x
   * @param y
   */
  public void retrieve(int x, int y) {
    GeoTile tile = tileCache.retrieve(x, y);
    if (tile != null) {
      triggerOnTileRetreive(tile);
    } else {
      try {
        MapZenApi.getTile(x, y, new Callback<MapZenLayersResult>() {
          @Override
          public void completed(HttpResponse<MapZenLayersResult> response) {
            buildAndCache(x, y, response.getBody());
          }

          @Override
          public void failed(UnirestException e) {
            //TODO think how handle exceptions
          }

          @Override
          public void cancelled() {

          }
        });
      } catch (UnirestException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Build {@link GeoTile} from {@link MapZenLayersResult}, cache it and trigger {@link Listener#onTileRetreive(GeoTile, TilesManager)}
   * @param x
   * @param y
   * @param result
   */
  private void buildAndCache(int x, int y, MapZenLayersResult result) {
    GeoTile geoTile = result.toGeoTile();
    geoTile.setId(x, y);
    tileCache.save(geoTile);
  }

  /**
   * Add existing {@link Listener}
   * @param listener
   */
  public void addListener(Listener listener) {
    if (listeners.contains(listener, true)) {
      throw new RuntimeException("Listener already added to TilesManager!: " + listener.toString());
    }
    listeners.add(listener);
  }

  /**
   * Trigger all listeners with event {@link Listener#onTileRetreive(GeoTile, TilesManager)}
   * @param tile
   */
  private void triggerOnTileRetreive(GeoTile tile) {
    for (int i = 0; i < listeners.size; i++) {
      listeners.get(i).onTileRetreive(tile, this);
    }
  }

  @Override
  public void dispose() {
    listeners.clear();
  }

  public interface Listener {
    /**
     * Triggered when tile has ben loaded
     * @param tile
     * @param manager
     */
    public void onTileRetreive(GeoTile tile, TilesManager manager);
  }
}
