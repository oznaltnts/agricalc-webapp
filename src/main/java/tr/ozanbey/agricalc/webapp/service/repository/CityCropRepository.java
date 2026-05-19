package tr.ozanbey.agricalc.webapp.service.repository;


import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.CityCrop;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;

import java.util.List;

public interface CityCropRepository extends JpaRepository<CityCrop, Long> {

    @EntityGraph(attributePaths = {"city", "crop"})
    List<CityCrop> getByStatusAndCrop_StatusOrderByCity_CodeAscCrop_NameAsc(EnumStatus status, EnumStatus cropStatus);

}
