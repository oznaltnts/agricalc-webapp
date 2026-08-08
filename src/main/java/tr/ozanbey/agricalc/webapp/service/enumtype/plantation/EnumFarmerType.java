package tr.ozanbey.agricalc.webapp.service.enumtype.plantation;


import lombok.Getter;

@Getter
public enum EnumFarmerType {

    INDIVIDUAL(0),  // Gerçek Kişi
    COOPERATIVE(1), // Kooperatif
    BUSINESS(2);    // İşletme

    private final int value;

    EnumFarmerType(int value) {
        this.value = value;
    }

}
