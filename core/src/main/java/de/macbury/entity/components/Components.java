package de.macbury.entity.components;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;

/**
 * Simple helper for retrieving components from {@link Entity}
 */
public class Components {
  public static final ComponentMapper<TileComponent> Tile = ComponentMapper.getFor(TileComponent.class);
  public static final ComponentMapper<PositionComponent> Position = ComponentMapper.getFor(PositionComponent.class);
}
