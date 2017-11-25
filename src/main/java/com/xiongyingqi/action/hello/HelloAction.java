package com.xiongyingqi.action.hello;

import com.xiongyingqi.action.Action;
import com.xiongyingqi.vo.RequestMessage;
import com.xiongyingqi.vo.ResponseMessage;
import com.xiongyingqi.vo.StringRequestMessage;
import com.xiongyingqi.vo.StringResponseMessage;

/**
 * @author xiongyingqi
 * @since 2017/11/25
 */
public class HelloAction implements Action<StringRequestMessage, StringResponseMessage> {

  @Override
  public String actionType() {
    return "hello";
  }

  @Override
  public StringResponseMessage action(StringRequestMessage requestMessage) {
    String requestBody = requestMessage.getRequestBody();
    StringResponseMessage responseMessage = new StringResponseMessage();
    responseMessage.setResponse("Hello!" + requestBody);
    responseMessage.setMessageId(requestMessage.getMessageId());
    return responseMessage;
  }

}
