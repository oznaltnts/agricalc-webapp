package tr.ozanbey.agricalc.webapp.service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.UserPreference;


public interface UserPreferenceRepository extends JpaRepository<UserPreference, Long> {

    UserPreference findByUserId(Long userId);

}
