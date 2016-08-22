package de.macbury.json.serializers;

import com.google.gson.*;
import de.macbury.geo.core.*;
import de.macbury.geo.geometries.FeatureGeometry;
import de.macbury.geo.geometries.MultiLineStringGeometry;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Transform json to proper object of {@link de.macbury.geo.core.GeoJSON}
 */
public class GeoJSONSerializerAndDeserializer implements JsonDeserializer<de.macbury.geo.core.GeoJSON>, JsonSerializer<de.macbury.geo.core.GeoJSON> {
  private static final String KEY_TYPE = "type";
  private static final String KEY_FEATURES = "features";
  private static final String KEY_GEOMETRY = "geometry";
  private static final String KEY_PROPERTIES = "properties";
  private static final String KEY_COORDINATES = "coordinates";
  private static final int INDEX_LAT = 1;
  private static final int INDEX_LNG = 0;

  @Override
  public GeoJSON deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    JsonObject root       = json.getAsJsonObject();
    GeoJSON.Type mainType = GeoJSON.Type.valueOf(root.get(KEY_TYPE).getAsString());
    GeoJSON rootGeoJson   = null;

    switch (mainType) {
      case FeatureCollection:
        rootGeoJson = assembleFeatureCollection(root);
      break;
      default:
        throw new RuntimeException("Not supported GeoJSON type: " + mainType.toString());

    }

    rootGeoJson.setType(mainType);
    return rootGeoJson;
  }

  private FeatureCollection assembleFeatureCollection(JsonObject root) {
    FeatureCollection collection = new FeatureCollection();
    JsonArray jsonFeatures       = root.get(KEY_FEATURES).getAsJsonArray();
    for (JsonElement jsonFeatureElement: jsonFeatures) {
      JsonObject jsonFeatureObject = jsonFeatureElement.getAsJsonObject();
      collection.add(assembleFeature(jsonFeatureObject));
    }
    return collection;
  }

  private Feature assembleFeature(JsonObject jsonFeatureObject) {
    JsonObject geometryJson   = jsonFeatureObject.getAsJsonObject(KEY_GEOMETRY);

    Feature feature = new Feature();
    feature.setType(GeoJSON.Type.valueOf(jsonFeatureObject.get(KEY_TYPE).getAsString()));
    feature.setGeometry(buildGeometryType(geometryJson));

    JsonObject properties = jsonFeatureObject.getAsJsonObject(KEY_PROPERTIES);
    for(Map.Entry<String, JsonElement> entry : properties.entrySet()) {
      feature.putProp(entry.getKey(), entry.getValue().getAsString());
    }

    return feature;
  }


  private void createPath(JsonArray jsonCoordinates, MultiLineStringGeometry lineStringGeometry) {
    GeoPath path = lineStringGeometry.path();
    for (int j = 0; j < jsonCoordinates.size(); j++) {
      JsonArray singleCoord =  jsonCoordinates.get(j).getAsJsonArray();
      GeoPoint geoPoint     = new GeoPoint();
      geoPoint.set(
              singleCoord.get(INDEX_LAT).getAsDouble(),
              singleCoord.get(INDEX_LNG).getAsDouble()
      );

      path.add(geoPoint);
    }
  }

  private FeatureGeometry buildGeometryType(JsonObject geometryJson) {
    GeoJSON.Type geometryType = GeoJSON.Type.valueOf(geometryJson.get(KEY_TYPE).getAsString());
    JsonArray jsonCoordinates = null;
    FeatureGeometry geometry  = null;
    switch (geometryType) {
      case MultiLineString:
        MultiLineStringGeometry multiLineStringGeometry = new MultiLineStringGeometry();
        JsonArray jsonPaths = geometryJson.getAsJsonArray(KEY_COORDINATES);
        for (int i = 0; i < jsonPaths.size(); i++) {
          jsonCoordinates = jsonPaths.get(i).getAsJsonArray();
          createPath(jsonCoordinates, multiLineStringGeometry);
        }
        geometry = multiLineStringGeometry;
        break;
      case LineString:
        MultiLineStringGeometry lineStringGeometry = new MultiLineStringGeometry();
        jsonCoordinates = geometryJson.getAsJsonArray(KEY_COORDINATES);
        createPath(jsonCoordinates, lineStringGeometry);
        geometry = lineStringGeometry;
        break;
      default:
        throw new RuntimeException("Unsupported geometry type: " + geometryType.toString());
    }

    geometry.setType(geometryType);

    return geometry;
  }

  @Override
  public JsonElement serialize(de.macbury.geo.core.GeoJSON src, Type typeOfSrc, JsonSerializationContext context) {
    throw new RuntimeException("No deserialization avaiable");
  }
}
