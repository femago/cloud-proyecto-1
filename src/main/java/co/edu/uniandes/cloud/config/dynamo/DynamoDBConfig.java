package co.edu.uniandes.cloud.config.dynamo;

import co.edu.uniandes.cloud.config.Constants;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import org.apache.commons.lang3.StringUtils;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableDynamoDBRepositories(basePackages = "co.edu.uniandes.cloud.repository.dynamo")
@Profile(Constants.CLOICE_PROFILE_DYNAMODB)
public class DynamoDBConfig {

    @Value("${amazon.dynamodb.endpoint}")
    private String amazonDynamoDBEndpoint;

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        //AmazonDynamoDBClientBuilder.standard().withCredentials(amazonAWSCredentialsProvider());
        AmazonDynamoDB amazonDynamoDB
            = AmazonDynamoDBClientBuilder.defaultClient();

        if (!StringUtils.isEmpty(amazonDynamoDBEndpoint)) {
            amazonDynamoDB.setEndpoint(amazonDynamoDBEndpoint);
        }

        return amazonDynamoDB;
    }

}
