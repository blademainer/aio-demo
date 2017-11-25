package com.xiongyingqi.vo;

import com.xiongyingqi.serializer.jackson.JacksonMessageSerializer;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author xiongyingqi
 * @since 2017/11/25
 */
public class StringRequestMessageTest {
  @Test
  public void genJson() throws Exception {
    StringRequestMessage stringRequestMessage = new StringRequestMessage();
    stringRequestMessage.setAction("hello");
    stringRequestMessage.setRequestBody("nc");
    stringRequestMessage.setMessageId("1");
    JacksonMessageSerializer jacksonMessageSerializer = new JacksonMessageSerializer();
    String serialize = jacksonMessageSerializer.serialize(stringRequestMessage);
    System.out.println(serialize);
  }
}