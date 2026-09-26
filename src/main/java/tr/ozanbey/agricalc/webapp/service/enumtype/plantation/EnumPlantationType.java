package tr.ozanbey.agricalc.webapp.service.enumtype.plantation;


import lombok.Getter;

@Getter
public enum EnumPlantationType {

    OPEN_FIELD, // Tarla tipi (Dönemlik)
    ORCHARD,    // Bahçe tipi (Çok yıllık)
    GREENHOUSE; // Sera tipi (Dört mevsim)

}
