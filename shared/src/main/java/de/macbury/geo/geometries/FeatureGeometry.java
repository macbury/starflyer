package de.macbury.geo.geometries;

import de.macbury.geo.core.GeoJSON;

/**
 * A geometry is a GeoJSON object where the type member's value is one of the following strings: "Point", "MultiPoint", "LineString", "MultiLineString", "Polygon", "MultiPolygon", or "GeometryCollection".
 * A GeoJSON geometry object of any type other than "GeometryCollection" must have a member with the name "coordinates". The value of the coordinates member is always an array. The structure for the elements in this array is determined by the type of geometry.
 */
public abstract class FeatureGeometry extends GeoJSON {
  @Override
  public String toString() {
    return "FeatureGeometry{ type="+getType().toString()+" }";
  }
}
