package co.edu.uniandes.cloud.repository.dynamo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.time.Instant;

public class DynamoInstantConverter implements DynamoDBTypeConverter<String, Instant> {
        @Override
        public String convert(Instant object) {
            return object.toString();
        }

        @Override
        public Instant unconvert(String string) {
            return Instant.parse(string);
        }
    }
