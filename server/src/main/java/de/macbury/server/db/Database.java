package de.macbury.server.db;
import com.badlogic.gdx.utils.Disposable;
import com.rethinkdb.net.Connection;
import de.macbury.server.db.tables.LocationsTable;

import static com.rethinkdb.RethinkDB.r;
/**
 * Manage connection to rethinkdb, creating database, quering data, and connection pool
 */
public class Database implements Disposable {
  private static final String TABLE_POINTS_OF_INTERESTS = "point_of_interest";
  private final String name;
  private final String host;
  private final Integer port;
  private final Connection mainConnection;
  public ConnectionPool connections;
  public final LocationsTable locations;

  public Database(String host, Integer port, String name) {
    this.name = name;
    this.host = host;
    this.port = port;
    this.connections = new ConnectionPool(this);

    this.mainConnection = connections.obtain();
    create();

    this.locations = new LocationsTable(this);
  }

  /**
   * Create database
   */
  public void create() {
    if (!exists()) {
      r.dbCreate(name).run(mainConnection);
    }
  }

  /**
   * Check if database exists
   * @return
   */
  public boolean exists() {
    Connection connection = connections.obtain();
    boolean exists = r.dbList().contains(name).run(connection);
    connections.free(connection);
    return exists;
  }

  /**
   * Check if table exists in our database
   * @param tableName
   * @return
   */
  public boolean tableExists(String tableName) {
    Connection connection = connections.obtain();
    boolean exists = r.db(name).tableList().contains(tableName).run(connection);
    connections.free(connection);
    return exists;
  }

  /**
   * Create table in current database
   * @param tableName
   */
  public void createTable(String tableName) {
    Connection connection = connections.obtain();
    r.db(name).tableCreate(tableName).run(connection);
    connections.free(connection);
  }

  /**
   * Get name of database
   * @return
   */
  public String getName() {
    return name;
  }

  /**
   * Host used for connection
   * @return
   */
  public String getHost() {
    return host;
  }

  /**
   * Port for connection
   * @return
   */
  public Integer getPort() {
    return port;
  }

  /**
   * Drop database
   */
  public void drop() {
    if (exists()) {
      r.dbDrop(name).run(mainConnection);
    }
  }

  @Override
  public void dispose() {
    connections.clear();
    mainConnection.close();
  }
}
