package de.macbury.tests.geo;

import com.badlogic.gdx.Gdx;
import de.macbury.geo.Feature;
import de.macbury.geo.FeatureCollection;
import de.macbury.geo.FeatureGeometry;
import de.macbury.geo.GeoJSON;
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

    Feature feature = collection.getFeatures().get(0);
    Assert.assertEquals("openstreetmap.org", feature.getPropSource());
    Assert.assertEquals(163221025, feature.getPropId());
    Assert.assertEquals("church", feature.getPropKind());
    Assert.assertEquals("place_of_worship", feature.getPropLandUse());

    FeatureGeometry geometry = feature.getGeometry();
    Assert.assertNotNull(geometry);
    Assert.assertEquals(17, geometry.getCoordinates().size());
  }
}
