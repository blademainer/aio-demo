package com.xiongyingqi.io.server.handler.write;

import com.xiongyingqi.io.server.handler.SessionContext;
import com.xiongyingqi.io.server.handler.read.ReadHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * @author xiongyingqi
 * @since 2017/11/25
 */
public class WriteHandler implements CompletionHandler<Integer, ByteBuffer> {
  private static final Logger logger = LoggerFactory.getLogger(WriteHandler.class);

  private SessionContext sessionContext;

  public WriteHandler(SessionContext sessionContext) {
    this.sessionContext = sessionContext;
  }

  @Override
  public void completed(Integer result, ByteBuffer buffer) {
    AsynchronousSocketChannel channel = sessionContext.getSocketChannel();
    try {
      //如果没有发送完，就继续发送直到完成
      if (buffer.hasRemaining()) {
        channel.write(buffer, buffer, this);

      } else {
        //创建新的Buffer
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        //异步读  第三个参数为接收消息回调的业务Handler
        channel.read(readBuffer, readBuffer, new ReadHandler(sessionContext));

      }
    } catch (Exception e) {
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
