package de.macbury.geo;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * A geometry is a GeoJSON object where the type member's value is one of the following strings: "Point", "MultiPoint", "LineString", "MultiLineString", "Polygon", "MultiPolygon", or "GeometryCollection".
 * A GeoJSON geometry object of any type other than "GeometryCollection" must have a member with the name "coordinates". The value of the coordinates member is always an array. The structure for the elements in this array is determined by the type of geometry.
 */
public class FeatureGeometry extends GeoJSON {
  @Expose
  private ArrayList<GeoPoint> coordinates;

  public ArrayList<GeoPoint> getCoordinates() {
    return coordinates;
  }
}
