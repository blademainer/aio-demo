package com.xiongyingqi.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author xiongyingqi
 * @since 2017/11/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ResponseMessage<T> extends MessageVo {
  private T response;
}
