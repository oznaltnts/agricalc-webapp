package tr.ozanbey.agricalc.webapp.service.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.ozanbey.agricalc.webapp.service.domain.User;
import tr.ozanbey.agricalc.webapp.service.domain.UserPlantAsset;
import tr.ozanbey.agricalc.webapp.service.domain.UserPlantAssetDetail;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumPlantAsset;
import tr.ozanbey.agricalc.webapp.service.repository.UserPlantAssetDetailRepository;
import tr.ozanbey.agricalc.webapp.service.repository.UserPlantAssetRepository;
import tr.ozanbey.agricalc.webapp.webapp.view.UserPlantAssetView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FixedAssetService extends BaseService {

    @Autowired
    private UserPlantAssetRepository plantAssetRepository;
    
    @Autowired
    private UserPlantAssetDetailRepository plantAssetDetailRepository;

    @Transactional
    public void savePlantAsset(List<UserPlantAssetView> plantAssetViewList, User user) {
        plantAssetRepository.deleteByUser_Id(user.getId());

        List<UserPlantAsset> saveList = new ArrayList<>();
        for (UserPlantAssetView view : plantAssetViewList) {
            if (view.getQuantity() > 0) {
                UserPlantAsset plantAsset = new UserPlantAsset();
                plantAsset.setUser(user);
                plantAsset.setPlantAsset(view.getPlantAsset());
                plantAsset.setQuantity(view.getQuantity());
                List<UserPlantAssetDetail> assetDetailList = new ArrayList<>();
                for (UserPlantAssetView.AssetDetail viewDetail : view.getDetailList()) {
                    UserPlantAssetDetail detail = new UserPlantAssetDetail();
                    detail.setUserPlantAsset(plantAsset);

                    detail.setAssetModel(viewDetail.getAssetModel());
                    detail.setAssetPrice(viewDetail.getAssetPrice());
                    detail.setTractorBrand(viewDetail.getTractorBrand());

                    if (viewDetail.getSprayingType() != null)
                        detail.setAssetType(viewDetail.getSprayingType().toString());
                    else if (viewDetail.getSeedingType() != null)
                        detail.setAssetType(viewDetail.getSeedingType().toString());
                    else if (viewDetail.getHarvestingType() != null)
                        detail.setAssetType(viewDetail.getHarvestingType());
                    else if (viewDetail.getOtherType() != null)
                        detail.setAssetType(viewDetail.getOtherType().toString());

                    detail.setSprayingTank(viewDetail.getSprayingTank());

                    if (viewDetail.isRentIncome())
                        detail.setRentIncomePrice(viewDetail.getRentIncomePrice());

                    assetDetailList.add(detail);
                }
                plantAsset.setPlantAssetDetailList(assetDetailList);
                saveList.add(plantAsset);
            }
        }
        plantAssetRepository.saveAll(saveList);
    }

    public List<UserPlantAssetView> getPlantAssetByUserId(Long userId) {
        List<UserPlantAssetView> returnList = new ArrayList<>();
        List<UserPlantAsset> plantAssetList = plantAssetRepository.findByUser_Id(userId);
        for (EnumPlantAsset asset : EnumPlantAsset.values()) {
            UserPlantAssetView userPlantAssetView = new UserPlantAssetView();
            userPlantAssetView.setPlantAsset(asset);
            Optional<UserPlantAsset> plantAssetOptional = plantAssetList.stream().filter(pa -> pa.getPlantAsset().equals(asset)).findFirst();
            if (plantAssetOptional.isPresent()) {
                userPlantAssetView.setQuantity(plantAssetOptional.get().getQuantity());
                List<UserPlantAssetView.AssetDetail> detailList = new ArrayList<>();
                for (UserPlantAssetDetail detail : plantAssetOptional.get().getPlantAssetDetailList()) {
                    UserPlantAssetView.AssetDetail detailView = new UserPlantAssetView.AssetDetail();
                    detailView.setAssetModel(detail.getAssetModel());
                    detailView.setAssetPrice(detail.getAssetPrice());
                    detailView.setTractorBrand(detail.getTractorBrand());
                    if (asset.equals(EnumPlantAsset.SPRAYING))
                        detailView.setSprayingType(UserPlantAssetView.SprayingType.valueOf(detail.getAssetType()));
                    else if (asset.equals(EnumPlantAsset.SEEDING))
                        detailView.setSeedingType(UserPlantAssetView.SeedingType.valueOf(detail.getAssetType()));
                    else if (asset.equals(EnumPlantAsset.HARVESTING))
                        detailView.setHarvestingType(detail.getAssetType());
                    else if (asset.equals(EnumPlantAsset.OTHER))
                        detailView.setOtherType(UserPlantAssetView.OtherType.valueOf(detail.getAssetType()));
                    detailView.setSprayingTank(detail.getSprayingTank());
                    if (detail.getRentIncomePrice() != null) {
                        detailView.setRentIncome(true);
                        detailView.setRentIncomePrice(detail.getRentIncomePrice());
                    }
                    detailList.add(detailView);
                }
                userPlantAssetView.setDetailList(detailList);
            }
            returnList.add(userPlantAssetView);
        }
        return returnList;
    }

}
