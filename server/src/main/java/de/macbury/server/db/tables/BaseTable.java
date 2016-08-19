package de.macbury.server.db.tables;

import com.badlogic.gdx.utils.ObjectMap;
import com.rethinkdb.gen.ast.Point;
import com.rethinkdb.gen.ast.Table;
import com.rethinkdb.model.MapObject;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;
import de.macbury.server.db.Database;
import de.macbury.server.db.models.BaseModel;

import java.util.ArrayList;
import java.util.HashMap;

import static com.rethinkdb.RethinkDB.r;

/**
 * Manage saving, finding, updating data in RethinkDB
 */
public abstract class BaseTable<SomeModel extends BaseModel, TypeOfId> {
  private static final String KEY_GENERATED_KEYS = "generated_keys";
  protected static final String KEY_ID = "id";
  private final String name;
  private final Database database;

  public BaseTable(String name, Database database) {
    this.name = name;
    this.database = database;
    setup();
  }


  /**
   * Base query with selecting database and current table
   * @return
   */
  protected Table query() {
    return r.db(database.getName()).table(name);
  }

  /**
   * Creates new model in database and assigns it id
   * @param model
   */
  public boolean create(SomeModel model) {
    Connection connection = database.connections.obtain();
    HashMap<String, Object> result = query().insert(serialize(model)).run(connection);
    database.connections.free(connection);

    if (result.containsKey(KEY_GENERATED_KEYS)) {
      ArrayList<String> keys = (ArrayList<String>) result.get(KEY_GENERATED_KEYS);
      model.setId(keys.get(0));
      return true;
    } else {
      return false;
    }
  }

  /**
   * Retrive model from db by its id
   * @param id
   * @return
   */
  public SomeModel get(TypeOfId id) {
    Connection connection = database.connections.obtain();
    try {
      HashMap<String, Object> data = query().get(id).run(connection);
      if (data == null) {
        return null;
      } else {
        SomeModel model = deserialize(data);
        if (model == null) {
          throw new RuntimeException("Deserialization returned null object: " + getClass().toString());
        }
        model.setId((TypeOfId)data.get(KEY_ID));
        return model;
      }
    } finally {
      database.connections.free(connection);
    }
  }

  /**
   * Serialize model into database {@link MapObject}
   * @param model
   * @return
   */
  public abstract MapObject serialize(SomeModel model);

  /**
   * Deserialize data from database into {@link SomeModel}
   * @param data from database
   * @return
   */
  public abstract SomeModel deserialize(HashMap data);

  /**
   * Setup table in database
   */
  private void setup() {
    if (!database.tableExists(name)) {
      database.createTable(name);
    }
  }

}
