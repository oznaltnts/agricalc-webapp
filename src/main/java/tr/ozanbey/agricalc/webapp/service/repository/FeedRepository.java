package tr.ozanbey.agricalc.webapp.service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tr.ozanbey.agricalc.webapp.service.domain.Feed;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.webapp.view.DairyCowView;

import java.util.List;

public interface FeedRepository extends JpaRepository<Feed, Long> {

    @Query("""
            SELECT new tr.ozanbey.agricalc.webapp.webapp.view.DairyCowView(t1)
            FROM Feed t1
            WHERE t1.status = :status
            """)
    List<DairyCowView> findAllAsCowView(@Param("status") EnumStatus status);

}
