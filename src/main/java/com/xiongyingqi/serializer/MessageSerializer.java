package com.xiongyingqi.serializer;

import com.xiongyingqi.vo.MessageVo;

/**
 * @author xiongyingqi
 * @since 2017/11/25
 */
public interface MessageSerializer {
  <T> String serialize(T instance);

  <T> T deserialize(String message, Class<T> type);
}
