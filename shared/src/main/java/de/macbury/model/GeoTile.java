package de.macbury.model;

/**
 * This model define a map tile with cropped features like roads, land uses, lakes and buildings
 */
public class GeoTile {
  /**
   * Tile id in format x/y
   */
  private String id;

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
}
