package tr.ozanbey.agricalc.webapp.service.enumtype.plantation;


import lombok.Getter;

@Getter
public enum EnumParcelType {

    OPEN_FIELD(0),  // açık tarla
    GREENHOUSE(1);  // seracılık;

    private final int value;

    EnumParcelType(int value) {
        this.value = value;
    }

}
