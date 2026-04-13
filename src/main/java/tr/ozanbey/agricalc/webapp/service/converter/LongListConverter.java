package tr.ozanbey.agricalc.webapp.service.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class LongListConverter implements AttributeConverter<List<Long>, String> {

    private static final String SEPARATOR = ",";

    @Override
    public String convertToDatabaseColumn(List<Long> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "";
        }
        // Listeyi "1,2,3" formatında String'e çevirir
        return attribute.stream()
                .map(Object::toString)
                .collect(Collectors.joining(SEPARATOR));
    }

    @Override
    public List<Long> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return new ArrayList<>();
        }
        // String'i tekrar List<Long>'a çevirir
        return Arrays.stream(dbData.split(SEPARATOR))
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }
}
