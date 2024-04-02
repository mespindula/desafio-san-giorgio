package br.com.desafio.controller.handler;

public class ResponseError extends Model {

  private final String code;
  private final String message;

  public ResponseError(final String code, final String message) {
    this.code = code;
    this.message = message;
  }

  public String getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }
}
