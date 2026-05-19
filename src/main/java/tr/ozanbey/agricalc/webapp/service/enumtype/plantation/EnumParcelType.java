package tr.ozanbey.agricalc.webapp.service.enumtype.plantation;


import lombok.Getter;

@Getter
public enum EnumParcelType {

    OPEN_FIELD(0),  // açık tarla
    GREENHOUSE(1),  // seracılık
    CUT_FLOWER(2),  // kesme çiçekçilik
    NURSERY(3),     // fidancılık
    ROLLED_TURF(4); // rulo çim

    private final int value;

    EnumParcelType(int value) {
        this.value = value;
    }

}
