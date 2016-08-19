package de.macbury.json.serializers;

import com.google.gson.*;
import de.macbury.geo.Coordinates;
import de.macbury.geo.GeoPoint;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by macbury on 19.08.16.
 */
public class CoordinatesSerializerAndDeserializer implements JsonDeserializer<Coordinates>, JsonSerializer<Coordinates> {

  private static final int LAT_KEY = 1;
  private static final int LNG_KEY = 0;

  @Override
  public Coordinates deserialize(JsonElement root, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    Coordinates coordinates = new Coordinates();
    JsonElement firstElement = root.getAsJsonArray().get(0);
    if (firstElement.isJsonArray()) {// We got multiple points here
      JsonArray coordinatesJsonArray = firstElement.getAsJsonArray();
      for (int i = 0; i < coordinatesJsonArray.size(); i++) {
        JsonArray jsonCoords = coordinatesJsonArray.get(i).getAsJsonArray();
        GeoPoint geoPoint = new GeoPoint();
        geoPoint.set(jsonCoords.get(LAT_KEY).getAsDouble(), jsonCoords.get(LNG_KEY).getAsDouble());
        coordinates.add(geoPoint);
      }
      return coordinates;
    } else { // just single point
      JsonArray jsonCoords = root.getAsJsonArray();
      GeoPoint geoPoint = new GeoPoint();
      geoPoint.set(jsonCoords.get(LAT_KEY).getAsDouble(), jsonCoords.get(LNG_KEY).getAsDouble());
      coordinates.add(geoPoint);
    }
    return coordinates;
  }

  @Override
  public JsonElement serialize(Coordinates coordinates, Type typeOfSrc, JsonSerializationContext context) {
    if (coordinates.size() == 1) {// if we have point
      JsonArray jsonCoordinates = new JsonArray();
      jsonCoordinates.add(coordinates.get(0).lng);
      jsonCoordinates.add(coordinates.get(0).lat);
      return jsonCoordinates;
    } else {
      JsonArray jsonCoordinates = new JsonArray();
      for (GeoPoint point : coordinates) {
        JsonArray pointArray = new JsonArray();
        pointArray.add(point.lng);
        pointArray.add(point.lat);
        jsonCoordinates.add(pointArray);
      }

      JsonArray root = new JsonArray();
      root.add(jsonCoordinates);
      return root;
    }
  }
}
