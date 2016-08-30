package de.macbury.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

/**
 *
 */
public class ModelInstanceComponent extends ModelInstance implements Component {
  public ModelInstanceComponent(Model model) {
    super(model);
  }
}
