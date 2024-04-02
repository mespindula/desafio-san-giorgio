package br.com.desafio.domain.exception;

public class DataNotFoundException extends RuntimeException {

  public DataNotFoundException() {
    super();
  }

  public DataNotFoundException(final String msg) {
    super(msg);
  }

}