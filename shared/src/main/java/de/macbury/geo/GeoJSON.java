package de.macbury.geo;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.google.gson.annotations.Expose;
import de.macbury.json.JsonHelper;

/**
 * GeoJSON is a format for encoding a variety of geographic data structures. A GeoJSON object may represent a geometry, a feature, or a collection of features. GeoJSON supports the following geometry types: Point, LineString, Polygon, MultiPoint, MultiLineString, MultiPolygon, and GeometryCollection. Features in GeoJSON contain a geometry object and additional properties, and a feature collection represents a list of features.
 * A complete GeoJSON data structure is always an object (in JSON terms). In GeoJSON, an object consists of a collection of name/value pairs -- also called members. For each member, the name is always a string. Member values are either a string, number, object, array or one of the literals: true, false, and null. An array consists of elements where each element is a value as described above.
 */
public abstract class GeoJSON {
  public enum Type {
    Point, MultiPoint, LineString, MultiLineString, Polygon, MultiPolygon, GeometryCollection, Feature, FeatureCollection
  }
  @Expose
  private Type type;


  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

}
