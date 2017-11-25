package com.xiongyingqi.action;

import com.xiongyingqi.common.utils.scan.PackageScanner;
import com.xiongyingqi.vo.ErrorMessage;
import com.xiongyingqi.vo.RequestMessage;
import com.xiongyingqi.vo.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiongyingqi
 * @since 2017/11/25
 */
public class ActionManager {
  private static final Logger logger = LoggerFactory.getLogger(ActionManager.class);
  private Map<String, Action> actionMap;
  private Map<Class<? extends Action>, Class<? extends RequestMessage>> actionRequestMessageGenericTypeMap = new HashMap<>();
  private Map<Class<? extends Action>, Class<? extends ResponseMessage>> actionResponseMessageGenericTypeMap = new HashMap<>();


  private ActionManager() {
    try {
      init();
    } catch (Exception e) {
      logger.error("", e);
    }
  }

  private static class InstanceHolder {
    private static final ActionManager INSTANCE = new ActionManager();
  }

  public static ActionManager getInstance() {
    return InstanceHolder.INSTANCE;
  }

  @SuppressWarnings("unchecked")
  private void init() throws IllegalAccessException, InstantiationException {
    Collection<Class<?>> classes = PackageScanner.newScanner()
        .addPackage(getClass().getPackage())
        .andInterface(Action.class)
        .ignoreInterface(true)
        .recursive(true)
        .scan();
    actionMap = new HashMap<>();
    for (Class<?> actionClass : classes) {
      readGeneric((Class<Action>) actionClass);

      Action action = (Action) actionClass.newInstance();
      String actionType = action.actionType();
      logger.info("Found actionType: {} and instance: {}", actionType, action);
      Action exists = actionMap.put(actionType, action);
      if (exists != null) {
        logger.error("Already exists action: {} of actionType: {}", exists, actionType);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private void readGeneric(Class<? extends Action> actionClass) {
    Type[] genericInterfaces = actionClass.getGenericInterfaces();
    if (genericInterfaces == null || genericInterfaces.length < 1) {
      logger.error("Class: {} has not generic type!", actionClass);
      return;
    }
//    ParameterizedType[] parameterizedTypes = (ParameterizedType[]) genericInterfaces;
    ParameterizedType parameterizedType = (ParameterizedType) genericInterfaces[0];
    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
    if (actualTypeArguments == null || actualTypeArguments.length < 2) {
      logger.error("Class: {} hasn't two actualTypeArguments! size: {}", actionClass, Arrays.toString(actualTypeArguments));
      return;
    }

    Class<? extends RequestMessage> requestMessageType = (Class<? extends RequestMessage>) actualTypeArguments[0];
    Class<? extends ResponseMessage> responseMessageType = (Class<? extends ResponseMessage>) actualTypeArguments[1];
    logger.info("Found requestMessageType: {} and responseMessageType: {} from action class: {}", requestMessageType, responseMessageType, actionClass);
    actionRequestMessageGenericTypeMap.put(actionClass, requestMessageType);
    actionResponseMessageGenericTypeMap.put(actionClass, responseMessageType);
  }

  public Class<? extends RequestMessage> getRequestType(Class<? extends Action> actionClass) {
    return actionRequestMessageGenericTypeMap.get(actionClass);
  }

  public Class<? extends ResponseMessage> getResponseType(Class<? extends Action> actionClass) {
    return actionResponseMessageGenericTypeMap.get(actionClass);
  }

  public Action getAction(RequestMessage<?> requestMessage) {
    String actionType = requestMessage.getAction();
    Action action = actionMap.get(actionType);
    return action;
  }

  public ResponseMessage<?> doAction(RequestMessage<?> requestMessage) {
    Action action = getAction(requestMessage);
    if (action == null) {
      return ErrorMessage.errorMessage("Not found action: " + requestMessage.getAction());
    }
    try {
      ResponseMessage responseMessage = action.action(requestMessage);
      logger.info("Invoke action: {} request: {} response: {}", action, requestMessage, responseMessage);
      return responseMessage;
    } catch (Exception e) {
      logger.error("", e);
      return ErrorMessage.errorMessage("Error with message: " + e.getMessage());
    }
  }
}
