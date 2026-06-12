package tr.ozanbey.agricalc.webapp.service.repository.animal.dairycow;

import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.animal.dairycow.Feed;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;

import java.util.List;

public interface FeedRepository extends JpaRepository<Feed, Long> {

    List<Feed> findByStatusInOrderByFeedCategoryAscFeedTypeAsc(EnumStatus[] statuses);

}
