package de.macbury.server.tiles.cache;

import de.macbury.model.GeoTile;

/**
 * This cache dont save {@link GeoTile}
 */
public class NullTileCache implements ITileCache {
  @Override
  public GeoTile retrieve(int x, int y) {
    return null;
  }

  @Override
  public void save(GeoTile tile) {

  }
}
