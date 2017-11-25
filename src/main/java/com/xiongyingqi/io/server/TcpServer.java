package com.xiongyingqi.io.server;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.xiongyingqi.io.server.handler.ConnectionHandler;

/**
 * @author xiongyingqi
 * @since 2017/11/25
 */
public class TcpServer extends Thread {
  private static final Logger logger = LoggerFactory.getLogger(TcpServer.class);
  public static final String ENCODING = "UTF-8";
  private static AtomicInteger clientSize = new AtomicInteger();
  public CountDownLatch latch = new CountDownLatch(1);
  private AsynchronousChannelGroup channelGroup;
  private AsynchronousServerSocketChannel channel;

  private TcpServer() {
  }

  private static class InstanceHolder {
    private static final TcpServer INSTANCE = new TcpServer();
  }

  public static TcpServer getInstance() {
    return InstanceHolder.INSTANCE;
  }

  public void startSocket() throws IOException, InterruptedException {
    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 20, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10), new ThreadFactoryBuilder().setNameFormat("tcp-server-%d").build());
    channelGroup = AsynchronousChannelGroup.withThreadPool(threadPoolExecutor);
    channel = AsynchronousServerSocketChannel.open(channelGroup).bind(new InetSocketAddress("0.0.0.0", 8888));
    channel.accept(this, new ConnectionHandler());
    logger.info("Started server! ");
  }

  @Override
  public void run() {
    try {
      startSocket();
      latch.await();
    } catch (Exception e) {
      logger.error("", e);
    }
  }

  public int disconnected() {
    return clientSize.decrementAndGet();
  }


  public int connected() {
    return clientSize.incrementAndGet();
  }

  public CountDownLatch getLatch() {
    return latch;
  }

  public AsynchronousChannelGroup getChannelGroup() {
    return channelGroup;
  }

  public AsynchronousServerSocketChannel getChannel() {
    return channel;
  }

  public static void main(String[] args) throws Exception {
    TcpServer.getInstance().start();
  }

}
