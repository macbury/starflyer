package de.macbury.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

/**
 * Render frustum shape
 */
public class FrustumDebugAndRenderer {
  private final static short[][] RENDER_SEQUENCE = {
          {0,1,1,2,2,3,3,0},
          {4,5,5,6,6,7,7,4},
          {0,4,1,5,2,6,3,7}
  };

  private final static short[][] D = {
          {0,4,1,5,2,6,3,7}
  };

  private ShapeRenderer renderer;

  public FrustumDebugAndRenderer() {
    this.renderer       = new ShapeRenderer();
  }

  public Vector3 tempA = new Vector3();
  public Vector3 tempB = new Vector3();

  public void render(GeoPerspectiveCamera camera) {
    if (camera.haveDebugFrustrum()) {
      renderer.setProjectionMatrix(camera.combined);
      renderer.begin(ShapeRenderer.ShapeType.Line); {
        renderer.setColor(Color.WHITE);

        Frustum frustum = camera.normalOrDebugFrustrum();
        for (int sequence = 0; sequence < RENDER_SEQUENCE.length; sequence++) {
          for (int index = 1; index < RENDER_SEQUENCE[sequence].length; index += 2) {
            short a = RENDER_SEQUENCE[sequence][index];
            short b = RENDER_SEQUENCE[sequence][index - 1];
            Vector3 aPoint = frustum.planePoints[a];
            Vector3 bPoint = frustum.planePoints[b];
            renderer.line(bPoint, aPoint);
          }
        }

      } renderer.end();
    }

  }

}
