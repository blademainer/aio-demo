package com.xiongyingqi.vo;

/**
 * @author xiongyingqi
 * @since 2017/11/25
 */
public class ErrorMessage extends ResponseMessage<String> {
  public static ErrorMessage errorMessage(String message) {
    ErrorMessage errorMessage = new ErrorMessage();
    errorMessage.setResponse(message);
    return errorMessage;
  }
}
