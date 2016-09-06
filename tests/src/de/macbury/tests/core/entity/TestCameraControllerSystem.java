package de.macbury.tests.core.entity;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.InputMultiplexer;
import de.macbury.entity.EntityManager;
import de.macbury.entity.components.CameraComponent;
import de.macbury.entity.components.WorldPositionComponent;
import de.macbury.entity.messages.MessageType;
import de.macbury.entity.messages.MessagesManager;
import de.macbury.entity.systems.CameraControllerSystem;
import de.macbury.geo.core.GeoPoint;
import de.macbury.graphics.GeoPerspectiveCamera;
import de.macbury.tests.support.GdxTestRunner;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class TestCameraControllerSystem {

  @Test
  public void itShouldCenterCamera() {
    GeoPerspectiveCamera camera = new GeoPerspectiveCamera();
    MessagesManager messages = new MessagesManager();
    EntityManager entities = new EntityManager(messages);

    Entity playerEntity = entities.createEntity();
    WorldPositionComponent worldPositionComponent = entities.createComponent(WorldPositionComponent.class);
    CameraComponent cameraComponent = entities.createComponent(CameraComponent.class);

    cameraComponent.zoom = 10;
    cameraComponent.tilt = 0.4f;
    cameraComponent.rotation = 0.0f;

    playerEntity.add(worldPositionComponent);
    playerEntity.add(cameraComponent);

    entities.addEntity(playerEntity);

    CameraControllerSystem cameraControllerSystem = new CameraControllerSystem(new InputMultiplexer(), messages);
    entities.setCameraControllerSystem(cameraControllerSystem);
    entities.update(10);

    messages.dispatchMessage(MessageType.CenterCamera.ordinal(), new GeoPoint(10, 10));

    Assert.assertEquals(222638.98f, worldPositionComponent.x);
    Assert.assertEquals(223778.0f, worldPositionComponent.y);
  }
}
