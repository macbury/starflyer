package de.macbury.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector3;
import de.macbury.entity.Components;
import de.macbury.entity.components.ScenePositionComponent;
import de.macbury.entity.components.WorldPositionComponent;

/**
 * Calculate position relative to camera({@link ScenePositionComponent}) using {@link WorldPositionComponent} and camera offset
 */
public class SceneOffsetSystem extends IteratingSystem implements EntityListener{
  private Entity cameraEntity;
  private final Vector3 worldOffset;

  public SceneOffsetSystem() {
    super(Family.all(WorldPositionComponent.class, ScenePositionComponent.class).get());
    this.worldOffset = new Vector3();
  }

  @Override
  public void update(float deltaTime) {
    if (cameraEntity != null) {
      worldOffset.set(Components.WorldPosition.get(cameraEntity)).scl(-1);
    } else {
      worldOffset.setZero();
    }
    super.update(deltaTime);
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    WorldPositionComponent worldPosition          = Components.WorldPosition.get(entity);
    ScenePositionComponent scenePositionComponent = Components.ScenePosition.get(entity);

    scenePositionComponent.set(worldOffset).add(worldPosition);
  }

  @Override
  public void entityAdded(Entity entity) {
    if (Components.Camera.has(entity)) {
      if (cameraEntity != null) {
        throw new RuntimeException("There is already one camera entity in scene");
      }
      cameraEntity = entity;
    }
  }

  @Override
  public void entityRemoved(Entity entity) {
    if (entity == cameraEntity) {
      cameraEntity = null;
    }
  }
}
