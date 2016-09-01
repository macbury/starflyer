package de.macbury.tiles.assembler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;
import de.macbury.geo.MercatorProjection;
import de.macbury.geo.core.GeoPath;
import de.macbury.geo.core.GeoPoint;
import de.macbury.model.GeoTile;
import de.macbury.model.Road;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * This task creates model for passed {@link de.macbury.model.GeoTile}
 */
public class AssembleGeoTileTask implements RunnableFuture<ModelInstance>, Pool.Poolable {
  private static final String TAG = "AssembleGeoTileTask";
  private final ModelBuilder modelBuilder;
  private ModelInstance result;
  private GeoTile geoTile;
  private boolean done = false;
  private final Vector3 startVec = new Vector3();
  private final Vector3 finalVec = new Vector3();
  private final Vector3 originVec = new Vector3();
  private final GeoPoint originPoint = new GeoPoint();

  public AssembleGeoTileTask() {
    this.modelBuilder = new ModelBuilder();
  }

  private void generateGeoTileMesh(GeoTile tile) {
    this.startVec.setZero();
    this.finalVec.setZero();
    this.originVec.setZero();

    originPoint.set(tile.north, tile.west);
    MercatorProjection.project(originPoint, originVec);

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

      //GeoPoint startPoint = new GeoPoint();
      //GeoPoint finalPoint = new GeoPoint();

      // MeshPartBuilder boundingLine = modelBuilder.part("boundingBox", GL30.GL_LINES, VertexAttributes.Usage.WorldPosition, new Material(ColorAttribute.createDiffuse(Color.RED)));
/*
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

      boundingLine.line(startVec, finalVec);*/
    }
  }

  @Override
  public void run() {
    try {
      generateGeoTileMesh(geoTile);
      done = true;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public boolean cancel(boolean mayInterruptIfRunning) {
    return false;
  }

  @Override
  public boolean isCancelled() {
    return false;
  }

  @Override
  public boolean isDone() {
    return done;
  }

  @Override
  public ModelInstance get() throws InterruptedException, ExecutionException {
    if (result == null) {
      Model model = modelBuilder.end();
      this.result = new ModelInstance(model);
      this.result.transform.idt().translate(originVec);
    }

    return result;
  }

  @Override
  public ModelInstance get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
    return get();
  }

  @Override
  public void reset() {
    done = false;
    result = null;
    geoTile = null;
  }

  public void setGeoTile(GeoTile geoTile) {
    this.geoTile = geoTile;
  }

  public GeoTile getGeoTile() {
    return geoTile;
  }
}
