package de.macbury.tests.support;

import de.macbury.server.db.Database;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

/**
 * Base class that wraps tests for database
 */
@RunWith(GdxTestRunner.class)
public class TestWithDatabase {
  protected Database database;

  @Before
  public void setupDatabase() {
    this.database = new Database("127.0.0.1", 28015, "starflyer_test");
  }

  @After
  public void cleanUp() {
    database.drop();
    database.dispose();
    database = null;
  }
}
