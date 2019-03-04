package cc.cloudmonitor.objects.vm;

public class VirtualMachineNotFoundException extends RuntimeException {

  public VirtualMachineNotFoundException() {
    super("Could not find VM ");
  }

  public VirtualMachineNotFoundException(Long vmId, Long ccId) {
    super("Could not find VM with this user " + vmId + " " + ccId);
  }

}



