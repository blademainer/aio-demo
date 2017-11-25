package com.xiongyingqi.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author xiongyingqi
 * @since 2017/11/25
 */
@Data
@ToString
@EqualsAndHashCode
public class MessageVo {
  private String messageId;
  private String status;
  private String describe;
}
