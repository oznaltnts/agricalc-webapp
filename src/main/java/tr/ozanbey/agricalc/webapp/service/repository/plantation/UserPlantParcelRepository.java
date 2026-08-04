package tr.ozanbey.agricalc.webapp.service.repository.plantation;

import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantParcel;

import java.util.List;
import java.util.Optional;

public interface UserPlantParcelRepository extends JpaRepository<UserPlantParcel, Long> {

    List<UserPlantParcel> findByUser_IdOrderByInsertDateAsc(Long userId);

    void deleteById(Long recordId);

    Optional<UserPlantParcel> findByIdAndUser_Id(Long id, Long userId);

}
