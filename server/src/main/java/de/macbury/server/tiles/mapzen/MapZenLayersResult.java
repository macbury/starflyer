package de.macbury.server.tiles.mapzen;


import com.google.gson.annotations.Expose;
import de.macbury.geo.core.Feature;
import de.macbury.geo.core.FeatureCollection;
import de.macbury.geo.core.GeoPath;
import de.macbury.geo.geometries.MultiLineStringGeometry;
import de.macbury.model.GeoTile;
import de.macbury.model.GeoTileFeatureType;
import de.macbury.model.Road;

/**
 * The Mapzen vector tile service provides worldwide basemap coverage sourced from OpenStreetMap and other open data projects, updated daily as a free & shared service.
 * Data is organized into several thematic layers, each of which is named, for example; buildings, pois, and water. A selection of these layers are typically used for base map rendering, and are provided under the short-hand name all. Each layer includes a simplified view of OpenStreetMap data for easier consumption, with common tags often condensed into a single kind field as noted below.
 * Need help displaying vector tiles in a map? We have several examples using Mapzen vector tiles to style in your favorite graphics library including Tangram, Mapbox GL, D3, and Hoverboard.
 */
public class MapZenLayersResult {
  //@Expose
  //private FeatureCollection water;
  @Expose
  private FeatureCollection roads;
  //@Expose
  //private FeatureCollection buildings;
  //@Expose
  //private FeatureCollection landuse;

  //public FeatureCollection getWater() {
   // return water;
  //}

  public FeatureCollection getRoads() {
    return roads;
  }
/*
  //public FeatureCollection getBuildings() {
    return buildings;
  }

  //public FeatureCollection getLanduse() {
   // return landuse;
  //}*/

  /**
   * Extract informations like roads, etc and format them into geotile
   * @return
   */
  public GeoTile toGeoTile() {
    GeoTile geoTile = new GeoTile();

    for (Feature feature: roads) {
      GeoTileFeatureType type = GeoTileFeatureType.fromKind(feature.getPropKind());
      switch (type) {
        case Highway:
        case MajorRoad:
        case MinorRoad:
        case Path:
          Road road = geoTile.road();
          road.setType(type);

          MultiLineStringGeometry geometry = (MultiLineStringGeometry) feature.getGeometry();

          for (GeoPath path: geometry.all()) {
            road.add(path);
          }

          break;
      }
    }

    return geoTile;
  }
}
