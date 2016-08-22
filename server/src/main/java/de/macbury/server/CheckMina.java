package de.macbury.server;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import de.macbury.server.tiles.mapzen.MapZenApi;
import de.macbury.server.tiles.mapzen.MapZenLayersResult;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * Created by macbury on 16.08.16.
 */
public class CheckMina {
  public static void main(String[] args) {
    MapZenApi.setApiKey("vector-tiles-XmQtRkM");
    try {
      for (int i = 0; i < 100; i++) {
        System.err.println("Enq: " + i);
        MapZenApi.getTile(72839, 44399, new Callback<MapZenLayersResult>() {
          @Override
          public void completed(HttpResponse<MapZenLayersResult> response) {
            System.err.println(response.toString());
          }

          @Override
          public void failed(UnirestException e) {
            System.err.println(e);
          }

          @Override
          public void cancelled() {
            System.err.println();
          }
        });
      }

    } catch (UnirestException e) {
      e.printStackTrace();
    }
    /*IoAcceptor acceptor = new NioSocketAcceptor();
    acceptor.getFilterChain().addLast("logger", new LoggingFilter());
    acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter( new TextLineCodecFactory( Charset.forName( "UTF-8" ))));

    acceptor.setHandler(new TimeServerHandler());
    acceptor.getSessionConfig().setReadBufferSize( 2048 );
    acceptor.getSessionConfig().setIdleTime( IdleStatus.BOTH_IDLE, 10 );

    try {
      acceptor.bind( new InetSocketAddress(3000) );
      while(acceptor.isActive()) {
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      acceptor.dispose(false);
    }*/
  }

  private static class TimeServerHandler extends IoHandlerAdapter {

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
      System.out.println( "IDLE " + session.getIdleCount( status ));
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
      cause.printStackTrace();
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
      String str = message.toString();
      if( str.trim().equalsIgnoreCase("quit") ) {
        session.closeNow();
        return;
      }

      Date date = new Date();
      session.write( date.toString() );
      System.out.println("Message written...");
    }
  }
}
