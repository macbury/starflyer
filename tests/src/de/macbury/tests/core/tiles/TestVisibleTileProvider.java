package de.macbury.tests.core.tiles;

import com.badlogic.gdx.math.Vector3;
import de.macbury.graphics.GeoPerspectiveCamera;
import de.macbury.tests.support.GdxTestRunner;
import de.macbury.tiles.VisibleTileProvider;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class TestVisibleTileProvider {

  @Test
  public void itReturnsOnlyVisibleTiles() {
    GeoPerspectiveCamera camera = new GeoPerspectiveCamera();
    camera.position.set(0,0,2);
    camera.lookAt(new Vector3(1,1,0));
    camera.update();
    VisibleTileProvider visibleTileProvider = new VisibleTileProvider();

    visibleTileProvider.update(new Vector3(1,1,0), camera);

    Assert.assertTrue(visibleTileProvider.isVisible(0,0));
  }
}
