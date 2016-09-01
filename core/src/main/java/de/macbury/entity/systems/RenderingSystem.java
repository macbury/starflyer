package de.macbury.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import de.macbury.entity.Components;
import de.macbury.entity.components.ModelInstanceComponent;
import de.macbury.entity.components.ScenePositionComponent;
import de.macbury.entity.components.WorldPositionComponent;
import de.macbury.entity.components.TileComponent;
import de.macbury.graphics.GeoPerspectiveCamera;
import de.macbury.tiles.TileInstance;

public class RenderingSystem extends IteratingSystem implements Disposable {
  private ModelBatch modelBatch;
  private GeoPerspectiveCamera camera;

  public RenderingSystem(GeoPerspectiveCamera camera, ModelBatch modelBatch) {
    super(Family.all(ScenePositionComponent.class).one(ModelInstanceComponent.class, TileComponent.class).get());
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
    ScenePositionComponent scenePositionComponent = Components.ScenePosition.get(entity);

    if (Components.WorldPosition.get(entity).isVisible()) {
      if (Components.Tile.has(entity)) {
        TileInstance instance = Components.Tile.get(entity).getInstance();
        if (instance.isReady()) {
          calculateTransform(instance.model.transform, scenePositionComponent);
        }
        modelBatch.render(instance);
      } else if (Components.ModelInstance.has(entity)) {
        ModelInstanceComponent modelInstanceComponent = Components.ModelInstance.get(entity);
        calculateTransform(modelInstanceComponent.transform, scenePositionComponent);
        modelBatch.render(modelInstanceComponent);
      }

    }
  }

  /**
   * Apply world offset to transform
   * @param transform
   * @param worldPositionComponent
   */
  private void calculateTransform(Matrix4 transform, ScenePositionComponent scenePositionComponent) {
    transform.idt();
    transform.translate(scenePositionComponent);
  }

  @Override
  public void dispose() {
    modelBatch = null;
    camera = null;
  }

}
