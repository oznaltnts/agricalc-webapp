package tr.ozanbey.agricalc.webapp.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.Feed;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;

import java.util.List;

public interface FeedRepository extends JpaRepository<Feed, Long> {

    List<Feed> findByStatusInOrderByFeedCategoryAscFeedTypeAsc(EnumStatus[] statuses);

}
