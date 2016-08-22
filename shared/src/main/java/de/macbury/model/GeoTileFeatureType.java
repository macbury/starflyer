package de.macbury.model;

/**
 * Feature for {@link GeoTile}
 */
public enum GeoTileFeatureType {
  Highway("highway"), MajorRoad("major_road"), MinorRoad("minor_road"), Path("path"), Unsupported("-1");

  private final String kind;

  GeoTileFeatureType(String originalName) {
    this.kind = originalName;
  }

  public boolean is(String name) {
    return kind.equals(name);
  }

  /**
   * Transform https://mapzen.com/documentation/vector-tiles/layers/#road-properties-common-optional into supported {@link Type}
   * @param name
   * @return
   */
  public static GeoTileFeatureType fromKind(String name) {
    for (GeoTileFeatureType type : values()) {
      if (type.is(name))
        return type;
    }

    return Unsupported;
  }
}
