package tr.ozanbey.agricalc.webapp.service.service.plantation;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.ozanbey.agricalc.webapp.service.domain.User;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantation;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantationPlan;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumPlantationType;
import tr.ozanbey.agricalc.webapp.service.repository.CityDistrictRepository;
import tr.ozanbey.agricalc.webapp.service.repository.plantation.PlantationProductRepository;
import tr.ozanbey.agricalc.webapp.service.repository.plantation.UserPlantationPlanRepository;
import tr.ozanbey.agricalc.webapp.service.repository.plantation.UserPlantationRepository;
import tr.ozanbey.agricalc.webapp.service.service.BaseService;
import tr.ozanbey.agricalc.webapp.webapp.view.plantation.PlantationPlanView;
import tr.ozanbey.agricalc.webapp.webapp.view.plantation.PlantationView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserPlantationService extends BaseService {

    private final UserPlantationRepository userPlantationRepository;
    private final UserPlantationPlanRepository userPlantationPlanRepository;
    private final CityDistrictRepository cityDistrictRepository;
    private final PlantationProductRepository productRepository;

    public UserPlantationService(UserPlantationRepository userPlantationRepository,
                                 UserPlantationPlanRepository userPlantationPlanRepository,
                                 CityDistrictRepository cityDistrictRepository,
                                 PlantationProductRepository productRepository) {
        this.userPlantationRepository = userPlantationRepository;
        this.userPlantationPlanRepository = userPlantationPlanRepository;
        this.cityDistrictRepository = cityDistrictRepository;
        this.productRepository = productRepository;
    }

    public List<PlantationView> getPlantationViewList(Long userId, EnumStatus[] statuses) {
        List<PlantationView> returnViewList = new ArrayList<>();
        List<UserPlantation> plantationList = userPlantationRepository.findByUser_IdAndStatusInOrderByUpdateDateDesc(userId, statuses);
        for (UserPlantation plantation : plantationList) {
            returnViewList.add(convertEntityToPlantationView(plantation, true));
        }
        return returnViewList;
    }

    private PlantationView convertEntityToPlantationView(UserPlantation userPlantation, boolean isList) {
        PlantationView returnView = new PlantationView();
        returnView.setUserPlantationId(userPlantation.getId());
        returnView.setSelectedStatus(userPlantation.getStatus());
        if (Hibernate.isInitialized(userPlantation.getPlantationPlanList())) {
            returnView.setAnyPlanStarted(!userPlantation.getPlantationPlanList().isEmpty());
        }
        returnView.setName(userPlantation.getName());
        returnView.setArea(userPlantation.getArea());
        returnView.setOwnedOrRental(userPlantation.getOwnedOrRental());
        returnView.setSellOrRentPrice(userPlantation.getSellOrRentPrice());
        if (Hibernate.isInitialized(userPlantation.getDistrict())) {
            returnView.setSelectedCityId(userPlantation.getDistrict().getCity().getId());
            returnView.setSelectedCityName(userPlantation.getDistrict().getCity().getName());
            returnView.setSelectedCityTriple(userPlantation.getDistrict().getCity().getIsTriple());
            returnView.setSelectedCityDistrictId(userPlantation.getDistrict().getId());
            returnView.setSelectedDistrictName(userPlantation.getDistrict().getName());
        }
        returnView.setVillage(userPlantation.getVillage());
        returnView.setNeighborhood(userPlantation.getNeighborhood());
        returnView.setAdaNumber(userPlantation.getAdaNumber());
        returnView.setPaftaNumber(userPlantation.getPaftaNumber());
        if (userPlantation.getOrchardProduct() != null && Hibernate.isInitialized(userPlantation.getOrchardProduct())) {
            returnView.setAnyPlanStarted(true);
            returnView.setSelectedType(EnumPlantationType.ORCHARD);
            returnView.setSelectedPrimaryProductId(userPlantation.getOrchardProduct().getId());
            returnView.setSelectedPrimaryProductName(userPlantation.getOrchardProduct().getName());
        } else if (isList) {
            if (!userPlantation.getPlantationPlanList().isEmpty()) {
                returnView.setSelectedPrimaryProductName(userPlantation.getPlantationPlanList().getFirst().getPrimaryProduct().getName());
            }
        }
        returnView.setSelectedStatusType(userPlantation.getStatusType());
        returnView.setSelectedNadas(userPlantation.getNadas());
        returnView.setSelectedSlope(userPlantation.getSlope());
        returnView.setSelectedOrientation(userPlantation.getOrientation());
        returnView.setSelectedSoilTexture(userPlantation.getSoilTexture());
        returnView.setSelectedSoilDepth(userPlantation.getSoilDepth());
        returnView.setSelectedOrganicMatter(userPlantation.getOrganicMatter());
        returnView.setSelectedSoilSalinity(userPlantation.getSoilSalinity());
        returnView.setSelectedLime(userPlantation.getLime());
        returnView.setSelectedPhosphorus(userPlantation.getPhosphorus());
        returnView.setSelectedPotassium(userPlantation.getPotassium());
        return returnView;
    }

    private UserPlantation convertPlantationViewToEntity(PlantationView plantationView, User currentUser) {
        UserPlantation returnEntity = new UserPlantation();
        returnEntity.setId(plantationView.getUserPlantationId());
        returnEntity.setStatus(plantationView.getSelectedStatus());
        returnEntity.setUser(currentUser);
        returnEntity.setName(plantationView.getName());
        returnEntity.setArea(plantationView.getArea());
        returnEntity.setOwnedOrRental(plantationView.getOwnedOrRental());
        returnEntity.setSellOrRentPrice(plantationView.getSellOrRentPrice());
        returnEntity.setDistrict(cityDistrictRepository.getReferenceById(plantationView.getSelectedCityDistrictId()));
        returnEntity.setVillage(plantationView.getVillage());
        returnEntity.setNeighborhood(plantationView.getNeighborhood());
        returnEntity.setAdaNumber(plantationView.getAdaNumber());
        returnEntity.setPaftaNumber(plantationView.getPaftaNumber());
        if (plantationView.getSelectedType().equals(EnumPlantationType.ORCHARD))
            returnEntity.setOrchardProduct(productRepository.getReferenceById(plantationView.getSelectedPrimaryProductId()));
        returnEntity.setStatusType(plantationView.getSelectedStatusType());
        returnEntity.setNadas(plantationView.getSelectedNadas());
        returnEntity.setSlope(plantationView.getSelectedSlope());
        returnEntity.setOrientation(plantationView.getSelectedOrientation());
        returnEntity.setSoilTexture(plantationView.getSelectedSoilTexture());
        returnEntity.setSoilDepth(plantationView.getSelectedSoilDepth());
        returnEntity.setOrganicMatter(plantationView.getSelectedOrganicMatter());
        returnEntity.setSoilSalinity(plantationView.getSelectedSoilSalinity());
        returnEntity.setLime(plantationView.getSelectedLime());
        returnEntity.setPhosphorus(plantationView.getSelectedPhosphorus());
        returnEntity.setPotassium(plantationView.getSelectedPotassium());
        return returnEntity;
    }

    @Transactional
    public void savePlantationDetail(PlantationView plantationView, User currentUser) {
        userPlantationRepository.save(convertPlantationViewToEntity(plantationView, currentUser));
    }

    @Transactional
    public void deletePlantationDetail(PlantationView plantationView, User currentUser) {
        plantationView.setSelectedStatus(EnumStatus.DELETED);
        savePlantationDetail(plantationView, currentUser);
        int deleteCount = userPlantationPlanRepository.updateStatusByUserPlantationId(plantationView.getUserPlantationId(), EnumStatus.DELETED);
    }

    public List<PlantationPlanView> getPlantationPlanViewList(Long plantationId, EnumStatus[] statuses) {
        List<PlantationPlanView> returnViewList = new ArrayList<>();
        List<UserPlantationPlan> plantationPlanList = userPlantationPlanRepository.findByUserPlantation_IdAndStatusInOrderByUpdateDateDesc(plantationId, statuses);
        for (UserPlantationPlan plantationPlan : plantationPlanList) {
            returnViewList.add(convertEntityToPlantationPlanView(plantationPlan));
        }
        return returnViewList;
    }

    private PlantationPlanView convertEntityToPlantationPlanView(UserPlantationPlan userPlantationPlan) {
        PlantationPlanView returnView = new PlantationPlanView();
        returnView.setPlantationPlanId(userPlantationPlan.getId());
        returnView.setSelectedStatus(userPlantationPlan.getStatus());
        if (userPlantationPlan.getGrossIncome() != null && userPlantationPlan.getGrossIncome().compareTo(BigDecimal.ZERO) > 0) {
            returnView.setAnyProcessStarted(true);
        }
        returnView.setPlanStartDate(userPlantationPlan.getPlanStartDate());
        if (userPlantationPlan.getUserPlantation().getOrchardProduct() != null) {
            returnView.setSelectedPrimaryProductId(userPlantationPlan.getUserPlantation().getOrchardProduct().getId());
            returnView.setSelectedPrimaryProductName(userPlantationPlan.getUserPlantation().getOrchardProduct().getName());
            returnView.setProcessTooltipMessage("Bahçe tipi (Çok yıllık) ürünler değiştirilemez.");
            returnView.setAnyProcessStarted(true);
        } else {
            if (userPlantationPlan.getPrimaryProduct() != null) {
                returnView.setSelectedPrimaryProductId(userPlantationPlan.getPrimaryProduct().getId());
                returnView.setSelectedPrimaryProductName(userPlantationPlan.getPrimaryProduct().getName());
            }
            if (userPlantationPlan.getSecondaryProduct() != null) {
                returnView.setSelectedSecondaryProductId(userPlantationPlan.getSecondaryProduct().getId());
                returnView.setSelectedSecondaryProductName(userPlantationPlan.getSecondaryProduct().getName());
            }
            if (userPlantationPlan.getLastProduct() != null) {
                returnView.setSelectedLastProductId(userPlantationPlan.getLastProduct().getId());
                returnView.setSelectedLastProductName(userPlantationPlan.getLastProduct().getName());
            }
        }
        returnView.setGrossIncome(userPlantationPlan.getGrossIncome());
        returnView.setTotalExpense(userPlantationPlan.getTotalExpense());
        return returnView;
    }

    private UserPlantationPlan convertPlantationPlanViewToEntity(PlantationPlanView plantationPlanView, Long userPlantationId) {
        UserPlantationPlan returnEntity = new UserPlantationPlan();
        returnEntity.setId(plantationPlanView.getPlantationPlanId());
        returnEntity.setStatus(plantationPlanView.getSelectedStatus());
        returnEntity.setUserPlantation(userPlantationRepository.getReferenceById(userPlantationId));
        returnEntity.setPlanStartDate(plantationPlanView.getPlanStartDate());
        if (plantationPlanView.getSelectedPrimaryProductId() != null)
            returnEntity.setPrimaryProduct(productRepository.getReferenceById(plantationPlanView.getSelectedPrimaryProductId()));
        if (plantationPlanView.getSelectedSecondaryProductId() != null)
            returnEntity.setSecondaryProduct(productRepository.getReferenceById(plantationPlanView.getSelectedSecondaryProductId()));
        if (plantationPlanView.getSelectedLastProductId() != null)
            returnEntity.setLastProduct(productRepository.getReferenceById(plantationPlanView.getSelectedLastProductId()));
        returnEntity.setGrossIncome(plantationPlanView.getGrossIncome());
        returnEntity.setTotalExpense(plantationPlanView.getTotalExpense());
        return returnEntity;
    }

    @Transactional
    public void savePlantationPlanDetail(PlantationPlanView plantationPlanView, Long userPlantationId) {
        userPlantationPlanRepository.save(convertPlantationPlanViewToEntity(plantationPlanView, userPlantationId));
    }

    public PlantationView getPlantationViewByIdAndUserId(Long userPlantationId, Long userId) {
        Optional<UserPlantation> optional = userPlantationRepository.findByIdAndUser_Id(userPlantationId, userId);
        return optional.map(o -> convertEntityToPlantationView(o, false)).orElse(null);
    }

    @Transactional
    public void deletePlantationPlanDetail(PlantationPlanView plantationPlanView, Long userPlantationId) {
        plantationPlanView.setSelectedStatus(EnumStatus.DELETED);
        savePlantationPlanDetail(plantationPlanView, userPlantationId);
    }

}
