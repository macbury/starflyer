package de.macbury.json.serializers;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Transform json to proper object of {@link de.macbury.geo.core.GeoJSON}
 */
public class GeoJSONSerializerAndDeserializer implements JsonDeserializer<de.macbury.geo.core.GeoJSON>, JsonSerializer<de.macbury.geo.core.GeoJSON> {
  private static final String KEY_TYPE = "type";
  private static final String KEY_FEATURES = "features";
  private static final String KEY_GEOMETRY = "geometry";
  private static final String KEY_PROPERTIES = "properties";
  private static final String KEY_COORDINATES = "coordinates";
  private static final int INDEX_LAT = 0;
  private static final int INDEX_LNG = 1;

  @Override
  public de.macbury.geo.core.GeoJSON deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    JsonObject root = json.getAsJsonObject();
    de.macbury.geo.core.GeoJSON.Type mainType = de.macbury.geo.core.GeoJSON.Type.valueOf(root.get(KEY_TYPE).getAsString());

    switch (mainType) {
      case FeatureCollection:
          return assembleFeatureCollection(root);

      default:
        throw new RuntimeException("Not supported GeoJSON type: " + mainType.toString());

    }
  }

  private de.macbury.geo.core.FeatureCollection assembleFeatureCollection(JsonObject root) {
    de.macbury.geo.core.FeatureCollection collection = new de.macbury.geo.core.FeatureCollection();
    JsonArray jsonFeatures       = root.get(KEY_FEATURES).getAsJsonArray();
    for (JsonElement jsonFeatureElement: jsonFeatures) {
      JsonObject jsonFeatureObject = jsonFeatureElement.getAsJsonObject();
      collection.add(assembleFeature(jsonFeatureObject));
    }
    return collection;
  }

  private de.macbury.geo.core.Feature assembleFeature(JsonObject jsonFeatureObject) {
    JsonObject geometryJson   = jsonFeatureObject.getAsJsonObject(KEY_GEOMETRY);

    de.macbury.geo.core.Feature feature = new de.macbury.geo.core.Feature();
    feature.setType(de.macbury.geo.core.GeoJSON.Type.valueOf(jsonFeatureObject.get(KEY_TYPE).getAsString()));
    feature.setGeometry(buildGeometryType(geometryJson));

    return feature;
  }

  private de.macbury.geo.geometries.FeatureGeometry buildGeometryType(JsonObject geometryJson) {
    de.macbury.geo.core.GeoJSON.Type geometryType = de.macbury.geo.core.GeoJSON.Type.valueOf(geometryJson.get(KEY_TYPE).getAsString());

    switch (geometryType) {
      case LineString:
        de.macbury.geo.geometries.LineStringGeometry lineStringGeometry = new de.macbury.geo.geometries.LineStringGeometry();
        //TODO: Treat line string as multiline string
        JsonArray jsonCoordinates = geometryJson.getAsJsonArray(KEY_COORDINATES);
        for (int i = 0; i < jsonCoordinates.size(); i++) {
          JsonArray singleCoord =  jsonCoordinates.get(i).getAsJsonArray();
          de.macbury.geo.core.GeoPoint geoPoint     = new de.macbury.geo.core.GeoPoint();
          geoPoint.set(
                  singleCoord.get(INDEX_LAT).getAsDouble(),
                  singleCoord.get(INDEX_LNG).getAsDouble()
          );

          lineStringGeometry.add(geoPoint);
        }
        return lineStringGeometry;
      default:
        throw new RuntimeException("Unsuported geometry type: " + geometryType.toString());
    }
  }

  @Override
  public JsonElement serialize(de.macbury.geo.core.GeoJSON src, Type typeOfSrc, JsonSerializationContext context) {
    return null;
  }
}
