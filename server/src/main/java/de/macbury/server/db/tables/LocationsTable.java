package de.macbury.server.db.tables;

import de.macbury.server.db.Database;
import de.macbury.server.db.models.Location;

/**
 * Table with locations in world
 */
public class LocationsTable extends BaseTable<Location> {
  public LocationsTable(Database database) {
    super("locations", database);
  }
}
