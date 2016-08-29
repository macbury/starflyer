package de.macbury.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;

/**
 *
 */
public class PositionComponent extends Vector3 implements Component, Pool.Poolable {
  public boolean visible;
  @Override
  public void reset() {
    visible = false;
    setZero();
  }

  public boolean isVisible() {
    return visible;
  }
}
