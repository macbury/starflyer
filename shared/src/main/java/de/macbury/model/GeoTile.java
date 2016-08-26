package de.macbury.model;

import com.badlogic.gdx.utils.Array;
import de.macbury.geo.Tile;

import java.util.ArrayList;

/**
 * This model define a map tile with cropped features like roads, land uses, lakes and buildings
 */
public class GeoTile extends Tile {

  /**
   * List of roads in geotile, Each {@link Road} have type
   */
  public final Array<Road> roads = new Array<Road>();

  /**
   * Tile id in format x/y
   * @return
   */
  public String getId() {
    return x+"/"+y;
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

  @Override
  public String toString() {
    return "GeoTile{" +
            "id='" + getId() + '\'' +
            ", roads=" + roads +
            '}';
  }

  public void setId(int x, int y) {
    setTilePosition(x,y);
  }
}
