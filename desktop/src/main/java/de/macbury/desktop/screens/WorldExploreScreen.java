package de.macbury.desktop.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.widget.VisTable;
import de.macbury.desktop.manager.MainStatusBarManager;
import de.macbury.desktop.manager.MenuBarManager;
import de.macbury.desktop.tiles.downloaders.MapZenGeoTileDownloader;
import de.macbury.desktop.ui.DebugTileCachePoolWindow;
import de.macbury.desktop.ui.DebugVisibleTileWindow;
import de.macbury.entity.EntityManager;
import de.macbury.entity.EntityManagerBuilder;
import de.macbury.entity.messages.MessagesManager;
import de.macbury.geo.MercatorProjection;
import de.macbury.geo.Tile;
import de.macbury.geo.core.GeoPoint;
import de.macbury.graphics.FrustumDebugAndRenderer;
import de.macbury.graphics.GeoPerspectiveCamera;
import de.macbury.screens.AbstractScreen;
import de.macbury.server.tiles.TilesManager;
import de.macbury.server.tiles.cache.MemoryTileCache;
import de.macbury.tiles.TileCachePool;
import de.macbury.tiles.VisibleTileProvider;
import de.macbury.tiles.assembler.TileAssembler;

public class WorldExploreScreen extends AbstractScreen implements MenuBarManager.Listener {
  private static final String TAG = "WorldExploreScreen";
  private ModelBatch modelBatch;
  private MessagesManager messages;
  private FrustumDebugAndRenderer frustumDebugger;
  private TileCachePool tileCachePool;
  private GeoPerspectiveCamera camera;
  private EntityManager entities;
  private DebugTileCachePoolWindow debugTileCachePoolWindow;
  private DebugVisibleTileWindow debugVisibleTileWindow;
  private MenuBarManager menuBarManger;
  private MainStatusBarManager statusBarManager;
  private Stage stage;
  private VisibleTileProvider visibleTileProvider;

  @Override
  public void preload() {

  }

  @Override
  public void create() {
    modelBatch            = new ModelBatch();
    this.messages         = new MessagesManager();
    this.frustumDebugger  = new FrustumDebugAndRenderer();
    this.tileCachePool    = new TileCachePool(new MapZenGeoTileDownloader(new TilesManager(new MemoryTileCache())), new TileAssembler());
    this.visibleTileProvider = new VisibleTileProvider();
    this.camera           = new GeoPerspectiveCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    this.entities = new EntityManagerBuilder()
            .withMessageDispatcher(messages)
            .withTileCachePool(tileCachePool, visibleTileProvider)
            .withModelBatch(modelBatch)
            .build();

    initializeUI();
  }

  private void initializeUI() {
    this.debugTileCachePoolWindow = new DebugTileCachePoolWindow(tileCachePool, new VisibleTileProvider());
    this.debugVisibleTileWindow   = new DebugVisibleTileWindow(new VisibleTileProvider());
    this.menuBarManger            = new MenuBarManager();
    statusBarManager              = new MainStatusBarManager();
    statusBarManager.setSpyCamera(camera);

    menuBarManger.addListener(this);

    stage               = new Stage(new ScreenViewport());

    VisTable root       = new VisTable();
    root.setFillParent(true);
    stage.addActor(root);

    root.add(menuBarManger.getTable()).growX().row();

    root.add().fill().expand().row();

    root.add(statusBarManager).growX().row();
    stage.addActor(debugVisibleTileWindow);
    stage.addActor(debugTileCachePoolWindow);
  }

  @Override
  public void resize(int width, int height) {
    stage.getViewport().update(width, height, true);
    camera.resize(width, height);
  }

  @Override
  public void show() {
    Gdx.input.setInputProcessor(stage);
    fetch(50.062191, 19.937002);
  }

  private void fetch(double lat, double lng) {
    GeoPoint point = new GeoPoint(lat, lng);

    Tile tileCursor = new Tile();
    tileCursor.set(point);

    Vector3 target = new Vector3();

    MercatorProjection.project(point, target);
    camera.setGeoPosition(point);

    messages.camera.centerAt(new Vector2(target.x, target.y));
  }
  
  @Override
  public boolean isDisposedAfterHide() {
    return true;
  }

  @Override
  public void hide() {
    Gdx.input.setInputProcessor(null);
  }

  @Override
  public void render() {
    Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

    entities.update(Gdx.graphics.getDeltaTime());

    frustumDebugger.render(camera);

    
    stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
    stage.draw();
  }

  @Override
  public void pause() {

  }

  @Override
  public void resume() {

  }

  @Override
  public void dispose() {
    stage.dispose();
    entities.dispose();
    modelBatch.dispose();
    tileCachePool.dispose();
  }

  @Override
  public void onMenuAction(MenuBarManager.Action action, MenuBarManager menuBarManager) {
    switch (action) {
      case DebugTileCachePool:
        debugTileCachePoolWindow.toggle();
        break;
      case DebugVisibleTiles:
        debugVisibleTileWindow.toggle();
        break;
      case FrustumSave:
        camera.saveDebugFrustum();
        break;
      case FrustumRestore:
        camera.restoreFrustum();
        break;
      default:
        Gdx.app.log(TAG, "Implement action: " + action.toString());
    }
  }
}


  /*private void renderDebugTiles() {
    shapeRenderer.setProjectionMatrix(camera.combined);
    shapeRenderer.begin(ShapeRenderer.ShapeType.Line); {
      for (TileInstance tileInstance : tilesToRender) {

        switch (tileInstance.state) {
          case Downloading:
            shapeRenderer.setColor(Color.FIREBRICK);
            break;
          case Assembling:
            shapeRenderer.setColor(Color.NAVY);
            break;
          case Error:
            shapeRenderer.setColor(Color.RED);
            break;
        }

        if (tileInstance.state != TileInstance.State.Ready && tileInstance.state != TileInstance.State.Unloaded) {
          GeoTile tile = tileInstance.geoTile;
          //TOP line
          tempStartPoint.set(tile.north, tile.west);
          tempFinalPoint.set(tile.north, tile.east);

          MercatorProjection.project(tempStartPoint, tempStartVec);
          MercatorProjection.project(tempFinalPoint, tempFinalVec);

          shapeRenderer.line(tempStartVec, tempFinalVec);

          // BOTTOM line
          tempStartPoint.set(tile.south, tile.east);
          tempFinalPoint.set(tile.south, tile.west);

          MercatorProjection.project(tempStartPoint, tempStartVec);
          MercatorProjection.project(tempFinalPoint, tempFinalVec);

          shapeRenderer.line(tempStartVec, tempFinalVec);

          // right line
          tempStartPoint.set(tile.south, tile.east);
          tempFinalPoint.set(tile.north, tile.east);

          MercatorProjection.project(tempStartPoint, tempStartVec);
          MercatorProjection.project(tempFinalPoint, tempFinalVec);

          shapeRenderer.line(tempStartVec, tempFinalVec);

          // left line
          tempStartPoint.set(tile.south, tile.west);
          tempFinalPoint.set(tile.north, tile.west);

          MercatorProjection.project(tempStartPoint, tempStartVec);
          MercatorProjection.project(tempFinalPoint, tempFinalVec);

          shapeRenderer.line(tempStartVec, tempFinalVec);
        }

      }
    } shapeRenderer.end();
  }*/