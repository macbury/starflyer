package de.macbury.test.osm;


import de.macbury.server.db.models.Location;
import de.macbury.server.osm.OSMNode;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TestOSMNode {

  @Test
  public void itShouldCreateProperLocation() {
    OSMNode osmNode = new OSMNode();
    osmNode.id = 902;
    osmNode.lat = 10;
    osmNode.lon = -2;
    osmNode.timestamp = new Date();
    osmNode.tags.put("amenity", "place_of_worship");
    osmNode.setName("Helo");
    Location location = osmNode.toLocation();

    assertEquals(osmNode.lat, location.getLat(), 0.2);
    assertEquals(osmNode.lon, location.getLng(), 0.2);

    assertEquals(osmNode.id, location.getOsmId());
    assertEquals(osmNode.timestamp, location.getTimestamp());
    assertEquals(osmNode.getName(), location.getName());
    assertEquals(osmNode.getType(), location.getType());
    assertEquals(osmNode.getSubType(), location.getSubType());
    assertTrue(location.isValid());
  }
}
