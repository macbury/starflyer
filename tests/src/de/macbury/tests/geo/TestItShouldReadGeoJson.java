package de.macbury.tests.geo;

import com.badlogic.gdx.Gdx;
import de.macbury.geo.*;
import de.macbury.tests.GdxTestRunner;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class TestItShouldReadGeoJson {

  @Test
  public void itReadsGeoJsonForHome() {
    FeatureCollection collection = FeatureCollection.parse(Gdx.files.internal("fixtures/geo_jsons/home.json").readString());

    Assert.assertEquals(GeoJSON.Type.FeatureCollection, collection.getType());
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
    Assert.assertEquals(20.05994134, pointCoord.lng);
  }
}
