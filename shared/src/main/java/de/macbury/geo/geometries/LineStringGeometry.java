package de.macbury.geo.geometries;

import de.macbury.geo.core.GeoPath;
import de.macbury.geo.core.GeoPoint;

/**
 * For type "LineString", the "coordinates" member must be an array of two or more positions.
 * A LinearRing is closed LineString with 4 or more positions. The first and last positions are equivalent (they represent equivalent points). Though a LinearRing is not explicitly represented as a GeoJSON geometry type, it is referred to in the Polygon geometry type definition.
 */
public class LineStringGeometry extends FeatureGeometry {
  private GeoPath path = new GeoPath();

  public GeoPath getPath() {
    return path;
  }

  public GeoPoint get(int index) {
    return path.get(index);
  }

  public void add(GeoPoint point) {
    path.add(point);
  }

  public int size() {
    return path.size();
  }
}
