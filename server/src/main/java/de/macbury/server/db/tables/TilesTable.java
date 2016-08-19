package de.macbury.server.db.tables;

import com.rethinkdb.model.MapObject;
import de.macbury.json.JsonHelper;
import de.macbury.server.db.Database;
import de.macbury.server.db.models.Tile;

import java.util.HashMap;

import static com.rethinkdb.RethinkDB.r;


public class TilesTable extends BaseTable<Tile, String> {
  public TilesTable(Database database) {
    super("tiles", database);
  }

  @Override
  public MapObject serialize(Tile model) {
    MapObject root = JsonHelper.fromJson(JsonHelper.toJson(model), MapObject.class);
    root.with(KEY_ID, model.getId());
    return root;
  }

  @Override
  public Tile deserialize(HashMap data) {
    Tile tile = JsonHelper.fromJson(JsonHelper.toJson(data), Tile.class);
    return tile;
  }
}
