package de.macbury.tests.db;

import de.macbury.server.db.models.Location;
import de.macbury.tests.support.BaseTest;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 *
 */
public class TestLocation extends BaseTest {
  @Test
  public void itShouldAssignIdAfterCreate() {
    Location exampleLocation = new Location();
    exampleLocation.setId(100l);
    database.locations.create(exampleLocation);
    assertNotNull(exampleLocation.getId());
  }

  @Test
  public void itShouldReturnNullForNonExistingId() {
    Location location = database.locations.get(-1l);
    assertNull(location);
  }

  @Test
  public void itShouldRetriveSimilarObjectAfterCreate() {
    Location exampleLocation = new Location();
    exampleLocation.setName("Example location");
    exampleLocation.setLat(10.3);
    exampleLocation.setLng(-12.2);
    exampleLocation.setId(1111l);
    exampleLocation.setType("amenity");
    exampleLocation.setSubType("place_of_worship");
    exampleLocation.setTimestamp(new Date());

    database.locations.create(exampleLocation);
    assertNotNull(exampleLocation.getId());

    Location persistedLocation = database.locations.get(exampleLocation.getId());

    assertNotNull(persistedLocation);
    assertNotNull(persistedLocation.getId());

    assertEquals(exampleLocation.getLat(), persistedLocation.getLat(), 0.2);
    assertEquals(exampleLocation.getLng(), persistedLocation.getLng(), 0.2);
    assertEquals(exampleLocation.getId(), persistedLocation.getId());
    assertEquals(exampleLocation.getName(), persistedLocation.getName());
    assertEquals(exampleLocation.getId(), persistedLocation.getId());
    assertEquals(exampleLocation.getTimestamp(), persistedLocation.getTimestamp());

    assertEquals(exampleLocation.getType(), persistedLocation.getType());
    assertEquals(exampleLocation.getSubType(), persistedLocation.getSubType());

    assertTrue(persistedLocation.isValid());
  }
}
