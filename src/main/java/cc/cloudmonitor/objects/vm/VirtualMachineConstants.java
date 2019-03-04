package cc.cloudmonitor.objects.vm;

public class VirtualMachineConstants {

  public static final String VM_ID = "vmId";
  public static final String CC_ID = "ccId";
  public static final String VM_TYPE = "vmType";
  public static final String EVENT_TYPE = "eventType";
  public static final String EVENT_STAMP = "eventStamp";

  public static final String VM_TYPE_BASIC = "BASIC";
  public static final String VM_TYPE_LARGE = "LARGE";
  public static final String VM_TYPE_UL = "ULTRA-LARGE";

  public static final String EVENT_CREATED = "CREATED";
  public static final String EVENT_RUNNING = "RUNNING";
  public static final String EVENT_DELETED = "DELETED";

  public enum STATUS {
    CREATED("CREATED"),
    RUNNING("RUNNING"),
    DELETED("DELETED");
    private String value;
    private STATUS(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }
  }

  enum PRICE {

    BASIC(5),
    LARGE(10),
    ULTRA(15);

    private int value;

    private PRICE(int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }
  }

}
