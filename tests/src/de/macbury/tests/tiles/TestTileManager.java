package de.macbury.tests.tiles;

import com.badlogic.gdx.Gdx;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import de.macbury.json.JsonHelper;
import de.macbury.model.GeoTile;
import de.macbury.server.tiles.TilesManager;
import de.macbury.server.tiles.cache.MemoryTileCache;
import de.macbury.server.tiles.cache.NullTileCache;
import de.macbury.server.tiles.mapzen.MapZenApi;
import de.macbury.server.tiles.mapzen.MapZenLayersResult;
import de.macbury.tests.support.GdxTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

import static org.mockito.Mockito.mock;

@RunWith(GdxTestRunner.class)
public class TestTileManager {
  private MapZenApi mapZenMock;

  private String fixture(String name) {
    return Gdx.files.internal("fixtures/mapzen_layers/"+name+".json").readString();
  }

  public HttpResponse<MapZenLayersResult> mockResponseWithFixture(String name) {
    MapZenLayersResult result = JsonHelper.fromJson(fixture("home"), MapZenLayersResult.class);
    return mockResponse(result);
  }

  public HttpResponse<MapZenLayersResult> mockResponse(MapZenLayersResult result) {
    HttpResponse<MapZenLayersResult> response = mock(HttpResponse.class);
    when(response.getBody()).thenReturn(result);
    return response;
  }

  @Before
  public void setupMapzenApi() {
    MapZenApi.setApiKey("test");
    mapZenMock = mock(MapZenApi.class);
    MapZenApi.setInstance(mapZenMock);
  }

  @Test
  public void itShouldRetrieveGeoTileFromCache() {
    TilesManager.Listener listener = mock(TilesManager.Listener.class);

    NullTileCache mockCache = mock(NullTileCache.class);
    TilesManager tilesManager = new TilesManager(mockCache);
    tilesManager.addListener(listener);

    when(mockCache.retrieve(eq(0), eq(0))).thenReturn(new GeoTile());
    tilesManager.retrieve(0,0);

    try {
      verify(mapZenMock, times(0)).get(eq(0),eq(0), any(Callback.class));
    } catch (UnirestException e) {
      e.printStackTrace();
    }

    verify(mockCache, times(1)).retrieve(0,0);
    verify(listener, times(1)).onTileRetrieve(any(GeoTile.class), eq(tilesManager));
  }

  @Test
  public void itShouldFetchGeoTileUsingApiAndCacheIt() {
    TilesManager.Listener listener = mock(TilesManager.Listener.class);
    MemoryTileCache cache     = new MemoryTileCache();
    TilesManager tilesManager = new TilesManager(cache);
    tilesManager.addListener(listener);
    tilesManager.retrieve(1,1);

    ArgumentCaptor<Callback> callbackArgumentCaptor = ArgumentCaptor.forClass(Callback.class);

    try {
      verify(mapZenMock, times(1)).get(eq(1),eq(1), callbackArgumentCaptor.capture());
    } catch (UnirestException e) {
      e.printStackTrace();
    }

    callbackArgumentCaptor.getValue().completed(mockResponseWithFixture("home"));

    assertNotNull(cache.retrieve(1,1));
    verify(listener, times(1)).onTileRetrieve(any(GeoTile.class), eq(tilesManager));
  }
}
