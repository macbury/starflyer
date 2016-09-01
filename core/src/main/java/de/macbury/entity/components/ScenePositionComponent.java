package de.macbury.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;

/**
 * WorldPosition that takes to account position of camera offset.
 * We move the whole world not the camera. So we take camera offset position and substract it from {@link PositionComponent} to get {@link ScenePositionComponent}.
 * Use this position is relative to camera. This component is updated using {@link SceneOffsetSystem}
 */
public class ScenePositionComponent extends Vector3 implements Pool.Poolable, Component {
  @Override
  public void reset() {
    setZero();
  }
}
