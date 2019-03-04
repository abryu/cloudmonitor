package cc.cloudmonitor.objects.consumer;

import cc.cloudmonitor.objects.vm.VirtualMachine;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "Consumer")
public class  Consumer {

  public Long getCcId() {
    return ccId;
  }

  public void setCcId(Long ccId) {
    this.ccId = ccId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Long getBalance() {
    return balance;
  }

  public void setBalance(Long balance) {
    this.balance = balance;
  }

  private @Id
  @GeneratedValue
  Long ccId;

  @Column(unique = true)
  private String username;

  private String password;
  private Long balance;

  public static class Builder {

    private String username;
    private String password;
    private Long ccId;
    private Long balance;

    private List<VirtualMachine> ownedVMs;

    public Builder setUsername(String username) {
      this.username = username;
      return this;
    }

    public Builder setPassword(String password) {
      this.password = password;
      return this;
    }

    public Builder setCcId(long ccId) {
      this.ccId = ccId;
      return this;
    }

    public Builder setBalance(Long balance) {
      this.balance = balance;
      return this;
    }

    public Consumer build() {
      return new Consumer(this);
    }

  }

  private Consumer(Builder builder) {
    this.username = builder.username;
    this.password = builder.password;
    this.ccId = builder.ccId;
    this.balance = builder.balance;
  }

  public Consumer() {

  }
}
