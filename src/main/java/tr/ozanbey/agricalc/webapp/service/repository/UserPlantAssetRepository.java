package tr.ozanbey.agricalc.webapp.service.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.UserPlantAsset;

import java.util.List;

public interface UserPlantAssetRepository extends JpaRepository<UserPlantAsset, Long> {

    List<UserPlantAsset> findByUserId(Long userId);
    @EntityGraph(attributePaths = {"plantAssetDetailList"})

    void deleteByUserId(Long userId);

}
