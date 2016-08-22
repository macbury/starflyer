package de.macbury.model;

import com.badlogic.gdx.utils.Array;
import de.macbury.geo.core.GeoPath;

/**
 * Describe road in {@link GeoTile}. Road is array of {@link GeoPath}
 */
public class Road extends Array<GeoPath> {
  private GeoTileFeatureType type;

  /**
   * Type of road. It can be {@link GeoTileFeatureType#Highway}, {@link GeoTileFeatureType#MajorRoad}, {@link GeoTileFeatureType#MinorRoad}, {@link GeoTileFeatureType#Path}
   * @return
   */
  public GeoTileFeatureType getType() {
    return type;
  }

  public void setType(GeoTileFeatureType type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return "Road{" +
            "type=" + type + " " + super.toString() +
            '}';
  }
}
