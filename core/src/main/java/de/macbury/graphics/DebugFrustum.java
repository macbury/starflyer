package de.macbury.graphics;

import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;

/**
 * {@link Frustum} with ability to copy itself
 */
public class DebugFrustum extends Frustum {

  public DebugFrustum(Frustum copy, Matrix4 invProjectionView) {
    super();
    for (int i = 0; i < copy.planes.length; i++) {
      planes[i] = new Plane(copy.planes[i].getNormal(), copy.planes[i].getD());
    }

    for (int i = 0; i < copy.planePoints.length; i++) {
      planePoints[i] = new Vector3(copy.planePoints[i].x, copy.planePoints[i].y, copy.planePoints[i].z);
    }

    update(invProjectionView);
  }
}