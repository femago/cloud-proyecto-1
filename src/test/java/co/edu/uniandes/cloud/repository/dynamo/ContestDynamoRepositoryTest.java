package co.edu.uniandes.cloud.repository.dynamo;

import co.edu.uniandes.cloud.config.Constants;
import co.edu.uniandes.cloud.config.dynamo.DynamoDBConfig;
import co.edu.uniandes.cloud.domain.Contest;
import co.edu.uniandes.cloud.domain.User;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.*;
import com.baeldung.spring.data.dynamodb.repository.rule.LocalDbCreationRule;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.Iterator;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DynamoDBConfig.class})
@TestPropertySource(properties = {
//    "amazon.dynamodb.endpoint=http://localhost:8000/"})
    "amazon.dynamodb.endpoint=dynamodb.us-east-1.amazonaws.com"})
@ActiveProfiles(Constants.CLOICE_PROFILE_DYNAMODB)
public class ContestDynamoRepositoryTest {

    PodamFactory factory = new PodamFactoryImpl();

    @ClassRule
    public static LocalDbCreationRule dynamoDB = new LocalDbCreationRule();

    private DynamoDBMapper dynamoDBMapper;

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    @Autowired
    private ContestDynamoRepository repo;


    @Before
    public void setUp() {
        if (false) {
            try {
                dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
                CreateTableRequest tableRequest = dynamoDBMapper.generateCreateTableRequest(Contest.class);
                tableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
                Projection projection = new Projection().withProjectionType(ProjectionType.ALL);
                tableRequest.getGlobalSecondaryIndexes()
                    .forEach(gsi -> {
                        gsi.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
                        gsi.setProjection(projection);
                    });
                amazonDynamoDB.createTable(tableRequest);
            } catch (ResourceInUseException e) {
            }
            repo.deleteAll();
        }
    }

    @After
    public void tearDown() {
        // repo.deleteAll();
    }

    @Test
    @Ignore
    public void save() {
        Contest cnts = factory.manufacturePojo(Contest.class);
        cnts.setId(null);
        repo.save(cnts);

        final Iterable<Contest> all = repo.findAll();
        final Iterator<Contest> iterator = all.iterator();
        assertTrue(iterator.hasNext());
        final Contest stored = iterator.next();

        adjustToHowWasSaved(cnts);

        assertThat(cnts.toString(), is(stored.toString()));

    }

    private void adjustToHowWasSaved(Contest cnts) {
        //Aplicaciones no se guardan
        cnts.getApplications().clear();
        //El usuario solo guarda el login
        final User user = new User();
        user.setLogin(cnts.getUser().getLogin());
        cnts.user(user);
    }

    @Test
    public void testFindAllPaged() {
        Sort sort = new Sort("createDate");
        Page<Contest> all = repo.findAll(new PageRequest(0, 20, sort));
        assertTrue(!all.getContent().isEmpty());
    }

    @Test
    public void testFindByUser() {
        Sort sort = new Sort("createDate");
        User user1 = new User();
        user1.setLogin("user");
        Page<Contest> user = repo.findByUser(new PageRequest(0, 20, sort), user1);
        List<Contest> content = user.getContent();
        assertTrue(!content.isEmpty());
        System.out.println(content.size() + " " + content);
    }
}
