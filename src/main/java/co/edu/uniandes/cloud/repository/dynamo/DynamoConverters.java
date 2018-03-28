package co.edu.uniandes.cloud.repository.dynamo;

import co.edu.uniandes.cloud.domain.enumeration.ApplicationState;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.time.Instant;

public class DynamoConverters {

    public static class InstantConverter implements DynamoDBTypeConverter<String, Instant> {
        @Override
        public String convert(Instant object) {
            return object.toString();
        }

        @Override
        public Instant unconvert(String string) {
            return Instant.parse(string);
        }
    }

    public static class ApplicationStateConverter implements DynamoDBTypeConverter<String, ApplicationState> {
        @Override
        public String convert(ApplicationState object) {
            return object.toString();
        }

        @Override
        public ApplicationState unconvert(String string) {
            return ApplicationState.valueOf(string);
        }
    }
}
