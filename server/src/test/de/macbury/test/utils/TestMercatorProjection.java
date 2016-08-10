package de.macbury.test.utils;

import com.badlogic.gdx.math.Vector2;
import de.macbury.utils.GeoPoint;
import de.macbury.utils.MercatorProjection;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

public class TestMercatorProjection {

  @Test
  public void projectsAtCenter() {
    GeoPoint point = new GeoPoint();
    Vector2 result = new Vector2();

    MercatorProjection.project(point, result);
    assertEquals(0.0, result.x, 0);
    assertEquals(0.0, result.y, 0);
  }

  @Test
  public void projectsTheNortheastCornerOfTheWorld() {
    GeoPoint point = new GeoPoint(85.0511287798, 180);
    Vector2 result = new Vector2();

    MercatorProjection.project(point, result);
    assertEquals(20037508.0, result.x, 0);
    assertEquals(20037508.0, result.y, 0);
  }

  @Test
  public void projectsTheSouthwestCornerOfTheWorld() {
    GeoPoint point = new GeoPoint(-85.0511287798, -180);
    Vector2 result = new Vector2();

    MercatorProjection.project(point, result);
    assertEquals(-20037508.0, result.x, 0);
    assertEquals(-20037508.0, result.y, 0);
  }

  @Test
  public void projectsOtherPoints() {
    GeoPoint point = new GeoPoint(50, 30);
    Vector2 result = new Vector2();

    MercatorProjection.project(point, result);
    assertEquals(3339584.0, result.x, 1);
    assertEquals(6446275.0, result.y, 1);
  }
}
