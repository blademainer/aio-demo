package com.xiongyingqi.serializer.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiongyingqi.serializer.MessageSerializer;
import com.xiongyingqi.vo.MessageVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author xiongyingqi
 * @since 2017/11/25
 */
public class JacksonMessageSerializer implements MessageSerializer {
  private static final Logger logger = LoggerFactory.getLogger(JacksonMessageSerializer.class);

  @Override
  public <T> String serialize(T messageVo) {
    try {
      String json = getMapper().writeValueAsString(messageVo);
      if (logger.isDebugEnabled()) {
        logger.debug("Write messageVo: {} to json: {}", messageVo, json);
      }
      return json;
    } catch (JsonProcessingException e) {
      logger.error("", e);
      return null;
    }
  }

  @Override
  public <T> T deserialize(String message, Class<T> type) {
    try {
      T messageVo = getMapper().readValue(message, type);
      if (logger.isDebugEnabled()) {
        logger.debug("Read message: {} to messageVo: {}", message, messageVo);
      }
      return messageVo;
    } catch (IOException e) {
      logger.error("", e);
      return null;
    }
  }

  private ObjectMapper getMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return objectMapper;
  }
}
