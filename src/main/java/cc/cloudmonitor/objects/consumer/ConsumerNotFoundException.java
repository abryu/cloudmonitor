package cc.cloudmonitor.objects.consumer;

public class ConsumerNotFoundException extends RuntimeException {

  public ConsumerNotFoundException(Long id) {
    super("Could not find consumer " + id);
  }

  public ConsumerNotFoundException(String username) {
    super("Could not find consumer with username " + username);
  }


}
