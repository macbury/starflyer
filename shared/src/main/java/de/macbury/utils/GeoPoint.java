package de.macbury.utils;

/**
 * Encapsulates a geo coordinate. Allows chaining methods by returning a reference to itself
 */
public class GeoPoint {
  public double lat;
  public double lng;

  /**
   * @param lat Szerokość geograficzna
   * @param lng Długość geograficzna
   */
  public GeoPoint(double lat, double lng) {
    set(lat, lng);
  }

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

  @Override
  public boolean equals(Object obj) {
    if (GeoPoint.class.isInstance(obj)) {
      GeoPoint otherPoint = (GeoPoint)obj;
      return lat == otherPoint.lat && lng == otherPoint.lng;
    } else {
      return super.equals(obj);
    }
  }
}
