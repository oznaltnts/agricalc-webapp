package tr.ozanbey.agricalc.webapp.service.enumtype;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum EnumLanguage {

    ENGLISH("English"),
    TURKISH("Türkçe");

    private final String language;

    EnumLanguage(String language) {
        this.language = language;
    }

    public static List<EnumLanguage> convertStringListToEnum(List<String> stringList) {
        List<EnumLanguage> returnList = new ArrayList<>();
        for (String s : stringList) {
            returnList.add(valueOf(s));
        }
        return returnList;
    }
}
