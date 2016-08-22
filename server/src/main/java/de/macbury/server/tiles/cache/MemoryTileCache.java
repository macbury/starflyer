package de.macbury.server.tiles.cache;

import com.badlogic.gdx.utils.ObjectMap;
import de.macbury.model.GeoTile;

public class MemoryTileCache implements ITileCache {
  private ObjectMap<String, GeoTile> tiles = new ObjectMap<String, GeoTile>();
  @Override
  public GeoTile retrieve(int x, int y) {
    return tiles.get(x+"/"+y);
  }

  @Override
  public void save(GeoTile tile) {
    tiles.put(tile.getId(), tile);
  }
}
