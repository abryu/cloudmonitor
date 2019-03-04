package cc.cloudmonitor.objects.vm;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class VirtualMachineAdvice {

  @ResponseBody
  @ExceptionHandler(VirtualMachineNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String vmNotFoundHandler(VirtualMachineNotFoundException ex) {
    return ex.getMessage();
  }

  @ResponseBody
  @ExceptionHandler(VirtualMachineFailedOperationException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String vmFailedOperationHandler(VirtualMachineFailedOperationException ex) {
    return ex.getMessage();
  }

}
