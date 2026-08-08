package tr.ozanbey.agricalc.webapp.service.service.plantation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.ozanbey.agricalc.webapp.service.domain.User;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantParcel;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumParcelType;
import tr.ozanbey.agricalc.webapp.service.repository.CityRepository;
import tr.ozanbey.agricalc.webapp.service.repository.plantation.PlantationProductRepository;
import tr.ozanbey.agricalc.webapp.service.repository.plantation.UserPlantParcelRepository;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserPlantParcelService {

    @Autowired
    private UserPlantParcelRepository userPlantParcelRepository;

    @Autowired
    private PlantationProductRepository plantationProductRepository;

    @Autowired
    private CityRepository cityRepository;

    public List<UserPlantParcel> getPlantParcelList(Long userId) {
        return userPlantParcelRepository.findByUser_IdOrderByInsertDateAsc(userId);
    }

    public Optional<UserPlantParcel> getUserParcelByIdAndUserId(Long parcelId, Long userId) {
        return userPlantParcelRepository.findByIdAndUser_Id(parcelId, userId);
    }

    public void saveParcel(Long selectedParcelId, Long selectedProductId, String parcelName, Double areaDecare, Long selectedCityId, User user) {
        UserPlantParcel plantParcel = new UserPlantParcel();
        if (selectedParcelId != null)
            plantParcel.setId(selectedParcelId);
        plantParcel.setUser(user);
        plantParcel.setProduct(plantationProductRepository.getReferenceById(selectedProductId));
        // Parsel Bilgisi
        plantParcel.setParcelType(EnumParcelType.OPEN_FIELD);
        plantParcel.setParcelName(parcelName);
        plantParcel.setAreaDecare(areaDecare);
        // Adres Bilgisi
        plantParcel.setCity(cityRepository.getReferenceById(selectedCityId));
        // Arazi Yapısı
        // Toprak Özellikleri
        // Sulama Bilgileri
        userPlantParcelRepository.save(plantParcel);
    }

    public void saveParcelDetail(UserPlantParcel userPlantParcel) {
        userPlantParcelRepository.save(userPlantParcel);
    }
}
