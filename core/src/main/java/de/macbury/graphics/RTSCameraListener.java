package de.macbury.graphics;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public interface RTSCameraListener {
  public BoundingBox getCameraBounds(BoundingBox out);
  public float getCameraElevation(RTSCameraController cameraController, Vector3 cameraPosition);
}