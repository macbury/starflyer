package de.macbury;


import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class MathExt {

  /**
   * Round values in passed vector
   * @param in
   * @return
   */
  public static Vector3 round(Vector3 in) {
    in.x = MathUtils.round(in.x);
    in.y = MathUtils.round(in.y);
    in.z = MathUtils.round(in.z);
    return in;
  }
}
