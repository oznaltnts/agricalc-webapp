package tr.ozanbey.agricalc.webapp.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.DairyCowIncome;

public interface DairyCowIncomeRepository extends JpaRepository<DairyCowIncome, Long> {


}
