package de.macbury.tests.utils;

import de.macbury.geo.core.GeoPoint;
import de.macbury.utils.Sector;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestSector {
  @Test
  public void itShouldCreateSectorForCenter() {
    Sector sector = new Sector();
    sector.from(new GeoPoint());

    assertEquals(0, sector.x);
    assertEquals(0, sector.y);
  }

  @Test
  public void itShouldCreateSectorForNearCenter() {
    Sector sector = new Sector();
    sector.from(new GeoPoint(1,0));

    assertEquals(222, sector.y);
    assertEquals(0, sector.x);
  }

  @Test
  public void itShouldCreateSectorForNegativeNearCenter() {
    Sector sector = new Sector();
    sector.from(new GeoPoint(-1,-1));

    assertEquals(-223, sector.y);
    assertEquals(-223, sector.x);
  }

  @Test
  public void calculateTheNortheastCornerOfTheWorld() {
    Sector sector = new Sector();
    GeoPoint point = new GeoPoint(85.0511287798, 180);
    sector.from(point);

    assertEquals(40075, sector.y);
    assertEquals(40075, sector.x);
  }
}
