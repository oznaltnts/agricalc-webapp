package tr.ozanbey.agricalc.webapp.service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.UserInformation;


public interface UserInformationRepository extends JpaRepository<UserInformation, Long> {

    UserInformation findByUserId(Long userId);

}
