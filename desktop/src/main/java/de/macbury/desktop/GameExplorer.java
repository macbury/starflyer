package de.macbury.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.*;
import de.macbury.ActionTimer;
import de.macbury.Starflyer;
import de.macbury.desktop.manager.MainStatusBarManager;
import de.macbury.desktop.manager.MenuBarManager;
import de.macbury.desktop.ui.DebugVisibleTileWindow;
import de.macbury.geo.MercatorProjection;
import de.macbury.geo.Tile;
import de.macbury.geo.core.GeoPath;
import de.macbury.geo.core.GeoPoint;
import de.macbury.graphics.GeoPerspectiveCamera;
import de.macbury.graphics.Overlay;
import de.macbury.graphics.RTSCameraController;
import de.macbury.model.GeoTile;
import de.macbury.model.Road;
import de.macbury.occlusion.VisibleTileProvider;
import de.macbury.server.tiles.TilesManager;
import de.macbury.server.tiles.cache.MemoryTileCache;

/**
 * Explore game content
 */
public class GameExplorer extends Starflyer implements ActionTimer.TimerListener {
  private Stage stage;
  private GeoPerspectiveCamera camera;
  private Model model;
  private ModelInstance instance;
  private ModelBatch modelBatch;
  public TilesManager tilesManager;
  private ShapeRenderer shapeRenderer;
  private RTSCameraController cameraController;

  private Array<ModelInstance> tiles = new Array<ModelInstance>();
  private Tile tileCursor;
  VisibleTileProvider visibleTileProvider = new VisibleTileProvider();

  private VisLabel visibleTileLabel;
  private MenuBarManager menuBarManger;
  private MainStatusBarManager statusBarManager;
  private DebugVisibleTileWindow debugVisibleTileWindow;

  @Override
  public void create() {
    super.create();
    VisUI.load(VisUI.SkinScale.X1);

    initializeGameEngine();
    initializeUI();

    this.shapeRenderer = new ShapeRenderer();

    modelBatch = new ModelBatch();

    tilesManager.addListener(new TilesManager.Listener() {
      @Override
      public void onTileRetrieve(GeoTile tile, TilesManager manager) {
        final GeoTile t = tile;
        Gdx.app.postRunnable(new Runnable() {
          @Override
          public void run() {
            generateGeoTileMesh(t);
          }
        });
      }
    });
  }

  private void initializeUI() {
    this.debugVisibleTileWindow = new DebugVisibleTileWindow(visibleTileProvider);
    Overlay overlay = new Overlay();
    this.menuBarManger = new MenuBarManager(debugVisibleTileWindow);
    statusBarManager   = new MainStatusBarManager();
    statusBarManager.setSpyCamera(camera);

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
  }

  private void initializeGameEngine() {
    this.tilesManager  = new TilesManager(new MemoryTileCache());

    this.camera = new GeoPerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    this.cameraController = new RTSCameraController();
    cameraController.setCamera(camera);
    //cameraController.target.set(Vector3.Zero);

    fetch(50.093125, 20.059642);
  }

  private void fetch(double lat, double lng) {
    GeoPoint point = new GeoPoint(lat, lng);

    tileCursor = new Tile();
    tileCursor.set(point);

    Vector3 target = new Vector3();

    MercatorProjection.project(point, target);
    camera.setGeoPosition(point);

    cameraController.setCenter(target.x, target.y);

    camera.position.add(0, -30, 10);
    camera.update();

    tilesManager.retrieve(tileCursor);
  }

  private void createUI() {
    final VisWindow window = new VisWindow("Debug navigation to");
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

        tileCursor = new Tile();
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
    stage.addActor(window.fadeIn());
  }

