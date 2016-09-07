package de.macbury.graphics;


import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;

/**
 * Created by macbury on 09.03.14.
 */
public class DebugShape {
  private final static Vector3 vecA              = new Vector3();
  private final static Vector3 vecB              = new Vector3();

  public static void draw(ShapeRenderer renderer, BoundingBox box) {
    box.getCorner000(vecA);
    box.getCorner001(vecB);
    renderer.line(vecA, vecB);

    box.getCorner010(vecA);
    box.getCorner011(vecB);
    renderer.line(vecA, vecB);

    box.getCorner000(vecA);
    box.getCorner010(vecB);
    renderer.line(vecA, vecB);

    box.getCorner001(vecA);
    box.getCorner011(vecB);
    renderer.line(vecA, vecB);

    box.getCorner100(vecA);
    box.getCorner000(vecB);
    renderer.line(vecA, vecB);

    box.getCorner101(vecA);
    box.getCorner001(vecB);
    renderer.line(vecA, vecB);

    box.getCorner101(vecA);
    box.getCorner100(vecB);
    renderer.line(vecA, vecB);

    box.getCorner101(vecA);
    box.getCorner111(vecB);
    renderer.line(vecA, vecB);

    box.getCorner011(vecA);
    box.getCorner111(vecB);
    renderer.line(vecA, vecB);

    box.getCorner010(vecA);
    box.getCorner110(vecB);
    renderer.line(vecA, vecB);

    box.getCorner111(vecA);
    box.getCorner110(vecB);
    renderer.line(vecA, vecB);

    box.getCorner100(vecA);
    box.getCorner110(vecB);
    renderer.line(vecA, vecB);
  }

}