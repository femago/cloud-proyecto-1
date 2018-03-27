package co.edu.uniandes.cloud.repository.dynamo;

import co.edu.uniandes.cloud.config.DynamoDBConfig;
import com.baeldung.spring.data.dynamodb.repository.rule.LocalDbCreationRule;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DynamoDBConfig.class})
public class ApplicationDynamoRepositoryTest {
    @ClassRule
    public static LocalDbCreationRule dynamoDB = new LocalDbCreationRule();

    private ApplicationDynamoRepository repo;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
}
