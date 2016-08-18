package de.macbury.tests.geo;

import com.badlogic.gdx.Gdx;
import de.macbury.geo.GeoJSON;
import de.macbury.tests.GdxTestRunner;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class TestItShouldReadGeoJson {

  @Test
  public void itReadsGeoJsonForHome() {
    GeoJSON geoJSON = GeoJSON.parse(Gdx.files.internal("fixtures/geo_jsons/home.json").readString());

    Assert.assertEquals(GeoJSON.Type.FeatureCollection, geoJSON.getType());
    Assert.assertNotNull(geoJSON);
  }
}
