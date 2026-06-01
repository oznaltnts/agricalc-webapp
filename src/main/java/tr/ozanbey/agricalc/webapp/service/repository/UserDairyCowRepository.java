package tr.ozanbey.agricalc.webapp.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.UserDairyCow;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;

import java.util.List;

public interface UserDairyCowRepository extends JpaRepository<UserDairyCow, Long> {

    List<UserDairyCow> findByStatusInAndUser_Id(EnumStatus[] status, Long userId);

}
