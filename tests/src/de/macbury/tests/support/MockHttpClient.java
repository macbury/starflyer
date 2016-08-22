package de.macbury.tests.support;

import com.mashape.unirest.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
import org.apache.http.nio.protocol.HttpAsyncResponseConsumer;
import org.apache.http.protocol.HttpContext;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by macbury on 22.08.16.
 */
public class MockHttpClient<ResponseType> extends CloseableHttpAsyncClient {
  private final ResponseType responseContent;

  public MockHttpClient(ResponseType responseContent) {
    this.responseContent = responseContent;
  }

  @Override
  public boolean isRunning() {
    return false;
  }

  @Override
  public void start() {

  }

  @Override
  public void close() throws IOException {

  }

  @Override
  public <T> Future<T> execute(HttpAsyncRequestProducer requestProducer, HttpAsyncResponseConsumer<T> responseConsumer, HttpContext context, FutureCallback<T> callback) {
    HttpResponse<ResponseType> response = Mockito.mock(HttpResponse.class);
    Mockito.when(response.getBody()).thenReturn(responseContent);
    Future<T> future = Mockito.mock(Future.class);
    Mockito.when(future.isDone()).thenReturn(true);
    try {
      Mockito.when(future.get()).thenReturn((T)response);
      Mockito.when(future.get(Mockito.anyInt(), Mockito.any(TimeUnit.class))).thenReturn((T)response);
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    } catch (TimeoutException e) {
      e.printStackTrace();
    }
    callback.completed((T)responseContent);
    return future;
  }
}
