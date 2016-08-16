package de.macbury.tests;

import de.macbury.server.db.Database;
import org.junit.After;
import org.junit.Before;

/**
 * Base class that wraps tests for database
 */
public class TestWithDatabase {
  protected Database database;

  @Before
  public void setupDatabase() {
    this.database = new Database("192.168.56.1", 28015, "starflyer_test");
  }

  @After
  public void cleanUp() {
    database.drop();
    database.dispose();
    database = null;
  }
}
