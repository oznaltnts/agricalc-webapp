package tr.ozanbey.agricalc.webapp.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tr.ozanbey.agricalc.webapp.service.domain.literacy.LiteracyQuestion;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.repository.literacy.LiteracyQuestionOptionRepository;
import tr.ozanbey.agricalc.webapp.service.repository.literacy.LiteracyQuestionRepository;
import tr.ozanbey.agricalc.webapp.service.repository.literacy.UserLiteracyAnswerRepository;
import tr.ozanbey.agricalc.webapp.service.repository.literacy.UserLiteracyAssessmentRepository;

import java.util.List;

@Service
public class LiteracyService {

    @Autowired
    private LiteracyQuestionRepository literacyQuestionRepository;

    @Autowired
    private LiteracyQuestionOptionRepository literacyQuestionOptionRepository;

    @Autowired
    private UserLiteracyAssessmentRepository userLiteracyAssessmentRepository;

    @Autowired
    private UserLiteracyAnswerRepository userLiteracyAnswerRepository;

    @Cacheable(value = "publicQuestions")
    public List<LiteracyQuestion> getLiteracyQuestionListByStatus(EnumStatus status) {
        return literacyQuestionRepository.findByStatus(status);
    }

    @CacheEvict(value = "publicQuestions", allEntries = true)
    public void clearQuestionCache() {
        // Admin güncellemesinde cache sıfırlama
    }

}
