package de.macbury.json.serializers;

import com.google.gson.*;
import de.macbury.geo.Coordinates;
import de.macbury.geo.Feature;
import de.macbury.geo.FeatureCollection;
import de.macbury.geo.GeoJSON;

import java.lang.reflect.Type;

/**
 * Transform json to proper object of {@link GeoJSON}
 */
public class GeoJSONSerializerAndDeserializer implements JsonDeserializer<GeoJSON>, JsonSerializer<GeoJSON> {
  private static final String KEY_TYPE = "type";
  private static final String KEY_FEATURES = "features";

  @Override
  public GeoJSON deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    JsonObject root = json.getAsJsonObject();
    GeoJSON.Type mainType = GeoJSON.Type.valueOf(root.get(KEY_TYPE).getAsString());

    switch (mainType) {
      case FeatureCollection:
          return assembleFeatureCollection(root);

      default:
        throw new RuntimeException("Not supported GeoJSON type: " + mainType.toString());

    }
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
    //
    return null;
  }

  @Override
  public JsonElement serialize(GeoJSON src, Type typeOfSrc, JsonSerializationContext context) {
    return null;
  }
}
