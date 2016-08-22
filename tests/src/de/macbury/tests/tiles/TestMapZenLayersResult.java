package de.macbury.tests.tiles;

import com.badlogic.gdx.Gdx;
import de.macbury.json.JsonHelper;
import de.macbury.model.GeoTile;
import de.macbury.server.tiles.mapzen.MapZenLayersResult;
import de.macbury.tests.support.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class TestMapZenLayersResult {
  private String fixture(String name) {
    return Gdx.files.internal("fixtures/mapzen_layers/"+name+".json").readString();
  }

  @Test
  public void itShouldTransformMapZenLayersIntoGeoTile() {
    MapZenLayersResult result = JsonHelper.fromJson(fixture("home"), MapZenLayersResult.class);
    GeoTile tile = result.toGeoTile();

    assertNull(tile.getId());
  }
}
