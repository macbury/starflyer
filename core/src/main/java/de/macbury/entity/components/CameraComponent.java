package de.macbury.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;

/**
 * Entity with this component will be followed by {@link de.macbury.graphics.GeoPerspectiveCamera}
 */
public class CameraComponent implements Component, Pool.Poolable {
  public float tilt;
  public float rotation;
  public float zoom;

  @Override
  public void reset() {
    zoom = 10;
    rotation  = 0;
    tilt = 0;
  }
}
