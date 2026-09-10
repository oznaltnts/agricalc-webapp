package tr.ozanbey.agricalc.webapp.service.enumtype.plantation;

import lombok.Getter;

@Getter
public enum EnumDiseaseType {
    HERBICIDE(136L),
    FUNGICIDE(195L),
    INSECTICIDE(197L),
    ACARICIDE(198L),
    PESTICIDE(199L),
    HORMONE(200L),
    OTHERS(201L);

    private final Long questionId;

    EnumDiseaseType(Long questionId) {
        this.questionId = questionId;
    }
}
