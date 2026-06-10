package tr.ozanbey.agricalc.webapp.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tr.ozanbey.agricalc.webapp.service.domain.UserDairyCowIncome;
import tr.ozanbey.agricalc.webapp.webapp.view.DairyCowIncomeView;

import java.util.List;

public interface UserDairyCowIncomeRepository extends JpaRepository<UserDairyCowIncome, Long> {

    @Query("""
            SELECT new tr.ozanbey.agricalc.webapp.webapp.view.DairyCowIncomeView(
                        t2.id, t1.id, t1.name, t1.unit, t2.incomeValue
                    )
            FROM DairyCowIncome t1
            left join UserDairyCowIncome t2 on t1.id = t2.dairyCowIncome.id and t2.userDairyCowBarn.id = :barnId
            ORDER BY t1.id
            """)
    List<DairyCowIncomeView> findAsViewListByBarnId(@Param("barnId") Long barnId);

}
