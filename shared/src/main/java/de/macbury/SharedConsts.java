package de.macbury;


public class SharedConsts {
  /**
   * Port on which server listen
   */
  public static final int PORT = 56837;

  /**
   * How many meter fit in each world unit
   */
  public static final float PROJECTION_SCALE = 5f;
  public static final float METER_TO_UNIT = 1f / PROJECTION_SCALE;
  public static final float UNIT_TO_METER = PROJECTION_SCALE;

  /**
   * Default tile zoom level
   */
  public final static int DEFAULT_ZOOM = 16;

}
