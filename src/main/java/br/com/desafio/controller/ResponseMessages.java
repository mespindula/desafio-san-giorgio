package br.com.desafio.controller;

import br.com.desafio.controller.handler.Model;
import br.com.desafio.controller.handler.ResponseError;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ResponseMessages extends Model {

  private Set<String> messages;

  @JsonInclude(Include.NON_NULL)
  private List<ResponseError> errors;

  public ResponseMessages() {
    messages = new HashSet<>();
  }

  public ResponseMessages(final Set<String> messages, final List<ResponseError> errors) {
    this.messages = messages;
    this.errors = errors;
  }

  public void addMessage(final String message) {
    this.messages.add(message);
  }

  public void addError(final String exceptionCode, final String message) {
    errors = new ArrayList<>();
    this.errors.add(new ResponseError(exceptionCode, message));
  }

  public Set<String> getMessages() {
    return messages;
  }

  public void setMessages(final Set<String> messages) {
    this.messages = messages;
  }

  public List<ResponseError> getErrors() {
    return errors;
  }

  public void setErrors(final List<ResponseError> errors) {
    this.errors = errors;
  }
}
