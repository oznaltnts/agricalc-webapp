package tr.ozanbey.agricalc.webapp.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.ozanbey.agricalc.webapp.service.domain.CityCropQuestion;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.repository.CityCropQuestionRepository;

import java.util.List;

@Service
public class CityCropQuestionService extends BaseService {

    @Autowired
    private CityCropQuestionRepository cityCropQuestionRepository;

    public List<CityCropQuestion> getByStatusAndCityCropIdOrderByQuestionId(EnumStatus status, Long cityCropId) {
        return cityCropQuestionRepository.findByStatusAndCityCropIdOrderByQuestionId(status, cityCropId);
    }
}
