package de.macbury.tests;

import com.mashape.unirest.http.HttpMethod;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import de.macbury.server.tiles.mapzen.MapZenApi;
import de.macbury.server.tiles.mapzen.MapZenLayersResult;
import de.macbury.tests.support.BaseTest;
import static junit.framework.Assert.*;

import de.macbury.tests.support.MockHttpClient;
import org.apache.http.client.HttpClient;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
import org.apache.http.nio.protocol.HttpAsyncResponseConsumer;
import org.apache.http.protocol.HttpContext;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.concurrent.Future;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TestHttpMock {



  @Test
  public void itShouldReturnMockedStuff() throws UnirestException {
    MapZenApi.setApiKey("test");

    MapZenApi api = mock(MapZenApi.class);
    MapZenApi.setInstance(api);

    ArgumentCaptor<Callback> callbackArg = ArgumentCaptor.forClass(Callback.class);

    when(api.get(anyInt(), anyInt(), callbackArg.capture())).thenReturn(null);

    HttpResponse<MapZenLayersResult> apiResponse = mock(HttpResponse.class);
    when(apiResponse.getBody()).thenReturn(new MapZenLayersResult());
    //verify(resultCallback, times(1)).completed(apiResponse);

    MapZenApi.getTile(0, 0, new Callback<MapZenLayersResult>() {
      @Override
      public void completed(HttpResponse<MapZenLayersResult> response) {
        assertNotNull(response.getBody());
      }

      @Override
      public void failed(UnirestException e) {

      }

      @Override
      public void cancelled() {

      }
    });

    callbackArg.getValue().completed(apiResponse);
  }
}
