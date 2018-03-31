package co.edu.uniandes.cloud.repository.dynamo;

import co.edu.uniandes.cloud.domain.Contest;
import co.edu.uniandes.cloud.domain.User;
import co.edu.uniandes.cloud.domain.enumeration.ApplicationState;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMarshaller;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Base64;

public class DynamoConverters {

    public static class InstantConverter implements DynamoDBTypeConverter<String, Instant> {
        @Override
        public String convert(Instant object) {
            return object.toString();
        }

        @Override
        public Instant unconvert(String instant) {
            return Instant.parse(instant);
        }
    }

    public static class LocalDateConverter implements DynamoDBTypeConverter<String, LocalDate> {
        @Override
        public String convert(LocalDate localDate) {
            return localDate.toString();
        }

        @Override
        public LocalDate unconvert(String instant) {
            return LocalDate.parse(instant);
        }
    }

    public static class ApplicationStateConverter implements DynamoDBTypeConverter<String, ApplicationState>, DynamoDBMarshaller<ApplicationState> {
        @Override
        public String convert(ApplicationState object) {
            return object.toString();
        }

        @Override
        public ApplicationState unconvert(String state) {
            return ApplicationState.valueOf(state);
        }

        @Override
        public String marshall(ApplicationState applicationState) {
            return convert(applicationState);
        }

        @Override
        public ApplicationState unmarshall(Class<ApplicationState> clazz, String applicationState) {
            return unconvert(applicationState);
        }
    }

    public static class ContestConverter implements DynamoDBTypeConverter<String, Contest>, DynamoDBMarshaller<Contest> {

        @Override
        public String convert(Contest contest) {
            return contest.getId();
        }

        @Override
        public Contest unconvert(String id) {
            return new Contest(id);
        }

        @Override
        public String marshall(Contest contest) {
            return convert(contest);
        }

        @Override
        public Contest unmarshall(Class<Contest> clazz, String id) {
            return unconvert(id);
        }
    }

    public static class ByteArrayConverter implements DynamoDBTypeConverter<String, byte[]> {
        @Override
        public String convert(byte[] object) {
            return Base64.getEncoder().encodeToString(object);
        }

        @Override
        public byte[] unconvert(String base64) {
            return Base64.getDecoder().decode(base64);
        }
    }

    public static class BigDecimalConverter implements DynamoDBTypeConverter<String, BigDecimal> {
        @Override
        public String convert(BigDecimal object) {
            return object.toString();
        }

        @Override
        public BigDecimal unconvert(String bigDecimalString) {
            return new BigDecimal(bigDecimalString);
        }
    }

    public static class UserConverter implements DynamoDBMarshaller<User> {
        @Override
        public String marshall(User user) {
            return user.getLogin();
        }

        @Override
        public User unmarshall(Class<User> clazz, String loginSaved) {
            final User user = new User();
            user.setLogin(loginSaved);
            return user;
        }
    }
}
