package de.macbury.test.db;

import de.macbury.server.db.models.Location;
import de.macbury.test.TestWithDatabase;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 *
 */
public class TestLocation extends TestWithDatabase {
  @Test
  public void itShouldAssignIdAfterCreate() {
    Location exampleLocation = new Location();
    database.locations.create(exampleLocation);
    assertNotNull(exampleLocation.getId());
  }

  @Test
  public void itShouldRetriveSimilarObjectAfterCreate() {
    Location exampleLocation = new Location();
    exampleLocation.setName("Example location");
    exampleLocation.setLat(10.3);
    exampleLocation.setLng(-12.2);
    exampleLocation.setOsmId("random id");

    database.locations.create(exampleLocation);
    assertNotNull(exampleLocation.getId());

    Location persistedLocation = database.locations.get(exampleLocation.getId());
    assertNotNull(persistedLocation);

    assertEquals(exampleLocation.getLat(), persistedLocation.getLat(), 0.2);
    assertEquals(exampleLocation.getLng(), persistedLocation.getLng(), 0.2);
    assertEquals(exampleLocation.getOsmId(), persistedLocation.getOsmId());
    assertEquals(exampleLocation.getName(), persistedLocation.getName());
  }
}
