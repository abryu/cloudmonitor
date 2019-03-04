package cc.cloudmonitor.util;

import cc.cloudmonitor.objects.vm.VirtualMachine;
import cc.cloudmonitor.objects.vm.VirtualMachineConstants;

import java.sql.Timestamp;

public class Helper {

  public static Long getUsageAndCost(VirtualMachine vm) {

    if (vm.getEventType().equals(VirtualMachineConstants.STATUS.DELETED.getValue())
            || vm.getEventType().equals(VirtualMachineConstants.STATUS.CREATED.getValue()))
      return Long.valueOf(0);

    Timestamp old = vm.getEventStamp();
    Timestamp current = new Timestamp(System.currentTimeMillis());
    Long diff = (current.getTime() - old.getTime()) / 60000;
    return diff * vm.getCostPerMinByType();

  }

}
