package com.xiongyingqi.io.server.handler;

import com.xiongyingqi.io.server.TcpServer;
import com.xiongyingqi.io.server.handler.read.ReadHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * @author xiongyingqi
 * @since 2017/11/25
 */
public class ConnectionHandler implements CompletionHandler<AsynchronousSocketChannel, TcpServer> {
  private static final Logger logger = LoggerFactory.getLogger(ConnectionHandler.class);

  @Override
  public void completed(AsynchronousSocketChannel socketChannel, TcpServer tcpServer) {
//    TcpServer.getInstance().connected();
    tcpServer.getChannel().accept(tcpServer, new ConnectionHandler());
    try {
      SocketAddress remoteAddress = socketChannel.getRemoteAddress();
      logger.info("Accepting connection: {}", remoteAddress);

      ByteBuffer buffer = ByteBuffer.allocate(1024);

      SessionContext sessionContext = new SessionContext();
      sessionContext.setConnectionHandler(this);
      sessionContext.setTcpServer(tcpServer);
      sessionContext.setServerSocketChannel(tcpServer.getChannel());
      sessionContext.setSocketChannel(socketChannel);
      sessionContext.opened();

      socketChannel.read(buffer, buffer, new ReadHandler(sessionContext));
    } catch (IOException e) {
      logger.error("", e);
      try {
        socketChannel.close();
      } catch (IOException e1) {
        logger.error("", e);
      } finally {
        TcpServer.getInstance().disconnected();
      }

    }
  }

  @Override
  public void failed(Throwable exc, TcpServer attachment) {
    logger.error("", exc);
    attachment.latch.countDown();
  }
}
