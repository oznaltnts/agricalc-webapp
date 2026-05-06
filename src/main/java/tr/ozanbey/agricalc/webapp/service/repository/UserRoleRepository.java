package tr.ozanbey.agricalc.webapp.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

}
