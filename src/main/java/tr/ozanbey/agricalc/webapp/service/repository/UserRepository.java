package tr.ozanbey.agricalc.webapp.service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.User;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByPhoneAndStatus(String phone, EnumStatus status);

    Optional<User> findByPhone(String phone);

    Optional<User> findByEmail(String email);

}
