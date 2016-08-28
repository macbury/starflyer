package de.macbury.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.*;
import com.mashape.unirest.http.Unirest;
import de.macbury.ActionTimer;
import de.macbury.Starflyer;
import de.macbury.desktop.manager.MainStatusBarManager;
import de.macbury.desktop.manager.MenuBarManager;
import de.macbury.desktop.tiles.downloaders.MapZenGeoTileDownloader;
import de.macbury.desktop.ui.DebugTileCachePoolWindow;
import de.macbury.desktop.ui.DebugVisibleTileWindow;
import de.macbury.geo.MercatorProjection;
import de.macbury.geo.Tile;
import de.macbury.geo.core.GeoPath;
import de.macbury.geo.core.GeoPoint;
import de.macbury.graphics.FrustumDebugAndRenderer;
import de.macbury.graphics.GeoPerspectiveCamera;
import de.macbury.graphics.Overlay;
import de.macbury.graphics.RTSCameraController;
import de.macbury.model.GeoTile;
import de.macbury.model.Road;
import de.macbury.tiles.*;
import de.macbury.server.tiles.TilesManager;
import de.macbury.server.tiles.cache.MemoryTileCache;
import de.macbury.tiles.assembler.TileAssembler;

/**
 * Explore game content
 */
public class GameExplorer extends Starflyer implements ActionTimer.TimerListener, MenuBarManager.Listener {
  private static final String TAG = "GameExplorer";
  private Stage stage;
  private GeoPerspectiveCamera camera;
  private Model model;
  private ModelInstance instance;
  private ModelBatch modelBatch;
  private ShapeRenderer shapeRenderer;
  private RTSCameraController cameraController;

  private Array<ModelInstance> tiles = new Array<ModelInstance>();
  VisibleTileProvider visibleTileProvider = new VisibleTileProvider();

  private VisLabel visibleTileLabel;
  private MenuBarManager menuBarManger;
  private MainStatusBarManager statusBarManager;
  private DebugVisibleTileWindow debugVisibleTileWindow;
  private TileCachePool tileCachePool;
  private TilesToRender tilesToRender;
  private DebugTileCachePoolWindow debugTileCachePoolWindow;
  private FrustumDebugAndRenderer frustumDebugger;

  @Override
  public void create() {
    super.create();
    VisUI.load(VisUI.SkinScale.X1);
    //Unirest.setConcurrency(5,1);
    initializeGameEngine();
    initializeUI();

    this.shapeRenderer = new ShapeRenderer();

    modelBatch = new ModelBatch();
  }

  private void initializeUI() {
    this.debugTileCachePoolWindow = new DebugTileCachePoolWindow(tileCachePool, visibleTileProvider);
    this.debugVisibleTileWindow = new DebugVisibleTileWindow(visibleTileProvider);
    Overlay overlay = new Overlay();
    this.menuBarManger = new MenuBarManager();
    statusBarManager   = new MainStatusBarManager();
    statusBarManager.setSpyCamera(camera);

    menuBarManger.addListener(this);

    stage               = new Stage(new ScreenViewport());

    VisTable root = new VisTable();
    root.setFillParent(true);
    stage.addActor(root);

    root.add(menuBarManger.getTable()).growX().row();

    root.add(overlay).fill().expand().row();

    root.add(statusBarManager).growX().row();

    cameraController.setOverlay(overlay);
    Gdx.input.setInputProcessor(stage);
    stage.addActor(debugVisibleTileWindow);
    stage.addActor(debugTileCachePoolWindow);
  }

  private void initializeGameEngine() {
    this.frustumDebugger = new FrustumDebugAndRenderer();
    this.tileCachePool = new TileCachePool(new MapZenGeoTileDownloader(new TilesManager(new MemoryTileCache())), new TileAssembler());
    this.tilesToRender = new TilesToRender();
    this.camera = new GeoPerspectiveCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    this.cameraController = new RTSCameraController();
    cameraController.setCamera(camera);
    //cameraController.target.set(Vector3.Zero);

    fetch(50.062191, 19.937002);
  }

  private void fetch(double lat, double lng) {
    GeoPoint point = new GeoPoint(lat, lng);

    Tile tileCursor = new Tile();
    tileCursor.set(point);

    Vector3 target = new Vector3();

    MercatorProjection.project(point, target);
    camera.setGeoPosition(point);

    cameraController.setCenter(target.x, target.y);

    camera.position.add(0, -30, 10);
    camera.update();

    //tilesManager.retrieve(tileCursor);
  }

  private void createUI() {
    /*final VisWindow window = new VisWindow("Debug navigation to");
    final VisLabel cameraPositionLabel = new VisLabel();
    final VisLabel tilePositionLabel = new VisLabel();
    final VisTextField latField = new VisTextField("50.093113");
    final VisTextField lngField = new VisTextField("20.059572");

    this.visibleTileLabel = new VisLabel("");

    final VisTextButton textButton = new VisTextButton("Go to");

    textButton.addListener(new ChangeListener() {
      @Override
      public void changed (ChangeEvent event, Actor actor) {
        double lat = Double.valueOf(latField.getText());
        double lng = Double.valueOf(lngField.getText());

        GeoPoint point = new GeoPoint(lat, lng);

        Tile tileCursor = new Tile();
        tileCursor.set(point);

        camera.setGeoPosition(point);
        camera.position.add(0, -30, 10);
        camera.update();

        tilesManager.retrieve(tileCursor);

        cameraPositionLabel.setText("Camera position: " + camera.position.toString());
        tilePositionLabel.setText(tileCursor.toString());

        window.pack();
      }
    });

    window.add(visibleTileLabel).row();
    window.add(cameraPositionLabel).row();
    window.add(tilePositionLabel).row();
    window.add(latField).row();
    window.add(lngField).row();
    window.add(textButton).pad(10f);
    window.pack();
    window.left().top();
    stage.addActor(window.fadeIn());*/
  }

  @Override
  public void resize (int width, int height) {
    stage.getViewport().update(width, height, true);
    camera.viewportWidth = width;
    camera.viewportHeight = height;
    camera.update(true);
  }

  private final GeoPoint tempStartPoint = new GeoPoint();
  private final GeoPoint tempFinalPoint = new GeoPoint();
  private final Vector3 tempStartVec    = new Vector3();
  private final Vector3 tempFinalVec    = new Vector3();


  @Override
  public void render () {
    cameraController.update(Gdx.graphics.getDeltaTime());
    tileCachePool.update();
    visibleTileProvider.update(camera);

    Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

    shapeRenderer.setProjectionMatrix(camera.combined);

    tilesToRender.begin(tileCachePool, visibleTileProvider); {
      modelBatch.begin(camera); {
        modelBatch.render(tilesToRender);
      } modelBatch.end();

      //renderDebugTiles();
    } tilesToRender.end();

    frustumDebugger.render(camera);

    stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
    stage.draw();
  }

  private void renderDebugTiles() {
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
  }

  @Override
  public void dispose () {
    VisUI.dispose();
    stage.dispose();
  }

  @Override
  public void onTimerTick(ActionTimer timer) {
    //visibleTileLabel.setText("Visible tiles: " + visibleTileProvider.getVisibleCount());
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
