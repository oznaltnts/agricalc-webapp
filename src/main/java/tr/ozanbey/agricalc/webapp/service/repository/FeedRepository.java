package tr.ozanbey.agricalc.webapp.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.Feed;

public interface FeedRepository extends JpaRepository<Feed, Long> {

}
