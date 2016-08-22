package de.macbury.server.tiles.mapzen;

import com.mashape.unirest.http.HttpMethod;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import de.macbury.geo.core.GeoJSON;
import de.macbury.json.JsonHelper;

import java.util.concurrent.Future;

/**
 * Simple wrapper around mapzen api
 */
public class MapZenApi {
  private final static String ENDPOINT = "https://vector.mapzen.com/osm/buildings,water,roads,landuse/{zoom}/{x}/{y}.json";
  private static final int DEFAULT_ZOOM = 17;
  private static String apiKey;
  private static MapZenApi instance;

  public static void setApiKey(String apiKey) {
    MapZenApi.apiKey = apiKey;
    setup();
  }

  private static void setup() {
    Unirest.setObjectMapper(new ObjectMapper() {
      @Override
      public <T> T readValue(String value, Class<T> valueType) {
        try {
          return JsonHelper.fromJson(value, valueType);
        } catch (Exception e) {
          e.printStackTrace();
          return null;
        }
      }

      @Override
      public String writeValue(Object value) {
        return value.toString();
      }
    });

    instance = new MapZenApi();
  }

  /**
   * Fetch {@link GeoJSON} from MapZen vector api
   * @param x
   * @param y
   * @return
   * @throws UnirestException
   */
  public Future<HttpResponse<MapZenLayersResult>> get(int x, int y, Callback<MapZenLayersResult> callback) throws UnirestException {
    GetRequest request = new GetRequest(HttpMethod.GET, ENDPOINT);
    return request
            .routeParam("zoom", String.valueOf(DEFAULT_ZOOM))
            .routeParam("x", String.valueOf(x))
            .routeParam("y", String.valueOf(y))
            .queryString("api_key", apiKey)
            .asObjectAsync(MapZenLayersResult.class, callback);
  }

  /**
   * Fetch {@link GeoJSON} from MapZen vector api
   * @param x
   * @param y
   * @return
   * @throws UnirestException
   */
  public static Future<HttpResponse<MapZenLayersResult>> getTile(int x, int y, Callback<MapZenLayersResult> callback) throws UnirestException {
    return getInstance().get(x,y, callback);
  }

  public static MapZenApi getInstance() {
    return instance;
  }

  public static void setInstance(MapZenApi api) {
    instance = api;
  }
}
