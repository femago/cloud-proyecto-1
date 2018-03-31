package co.edu.uniandes.cloud.repository.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

public class JpaConverters {
    @Converter
    public static class IdStringConverter implements
        AttributeConverter<String, Long> {

        @Override
        public Long convertToDatabaseColumn(String attribute) {
            return Long.parseLong(attribute);
        }

        @Override
        public String convertToEntityAttribute(Long dbData) {
            return dbData.toString();
        }
    }
}
