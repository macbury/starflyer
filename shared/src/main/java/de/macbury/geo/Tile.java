package de.macbury.geo;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import de.macbury.MathExt;
import de.macbury.SharedConsts;
import de.macbury.geo.core.GeoPoint;

/**
 * Simple structure to contain information about tile position
 */
public class Tile {
  /**
   * TILE size in game world size
   */
  public static final float TILE_SIZE = 31;
  public static final  float MAX_ELEVATION = TILE_SIZE;

  /**
   * X goes from 0 (left edge is 180 °W) to 2zoom − 1 (right edge is 180 °E)
   */
  public int x;
  /**
   * Y goes from 0 (top edge is 85.0511 °N) to 2zoom − 1 (bottom edge is 85.0511 °S) in a Mercator projection
   */
  public int y;
  /**
   * The zoom parameter is an integer between 0 (zoomed out) and 18 (zoomed in). 18 is normally the maximum, but some tile servers might go beyond that.
   */
  public int zoom;

  public double north;
  public double south;
  public double east;
  public double west;

  /**
   * Bounding box in meters
   */
  public BoundingBox box = new BoundingBox();

  public Tile set(double lat, double lng) {
    return this.set(lat, lng, SharedConsts.DEFAULT_ZOOM);
  }

  public Tile set(GeoPoint point) {
    return this.set(point, SharedConsts.DEFAULT_ZOOM);
  }

  public Tile set(GeoPoint point, final int zoom) {
    return this.set(point.lat, point.lng, zoom);
  }

  private static double tile2lng(int tileX, int zoom) {
    return tileX / Math.pow(2.0, zoom) * 360.0 - 180;
  }

  private static double tile2lat(int tileY, int zoom) {
    double n = Math.PI - (2.0 * Math.PI * tileY) / Math.pow(2.0, zoom);
    return Math.toDegrees(Math.atan(Math.sinh(n)));
  }

  /**
   * Reproject the coordinates to the Mercator projection (from EPSG:4326 to EPSG:3857):
   * x = lon
   * y = arsinh(tan(lat)) = log[tan(lat) + sec(lat)]
   * (lat and lon are in radians)
   * Transform range of x and y to 0 – 1 and shift origin to top left corner:
   * x = [1 + (x / π)] / 2
   * y = [1 − (y / π)] / 2
   * Calculate the number of tiles across the map, n, using 2zoom
   * Multiply x and y by n. Round results down to give tilex and tiley.
   * @param lat
   * @param lng
   * @param zoom
   * @return
   */
  public Tile set(double lat, double lng, final int zoom) {
    this.zoom = zoom;
    this.x = (int)Math.floor( (lng + 180) / 360 * (1<<zoom) ) ;
    this.y = (int)Math.floor( (1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2 * (1<<zoom) ) ;
    if (x < 0)
      x=0;
    if (x >= (1<<zoom))
      x=((1<<zoom)-1);
    if (y < 0)
      y=0;
    if (y >= (1<<zoom))
      y=((1<<zoom)-1);
    calculateBoundingBox();
    return this;
  }

  /**
   * Set tile position and update bounding box
   * @param tileX
   * @param tileY
   * @return
   */
  public Tile setTilePosition(int tileX, int tileY) {
    return setTilePosition(tileX, tileY, SharedConsts.DEFAULT_ZOOM);
  }

  public Tile setTilePosition(int tileX, int tileY, int zoom) {
    this.x = tileX;
    this.y = tileY;
    this.zoom = zoom;
    calculateBoundingBox();
    return this;
  }

  private final Vector3 tempA = new Vector3();
  private final Vector3 tempB = new Vector3();

  private final GeoPoint startPoint = new GeoPoint(north, west);
  private final GeoPoint endPoint   = new GeoPoint(south, east);

  private void calculateBoundingBox() {
    north = tile2lat(y, zoom);
    south = tile2lat(y + 1, zoom);
    west  = tile2lng(x, zoom);
    east  = tile2lng(x + 1, zoom);

    startPoint.set(north, west);
    endPoint.set(south, east);

    MercatorProjection.project(startPoint, tempA);
    MercatorProjection.project(endPoint, tempB);

    tempB.z = MAX_ELEVATION;

    MathExt.round(tempA);
    MathExt.round(tempB);

    box.set(tempA, tempB);
  }

  @Override
  public String toString() {
    return "Tile{" +
            "x=" + x +
            ", y=" + y +
            ", zoom=" + zoom +
            ", north=" + north +
            ", south=" + south +
            ", east=" + east +
            ", west=" + west +
            '}';
  }

}
