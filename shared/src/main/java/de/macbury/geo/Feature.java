package de.macbury.geo;

import com.google.gson.annotations.Expose;

import java.util.HashMap;

/**
 * A GeoJSON object with the type "Feature" is a feature object.
 * A feature object must have a member with the name "geometry". The value of the geometry member is a geometry object as defined above or a JSON null value.
 * A feature object must have a member with the name "properties". The value of the properties member is an object (any JSON object or a JSON null value).
 * If a feature has a commonly used identifier, that identifier should be included as a member of the feature object with the name "id".
 */
public class Feature extends GeoJSON {
  private static final String KEY_SOURCE = "source";
  private static final String KEY_ID = "id";
  private static final String KEY_KIND = "kind";
  private static final String KEY_LAND_USE = "landuse_kind";
  @Expose
  private HashMap<String, Object> properties;

  @Expose
  private FeatureGeometry geometry;

  /**
   * Return value for property
   * @param key
   * @return
   */
  public Object getProp(String key) {
    if (properties == null) {
      return null;
    }
    return properties.get(key);
  }

  /**
   * Return source of this feature(default openstreetmap)
   * @return
   */
  public String getPropSource() {
    return (String)properties.get(KEY_SOURCE);
  }

  /**
   * Id of object
   * @return
   */
  public int getPropId() {
    return ((Double)(properties.get(KEY_ID))).intValue();
  }

  public String getPropKind() {
    return (String)properties.get(KEY_KIND);
  }

  public String getPropLandUse() {
    return (String)properties.get(KEY_LAND_USE);
  }

  public FeatureGeometry getGeometry() {
    return geometry;
  }
}
