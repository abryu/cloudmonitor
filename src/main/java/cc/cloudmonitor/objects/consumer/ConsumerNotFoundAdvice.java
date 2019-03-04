package cc.cloudmonitor.objects.consumer;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class ConsumerNotFoundAdvice {

  @ResponseBody
  @ExceptionHandler(ConsumerNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String consumerNotFoundHandler(ConsumerNotFoundException ex) {
    return ex.getMessage();
  }
}