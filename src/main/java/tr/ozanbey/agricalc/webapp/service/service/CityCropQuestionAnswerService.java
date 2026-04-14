package tr.ozanbey.agricalc.webapp.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.ozanbey.agricalc.webapp.service.domain.CityCropQuestionAnswer;
import tr.ozanbey.agricalc.webapp.service.repository.CityCropQuestionAnswerRepository;

import java.util.List;

@Service
public class CityCropQuestionAnswerService {

    @Autowired
    private CityCropQuestionAnswerRepository cityCropQuestionAnswerRepository;

    public List<CityCropQuestionAnswer> getByCityCropQuestionId(Long cityCropQuestionId) {
        return cityCropQuestionAnswerRepository.findByCityCropQuestionId(cityCropQuestionId);
    }
}
