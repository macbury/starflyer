package de.macbury.entity.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import de.macbury.entity.components.CameraComponent;
import de.macbury.entity.components.Components;
import de.macbury.graphics.GeoPerspectiveCamera;

/**
 * This system calculates camera rotation and zoom around target
 */
public class CameraSystem extends EntitySystem {
  private final GeoPerspectiveCamera camera;
  private final Family family;
  private ImmutableArray<Entity> entities;
  private Vector3 center = new Vector3();
  public CameraSystem(GeoPerspectiveCamera camera) {
    this.camera = camera;
    this.family = Family.all(CameraComponent.class).get();
  }

  @Override
  public void addedToEngine(Engine engine) {
    entities = engine.getEntitiesFor(family);
  }

  @Override
  public void removedFromEngine(Engine engine) {
    entities = null;
  }

  @Override
  public void update(float deltaTime) {
    if (entities.size() == 1) {
      Entity targetCameraEntity = entities.get(0);
      process(targetCameraEntity);
    }

    if (entities.size() > 1) {
      throw new RuntimeException("There are more than one Entity with CameraComponent");
    }
  }

  /**
   * Set camera rotation around {@link Vector3.Zero}. We don`t move camera, but we move whole world
   * @param targetCameraEntity
   */
  private void process(Entity targetCameraEntity) {
    CameraComponent cameraComponent = Components.Camera.get(targetCameraEntity);
    float tilt = cameraComponent.tilt;
    float rotation = cameraComponent.rotation;
    float currentZoom = cameraComponent.zoom;

    camera.position.x = (currentZoom * MathUtils.cos(tilt) * MathUtils.sin(rotation));
    camera.position.z = (currentZoom * MathUtils.sin(tilt));
    camera.position.y = (currentZoom * MathUtils.cos(tilt) * MathUtils.cos(rotation));

    center.set(Vector3.Zero).sub(camera.position).nor();
    camera.direction.set(center);

    camera.update();
  }
}
