package de.macbury.tests.geo;

import com.badlogic.gdx.Gdx;
import de.macbury.geo.core.Feature;
import de.macbury.geo.core.GeoJSON;
import de.macbury.geo.core.GeoPath;
import de.macbury.geo.core.GeoPoint;
import de.macbury.geo.geometries.FeatureGeometry;
import de.macbury.geo.geometries.MultiLineStringGeometry;
import de.macbury.tests.support.GdxTestRunner;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(GdxTestRunner.class)
public class TestGeoJSON {

  private String fixture(String name) {
    return Gdx.files.internal("fixtures/geo_jsons/"+name+".json").readString();
  }

  @Test
  public void itDeserializesAllRoadsForHome() {
    de.macbury.geo.core.FeatureCollection collection = de.macbury.geo.core.GeoJSON.parse(fixture("home_roads"), de.macbury.geo.core.FeatureCollection.class);
    assertEquals(40, collection.size());

    Feature roadFeature = collection.get(0);
    assertEquals(GeoJSON.Type.Feature, roadFeature.getType());
    MultiLineStringGeometry roadFeatureGeometry = (MultiLineStringGeometry)roadFeature.getGeometry();
    assertEquals(1, roadFeatureGeometry.size());
    assertEquals(GeoJSON.Type.LineString, roadFeatureGeometry.getType());

    GeoPath roadPath       = roadFeatureGeometry.get(0);
    assertEquals(2, roadPath.size());

    GeoPoint firstCoord = roadPath.get(0);
    Assert.assertEquals(50.0928666, firstCoord.lat);
    Assert.assertEquals(20.05896545, firstCoord.lng);

    GeoPoint secondCoord = roadPath.get(1);
    Assert.assertEquals(50.09280552, secondCoord.lat);
    Assert.assertEquals(20.05899102, secondCoord.lng);

    Feature multiPathRoadFeature = collection.get(31);
    MultiLineStringGeometry multiPathGeometry = (MultiLineStringGeometry)multiPathRoadFeature.getGeometry();

    assertEquals(GeoJSON.Type.MultiLineString, multiPathGeometry.getType());
    assertEquals(2, multiPathGeometry.size());

    GeoPath secondRoad = multiPathGeometry.get(1);

    firstCoord = secondRoad.get(0);
    Assert.assertEquals(50.09287517, firstCoord.lat);
    Assert.assertEquals(20.05962449, firstCoord.lng);

    secondCoord = secondRoad.get(1);
    Assert.assertEquals(50.0928764, secondCoord.lat);
    Assert.assertEquals(20.05971887, secondCoord.lng);
  }

/*
  @Test
  public void itSerializeGeoJsonHome() {
    FeatureCollection collection = FeatureCollection.parse(Gdx.files.internal("fixtures/geo_jsons/home.json").readString());

    String json = JsonHelper.toJson(collection);
    FeatureCollection deserializedJson = FeatureCollection.parse(json);

    assertIsHome(deserializedJson);
  }

  @Test
  public void itReadsGeoJsonForHome() {
    FeatureCollection collection = FeatureCollection.parse(Gdx.files.internal("fixtures/geo_jsons/home.json").readString());
    assertIsHome(collection);
  }*/

  public static void assertIsHome(de.macbury.geo.core.FeatureCollection collection) {
    /*Assert.assertEquals(GeoJSON.Type.FeatureCollection, collection.getType());
    Assert.assertNotNull(collection);
    Assert.assertEquals(17, collection.getFeatures().size());

    Feature polygonFeature = collection.getFeatures().get(0);
    Assert.assertEquals(GeoJSON.Type.Feature, polygonFeature.getType());
    Assert.assertEquals("openstreetmap.org", polygonFeature.getPropSource());
    Assert.assertEquals(163221025, polygonFeature.getPropId());
    Assert.assertEquals("church", polygonFeature.getPropKind());
    Assert.assertEquals("place_of_worship", polygonFeature.getPropLandUse());

    FeatureGeometry geometry = polygonFeature.getGeometry();
    Assert.assertNotNull(geometry);
    Assert.assertEquals(GeoJSON.Type.Polygon, geometry.getType());
    Assert.assertEquals(179, geometry.getCoordinates().size());

    GeoPoint firstCoord = geometry.getCoordinates().get(0);
    Assert.assertEquals(50.0925789, firstCoord.lat);
    Assert.assertEquals(20.057813, firstCoord.lng);

    Feature pointFeature = collection.getFeatures().get(11);

    geometry = pointFeature.getGeometry();
    Assert.assertEquals(GeoJSON.Type.Point, geometry.getType());
    Assert.assertEquals(1, geometry.getCoordinates().size());

    GeoPoint pointCoord = geometry.getCoordinates().get(0);

    Assert.assertEquals(50.09398094, pointCoord.lat);
    Assert.assertEquals(20.05994134, pointCoord.lng);*/
  }
}
