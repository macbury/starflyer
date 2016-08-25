package de.macbury.graphics;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import de.macbury.geo.MercatorProjection;
import de.macbury.geo.Tile;
import de.macbury.geo.core.GeoPoint;

/**
 * Camera with helper functions to set current position using {@link de.macbury.geo.core.GeoPoint}. Additional it calculate what tile ids are visible on screen
 */
public class GeoPerspectiveCamera extends PerspectiveCamera {
  /**
   * Current geo position
   */
  public final GeoPoint geoPosition = new GeoPoint();

  public GeoPerspectiveCamera(float fieldOfViewY, float viewportWidth, float viewportHeight) {
    super(fieldOfViewY, viewportWidth, viewportHeight);
    this.up.set(0, 0, 1);
    this.direction.set(0,1,0);
    far = 100;
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
    update(true);
    return this;
  }

  @Override
  public void update(boolean updateFrustum) {
    super.update(updateFrustum);
    if (geoPosition != null)
      MercatorProjection.unproject(position, geoPosition);
  }
}
