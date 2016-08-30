package de.macbury.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;

/**
 * Entity with this component will be followed by {@link de.macbury.graphics.GeoPerspectiveCamera}
 */
public class CameraComponent implements Component, Pool.Poolable {
  public float zoom;
  public float yaw;
  public float pitch;
  public float roll;

  @Override
  public void reset() {
    zoom = 10;
    yaw  = 0;
    pitch = 0;
    roll = 0;
  }
}
