package cc.cloudmonitor;

import cc.cloudmonitor.dao.VirtualMachineRepository;
import cc.cloudmonitor.objects.vm.VirtualMachine;
import cc.cloudmonitor.objects.vm.VirtualMachineConstants;
import cc.cloudmonitor.objects.vm.VirtualMachineNotFoundException;
import cc.cloudmonitor.util.Helper;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
public class VirtualMachineController {

  private final VirtualMachineRepository repository;

  VirtualMachineController(VirtualMachineRepository repository) {
    this.repository = repository;
  }

  @PostMapping("/vm/create")
  public VirtualMachine create(@RequestParam(name = VirtualMachineConstants.CC_ID, required = true) String ccId,
                               @RequestParam(name = VirtualMachineConstants.VM_TYPE, required = true,
                                       defaultValue = VirtualMachineConstants.VM_TYPE_BASIC) String vmType) {

    VirtualMachine virtualMachine = new VirtualMachine.Builder()
            .ccId(Long.valueOf(ccId))
            .vmType(vmType)
            .eventType(VirtualMachineConstants.STATUS.CREATED.getValue())
            .eventStamp(new Timestamp(System.currentTimeMillis()))
            .accumulatedBalance(Long.valueOf(0))
            .build();
    return repository.save(virtualMachine);

  }

  private VirtualMachine retrieveByIds(Long vmId, Long ccId) {

    return repository.findByIds(vmId, ccId)
            .orElseThrow(() -> new VirtualMachineNotFoundException(vmId, ccId));

  }

  @PostMapping("/vm/start")
  public VirtualMachine start(@RequestParam(name = VirtualMachineConstants.VM_ID, required = true) String vmId,
                              @RequestParam(name = VirtualMachineConstants.CC_ID, required = true) String ccId) {

    Long vm = Long.valueOf(vmId);
    Long cc = Long.valueOf(ccId);

    VirtualMachine virtualMachine = null;

    try {
      virtualMachine = retrieveByIds(vm, cc);
    } catch (VirtualMachineNotFoundException e) {
      throw e;
    }

    if (virtualMachine.getEventType().equals(VirtualMachineConstants.STATUS.DELETED.getValue()))
      throw new VirtualMachineNotFoundException();

    if (virtualMachine.getEventType().equals(VirtualMachineConstants.STATUS.RUNNING.getValue()))
      return virtualMachine;

    repository.updateStatus(vm, cc, VirtualMachineConstants.STATUS.RUNNING.getValue(), new Timestamp(System.currentTimeMillis()));

    return retrieveByIds(vm, cc);

  }

  @PostMapping("/vm/stop")
  public VirtualMachine stop(@RequestParam(name = VirtualMachineConstants.VM_ID, required = true) String vmId,
                             @RequestParam(name = VirtualMachineConstants.CC_ID, required = true) String ccId) {

    Long vm = Long.valueOf(vmId);
    Long cc = Long.valueOf(ccId);

    VirtualMachine virtualMachine = null;

    try {
      virtualMachine = retrieveByIds(vm, cc);
    } catch (VirtualMachineNotFoundException e) {
      throw e;
    }

    if (virtualMachine.getEventType().equals(VirtualMachineConstants.STATUS.DELETED.getValue()))
      throw new VirtualMachineNotFoundException();

    if (virtualMachine.getEventType().equals(VirtualMachineConstants.STATUS.CREATED.getValue()))
      return virtualMachine;

    Long cost = Helper.getUsageAndCost(virtualMachine) + virtualMachine.getAccumulatedBalance();

    // update consumer

    repository.updateBalance(vm, cc, cost);

    repository.updateStatus(vm, cc, VirtualMachineConstants.STATUS.CREATED.getValue(), new Timestamp(System.currentTimeMillis()));

    return retrieveByIds(vm, cc);
  }

