package tr.ozanbey.agricalc.webapp.service.repository.animal.dairycow;

import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.animal.dairycow.DairyCow;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;

import java.util.List;

public interface DairyCowRepository extends JpaRepository<DairyCow, Long> {

    List<DairyCow> findByStatusIn(EnumStatus[] statuses);

}
