package com.xiongyingqi.io.server.handler;

import com.xiongyingqi.io.server.TcpServer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * @author xiongyingqi
 * @since 2017/11/25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class SessionContext {
  private static final Logger logger = LoggerFactory.getLogger(SessionContext.class);

  private ConnectionHandler connectionHandler;
  private AsynchronousServerSocketChannel serverSocketChannel;
  private AsynchronousSocketChannel socketChannel;
  private TcpServer tcpServer;
  private byte[] writeBytes;
  private byte[] readBytes;

  public void close() {
    if (socketChannel == null) {
      return;
    }
    try {
      socketChannel.close();
    } catch (IOException e) {
      logger.error("", e);
    } finally {
      int clientCount = tcpServer.disconnected();
      logger.info("Current clientSize: {}", clientCount);
    }
  }

  public void opened() {
    if (socketChannel == null) {
      logger.error("Please set socketChannel first!");
      return;
    }
    int clientCount = tcpServer.connected();
    logger.info("Current clientSize: {}", clientCount);
  }
}
