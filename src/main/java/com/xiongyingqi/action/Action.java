package com.xiongyingqi.action;

import com.xiongyingqi.vo.RequestMessage;
import com.xiongyingqi.vo.ResponseMessage;

/**
 * @author xiongyingqi
 * @since 2017/11/25
 */
public interface Action<Q extends RequestMessage, P extends ResponseMessage> {
  String actionType();

  P action(Q requestMessage);
}
