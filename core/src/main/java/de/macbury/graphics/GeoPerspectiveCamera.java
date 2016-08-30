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
  private static final float BASE_FOV = 67;
  private static final float EXTEND_FOV_BY = 19;
  /**
   * Current geo position
   */
  public final GeoPoint geoPosition = new GeoPoint();

  //<editor-fold desc="Debug Information" />
  private final Vector3 debugDirection = new Vector3();
  private final Vector3 debugPosition = new Vector3();
  private final GeoPoint debugGeoPoint = new GeoPoint();
  private DebugFrustum debugFrustrum;
  private float oldFieldOfView;
  //</editor-fold>

  public GeoPerspectiveCamera(float viewportWidth, float viewportHeight) {
    super(BASE_FOV, viewportWidth, viewportHeight);
    this.up.set(0, 0, 1);
    this.direction.set(0,1,0);
    far = 150;
    near = 0.1f;

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

  public void extendFov() {
    this.oldFieldOfView = this.fieldOfView;
    this.fieldOfView    += EXTEND_FOV_BY;
    this.update();
  }

  public void restoreFov() {
    this.fieldOfView = this.oldFieldOfView;
    this.update();
  }

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

  /**
   * Update viewport
   * @param width
   * @param height
   */
  public void resize(int width, int height) {
    viewportWidth = width;
    viewportHeight = height;
    update(true);
  }
}
