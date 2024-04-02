package br.com.desafio.domain.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class JsonMapper {

  private final ObjectMapper objectMapper;

  @Autowired
  public JsonMapper(final ObjectMapper objectMapper) {
    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    objectMapper.registerModule(new JavaTimeModule());
    this.objectMapper = objectMapper;
  }

  public <T> T stringAsObject(final String json, final Class<T> clazz) throws IOException {
    return objectMapper.readValue(json, clazz);
  }

  public String objectAsString(final Object object) throws IOException {
    return objectMapper.writeValueAsString(object);
  }
}
