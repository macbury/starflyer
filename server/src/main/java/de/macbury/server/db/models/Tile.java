package de.macbury.server.db.models;

import de.macbury.geo.core.FeatureCollection;
import de.macbury.geo.core.GeoJSON;
import de.macbury.json.JsonHelper;

/**
 * Terrain tile with information about roads, buildings, trees
 */
public class Tile extends FeatureCollection implements BaseModel<String> {
  private String id;

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void setId(String newId) {
    this.id = newId;
  }

  /**
   * Parses the given String as FeatureCollection and returns a concrete subclass of
   * {@link GeoJSON} corresponding to the type of the root object.
   * @param rawJson
   * @return
   */

  public static Tile parse(String rawJson) {
    return JsonHelper.fromJson(rawJson, Tile.class);
  }
}
