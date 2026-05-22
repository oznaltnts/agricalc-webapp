package tr.ozanbey.agricalc.webapp.service.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.UserPlantAsset;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumPlantAsset;

import java.util.List;

public interface UserPlantAssetRepository extends JpaRepository<UserPlantAsset, Long> {

    @EntityGraph(attributePaths = {"plantAssetDetailList"})
    List<UserPlantAsset> findByUser_IdOrderByInsertDateAsc(Long userId);

    void deleteByPlantAssetAndUser_Id(EnumPlantAsset plantAsset, Long userId);

}
