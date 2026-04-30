package tr.ozanbey.agricalc.webapp.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.ozanbey.agricalc.webapp.service.dto.QuestionWithFirstAnswer;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.repository.CityCropQuestionAnswerRepository;
import tr.ozanbey.agricalc.webapp.service.repository.CityCropQuestionRepository;

import java.util.List;

@Service
public class CityCropQuestionService extends BaseService {

    @Autowired
    private CityCropQuestionRepository cityCropQuestionRepository;

    @Autowired
    private CityCropQuestionAnswerRepository cityCropQuestionAnswerRepository;

    public List<QuestionWithFirstAnswer> getDTOsByStatusAndCityCropId(EnumStatus status, Long cityCropId) {
        return cityCropQuestionRepository.findDTOsByStatusAndCityCropId(status.getValue(), cityCropId);
    }

}
