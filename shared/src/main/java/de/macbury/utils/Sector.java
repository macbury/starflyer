package de.macbury.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

/**
 * Represents sector in world.
 */
public class Sector {
  public final int SIZE = 500; // size in meters
  /**
   * Tile position
   */
  public int x;
  /**
   * Tile position
   */
  public int y;
  public BoundingBox box = new BoundingBox();
  private Vector3 tempA = new Vector3();
  private Vector3 tempB = new Vector3();
  private Vector2 tempC = new Vector2();

  /**
   * Calculate on which tile is passed point
   * @param point
   * @return
   */
  public Sector from(GeoPoint point) {
    // tempA minimum point
    // tempB maxiumum point
    MercatorProjection.project(point, tempC);
    x = MathUtils.floor(tempC.x / SIZE);
    y = MathUtils.floor(tempC.y / SIZE);

    tempA.set(x,y, 0);
    tempB.set(tempA).add(SIZE, SIZE, 1);
    box.set(tempA, tempB);
    return this;
  }

  /**
   * Game world position
   * @return vector with position in meters
   */
  public Vector3 getPosition() {
    return box.getMin(tempA);
  }
}
