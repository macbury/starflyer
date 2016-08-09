package de.macbury.server.db.tables;

import de.macbury.server.db.Database;

/**
 * Manage saving, finding, updating data in RethinkDB
 */
public abstract class BaseTable<SomeModel> {
  private final String name;
  private final Database database;

  public BaseTable(String name, Database database) {
    this.name = name;
    this.database = database;
    setup();
  }

  /**
   * Creates new model in database and assigns it id
   * @param model
   */
  public void create(SomeModel model) {

  }

  /**
   * Setup table in database
   */
  private void setup() {
    if (!database.tableExists(name)) {
      database.createTable(name);
    }
  }


}
