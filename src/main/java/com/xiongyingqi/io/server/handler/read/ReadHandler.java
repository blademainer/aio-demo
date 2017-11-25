package com.xiongyingqi.io.server.handler.read;

import com.xiongyingqi.handler.action.ActionMessageHandler;
import com.xiongyingqi.io.server.TcpServer;
import com.xiongyingqi.io.server.handler.ConnectionHandler;
import com.xiongyingqi.io.server.handler.SessionContext;
import com.xiongyingqi.io.server.handler.write.WriteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * @author xiongyingqi
 * @since 2017/11/25
 */
public class ReadHandler implements CompletionHandler<Integer, ByteBuffer> {
  private static final Logger logger = LoggerFactory.getLogger(ReadHandler.class);
  private SessionContext sessionContext;

  public ReadHandler(SessionContext sessionContext) {
    this.sessionContext = sessionContext;
  }

  @Override
  public void completed(Integer result, ByteBuffer attachment) {
    //flip操作
    attachment.flip();
    //根据
    byte[] message = new byte[attachment.remaining()];
    attachment.get(message);
    sessionContext.setReadBytes(message);

    try {
      String requestMessage = new String(message, TcpServer.ENCODING);
      logger.debug("Read result: {} buffer: {}", result, requestMessage);

      String response = ActionMessageHandler.getInstance().handle(requestMessage);

      byte[] bytes = response.getBytes(TcpServer.ENCODING);
      ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
      writeBuffer.put(bytes);
      writeBuffer.flip();
      sessionContext.getSocketChannel().write(writeBuffer, writeBuffer, new WriteHandler(sessionContext));
//    } catch (UnsupportedEncodingException e) {
//      logger.error("", e);
    } catch (IOException e) {
      logger.error("", e);
      sessionContext.close();
    }
  }

  @Override
  public void failed(Throwable exc, ByteBuffer attachment) {
    logger.error("", exc);
    sessionContext.close();
  }
}
