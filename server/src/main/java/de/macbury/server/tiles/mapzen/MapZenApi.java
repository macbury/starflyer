package de.macbury.server.tiles.mapzen;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import de.macbury.json.JsonHelper;

import java.util.concurrent.Future;

/**
 * Simple wrapper around mapzen api
 */
public class MapZenApi {
  private final static String ENDPOINT = "https://vector.mapzen.com/osm/buildings,water,roads,landuse/{zoom}/{x}/{y}.json";
  private static final int DEFAULT_ZOOM = 17;
  private static String apiKey;

  public static void setApiKey(String apiKey) {
    MapZenApi.apiKey = apiKey;
    setup();
  }

  private static void setup() {
    Unirest.setObjectMapper(new ObjectMapper() {
      @Override
      public <T> T readValue(String value, Class<T> valueType) {
        return JsonHelper.fromJson(value, valueType);
      }

      @Override
      public String writeValue(Object value) {
        return value.toString();
      }
    });
  }

  /**
   * Fetch {@link de.macbury.geo.GeoJSON} from MapZen vector api
   * @param x
   * @param y
   * @return
   * @throws UnirestException
   */
  public static Future<HttpResponse<MapZenLayersResult>> getTile(int x, int y, Callback<MapZenLayersResult> callback) throws UnirestException {
    return Unirest.get(ENDPOINT)
            .routeParam("zoom", String.valueOf(DEFAULT_ZOOM))
            .routeParam("x", String.valueOf(x))
            .routeParam("y", String.valueOf(y))
            .queryString("api_key", apiKey)
            .asObjectAsync(MapZenLayersResult.class, callback);
  }
}
