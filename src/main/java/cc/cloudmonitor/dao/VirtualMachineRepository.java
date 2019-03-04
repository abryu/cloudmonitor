package cc.cloudmonitor.dao;

import cc.cloudmonitor.objects.vm.VirtualMachine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface VirtualMachineRepository extends JpaRepository<VirtualMachine, Long> {


  @Query("SELECT v FROM VirtualMachine v WHERE v.vmId = ?1 AND v.ccId = ?2")
  Optional<VirtualMachine> findByIds(Long vmId, Long ccId);

  @Query("SELECT v FROM VirtualMachine v WHERE  v.ccId = ?1")
  List<VirtualMachine> findAllByCcId(Long ccId);

  @Transactional
  @Modifying(clearAutomatically = true)
  @Query("UPDATE VirtualMachine v SET v.eventType = ?3 , v.eventStamp = ?4 WHERE v.vmId = ?1 AND v.ccId = ?2")
  int updateStatus(Long vmId, Long ccId, String newStatus, Timestamp newTimeStamp);

  @Transactional
  @Modifying(clearAutomatically = true)
  @Query("UPDATE VirtualMachine v SET v.vmType = ?3 , v.eventStamp = ?4 WHERE v.vmId = ?1 AND v.ccId = ?2")
  int updateType(Long vmId, Long ccId, String newType, Timestamp newTimeStamp);

  @Transactional
  @Modifying(clearAutomatically = true)
  @Query("UPDATE VirtualMachine v SET v.accumulatedBalance = ?3 WHERE v.vmId = ?1 AND v.ccId = ?2")
  int updateBalance(Long vmId, Long ccId, Long newBalance);

}
