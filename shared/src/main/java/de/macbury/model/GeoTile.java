package de.macbury.model;

import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

/**
 * This model define a map tile with cropped features like roads, land uses, lakes and buildings
 */
public class GeoTile {
  /**
   * Tile id in format x/y
   */
  private String id;
  /**
   * List of roads in geotile, Each {@link Road} have type
   */
  public final Array<Road> roads = new Array<Road>();

  /**
   * Tile id in format x/y
   * @return
   */
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  /**
   * Generate id using tile x and tile y
   * @param x
   * @param y
   */
  public void setId(int x, int y) {
    this.id = x+"/"+y;
  }

  /**
   * Create new road
   * @return
   */
  public Road road() {
    Road road = new Road();
    roads.add(road);
    return road;
  }
}
