package de.macbury.desktop.tiles.downloaders;

import com.badlogic.gdx.Gdx;
import de.macbury.model.GeoTile;
import de.macbury.server.tiles.TilesManager;
import de.macbury.tiles.downloaders.AbstractGeoTileDownloader;

/**
 * Use map zen api to retrieve {@link de.macbury.geo.core.GeoJSON}
 */
public class MapZenGeoTileDownloader extends AbstractGeoTileDownloader implements TilesManager.Listener {
  private static final String TAG = "MapZenGeoTileDownloader";
  private final TilesManager tilesManager;

  public MapZenGeoTileDownloader(TilesManager tilesManager) {
    super();
    this.tilesManager = tilesManager;
    tilesManager.addListener(this);
  }

  @Override
  public void retrieve(int x, int y) {
    Gdx.app.log(TAG, "Downloading tile: " + x + "x" + y);
    tilesManager.retrieve(x,y);
  }

  @Override
  public void onTileRetrieve(final GeoTile tile, TilesManager manager) {
    Gdx.app.log(TAG, "Tile has been downloaded: "+ tile.toString());
    Gdx.app.postRunnable(new Runnable() {
      @Override
      public void run() {
        triggerListeners(tile);
      }
    });
  }
}
