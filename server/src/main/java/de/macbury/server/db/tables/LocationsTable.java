package de.macbury.server.db.tables;

import com.rethinkdb.gen.ast.Point;
import com.rethinkdb.model.MapObject;
import de.macbury.server.db.Database;
import de.macbury.server.db.models.Location;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.HashMap;

import static com.rethinkdb.RethinkDB.r;

/**
 * Table with locations in world
 */
public class LocationsTable extends BaseTable<Location, Long> {
  private static final String KEY_NAME = "name";
  private static final String KEY_TIMESTAMP = "timestamp";
  private static final String KEY_TYPE = "type";
  private static final String KEY_SUB_TYPE = "sub_type";
  private static final String KEY_POINT = "point";
  private static final Object KEY_COORDINATES = "coordinates";

  public LocationsTable(Database database) {
    super("locations", database);
  }

  @Override
  public MapObject serialize(Location model) {
    long timestamp = 0;
    if (model.getTimestamp() != null)
      timestamp = model.getTimestamp().getTime();
    return r.hashMap(KEY_NAME, model.getName())
            .with(KEY_POINT, r.point(model.getLng(), model.getLat()))
            .with(KEY_ID, model.getId())
            .with(KEY_TIMESTAMP, timestamp)
            .with(KEY_TYPE, model.getType())
            .with(KEY_SUB_TYPE, model.getSubType());
  }

  private Point extractCoordinates(JSONObject pointJson) {
    JSONArray points = (JSONArray) pointJson.get(KEY_COORDINATES);
    return r.point(points.get(0), points.get(1));
  }

  @Override
  public Location deserialize(HashMap data) {
    Location location = new Location();
    JSONObject pointJson = (JSONObject) data.get(KEY_POINT);
    JSONArray points = (JSONArray) pointJson.get(KEY_COORDINATES);
    location.setLng((Double) points.get(0));
    location.setLat((Double) points.get(1));
    location.setName((String)data.get(KEY_NAME));
    location.setId((long)data.get(KEY_ID));
    location.setTimestamp(new Date((long)data.get(KEY_TIMESTAMP)));
    location.setType((String)data.get(KEY_TYPE));
    location.setSubType((String)data.get(KEY_SUB_TYPE));
    return location;
  }
}
