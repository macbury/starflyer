package de.macbury.geo;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * For type "LineString", the "coordinates" member must be an array of two or more positions.
 * A LinearRing is closed LineString with 4 or more positions. The first and last positions are equivalent (they represent equivalent points). Though a LinearRing is not explicitly represented as a GeoJSON geometry type, it is referred to in the Polygon geometry type definition.
 */
public class LineStringGeometry extends FeatureGeometry {
  private ArrayList<GeoPoint> coordinates;

  public ArrayList<GeoPoint> getCoordinates() {
    return coordinates;
  }

  public GeoPoint get(int index) {
    return coordinates.get(index);
  }

  public int size() {
    return coordinates.size();
  }
}
