package de.macbury.server.db;

import com.badlogic.gdx.utils.Pool;
import com.rethinkdb.net.Connection;
import static com.rethinkdb.RethinkDB.r;

/**
 * Pool with connections to RethinkDB
 */
public class ConnectionPool extends Pool<Connection> {
  private final Database db;

  public ConnectionPool(Database db) {
    this.db = db;
  }

  @Override
  protected Connection newObject() {
    Connection connection = r.connection().hostname(db.getHost()).port(db.getPort()).connect();
    return connection;
  }
}
