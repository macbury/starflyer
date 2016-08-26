package de.macbury.tiles.downloaders;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import de.macbury.model.GeoTile;

/**
 * Manage retrieving {@link GeoTile} from remote source
 */
public abstract class AbstractGeoTileDownloader implements Disposable {
  final private Array<Listener> listeners = new Array<Listener>();

  public void addListener(Listener listener) {
    listeners.add(listener);
  }

  /**
   * Fetch {@link GeoTile} from remote source. After success trigger {@link Listener#onGeoTileFetch(GeoTile, AbstractGeoTileDownloader)}
   * @param x
   * @param y
   */
  public abstract void retrieve(int x, int y);

  /**
   * Trigger listener with result
   * @param geoTile
   */
  protected void triggerListeners(GeoTile geoTile) {
    for (Listener listener : listeners) {
      listener.onGeoTileFetch(geoTile, this);
    }
  }

  @Override
  public void dispose() {
    listeners.clear();
  }

  public interface Listener {
    public void onGeoTileFetch(GeoTile geoTile, AbstractGeoTileDownloader tileDownloader);
  }
}
