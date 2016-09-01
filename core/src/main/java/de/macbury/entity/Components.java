package de.macbury.entity;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import de.macbury.entity.components.*;

/**
 * Simple helper for retrieving components from {@link Entity}
 */
public class Components {
  public static final ComponentMapper<TileComponent> Tile = ComponentMapper.getFor(TileComponent.class);
  public static final ComponentMapper<WorldPositionComponent> WorldPosition = ComponentMapper.getFor(WorldPositionComponent.class);
  public static final ComponentMapper<ScenePositionComponent> ScenePosition = ComponentMapper.getFor(ScenePositionComponent.class);

  public static final ComponentMapper<CameraComponent> Camera = ComponentMapper.getFor(CameraComponent.class);
  public static final ComponentMapper<ModelInstanceComponent> ModelInstance = ComponentMapper.getFor(ModelInstanceComponent.class);
}
