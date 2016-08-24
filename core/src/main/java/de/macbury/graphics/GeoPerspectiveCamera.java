package de.macbury.graphics;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import de.macbury.geo.MercatorProjection;
import de.macbury.geo.core.GeoPoint;

/**
 * Camera with helper functions to set current position using {@link de.macbury.geo.core.GeoPoint}
 */
public class GeoPerspectiveCamera extends PerspectiveCamera {

  public GeoPerspectiveCamera(float fieldOfViewY, float viewportWidth, float viewportHeight) {
    super(fieldOfViewY, viewportWidth, viewportHeight);
    this.up.set(0, 0, 1);
    this.direction.set(0,1,0);
    far = 300;
    near = 1f;
    this.update();
  }

  /**
   * Update game world X and Y component to match passed point
   * @param point
   * @return
   */
  public GeoPerspectiveCamera setGeoPosition(GeoPoint point) {
    MercatorProjection.project(point, this.position);
    return this;
  }
}