  @DeleteMapping("/vm/delete")
  public VirtualMachine delete(@RequestParam(name = VirtualMachineConstants.VM_ID, required = true) String vmId,
                               @RequestParam(name = VirtualMachineConstants.CC_ID, required = true) String ccId) {

    Long vm = Long.valueOf(vmId);
    Long cc = Long.valueOf(ccId);

    VirtualMachine virtualMachine = null;

    try {
      virtualMachine = retrieveByIds(vm, cc);
    } catch (VirtualMachineNotFoundException e) {
      throw e;
    }

    if (virtualMachine.getEventType().equals(VirtualMachineConstants.STATUS.DELETED.getValue()))
      return virtualMachine;

    if (virtualMachine.getEventType().equals(VirtualMachineConstants.STATUS.RUNNING.getValue())) {
      stop(vmId, ccId);
      return delete(vmId, ccId);
    }

    // if created
    repository.updateStatus(vm, cc, VirtualMachineConstants.STATUS.DELETED.getValue(), new Timestamp(System.currentTimeMillis()));
    return retrieveByIds(vm, cc);
  }

  @PostMapping("/vm/upgrade")
  public VirtualMachine upgrade(@RequestParam(name = VirtualMachineConstants.VM_ID, required = true) String vmId,
                                @RequestParam(name = VirtualMachineConstants.CC_ID, required = true) String ccId) {

    Long vm = Long.valueOf(vmId);
    Long cc = Long.valueOf(ccId);

    VirtualMachine virtualMachine = null;

    try {
      virtualMachine = retrieveByIds(vm, cc);
    } catch (VirtualMachineNotFoundException e) {
      throw e;
    }

    if (virtualMachine.getVmType().equals(VirtualMachineConstants.VM_TYPE_UL))
      return virtualMachine;

    String newVmtype = "";
    if (virtualMachine.getVmType().equals(VirtualMachineConstants.VM_TYPE_LARGE))
      newVmtype = VirtualMachineConstants.VM_TYPE_UL;
    else
      newVmtype = VirtualMachineConstants.VM_TYPE_LARGE;

    // update consumer
    long cost = Helper.getUsageAndCost(virtualMachine) + virtualMachine.getAccumulatedBalance();

    repository.updateBalance(vm, cc, cost);
    repository.updateType(vm, cc, newVmtype, new Timestamp(System.currentTimeMillis()));

    return retrieveByIds(vm, cc);
  }

  @PostMapping("/vm/downgrade")
  public VirtualMachine downgrade(@RequestParam(name = VirtualMachineConstants.VM_ID, required = true) String vmId,
                                  @RequestParam(name = VirtualMachineConstants.CC_ID, required = true) String ccId) {

    Long vm = Long.valueOf(vmId);
    Long cc = Long.valueOf(ccId);

    VirtualMachine virtualMachine = null;

    try {
      virtualMachine = retrieveByIds(vm, cc);
    } catch (VirtualMachineNotFoundException e) {
      throw e;
    }

    if (virtualMachine.getVmType().equals(VirtualMachineConstants.VM_TYPE_BASIC))
      return virtualMachine;

    String newVmtype = "";
    if (virtualMachine.getVmType().equals(VirtualMachineConstants.VM_TYPE_LARGE))
      newVmtype = VirtualMachineConstants.VM_TYPE_BASIC;
    else
      newVmtype = VirtualMachineConstants.VM_TYPE_LARGE;

    // update consumer
    long cost = Helper.getUsageAndCost(virtualMachine) + virtualMachine.getAccumulatedBalance();

    repository.updateBalance(vm, cc, cost);
    repository.updateType(vm, cc, newVmtype, new Timestamp(System.currentTimeMillis()));

    return retrieveByIds(vm, cc);
  }

  @GetMapping("/vm/all")
  public List<VirtualMachine> get(@RequestParam(name = VirtualMachineConstants.CC_ID, required = true) String ccID) {
    return repository.findAllByCcId(Long.valueOf(ccID));
  }


}
