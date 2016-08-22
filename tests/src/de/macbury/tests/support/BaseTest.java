package de.macbury.tests.support;

import de.macbury.server.db.Database;
import de.macbury.server.tiles.mapzen.MapZenApi;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

/**
 * Base class that wraps tests for database and http
 */
public class BaseTest {
  protected Database database;

  @Before
  public void setupDatabase() {
    this.database = new Database("127.0.0.1", 28015, "starflyer_test");
    MapZenApi.setApiKey("test-key");
    
  }

  @After
  public void cleanUp() {
    database.drop();
    database.dispose();
    database = null;
  }
}
