package com.xiongyingqi.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author xiongyingqi
 * @since 2017/11/25
 */
@Data
@EqualsAndHashCode
@ToString
public class RequestMessage<T> {
  private String messageId;
  private String action;
  private T requestBody;
}
