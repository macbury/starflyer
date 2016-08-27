package de.macbury.tiles;

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.Comparator;

/**
 * Sort from farthest to nearest {@link TileInstance}
 */
public class TileInstanceComparator implements Comparator<TileInstance> {
  public int tileX;
  public int tileY;
  @Override
  public int compare(TileInstance o1, TileInstance o2) {
    final float dst = (int)(1000f * o1.dst(tileX, tileY)) - (int)(1000f * o2.dst(tileX, tileY));
    final int result = dst < 0 ? -1 : (dst > 0 ? 1 : 0);
    return -result;
  }

  public void set(int x, int y) {
    this.tileX = x;
    this.tileY = y;
  }
}
