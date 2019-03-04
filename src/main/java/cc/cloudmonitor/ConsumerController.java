package cc.cloudmonitor;

import cc.cloudmonitor.dao.VirtualMachineRepository;
import cc.cloudmonitor.objects.consumer.ConsumerNotFoundException;
import cc.cloudmonitor.dao.ConsumerRepository;
import cc.cloudmonitor.objects.consumer.Consumer;
import cc.cloudmonitor.objects.consumer.ConsumerConstants;
import cc.cloudmonitor.objects.vm.VirtualMachine;
import cc.cloudmonitor.objects.vm.VirtualMachineConstants;
import cc.cloudmonitor.objects.vm.VirtualMachineNotFoundException;
import cc.cloudmonitor.util.Helper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ConsumerController {

  private final ConsumerRepository repository;
  private final VirtualMachineRepository virtualMachineRepository;

  ConsumerController(ConsumerRepository repository, VirtualMachineRepository virtualMachineRepository) {
    this.repository = repository;
    this.virtualMachineRepository = virtualMachineRepository;
  }

  @PostMapping("/consumer")
  public Consumer create(@RequestParam(name = ConsumerConstants.USERNAME, required = true) String username,
                         @RequestParam(name = ConsumerConstants.PASSWORD, required = true) String password) {

    try {

      return retrieveByCredentials(username, password);

    } catch (ConsumerNotFoundException e) {

      System.out.println("Consumer does not exist ; Creating ...");

      Consumer newConsumer = new Consumer.Builder()
              .setUsername(username)
              .setPassword(password)
              .setBalance((long) 0.0)
              .build();
      return repository.save(newConsumer);

    }

  }

  @GetMapping("/consumer/{id}")
  public Consumer retrieveById(@PathVariable String id) {

    return repository.findById(Long.valueOf(id))
            .orElseThrow(() -> new ConsumerNotFoundException(id));

  }

  @GetMapping("/consumer")
  public Consumer retrieveByCredentials(@RequestParam(name = ConsumerConstants.USERNAME, required = true) String username,
                                        @RequestParam(name = ConsumerConstants.PASSWORD, required = true) String password) {

    return repository.findByCredentials(username, password).orElseThrow(() -> new ConsumerNotFoundException(username));

  }


  @GetMapping("/consumer/{id}/usage")
  public VirtualMachine getUsage(@PathVariable String id,
                                 @RequestParam(name = VirtualMachineConstants.VM_ID, required = true) String vmId) {

    Consumer consumer = retrieveById(id);

    List<VirtualMachine> vms = virtualMachineRepository.findAllByCcId(consumer.getCcId());

    for (VirtualMachine v : vms) {
      if (v.getVmId() == Long.valueOf(vmId)) {

        Long cost = Helper.getUsageAndCost(v) + v.getAccumulatedBalance();
        //virtualMachineRepository.updateBalance(Long.valueOf(vmId), consumer.getCcId(), cost);
        return v;

      }
    }

    throw new VirtualMachineNotFoundException();
  }

  @GetMapping("/consumer/{id}/all")
  public List<VirtualMachine> getAllUsage(@PathVariable String id) {

    Consumer consumer = retrieveById(id);

    List<VirtualMachine> vms = virtualMachineRepository.findAllByCcId(Long.valueOf(id));

    if (vms == null)
      throw new VirtualMachineNotFoundException();

    for (VirtualMachine v : vms) {

      Long cost = Helper.getUsageAndCost(v) + v.getAccumulatedBalance();
      //virtualMachineRepository.updateBalance(Long.valueOf(v.getVmId()), consumer.getCcId(), cost);
      v.setAccumulatedBalance(cost);

    }

    return vms;

  }

}
