package de.macbury.test.utils;

import com.badlogic.gdx.math.Vector2;
import de.macbury.utils.GeoPoint;
import de.macbury.utils.MercatorProjection;

import static org.junit.Assert.*;

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

  @Test
  public void unprojectsACenterPoint() {
    Vector2 vector = new Vector2();
    GeoPoint result = new GeoPoint();

    MercatorProjection.unproject(vector, result);
    assertEquals(0.0, result.lat, 0);
    assertEquals(0.0, result.lng, 0);
  }

  public GeoPoint pr(GeoPoint point) {
    Vector2 tempVec = new Vector2();
    GeoPoint newResult = new GeoPoint();
    MercatorProjection.project(point,tempVec);
    MercatorProjection.unproject(tempVec, newResult);
    return newResult;
  }

  @Test
  public void unprojectsPiPoints() {
    GeoPoint pA = new GeoPoint(-Math.PI, Math.PI);
    GeoPoint rA = pr(pA);

    assertEquals(pA.lng, rA.lng, GeoPoint.TOLERANCE);
    assertEquals(pA.lat, rA.lat, GeoPoint.TOLERANCE);

    assertTrue(pA.equals(rA));
  }

  @Test
  public void unprojectsCenterPoints() {
    GeoPoint pA = new GeoPoint(0, 0);
    GeoPoint rA = pr(pA);

    assertEquals(pA.lng, rA.lng, GeoPoint.TOLERANCE);
    assertEquals(pA.lat, rA.lat, GeoPoint.TOLERANCE);

    assertTrue(pA.equals(rA));
  }

  @Test
  public void unprojectsOtherPoints() {
    GeoPoint pA = new GeoPoint(0.523598775598, 1.010683188683);
    GeoPoint rA = pr(pA);

    assertEquals(pA.lng, rA.lng, GeoPoint.TOLERANCE);
    assertEquals(pA.lat, rA.lat, GeoPoint.TOLERANCE);

    assertTrue(pA.equals(rA));
  }

  @Test
  public void unprojectsCracowPoints() {
    GeoPoint pA = new GeoPoint(50.062082, 19.939232);
    GeoPoint rA = pr(pA);

    assertEquals(pA.lng, rA.lng, GeoPoint.TOLERANCE);
    assertEquals(pA.lat, rA.lat, GeoPoint.TOLERANCE);

    assertTrue(pA.equals(rA));
  }
}
