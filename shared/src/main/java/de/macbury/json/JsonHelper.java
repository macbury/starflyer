package de.macbury.json;

import com.badlogic.gdx.utils.Pool;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.macbury.geo.Coordinates;
import de.macbury.geo.FeatureCollection;
import de.macbury.geo.GeoJSON;
import de.macbury.json.serializers.CoordinatesSerializerAndDeserializer;
import de.macbury.json.serializers.GeoJSONSerializerAndDeserializer;

import java.util.HashMap;

/**
 * This class helps with serialization and deserialization objects. It contains poll of gson objects
 */
public class JsonHelper {
  private static final double CURRENT_VERSION = 0.1;

  private static Pool<Gson> gsonPool = new Pool<Gson>() {
    @Override
    protected Gson newObject() {
      return create();
    }
  };

  /**
   * Create new {@link Gson} and setup it
   * @return
   */
  private static Gson create() {
    GsonBuilder gson = new GsonBuilder();
    gson.excludeFieldsWithoutExposeAnnotation();
    gson.setVersion(CURRENT_VERSION);
    gson.registerTypeAdapter(FeatureCollection.class, new GeoJSONSerializerAndDeserializer());

    return gson.create();
  }

  /**
   * Transform object into json
   * @param src
   * @return
   */
  public static String toJson(Object src) {
    Gson gson = gsonPool.obtain();
    try {
      return gson.toJson(src);
    } finally {
      gsonPool.free(gson);
    }
  }

  /**
   * Transform object into hash
   * @param src
   * @return
   */
  public static HashMap toHash(Object src) {
    Gson gson = gsonPool.obtain();
    try {
      String json = gson.toJson(src);
      return gson.fromJson(json, HashMap.class);
    } finally {
      gsonPool.free(gson);
    }
  }

  /**
   * Create object from json string
   * @return
   */
  public static <T> T fromJson(String json, Class<T> classOfT) {
    Gson gson = gsonPool.obtain();
    try {
      return gson.fromJson(json, classOfT);
    } finally {
      gsonPool.free(gson);
    }
  }
}
