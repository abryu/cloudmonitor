package cc.cloudmonitor.dao;

import cc.cloudmonitor.objects.consumer.Consumer;
import cc.cloudmonitor.objects.vm.VirtualMachine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ConsumerRepository extends JpaRepository<Consumer, Long> {

  @Query("SELECT c FROM Consumer c WHERE c.username = ?1 AND c.password = ?2")
  Optional<Consumer> findByCredentials(String username, String password);


}