  private void generateGeoTileMesh(GeoTile tile) {
    Vector3 startVec = new Vector3();
    Vector3 finalVec = new Vector3();
    Vector3 originVec = new Vector3(0f, 0f,0f);

    ModelBuilder modelBuilder = new ModelBuilder();
    modelBuilder.begin(); {
      for (Road road: tile.roads) {
        Color roadColor = Color.WHITE;

        switch (road.getType()) {
          case Highway:
            roadColor = Color.BLUE;
            break;
          case Path:
            roadColor = Color.GRAY;
            break;
          case MinorRoad:
            roadColor = Color.GOLD;
            break;
          case MajorRoad:
            roadColor = Color.ORANGE;
            break;
        }

        MeshPartBuilder line = modelBuilder.part("road", GL30.GL_LINES, VertexAttributes.Usage.Position, new Material(ColorAttribute.createDiffuse(roadColor)));
        for (GeoPath path : road) {
          for (int i = 1; i < path.size(); i++) {
            GeoPoint startPoint = path.get(i-1);
            GeoPoint finalPoint = path.get(i);

            MercatorProjection.project(startPoint, startVec);
            MercatorProjection.project(finalPoint, finalVec);
            line.line(startVec.sub(originVec), finalVec.sub(originVec));
          }
        }
      }

      GeoPoint startPoint = new GeoPoint();
      GeoPoint finalPoint = new GeoPoint();

      MeshPartBuilder boundingLine = modelBuilder.part("boundingBox", GL30.GL_LINES, VertexAttributes.Usage.Position, new Material(ColorAttribute.createDiffuse(Color.RED)));

      //TOP line
      startPoint.set(tileCursor.north, tileCursor.west);
      finalPoint.set(tileCursor.north, tileCursor.east);

      MercatorProjection.project(startPoint, startVec);
      MercatorProjection.project(finalPoint, finalVec);

      boundingLine.line(startVec, finalVec);

      // BOTTOM line
      startPoint.set(tileCursor.south, tileCursor.east);
      finalPoint.set(tileCursor.south, tileCursor.west);

      MercatorProjection.project(startPoint, startVec);
      MercatorProjection.project(finalPoint, finalVec);

      boundingLine.line(startVec, finalVec);

      // right line
      startPoint.set(tileCursor.south, tileCursor.east);
      finalPoint.set(tileCursor.north, tileCursor.east);

      MercatorProjection.project(startPoint, startVec);
      MercatorProjection.project(finalPoint, finalVec);

      boundingLine.line(startVec, finalVec);

      // left line
      startPoint.set(tileCursor.south, tileCursor.west);
      finalPoint.set(tileCursor.north, tileCursor.west);

      MercatorProjection.project(startPoint, startVec);
      MercatorProjection.project(finalPoint, finalVec);

      boundingLine.line(startVec, finalVec);
    } model = modelBuilder.end();

    instance = new ModelInstance(model);
    tiles.add(instance);
  }

  @Override
  public void resize (int width, int height) {
    stage.getViewport().update(width, height, true);
    camera.viewportWidth = width;
    camera.viewportHeight = height;
    camera.update(true);
  }

  @Override
  public void render () {
    cameraController.update(Gdx.graphics.getDeltaTime());
    visibleTileProvider.update(camera);

    Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

    if (instance != null) {
      modelBatch.begin(camera); {
        for (ModelInstance tile: tiles) {
          modelBatch.render(tile);
        }
      } modelBatch.end();
    }

    shapeRenderer.setProjectionMatrix(camera.combined);
    shapeRenderer.translate(camera.position.x, camera.position.y, 0);
    shapeRenderer.begin(ShapeRenderer.ShapeType.Line); {
      shapeRenderer.setColor(Color.GREEN);
      shapeRenderer.line(Vector3.Zero, new Vector3(0,1,0));
      shapeRenderer.setColor(Color.RED);
      shapeRenderer.line(Vector3.Zero, new Vector3(1,0,0));
      shapeRenderer.setColor(Color.BLUE);
      shapeRenderer.line(Vector3.Zero, new Vector3(0,0,1));
    } shapeRenderer.end();

    //Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
    stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
    stage.draw();
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
}
