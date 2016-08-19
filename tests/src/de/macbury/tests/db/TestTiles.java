package de.macbury.tests.db;

import com.badlogic.gdx.Gdx;
import de.macbury.server.db.models.Tile;
import de.macbury.tests.geo.TestGeoJSON;
import de.macbury.tests.support.TestWithDatabase;
import junit.framework.Assert;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Created by macbury on 19.08.16.
 */
public class TestTiles extends TestWithDatabase {

  @Test
  public void itShouldSaveTile() {
    Tile originalTile = Tile.parse(Gdx.files.internal("fixtures/geo_jsons/home.json").readString());
    originalTile.setId("1/2");
    database.tiles.create(originalTile);

    Tile savedTile = database.tiles.get("1/2");

    TestGeoJSON.assertIsHome(savedTile);
    Assert.assertEquals("1/2", savedTile.getId());
  }
}
