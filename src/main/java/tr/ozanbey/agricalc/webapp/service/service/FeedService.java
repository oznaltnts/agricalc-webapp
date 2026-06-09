package tr.ozanbey.agricalc.webapp.service.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.ozanbey.agricalc.webapp.service.domain.Feed;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.repository.FeedRepository;

import java.util.List;

@Service
@Slf4j
public class FeedService {

    @Autowired
    private FeedRepository feedRepository;

    public List<Feed> getFeedsByStatuses(EnumStatus[] statuses) {
        return feedRepository.findByStatusInOrderByFeedCategoryAscFeedTypeAsc(statuses);
    }

    @Transactional
    public void saveFeed(Feed selectedFeed) {
        feedRepository.save(selectedFeed);
    }

}
