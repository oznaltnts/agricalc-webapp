package tr.ozanbey.agricalc.webapp.service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.CityCrop;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;

public interface CityCropRepository extends JpaRepository<CityCrop, Long> {

    CityCrop getByStatusAndCityIdAndCropId(EnumStatus status, Long cityId, Long cropId);

}
