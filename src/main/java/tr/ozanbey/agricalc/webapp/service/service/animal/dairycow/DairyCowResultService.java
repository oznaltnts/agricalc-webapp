package tr.ozanbey.agricalc.webapp.service.service.animal.dairycow;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.ozanbey.agricalc.webapp.service.domain.animal.dairycow.*;
import tr.ozanbey.agricalc.webapp.service.enumtype.animal.dairycow.EnumCostType;
import tr.ozanbey.agricalc.webapp.service.enumtype.animal.dairycow.EnumCowType;
import tr.ozanbey.agricalc.webapp.service.repository.animal.dairycow.UserDairyCowCostRepository;
import tr.ozanbey.agricalc.webapp.service.repository.animal.dairycow.UserDairyCowCountRepository;
import tr.ozanbey.agricalc.webapp.service.repository.animal.dairycow.UserDairyCowFeedRepository;
import tr.ozanbey.agricalc.webapp.service.repository.animal.dairycow.UserDairyCowIncomeRepository;
import tr.ozanbey.agricalc.webapp.webapp.view.animal.dairycow.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DairyCowResultService {

    @Autowired
    private UserDairyCowCountRepository cowCountRepository;

    @Autowired
    private UserDairyCowFeedRepository cowFeedRepository;

    @Autowired
    private UserDairyCowCostRepository cowCostRepository;

    @Autowired
    private UserDairyCowIncomeRepository cowIncomeRepository;

    public List<DairyCowResultCountView> calculateCountList(Long barnId) {
        return cowCountRepository.findAsResultViewListByBarnId(barnId);
    }

    public List<DairyCowResultFeedView> calculateFeedList(Long barnId) {
        return cowFeedRepository.findAsResultViewListByBarnId(barnId);
    }

    public DairyCowResultFeedTotalView calculateFeedTotalView(Long barnId) {
        List<UserDairyCowFeed> userFeedList = cowFeedRepository.findByUserDairyCowBarn_Id(barnId);
        BigDecimal lactationValue = BigDecimal.ZERO;
        BigDecimal roughageValue = BigDecimal.ZERO;
        BigDecimal averageFeedCount = BigDecimal.ZERO;
        for (UserDairyCowFeed userFeed : userFeedList) {
            if (userFeed.getLactation() != null)
                lactationValue = lactationValue.add(
                        userFeed.getBuyingPrice()
                                .multiply(BigDecimal.valueOf(userFeed.getLactation()))
                                .multiply(BigDecimal.valueOf(userFeed.getUserDairyCowBarn().getLactationPeriod())));
            if (userFeed.getRoughage() != null)
                roughageValue = roughageValue.add(
                        userFeed.getBuyingPrice()
                                .multiply(BigDecimal.valueOf(userFeed.getRoughage()))
                                .multiply(BigDecimal.valueOf(365)
                                        .subtract(BigDecimal.valueOf(userFeed.getUserDairyCowBarn().getLactationPeriod()))));
            averageFeedCount = BigDecimal.valueOf(userFeed.getUserDairyCowBarn().getAverageFeedTotalCount());
        }
        BigDecimal costPerAnimal = lactationValue.add(roughageValue);
        BigDecimal costPerBarn = costPerAnimal.multiply(averageFeedCount);

        return new DairyCowResultFeedTotalView(lactationValue, roughageValue, costPerAnimal, costPerBarn);
    }

    public DairyCowResultDepreciationView calculateCostTotalView(UserDairyCowBarn userDairyCowBarn, BigDecimal yearlyFeedCost, BigDecimal inseminationMultiplier) {
        BigDecimal maintenanceCost = BigDecimal.ZERO;
        BigDecimal energyCost = BigDecimal.ZERO;
        BigDecimal veterinaryCost = BigDecimal.ZERO;
        BigDecimal inseminationCost = BigDecimal.ZERO;
        BigDecimal otherCost = BigDecimal.ZERO;
        BigDecimal insuranceCost = BigDecimal.ZERO;
        BigDecimal interestCost = BigDecimal.ZERO;
        //Amortisman Gideri
        BigDecimal barnAmortisationPrice = BigDecimal.ZERO;

        List<UserDairyCowCost> cowCostList = cowCostRepository.findByUserDairyCowBarn_Id(userDairyCowBarn.getId());
        if (cowCostList != null && !cowCostList.isEmpty()) {
            BigDecimal interestCostOne = BigDecimal.ZERO;
            BigDecimal interestCostTwo = BigDecimal.ZERO;
            BigDecimal interestCostThree = BigDecimal.ZERO;
            for (UserDairyCowCost cowCost : cowCostList) {
                if (cowCost.getDairyCowCost().getCostType().equals(EnumCostType.MAINTENANCE_SALARY)) {
                    //Çitlikte Maaşlı Çalışan Bakım Personeli
                    maintenanceCost = maintenanceCost.add(
                            cowCost.getTotalCost()
                                    .multiply(BigDecimal.valueOf(13)));
                } else if (cowCost.getDairyCowCost().getCostType().equals(EnumCostType.MAINTENANCE_HOURLY)) {
                    //Maaş almadan çalışan Aile İşgücü (küçük aile  işletmeler)
                    maintenanceCost = maintenanceCost.add(
                            cowCost.getTotalCost()
                                    .multiply(BigDecimal.valueOf(cowCost.getHourlyOrInterest()))
                                    .multiply(BigDecimal.valueOf(365))
                                    .divide(BigDecimal.valueOf(8), 10, RoundingMode.HALF_UP)
                    );
                } else if (cowCost.getDairyCowCost().getCostType().equals(EnumCostType.ENERGY_WATER)) {
                    //Enerji - Su Gideri Soruları
                    energyCost = energyCost.add(
                            cowCost.getTotalCost()
                                    .multiply(BigDecimal.valueOf(cowCost.getCount()))
                                    .multiply(BigDecimal.valueOf(12)));
                } else if (cowCost.getDairyCowCost().getCostType().equals(EnumCostType.VETERINARY)) {
                    //Veterner Hizmetleri
                    veterinaryCost = veterinaryCost.add(
                            cowCost.getTotalCost()
                                    .multiply(BigDecimal.valueOf(12)));
                } else if (cowCost.getDairyCowCost().getCostType().equals(EnumCostType.INSEMINATION)) {
                    //Suni Tohumlama Gideri
                    inseminationCost = inseminationCost.add(
                            cowCost.getTotalCost()
                                    .multiply(BigDecimal.valueOf(cowCost.getCount()))
                                    .multiply(inseminationMultiplier));
                } else if (cowCost.getDairyCowCost().getCostType().equals(EnumCostType.INSURANCE)) {
                    //Hayvan Hayat Sigortası
                    insuranceCost = insuranceCost.add(cowCost.getTotalCost());
                } else if (cowCost.getDairyCowCost().getCostType().equals(EnumCostType.INTEREST_SUBSIDIZED)) {
                    //İşletmenin Kullandığı Sübvansiyonlu Kredi
                    interestCostOne = interestCostOne.add(
                            cowCost.getTotalCost()
                                    .multiply(BigDecimal.valueOf(cowCost.getCount()))
                                    .multiply(BigDecimal.valueOf(cowCost.getHourlyOrInterest()))
                                    .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
                                    .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP));
                    interestCostTwo = interestCostTwo.add(cowCost.getTotalCost());
                } else if (cowCost.getDairyCowCost().getCostType().equals(EnumCostType.INTEREST_UNSUBSIDIZED)) {
                    //İşletmenin Kullandığı Sübvansiyonsuz Kredi
                    interestCostOne = interestCostOne.add(
                            cowCost.getTotalCost()
                                    .multiply(BigDecimal.valueOf(cowCost.getCount()))
                                    .multiply(BigDecimal.valueOf(cowCost.getHourlyOrInterest()))
                                    .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
                                    .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP));
                    interestCostTwo = interestCostTwo.add(cowCost.getTotalCost());
                    interestCostThree = interestCostThree.add(BigDecimal.valueOf(cowCost.getHourlyOrInterest()));
                }
            }
            interestCost = interestCostOne.add(
                    yearlyFeedCost
                            .subtract(interestCostTwo)
                            .multiply(interestCostThree)
                            .divide(BigDecimal.valueOf(200), 10, RoundingMode.HALF_UP));
            otherCost = yearlyFeedCost.add(maintenanceCost).add(energyCost).add(veterinaryCost).add(inseminationCost)
                    .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
        }
        if (userDairyCowBarn.getBarnPrice() != null) {
            barnAmortisationPrice = userDairyCowBarn.getBarnPrice().divide(BigDecimal.valueOf(20), 10, RoundingMode.HALF_UP);
        }

        return new DairyCowResultDepreciationView(yearlyFeedCost, maintenanceCost, energyCost, veterinaryCost,
                inseminationCost, otherCost, insuranceCost, interestCost, BigDecimal.ZERO, barnAmortisationPrice, BigDecimal.ZERO);
    }

    public List<DairyCowResultIncomeView> calculateIncomeTotalView(Long barnId, BigDecimal totalCostPerBarn, DairyCowResultDepreciationView depreciationView) {
        List<DairyCowResultIncomeView> returnList = new ArrayList<>();

        BigDecimal milkSellIncome = BigDecimal.ZERO; //Süt Satış Geliri
        BigDecimal animalSellBuyIncome = BigDecimal.ZERO; //Hayvan Alım satım Geliri
        BigDecimal milkPrimIncome = BigDecimal.ZERO; //Süt Primi
        BigDecimal calfPrimIncome = BigDecimal.ZERO; //Buzağı Primi
        BigDecimal familyPrimIncome = BigDecimal.ZERO; //Soy Kütüğü Desteği
        BigDecimal sickFreeIncome = BigDecimal.ZERO; //Hastalıktan Arı
        BigDecimal fertilizerIncome = BigDecimal.ZERO; //Gübre Satış
        BigDecimal otherIncome = BigDecimal.ZERO; //Diğer Gelir

        Double averageCowCount = 0d; //Yıl İçinde Ortalama İnek sayısı

        Integer cowToSell = 0; //Bir Yıl içinde Satılacak Reforme İnek Sayısı
        Integer pregnantHeiferToSell = 0; //Bir Yıl içinde Satılacak Gebe Düve Sayısı
        Integer heiferToSell = 0; //Bir Yıl içinde Satılacak Düve Sayısı
        Integer steerToSell = 0; //Bir Yıl içinde Satılacak Dana Sayısı
        Integer calfToSell = 0; //Bir Yıl içinde Satılacak Buzağı Sayısı

        Integer cowToBuy = 0; //Bir Yıl içinde Alınacak İnek Sayısı
        Integer pregnantHeiferToBuy = 0; //Bir Yıl içinde Alınacak Gebe Düve Sayısı
        Integer heiferToBuy = 0; //Bir Yıl içinde Alınacak Düve Sayısı
        Integer steerToBuy = 0; //Bir Yıl içinde Alınacak Dana Sayısı
        Integer calfToBuy = 0; //Bir Yıl içinde Buzağı Sayısı

        BigDecimal reformedCowPrice = BigDecimal.ZERO; //Ortalama reforme inek fiyatı
        BigDecimal pregnantHeiferPrice = BigDecimal.ZERO; //Ortalama gebe düve satış fiyatı
        BigDecimal heiferPrice = BigDecimal.ZERO; //Ortalama düve fiyatı
        BigDecimal steerPrice = BigDecimal.ZERO; //Ortalama dana fiyatı
        BigDecimal calfPrice = BigDecimal.ZERO; //Ortalama buzağı fiyatı
        BigDecimal cowPrice = BigDecimal.ZERO; //Ortalama inek fiyatı


        List<UserDairyCowCount> cowCountList = cowCountRepository.findByUserDairyCowBarn_Id(barnId);
        for (UserDairyCowCount cowCount : cowCountList) {
            if (cowCount.getDairyCowCoefficient().getCowType().equals(EnumCowType.COW)) {
                averageCowCount = averageCowCount + cowCount.getAverageFeedCount();
                cowToSell = cowCount.getSellCount();
                cowToBuy = cowCount.getPurchaseCount();
                familyPrimIncome = familyPrimIncome.add(BigDecimal.valueOf(cowCount.getEndYearCount()));
                sickFreeIncome = sickFreeIncome.add(BigDecimal.valueOf(cowCount.getEndYearCount()));
            } else if (cowCount.getDairyCowCoefficient().getCowType().equals(EnumCowType.PREGNANT_HEIFER)) {
                averageCowCount = averageCowCount + cowCount.getAverageFeedCount();
                pregnantHeiferToSell = cowCount.getSellCount();
                pregnantHeiferToBuy = cowCount.getPurchaseCount();
                familyPrimIncome = familyPrimIncome.add(BigDecimal.valueOf(cowCount.getEndYearCount()));
            } else if (cowCount.getDairyCowCoefficient().getCowType().equals(EnumCowType.HEIFER)) {
                heiferToSell = cowCount.getSellCount();
                heiferToBuy = cowCount.getPurchaseCount();
            } else if (cowCount.getDairyCowCoefficient().getCowType().equals(EnumCowType.STEER)) {
                steerToSell = cowCount.getSellCount();
                steerToBuy = cowCount.getPurchaseCount();
            } else if (cowCount.getDairyCowCoefficient().getCowType().equals(EnumCowType.CALF)) {
                calfToSell = cowCount.getSellCount();
                calfToBuy = cowCount.getPurchaseCount();
                calfPrimIncome = BigDecimal.valueOf(cowCount.getEndYearCount() / 2);
            }
        }

        milkSellIncome = BigDecimal.valueOf(averageCowCount);
        milkPrimIncome = BigDecimal.valueOf(averageCowCount);
        List<UserDairyCowIncome> incomeList = cowIncomeRepository.findByUserDairyCowBarn_Id(barnId);
        for (UserDairyCowIncome income : incomeList) {
            if (income.getDairyCowIncome().getId().equals(1L)) {
                milkSellIncome = milkSellIncome.multiply(income.getIncomeValue());
                milkPrimIncome = milkPrimIncome.multiply(income.getIncomeValue());
            } else if (income.getDairyCowIncome().getId().equals(2L)) {
                milkSellIncome = milkSellIncome.multiply(income.getIncomeValue());
            } else if (income.getDairyCowIncome().getId().equals(3L)) {
                reformedCowPrice = income.getIncomeValue();
            } else if (income.getDairyCowIncome().getId().equals(4L)) {
                pregnantHeiferPrice = income.getIncomeValue();
            } else if (income.getDairyCowIncome().getId().equals(5L)) {
                heiferPrice = income.getIncomeValue();
            } else if (income.getDairyCowIncome().getId().equals(6L)) {
                steerPrice = income.getIncomeValue();
            } else if (income.getDairyCowIncome().getId().equals(7L)) {
                calfPrice = income.getIncomeValue();
            } else if (income.getDairyCowIncome().getId().equals(8L)) {
                cowPrice = income.getIncomeValue();
            } else if (income.getDairyCowIncome().getId().equals(9L)) {
                milkPrimIncome = milkPrimIncome.multiply(income.getIncomeValue());
            } else if (income.getDairyCowIncome().getId().equals(10L)) {
                calfPrimIncome = calfPrimIncome.multiply(income.getIncomeValue());
            } else if (income.getDairyCowIncome().getId().equals(11L)) {
                familyPrimIncome = familyPrimIncome.multiply(income.getIncomeValue());
            } else if (income.getDairyCowIncome().getId().equals(12L)) {
                sickFreeIncome = sickFreeIncome.multiply(income.getIncomeValue());
            } else if (income.getDairyCowIncome().getId().equals(13L)) {
                fertilizerIncome = fertilizerIncome.add(income.getIncomeValue());
            } else if (income.getDairyCowIncome().getId().equals(14L)) {
                otherIncome = otherIncome.add(income.getIncomeValue());
            }
        }

        milkSellIncome = milkSellIncome.multiply(BigDecimal.valueOf(365));
        returnList.add(new DairyCowResultIncomeView(0, null, "Süt Satış Geliri", milkSellIncome));

        animalSellBuyIncome = reformedCowPrice.multiply(BigDecimal.valueOf(cowToSell))
                .add(pregnantHeiferPrice.multiply(BigDecimal.valueOf(pregnantHeiferToSell)))
                .add(heiferPrice.multiply(BigDecimal.valueOf(heiferToSell)))
                .add(steerPrice.multiply(BigDecimal.valueOf(steerToSell)))
                .add(calfPrice.multiply(BigDecimal.valueOf(calfToSell)))
                .subtract(cowPrice.multiply(BigDecimal.valueOf(cowToBuy)))
                .subtract(pregnantHeiferPrice.multiply(BigDecimal.valueOf(pregnantHeiferToBuy)))
                .subtract(heiferPrice.multiply(BigDecimal.valueOf(heiferToBuy)))
                .subtract(steerPrice.multiply(BigDecimal.valueOf(steerToBuy)))
                .subtract(calfPrice.multiply(BigDecimal.valueOf(calfToBuy)));
        milkPrimIncome = milkPrimIncome.multiply(BigDecimal.valueOf(365));

        returnList.add(new DairyCowResultIncomeView(1, null, "Hayvan Alım satım Geliri", animalSellBuyIncome));
        returnList.add(new DairyCowResultIncomeView(2, "Destekleme Gelirleri", "Süt Primi", milkPrimIncome));
        returnList.add(new DairyCowResultIncomeView(3, "Destekleme Gelirleri", "Buzağı Primi", calfPrimIncome));
        returnList.add(new DairyCowResultIncomeView(4, "Destekleme Gelirleri", "Soy Kütüğü Desteği", familyPrimIncome));
        returnList.add(new DairyCowResultIncomeView(5, "Destekleme Gelirleri", "Hastalıktan Arı", sickFreeIncome));
        returnList.add(new DairyCowResultIncomeView(6, null, "Gübre Satış", fertilizerIncome));
        returnList.add(new DairyCowResultIncomeView(7, null, "Diğer Gelir", otherIncome));

        BigDecimal totalIncome = milkSellIncome
                .add(animalSellBuyIncome)
                .add(milkPrimIncome)
                .add(calfPrimIncome)
                .add(familyPrimIncome)
                .add(sickFreeIncome)
                .add(fertilizerIncome)
                .add(otherIncome);

        BigDecimal amortisationCost = (cowPrice.subtract(reformedCowPrice))
                .multiply(BigDecimal.valueOf(averageCowCount))
                .divide(BigDecimal.valueOf(7), 10, RoundingMode.HALF_UP); //Hayvan amortisman Gideri (ortalama yıllık)

        BigDecimal totalExpense = totalCostPerBarn
                .add(depreciationView.getYearlyMaintenanceCost())
                .add(depreciationView.getYearlyEnergyCost())
                .add(depreciationView.getYearlyVeterinaryCost())
                .add(depreciationView.getYearlyInseminationCost())
                .add(depreciationView.getYearlyOtherCost())
                .add(depreciationView.getYearlyInsuranceCost())
                .add(depreciationView.getYearlyInterestCost())
                .add(amortisationCost)
                .add(depreciationView.getBarnAmortisationPrice());

        returnList.add(new DairyCowResultIncomeView(8, null, "Toplam Brüt Gelir", totalIncome));
        returnList.add(new DairyCowResultIncomeView(9, null, "Toplam Gider", totalExpense));
        returnList.add(new DairyCowResultIncomeView(10, null, "Net Gelir (TL)", totalIncome.subtract(totalExpense)));
        returnList.add(new DairyCowResultIncomeView(11, null, null, amortisationCost));

        return returnList;
    }

}
