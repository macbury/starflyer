package de.macbury.geo.geometries;

import de.macbury.geo.core.GeoPath;
import de.macbury.geo.core.GeoPoint;

import java.util.ArrayList;

/**
 * For type "LineString", the "coordinates" member must be an array of two or more positions.
 * A LinearRing is closed LineString with 4 or more positions. The first and last positions are equivalent (they represent equivalent points). Though a LinearRing is not explicitly represented as a GeoJSON geometry type, it is referred to in the Polygon geometry type definition.
 */
public class MultiLineStringGeometry extends FeatureGeometry {
  private ArrayList<GeoPath> coordinates = new ArrayList<GeoPath>();

  public GeoPath get(int index) {
    return coordinates.get(index);
  }

  public GeoPath path() {
    GeoPath path = new GeoPath();
    coordinates.add(path);
    return path;
  }

  /**
   * All paths
   * @return
   */
  public ArrayList<GeoPath> all() {
    return coordinates;
  }

  public int size() {
    return coordinates.size();
  }
}
