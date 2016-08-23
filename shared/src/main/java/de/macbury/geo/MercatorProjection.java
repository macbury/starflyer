package de.macbury.geo;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.macbury.geo.core.GeoPoint;

/**
 * Based on https://github.com/Leaflet/Leaflet/blob/9560b28515586aa238218f1b29c1b68d4e09505d/src/geo/projection/Projection.SphericalMercator.js
 */
public class MercatorProjection {
  private static final double R = 6378137.0;
  private static final double MAX_LATITUDE = 85.0511287798;
  private static final double D = Math.PI / 180.0;
  private static final double D2 = 180.0 / Math.PI;
  /**
   * Project {@link de.macbury.geo.core.GeoPoint} into world position
   * @param inLatLng
   * @param outWorld in meters
   */
  public static void project(GeoPoint inLatLng, Vector2 outWorld) {
    double lat = Math.max(Math.min(MAX_LATITUDE, inLatLng.lat), -MAX_LATITUDE);
    double sin = Math.sin(lat * D);
    double x   = R * inLatLng.lng * D;
    double y   = R * Math.log( (1.0 + sin) / (1.0 - sin))  / 2.0;
    outWorld.set((float)x,(float)y);
  }

  /**
   * Project {@link de.macbury.geo.core.GeoPoint} into world position
   * @param inLatLng
   * @param outWorld in meters
   */
  public static void project(GeoPoint inLatLng, Vector3 outWorld) {
    double lat = Math.max(Math.min(MAX_LATITUDE, inLatLng.lat), -MAX_LATITUDE);
    double sin = Math.sin(lat * D);
    double x   = R * inLatLng.lng * D;
    double y   = R * Math.log( (1.0 + sin) / (1.0 - sin))  / 2.0;
    outWorld.set((float)x, 0, (float)y);
  }

  /**
   * Transform world {@link Vector2} into {@link de.macbury.geo.core.GeoPoint}
   * @param inWorld in meters
   * @param outLatLng
   */
  public static void unproject(Vector2 inWorld, de.macbury.geo.core.GeoPoint outLatLng) {
    double lat = (2.0 * Math.atan(Math.exp(inWorld.y / R)) - (Math.PI / 2.0)) * D2;
    double lng = (inWorld.x * D2 / R);
    outLatLng.set(lat, lng);
  }

}
