package de.macbury.geo;

/**
 * Encapsulates a geo coordinate. Allows chaining methods by returning a reference to itself
 */
public class GeoPoint {
  public double lat;
  public double lng;
  public final static double TOLERANCE = 0.00001;
  /**
   * @param lat Szerokość geograficzna
   * @param lng Długość geograficzna
   */
  public GeoPoint(double lat, double lng) {
    set(lat, lng);
  }

  /**
   * Point at center of earth
   */
  public GeoPoint() {
    set(0,0);
  }

  /**
   * @param lat Szerokość geograficzna
   * @param lng Długość geograficzna
   */
  public GeoPoint set(double lat, double lng) {
    this.lat = lat;
    this.lng = lng;
    return this;
  }

  /**
   * Copy values from geo point
   * @param otherGeoPoint
   * @return
   */
  public GeoPoint set(GeoPoint otherGeoPoint) {
    this.lat = otherGeoPoint.lat;
    this.lng = otherGeoPoint.lng;
    return this;
  }

  @Override
  public boolean equals(Object obj) {
    if (GeoPoint.class.isInstance(obj)) {
      return epsilonEquals((GeoPoint)obj, TOLERANCE);
    } else {
      return super.equals(obj);
    }
  }

  /** Compares this point with the other point, using the supplied epsilon for fuzzy equality testing.
   * @return whether the points are the same. */
  public boolean epsilonEquals(double lat, double lng, double epsilon) {
    if (Math.abs(lat - this.lat) > epsilon) return false;
    if (Math.abs(lng - this.lng) > epsilon) return false;
    return true;
  }

  /** Compares this point with the other point, using the supplied epsilon for fuzzy equality testing.
   * @return whether the points are the same. */
  public boolean epsilonEquals(GeoPoint otherGeoPoint, double epsilon) {
    return epsilonEquals(otherGeoPoint.lat, otherGeoPoint.lng, epsilon);
  }

  public GeoPoint abs() {
    lat = Math.abs(lat);
    lng = Math.abs(lng);
    return this;
  }

  /** Substracts the other {@link GeoPoint} from this.
   * @return This point for chaining */
  public GeoPoint sub(GeoPoint otherGeoPoint) {
    lat -= otherGeoPoint.lat;
    lng -= otherGeoPoint.lng;
    return this;
  }

  @Override
  public String toString() {
    return "GeoPoint{" +
            "lat=" + lat +
            ", lng=" + lng +
            '}';
  }
}
