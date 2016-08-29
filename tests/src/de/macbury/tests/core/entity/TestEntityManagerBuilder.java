package de.macbury.tests.core.entity;


import de.macbury.entity.EntityManager;
import de.macbury.entity.EntityManagerBuilder;
import de.macbury.entity.messages.MessagesManager;
import de.macbury.graphics.GeoPerspectiveCamera;
import de.macbury.tests.support.GdxTestRunner;
import junit.framework.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class TestEntityManagerBuilder {
  //<editor-fold desc="Setup test">
  @Rule
  public ExpectedException expectedEx = ExpectedException.none();
  //</editor-fold>


  @Test
  public void itBuildsValidDesktopEntityManager() {
    EntityManager entityManager = new EntityManagerBuilder()
            .withRTSGameCamera(new GeoPerspectiveCamera(1,1))
            .withMessageDispatcher(new MessagesManager())
            .build();
    Assert.assertNotNull(entityManager.getRtsCameraSystem());
  }
}
