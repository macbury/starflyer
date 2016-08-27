package de.macbury.tests.tiles;

import com.badlogic.gdx.utils.Array;
import de.macbury.tests.support.GdxTestRunner;
import de.macbury.tiles.TileInstance;
import de.macbury.tiles.TileInstanceComparator;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class TestTileInstanceComparator {

  @Test
  public void itSortsInstances() {
    Array<TileInstance> instances = new Array<TileInstance>();
    TileInstance farthest = new TileInstance(2,3);
    TileInstance nearest = new TileInstance(0,0);
    instances.add(farthest);
    instances.add(new TileInstance(0,0));
    instances.add(nearest);

    TileInstanceComparator comparator = new TileInstanceComparator();
    comparator.set(1,1);
    instances.sort(comparator);

    Assert.assertEquals(farthest, instances.get(0));
    Assert.assertEquals(nearest, instances.get(2));
  }
}
