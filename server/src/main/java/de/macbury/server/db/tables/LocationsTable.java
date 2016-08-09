package de.macbury.server.db.tables;

import com.rethinkdb.model.MapObject;
import de.macbury.server.db.Database;
import de.macbury.server.db.models.Location;

import java.util.HashMap;

import static com.rethinkdb.RethinkDB.r;

/**
 * Table with locations in world
 */
public class LocationsTable extends BaseTable<Location> {
  private static final String KEY_NAME = "name";
  private static final String KEY_LAT = "lat";
  private static final String KEY_LNG = "lng";
  private static final String KEY_OSM_ID = "osm_id";

  public LocationsTable(Database database) {
    super("locations", database);
  }

  @Override
  public MapObject serialize(Location model) {
    return r.hashMap(KEY_NAME, model.getName())
            .with(KEY_LAT, model.getLat())
            .with(KEY_LNG, model.getLng())
            .with(KEY_OSM_ID, model.getOsmId());
  }

  @Override
  public Location deserialize(HashMap data) {
    Location location = new Location();
    location.setLat((double)data.get(KEY_LAT));
    location.setLng((double)data.get(KEY_LNG));
    location.setName((String)data.get(KEY_NAME));
    location.setOsmId((String)data.get(KEY_OSM_ID));
    return location;
  }
}
