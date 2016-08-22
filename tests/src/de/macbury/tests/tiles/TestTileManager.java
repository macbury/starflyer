package de.macbury.tests.tiles;

import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import de.macbury.model.GeoTile;
import de.macbury.server.tiles.TilesManager;
import de.macbury.server.tiles.cache.NullTileCache;
import de.macbury.server.tiles.mapzen.MapZenApi;
import de.macbury.server.tiles.mapzen.MapZenLayersResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

import static org.mockito.Mockito.mock;

/**
 * Created by macbury on 22.08.16.
 */
public class TestTileManager {
  private MapZenApi mapZenMock;
  private NullTileCache mockCache;

  @Before
  public void setupMapzenApi() {
    MapZenApi.setApiKey("test");
    mapZenMock = mock(MapZenApi.class);
    MapZenApi.setInstance(mapZenMock);

    this.mockCache = mock(NullTileCache.class);
  }

  @Test
  public void itShouldRetrieveGeoTileFromCache() {
    TilesManager tilesManager = new TilesManager(mockCache);

    when(mockCache.retrieve(eq(0), eq(0))).thenReturn(new GeoTile());
    tilesManager.retrieve(0,0);

    try {
      verify(mapZenMock, times(0)).get(eq(0),eq(0), any(Callback.class));
    } catch (UnirestException e) {
      e.printStackTrace();
    }

    verify(mockCache, times(1)).retrieve(0,0);
  }

  @Test
  public void itShouldTransformMapZenLayersResultIntoGeoTile() {
    TilesManager tilesManager = new TilesManager(mockCache);

  }

  @Test
  public void itShouldFetchGeoTileUsingApiAndCacheIt() {
    TilesManager tilesManager = new TilesManager(mockCache);
    tilesManager.retrieve(1,1);

    ArgumentCaptor<GeoTile> geoTileArgument = ArgumentCaptor.forClass(GeoTile.class);

    verify(mockCache, times(1)).retrieve(1,1);
    verify(mockCache, times(1)).save(geoTileArgument.capture());
    try {
      verify(mapZenMock, times(1)).get(eq(1),eq(1), any(Callback.class));
    } catch (UnirestException e) {
      e.printStackTrace();
    }

    GeoTile geoTile = geoTileArgument.getValue();
    assertEquals("1/1", geoTile.getId());
  }
}
