package cc.cloudmonitor.objects.vm;

import cc.cloudmonitor.objects.consumer.Consumer;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "VirtualMachine")
public class VirtualMachine {

  public Long getVmId() {
    return vmId;
  }

  public void setVmId(Long vmId) {
    this.vmId = vmId;
  }

  public Long getCcId() {
    return ccId;
  }

  public void setCcId(Long ccId) {
    this.ccId = ccId;
  }

  public String getVmType() {
    return vmType;
  }

  public void setVmType(String vmType) {
    this.vmType = vmType;
  }

  public String getEventType() {
    return eventType;
  }

  public void setEventType(String eventType) {
    this.eventType = eventType;
  }

  public Timestamp getEventStamp() {
    return eventStamp;
  }

  public void setEventStamp(Timestamp eventStamp) {
    this.eventStamp = eventStamp;
  }

  private @Id
  @GeneratedValue
  Long vmId;

  private Long ccId;
  private String vmType;
  private String eventType;
  private Timestamp eventStamp;

  public Long getAccumulatedBalance() {
    return accumulatedBalance;
  }

  public void setAccumulatedBalance(Long accumulatedBalance) {
    this.accumulatedBalance = accumulatedBalance;
  }

  private Long accumulatedBalance;

  private VirtualMachine(Builder builder) {

    this.vmId = builder.vmId;
    this.ccId = builder.ccId;
    this.vmType = builder.vmType;
    this.eventType = builder.eventType;
    this.eventStamp = builder.eventStamp;
    this.accumulatedBalance = builder.accumulatedBalance;
  }


  public static class Builder {

    private Long accumulatedBalance;
    private Long vmId;
    private Long ccId;
    private String vmType;
    private String eventType;
    private Timestamp eventStamp;
    private String description;

    public Builder accumulatedBalance(Long accumulatedBalance) {
      this.accumulatedBalance = accumulatedBalance;
      return this;
    }

    public Builder vmId(Long vmId) {
      this.vmId = vmId;
      return this;
    }

    public Builder ccId(Long ccId) {
      this.ccId = ccId;
      return this;
    }

    public Builder vmType(String vmType) {
      this.vmType = vmType;
      return this;
    }

    public Builder eventType(String eventType) {
      this.eventType = eventType;
      return this;
    }

    public Builder eventStamp(Timestamp eventStamp) {
      this.eventStamp = eventStamp;
      return this;
    }

    public VirtualMachine build() {
      return new VirtualMachine(this);
    }

  }

  public int getCostPerMinByType() {
    if (this.vmType.equals(VirtualMachineConstants.VM_TYPE_BASIC))
      return VirtualMachineConstants.PRICE.BASIC.getValue();
    if (this.vmType.equals(VirtualMachineConstants.VM_TYPE_LARGE))
      return VirtualMachineConstants.PRICE.LARGE.getValue();
    return VirtualMachineConstants.PRICE.ULTRA.getValue();

  }


  public VirtualMachine() {

  }
}
