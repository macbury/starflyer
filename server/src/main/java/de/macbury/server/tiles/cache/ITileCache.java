package de.macbury.server.tiles.cache;

import de.macbury.model.GeoTile;

/**
 * Save and retrieve {@link de.macbury.model.GeoTile}
 */
public interface ITileCache {
  public GeoTile retrieve(int x, int y);
  public void save(GeoTile tile);
}
