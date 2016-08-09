package de.macbury.server.db;
import com.rethinkdb.net.Connection;

import static com.rethinkdb.RethinkDB.r;
/**
 * Manage connection to rethinkdb, creating database, quering data, and connection pool
 */
public class Database {
  private final String name;
  private final String host;
  private final Integer port;
  private final Connection mainConnection;

  public Database(String host, Integer port, String name) {
    this.name = name;
    this.host = host;
    this.port = port;

    this.mainConnection = r.connection().hostname(host).port(port).connect();
    create();
  }

  /**
   * Create database
   */
  public void create() {
    boolean dbExists = r.dbList().contains(name).run(mainConnection);

    if (!dbExists) {
      r.dbCreate(name).run(mainConnection);
    }
  }
}
