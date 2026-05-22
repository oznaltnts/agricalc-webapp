package tr.ozanbey.agricalc.webapp.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.UserPlantParcel;

import java.util.List;

public interface UserPlantParcelRepository extends JpaRepository<UserPlantParcel, Long> {

    List<UserPlantParcel> findByUser_IdOrderByInsertDateAsc(Long userId);

    void deleteById(Long recordId);

}
