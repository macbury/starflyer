package de.macbury.tests.core.entity;


import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.sun.javafx.sg.prism.NGShape;
import de.macbury.entity.EntityManager;
import de.macbury.entity.EntityManagerBuilder;
import de.macbury.entity.messages.MessagesManager;
import de.macbury.graphics.GeoPerspectiveCamera;
import de.macbury.tests.support.GdxTestRunner;
import de.macbury.tiles.TileCachePool;
import de.macbury.tiles.VisibleTileProvider;
import de.macbury.tiles.assembler.TileAssembler;
import de.macbury.tiles.downloaders.AbstractGeoTileDownloader;
import junit.framework.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

@RunWith(GdxTestRunner.class)
public class TestEntityManagerBuilder {
  //<editor-fold desc="Setup test">
  @Rule
  public ExpectedException expectedEx = ExpectedException.none();
  //</editor-fold>


  @Test
  public void itBuildsValidDesktopEntityManager() {
    EntityManager entityManager = new EntityManagerBuilder()
            .withMessageDispatcher(new MessagesManager())
            .withTileCachePool(new TileCachePool(Mockito.mock(AbstractGeoTileDownloader.class), Mockito.mock(TileAssembler.class)), Mockito.mock(VisibleTileProvider.class))
            .withModelBatch(Mockito.mock(ModelBatch.class))
            .withCamera(Mockito.mock(GeoPerspectiveCamera.class))
            .build();
  }
}
