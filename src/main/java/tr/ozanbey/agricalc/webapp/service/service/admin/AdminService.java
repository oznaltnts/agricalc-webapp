package tr.ozanbey.agricalc.webapp.service.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationCoefficient;
import tr.ozanbey.agricalc.webapp.service.repository.plantation.PlantationCoefficientRepository;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private PlantationCoefficientRepository plantationCoefficientRepository;

    public AdminService() {
    }

    public List<PlantationCoefficient> getPlantationCoefficientList() {
        return plantationCoefficientRepository.findAll();
    }

}
