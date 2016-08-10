package de.macbury.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Based on https://github.com/Leaflet/Leaflet/blob/9560b28515586aa238218f1b29c1b68d4e09505d/src/geo/projection/Projection.SphericalMercator.js
 */
public class MercatorProjection {
  private static final double R = 6378137.0;
  private static final double MAX_LATITUDE = 85.0511287798;
  private static final double D = Math.PI / 180.0;

  /**
   * Project {@link GeoPoint} into world position
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
   * Transform world {@link Vector2} into {@link GeoPoint}
   * @param inWorld in meters
   * @param outLatLng
   */
  public static void unproject(Vector2 inWorld, GeoPoint outLatLng) {
    /*outLatLng.set(
            yToLat(inWorld.y),
            xToLon(inWorld.x)
    );*/
  }

}
