package de.macbury.entity.systems;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.utils.Disposable;
import de.macbury.entity.components.Components;
import de.macbury.entity.components.ModelInstanceComponent;
import de.macbury.entity.components.PositionComponent;
import de.macbury.entity.components.TileComponent;
import de.macbury.graphics.GeoPerspectiveCamera;

/**
 * Created by macbury on 29.08.16.
 */
public class RenderingSystem extends IteratingSystem implements Disposable {
  private ModelBatch modelBatch;
  private GeoPerspectiveCamera camera;

  public RenderingSystem(GeoPerspectiveCamera camera, ModelBatch modelBatch) {
    super(Family.all(PositionComponent.class).one(ModelInstanceComponent.class, TileComponent.class).get());
    this.camera     = camera;
    this.modelBatch = modelBatch;
  }

  @Override
  public void update(float deltaTime) {
    modelBatch.begin(camera); {
      super.update(deltaTime);
    } modelBatch.end();
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    PositionComponent positionComponent = Components.Position.get(entity);
    if (Components.Position.get(entity).isVisible()) {
      if (Components.Tile.has(entity)) {
        modelBatch.render(Components.Tile.get(entity).getInstance());
      } else if (Components.ModelInstance.has(entity)) {
        ModelInstanceComponent modelInstanceComponent = Components.ModelInstance.get(entity);
        modelBatch.render(modelInstanceComponent);
      }

    }
  }

  @Override
  public void dispose() {
    modelBatch = null;
    camera = null;
  }
}
