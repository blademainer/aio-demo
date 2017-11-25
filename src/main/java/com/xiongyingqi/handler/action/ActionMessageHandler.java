package com.xiongyingqi.handler.action;

import com.xiongyingqi.action.Action;
import com.xiongyingqi.action.ActionManager;
import com.xiongyingqi.handler.MessageHandler;
import com.xiongyingqi.serializer.MessageSerializer;
import com.xiongyingqi.serializer.jackson.JacksonMessageSerializer;
import com.xiongyingqi.vo.ErrorMessage;
import com.xiongyingqi.vo.RequestMessage;
import com.xiongyingqi.vo.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xiongyingqi
 * @since 2017/11/25
 */
public class ActionMessageHandler implements MessageHandler {
  private static final Logger logger = LoggerFactory.getLogger(ActionMessageHandler.class);

  private MessageSerializer jacksonMessageSerializer = new JacksonMessageSerializer();
  private ActionManager actionManager = ActionManager.getInstance();
  private ActionMessageHandler() {
  }

  private static class InstanceHolder {
    private static final ActionMessageHandler INSTANCE = new ActionMessageHandler();
  }

  public static ActionMessageHandler getInstance() {
    return InstanceHolder.INSTANCE;
  }

  @Override
  public String handle(String message) {
    try {
      RequestMessage requestMessage = jacksonMessageSerializer.deserialize(message, RequestMessage.class);

      Action action = actionManager.getAction(requestMessage);
      if (action == null) {
        ErrorMessage errorMessage = ErrorMessage.errorMessage("Not found action: " + requestMessage.getAction());
        return jacksonMessageSerializer.serialize(errorMessage);
      }
      Class<? extends RequestMessage> requestType = actionManager.getRequestType(action.getClass());
      Class<? extends ResponseMessage> responseType = actionManager.getResponseType(action.getClass());

      RequestMessage request = jacksonMessageSerializer.deserialize(message, requestType);
      ResponseMessage<?> responseMessage = actionManager.doAction(request);
      String serialize = jacksonMessageSerializer.serialize(responseMessage);
      return serialize;
    } catch (Exception e){
      logger.error("", e);
      ErrorMessage errorMessage = ErrorMessage.errorMessage("Error with message: " + System.lineSeparator() + e.toString() + ": " + e.getMessage());
      return jacksonMessageSerializer.serialize(errorMessage);
    }

  }
}
