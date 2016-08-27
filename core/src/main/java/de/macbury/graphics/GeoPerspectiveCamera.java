package de.macbury.graphics;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import de.macbury.geo.MercatorProjection;
import de.macbury.geo.core.GeoPoint;

/**
 * Camera with helper functions to set current position using {@link de.macbury.geo.core.GeoPoint}. Additional it calculate what tile ids are visible on screen
 */
public class GeoPerspectiveCamera extends PerspectiveCamera {
  /**
   * Current geo position
   */
  public final GeoPoint geoPosition = new GeoPoint();

  //<editor-fold desc="Debug Information" />
  private final Vector3 debugDirection = new Vector3();
  private final Vector3 debugPosition = new Vector3();
  private final GeoPoint debugGeoPoint = new GeoPoint();
  private DebugFrustum debugFrustrum;
  //</editor-fold>

  public GeoPerspectiveCamera(float fieldOfViewY, float viewportWidth, float viewportHeight) {
    super(fieldOfViewY, viewportWidth, viewportHeight);
    this.up.set(0, 0, 1);
    this.direction.set(0,1,0);
    far = 150;
    near = 1f;

    this.update();
  }

  //<editor-fold desc="Debug helpers" />
  /**
   * Save current {@link com.badlogic.gdx.math.Frustum} direction and position
   */
  public void saveDebugFrustum() {
    this.update();
    this.debugFrustrum = new DebugFrustum(frustum, invProjectionView);
    this.debugDirection.set(direction);
    this.debugGeoPoint.set(geoPosition);
    debugPosition.set(position);
  }

  public void restoreFrustum() {
    debugFrustrum = null;
  }

  /**
   * Have camera saved debug frustrum
   * @return
   */
  public boolean haveDebugFrustrum() {
    return debugFrustrum != null;
  }

  /**
   * Return debug frustum if have or return normal frustrum
   * @return
   */
  public Frustum normalOrDebugFrustrum() {
    if (haveDebugFrustrum()) {
      return debugFrustrum;
    } else {
      return frustum;
    }
  }

  public Vector3 normalOrDebugDirection() {
    return haveDebugFrustrum() ? debugDirection : direction;
  }

  public Vector3 normalOrDebugPosition() {
    return haveDebugFrustrum() ? debugPosition : position;
  }

  public GeoPoint normalOrDebugGeoPoint() {
    return haveDebugFrustrum() ? debugGeoPoint : geoPosition;
  }

  //</editor-fold>

  /**
   * Check if bounding box is in current frustrum or debug frustrum. If user
   * @return true
   */
  public boolean boundsInFrustum(BoundingBox testBounds) {
    return normalOrDebugFrustrum().boundsInFrustum(testBounds);
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
