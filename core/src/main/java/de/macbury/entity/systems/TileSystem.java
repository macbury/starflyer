package de.macbury.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import de.macbury.entity.EntityManager;
import de.macbury.entity.Components;
import de.macbury.entity.components.ScenePositionComponent;
import de.macbury.entity.components.WorldPositionComponent;
import de.macbury.entity.components.TileComponent;
import de.macbury.geo.MercatorProjection;
import de.macbury.geo.Tile;
import de.macbury.geo.core.GeoPoint;
import de.macbury.graphics.GeoPerspectiveCamera;
import de.macbury.tiles.TileCachePool;
import de.macbury.tiles.TileInstance;
import de.macbury.tiles.VisibleTileProvider;

import java.awt.image.renderable.ParameterBlock;

/**
 * Check what tiles are visible, create them and assign to {@link Entity} that can be later render, etc.
 * Additional if {@link TileCachePool} exceeds pool size remove farthest tiles from system
 */
public class TileSystem extends IteratingSystem implements Disposable {
  private static final String TAG = "TileSystem";
  private final VisibleTileProvider visibleTileProvider;
  private final GeoPerspectiveCamera camera;
  private TileCachePool tileCachePool;
  private final Array<Entity> invisibleEntities;
  private final GeoPoint tempPoint;

  public TileSystem(TileCachePool tileCachePool, GeoPerspectiveCamera camera) {
    super(Family.all(WorldPositionComponent.class, TileComponent.class).get());
    this.tileCachePool       = tileCachePool;
    this.camera              = camera;
    this.visibleTileProvider = new VisibleTileProvider();
    this.invisibleEntities   = new Array<Entity>();
    this.tempPoint           = new GeoPoint();
  }

  private EntityManager manager() {
    return (EntityManager)getEngine();
  }

  @Override
  public void update(float deltaTime) {
    tileCachePool.update();
    visibleTileProvider.update(camera);

    //createNewVisibleTiles();
    invisibleEntities.clear();

    super.update(deltaTime);

    //for (Entity entity: invisibleEntities) {

    //}
    //TODO check which farthest non visible can be removed
  }

  /**
   * Check if we need to create new entities for visible tiles
   */
  private void createNewVisibleTiles() {
    for (Tile visibleTile : visibleTileProvider.getVisible()) {
      Entity tileEntity = findEntityForTile(visibleTile);
      if (tileEntity == null) {
        tileEntity                  = manager().createEntity();
        TileInstance tileInstance   = tileCachePool.get(visibleTile);
        TileComponent tileComponent = manager().createComponent(TileComponent.class);

        tileComponent.setInstance(tileInstance);
        tileEntity.add(tileComponent);

        WorldPositionComponent worldPositionComponent = manager().createComponent(WorldPositionComponent.class);
        tempPoint.set(visibleTile.north, visibleTile.west);
        MercatorProjection.project(tempPoint, worldPositionComponent);

        tileEntity.add(worldPositionComponent);
        tileEntity.add(manager().createComponent(ScenePositionComponent.class));

        manager().addEntity(tileEntity);
        Gdx.app.log(TAG, "Adding entity");
      }
    }
  }

  /**
   * Finds existing entity for passed position
   * @param tile
   * @return
   */
  private Entity findEntityForTile(Tile tile) {
    for (Entity e : getEntities()) {
      TileComponent tileComponent = Components.Tile.get(e);
      if (tileComponent.is(tile)) {
        return e;
      }
    }
    return null;
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    WorldPositionComponent position = Components.WorldPosition.get(entity);
    TileComponent tile         = Components.Tile.get(entity);
    TileInstance tileInstance  = tile.getInstance();

    position.visible           = visibleTileProvider.isVisible(tileInstance.tileX, tileInstance.tileY);

    if (!position.visible)
      this.invisibleEntities.add(entity);
  }

  @Override
  public void dispose() {
    invisibleEntities.clear();
    visibleTileProvider.dispose();
    tileCachePool = null;
  }
}
