package tr.ozanbey.agricalc.webapp.service.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.ozanbey.agricalc.webapp.service.domain.User;
import tr.ozanbey.agricalc.webapp.service.domain.UserPlantParcel;
import tr.ozanbey.agricalc.webapp.service.repository.UserPlantParcelRepository;
import tr.ozanbey.agricalc.webapp.webapp.view.ParcelInformationView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ParcelService extends BaseService {

    @Autowired
    private UserPlantParcelRepository plantParcelRepository;

    public List<ParcelInformationView> parcelListByUser(Long userId) {
        List<UserPlantParcel> plantParcelList = plantParcelRepository.findByUser_IdOrderByInsertDateAsc(userId);
        if (plantParcelList.isEmpty()) {
            return new ArrayList<>();
        } else {
            List<ParcelInformationView> returnList = new ArrayList<>();
            for (UserPlantParcel plantParcel : plantParcelList) {
                ParcelInformationView parcelView = new ParcelInformationView();
                parcelView.setRecordId(plantParcel.getId());
                // Parsel Bilgisi
                parcelView.setParcelName(plantParcel.getParcelName());
                parcelView.setParcelType(plantParcel.getParcelType());
                parcelView.setAdaNumber(plantParcel.getAdaNumber());
                parcelView.setPaftaNumber(plantParcel.getPaftaNumber());
                parcelView.setAreaDecare(plantParcel.getAreaDecare());
                parcelView.setRentPrice(null);
                parcelView.setParcelPrice(null);
                if (plantParcel.getRentPrice() != null && plantParcel.getRentPrice().compareTo(BigDecimal.ZERO) > 0) {
                    parcelView.setRent(true);
                    parcelView.setRentPrice(plantParcel.getRentPrice());
                } else if (plantParcel.getParcelPrice() != null && plantParcel.getParcelPrice().compareTo(BigDecimal.ZERO) > 0) {
                    parcelView.setRent(false);
                    parcelView.setParcelPrice(plantParcel.getParcelPrice());
                }
                // Arazi Yapısı
                parcelView.setSelectedStatusType(plantParcel.getStatusType());
                parcelView.setSelectedNadasStatus(plantParcel.getNadas());
                parcelView.setSelectedSlope(plantParcel.getSlope());
                parcelView.setSelectedOrientation(plantParcel.getOrientation());
                // Toprak Özellikleri
                parcelView.setSelectedSoilTexture(plantParcel.getSoilTexture());
                parcelView.setSelectedSoilDepth(plantParcel.getSoilDepth());
                parcelView.setSelectedOrganicMatter(plantParcel.getOrganicMatter());
                parcelView.setSelectedSoilSalinity(plantParcel.getSoilSalinity());
                parcelView.setSelectedLime(plantParcel.getLime());
                parcelView.setSelectedPhosphorus(plantParcel.getPhosphorus());
                parcelView.setSelectedPotassium(plantParcel.getPotassium());
                // Sulama Bilgileri
                parcelView.setSelectedWateringSource(plantParcel.getWateringSource());
                parcelView.setSelectedWateringType(plantParcel.getWateringType());
                parcelView.setElectricSource(plantParcel.getElectricSource());
                returnList.add(parcelView);
            }
            return returnList;
        }
    }

    public void saveParcel(ParcelInformationView parcelView, User user) {
        UserPlantParcel plantParcel = new UserPlantParcel();
        plantParcel.setId(parcelView.getRecordId());
        plantParcel.setUser(user);
        // Parsel Bilgisi
        plantParcel.setParcelName(parcelView.getParcelName());
        plantParcel.setParcelType(parcelView.getParcelType());
        plantParcel.setAdaNumber(parcelView.getAdaNumber());
        plantParcel.setPaftaNumber(parcelView.getPaftaNumber());
        plantParcel.setAreaDecare(parcelView.getAreaDecare());
        plantParcel.setRentPrice(null);
        plantParcel.setParcelPrice(null);
        if (parcelView.getRent() != null) {
            if (parcelView.getRent()) {
                plantParcel.setRentPrice(parcelView.getRentPrice());
            } else {
                plantParcel.setParcelPrice(parcelView.getParcelPrice());
            }
        }
        // Arazi Yapısı
        plantParcel.setStatusType(parcelView.getSelectedStatusType());
        plantParcel.setNadas(parcelView.getSelectedNadasStatus());
        plantParcel.setSlope(parcelView.getSelectedSlope());
        plantParcel.setOrientation(parcelView.getSelectedOrientation());
        // Toprak Özellikleri
        plantParcel.setSoilTexture(parcelView.getSelectedSoilTexture());
        plantParcel.setSoilDepth(parcelView.getSelectedSoilDepth());
        plantParcel.setOrganicMatter(parcelView.getSelectedOrganicMatter());
        plantParcel.setSoilSalinity(parcelView.getSelectedSoilSalinity());
        plantParcel.setLime(parcelView.getSelectedLime());
        plantParcel.setPhosphorus(parcelView.getSelectedPhosphorus());
        plantParcel.setPotassium(parcelView.getSelectedPotassium());
        // Sulama Bilgileri
        plantParcel.setWateringSource(parcelView.getSelectedWateringSource());
        plantParcel.setWateringType(parcelView.getSelectedWateringType());
        plantParcel.setElectricSource(parcelView.getElectricSource());
        plantParcelRepository.save(plantParcel);
    }

    public void deleteParcel(Long parcelRecordId) {
        plantParcelRepository.deleteById(parcelRecordId);
    }
}
