package co.edu.uniandes.cloud.repository.dynamo;

import co.edu.uniandes.cloud.config.DynamoDBConfig;
import com.baeldung.spring.data.dynamodb.repository.rule.LocalDbCreationRule;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DynamoDBConfig.class})
@TestPropertySource(properties = {
    "amazon.dynamodb.endpoint=http://localhost:8000/",
    "amazon.aws.accesskey=test1",
    "amazon.aws.secretkey=test231" })
public class ApplicationDynamoRepositoryTest {
    @ClassRule
    public static LocalDbCreationRule dynamoDB = new LocalDbCreationRule();

    @Autowired
    private ApplicationDynamoRepository repo;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void saveApplication() {
        Assert.assertNotNull(repo);
    }
}
