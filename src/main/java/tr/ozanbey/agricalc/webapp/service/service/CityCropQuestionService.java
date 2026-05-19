package tr.ozanbey.agricalc.webapp.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.ozanbey.agricalc.webapp.service.dto.QuestionWithFirstValue;
import tr.ozanbey.agricalc.webapp.service.repository.CityCropQuestionRepository;
import tr.ozanbey.agricalc.webapp.service.repository.CityCropQuestionValueRepository;

import java.util.List;

@Service
public class CityCropQuestionService extends BaseService {

    @Autowired
    private CityCropQuestionRepository cityCropQuestionRepository;

    @Autowired
    private CityCropQuestionValueRepository cityCropQuestionValueRepository;

    public List<QuestionWithFirstValue> getActiveDTOsByCityCropId(Long cityCropId) {
        return cityCropQuestionRepository.findActiveDTOsByCityCropId(cityCropId);
    }

}
