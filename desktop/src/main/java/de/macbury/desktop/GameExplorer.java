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
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;
import de.macbury.Starflyer;
import de.macbury.geo.MercatorProjection;
import de.macbury.geo.core.GeoPath;
import de.macbury.geo.core.GeoPoint;
import de.macbury.model.GeoTile;
import de.macbury.model.Road;
import de.macbury.server.tiles.TilesManager;
import de.macbury.server.tiles.cache.MemoryTileCache;

/**
 * Explore game content
 */
public class GameExplorer extends Starflyer {
  private Stage stage;
  private PerspectiveCamera camera;
  private Model model;
  private ModelInstance instance;
  private ModelBatch modelBatch;
  public TilesManager tilesManager;
  private ShapeRenderer shapeRenderer;
  private CameraInputController cameraController;

  @Override
  public void create() {
    super.create();


    this.tilesManager = new TilesManager(new MemoryTileCache());
    tilesManager.addListener(new TilesManager.Listener() {
      @Override
      public void onTileRetrieve(GeoTile tile, TilesManager manager) {
        Gdx.app.log("Geo tile", tile.toString());
      }
    });

    VisUI.load(VisUI.SkinScale.X1);

    stage = new Stage(new ScreenViewport());
    InputMultiplexer inputMultiplexer = new InputMultiplexer();
    inputMultiplexer.addProcessor(stage);

    Gdx.input.setInputProcessor(inputMultiplexer);

    this.camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    camera.position.set(10, 10, 10);
    camera.far = 300;
    camera.near = 1f;
    camera.lookAt(Vector3.Zero);


    this.cameraController = new CameraInputController(camera);
    inputMultiplexer.addProcessor(cameraController);

    this.shapeRenderer = new ShapeRenderer();

    modelBatch = new ModelBatch();
    VisTable root = new VisTable();
    root.setFillParent(true);
    stage.addActor(root);

    final VisTextButton textButton = new VisTextButton("click me!");
    textButton.addListener(new ChangeListener() {
      @Override
      public void changed (ChangeEvent event, Actor actor) {
        tilesManager.retrieve(72839, 44399);
      }
    });

    VisWindow window = new VisWindow("example window");
    window.add("this is a simple VisUI window").padTop(5f).row();
    window.add(textButton).pad(10f);
    window.pack();
    window.centerWindow();
    stage.addActor(window.fadeIn());

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

    tilesManager.retrieve(72839, 44399);
  }

  private void generateGeoTileMesh(GeoTile tile) {
    Vector3 startVec = new Vector3();
    Vector3 finalVec = new Vector3();
    Vector3 originVec = new Vector3(2232878.5f, 0, 6462425.5f);
    ModelBuilder modelBuilder = new ModelBuilder();
    modelBuilder.begin(); {
      for (Road road: tile.roads) {
        for (GeoPath path : road) {
          MeshPartBuilder line = modelBuilder.part("line-"+path.toString(), GL30.GL_LINES, VertexAttributes.Usage.Position, new Material(ColorAttribute.createDiffuse(Color.LIGHT_GRAY)));
          line.setColor(Color.WHITE);
          for (int i = 1; i < path.size(); i++) {
            GeoPoint startPoint = path.get(i-1);
            GeoPoint finalPoint = path.get(i);
            MercatorProjection.project(startPoint, startVec);
            MercatorProjection.project(finalPoint, finalVec);
            line.line(startVec.sub(originVec).scl(0.1f), finalVec.sub(originVec).scl(0.1f));
          }
        }
      }
    } model = modelBuilder.end();

    instance = new ModelInstance(model);
    instance.transform.rotate(Vector3.X, 180);
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
    cameraController.update();
    Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

    if (instance != null) {
      modelBatch.begin(camera); {
        modelBatch.render(instance);
      } modelBatch.end();
    }


    shapeRenderer.setProjectionMatrix(camera.combined);
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
}
