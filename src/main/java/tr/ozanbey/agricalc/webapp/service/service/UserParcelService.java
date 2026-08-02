package tr.ozanbey.agricalc.webapp.service.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.ozanbey.agricalc.webapp.service.domain.UserPlantParcel;
import tr.ozanbey.agricalc.webapp.service.repository.UserPlantParcelRepository;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserParcelService {

    @Autowired
    private UserPlantParcelRepository plantParcelRepository;

    public List<UserPlantParcel> getPlantParcelList(Long userId) {
        return plantParcelRepository.findByUser_IdOrderByInsertDateAsc(userId);
    }

    public Optional<UserPlantParcel> getUserParcelByIdAndUserId(Long parcelId, Long userId) {
        return plantParcelRepository.findByIdAndUser_Id(parcelId, userId);
    }

}
