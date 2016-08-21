package de.macbury.tests.geo;

import com.badlogic.gdx.Gdx;
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

    de.macbury.geo.core.Feature roadFeature = collection.get(0);
    assertEquals(de.macbury.geo.core.GeoJSON.Type.Feature, roadFeature.getType());

    de.macbury.geo.geometries.LineStringGeometry roadFeatureGeometry = (de.macbury.geo.geometries.LineStringGeometry)roadFeature.getGeometry();
    assertEquals(2, roadFeatureGeometry.size());

    de.macbury.geo.core.GeoPoint firstCoord = roadFeatureGeometry.get(0);
    Assert.assertEquals(50.0928666, firstCoord.lat);
    Assert.assertEquals(20.05896545, firstCoord.lng);

    de.macbury.geo.core.GeoPoint secondCoord = roadFeatureGeometry.get(0);
    Assert.assertEquals(50.09280552, secondCoord.lat);
    Assert.assertEquals(20.05899102, secondCoord.lng);
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
