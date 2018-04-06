package co.edu.uniandes.cloud.repository.dynamo;

import co.edu.uniandes.cloud.config.DynamoDBConfig;
import co.edu.uniandes.cloud.domain.Application;
import co.edu.uniandes.cloud.domain.Contest;
import co.edu.uniandes.cloud.domain.enumeration.ApplicationState;
import co.edu.uniandes.cloud.repository.EntitiesBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceInUseException;
import com.baeldung.spring.data.dynamodb.repository.rule.LocalDbCreationRule;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Iterator;
import java.util.List;

import static co.edu.uniandes.cloud.domain.enumeration.ApplicationState.CONVERTED;
import static co.edu.uniandes.cloud.domain.enumeration.ApplicationState.IN_PROCESS;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DynamoDBConfig.class})
@TestPropertySource(properties = {
    "amazon.dynamodb.endpoint=http://localhost:8000/",
    "amazon.aws.accesskey=test1",
    "amazon.aws.secretkey=test231"})
@ActiveProfiles(DynamoDBConfig.SPRING_PROFILE_DYNAMODB)
public class ApplicationDynamoRepositoryTest {

    @ClassRule
    public static LocalDbCreationRule dynamoDB = new LocalDbCreationRule();

    private DynamoDBMapper dynamoDBMapper;

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    @Autowired
    private ApplicationDynamoRepository repo;

    private Application application;
    private EntitiesBuilder entitiesBuilder = new EntitiesBuilder();

    @Before
    public void setUp() {
        try {
            dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
            CreateTableRequest tableRequest = dynamoDBMapper.generateCreateTableRequest(Application.class);
            tableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
            amazonDynamoDB.createTable(tableRequest);
        } catch (ResourceInUseException e) {
        }
        dynamoDBMapper.batchDelete(repo.findAll());
    }

    @After
    public void tearDown() {
    }

    @Test
    public void save() {
        saveApplication();

        final Iterator<Application> iterator = repo.findAll().iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(application, iterator.next());
    }

    private void saveApplication() {
        Assert.assertNotNull(repo);
        application = entitiesBuilder.buildApplication();
        application.getContest().setId("id_contest");
        repo.save(application);
    }


    @Test
    public void findById() {
        saveApplication();
        final Application byContest_id = repo.findOne(application.getId());
        Assert.assertEquals(application, byContest_id);
    }

    @Test
    public void findByContest() {
        saveApplication();
        saveApplication();
        saveApplication();
        saveApplication();
        Page<Application> byContest = repo.findByContest(new PageRequest(0, 2), application.getContest());
        assertThat(byContest.getTotalPages(), is(2));
        assertThat(byContest.getTotalElements(), is(4L));

        byContest = repo.findByContest(new PageRequest(0, 2), new Contest("fake_id"));
        assertThat(byContest.getTotalElements(), is(0L));
    }

    @Test
    public void findByStatus() {
        saveApplication();
        saveApplication();
        saveApplication();
        saveApplication();

        List<Application> byStatus = repo.findByStatus(IN_PROCESS);
        assertThat(byStatus.size(), is(4));

        byStatus = repo.findByStatus(ApplicationState.CONVERTED);
        assertTrue(byStatus.isEmpty());
    }

    @Test
    public void findByStatusAndContest() {
        saveApplication();
        saveApplication();
        saveApplication();
        saveApplication();
        final Contest contest = application.getContest();

        Page<Application> byStatusAndContest = repo.findByStatusAndContest(new PageRequest(0, 2), IN_PROCESS, contest);
        assertThat(byStatusAndContest.getTotalPages(), is(2));
        List<Application> content = byStatusAndContest.getContent();
        assertThat(content.size(), is(2));
        content.forEach(app -> assertThat(app.getStatus(), is(ApplicationState.IN_PROCESS)));
        content.forEach(app -> assertThat(app.getContest().getId(), is(contest.getId())));


        byStatusAndContest = repo.findByStatusAndContest(new PageRequest(0, 2), CONVERTED, contest);
        assertThat(byStatusAndContest.getTotalPages(), is(0));
        assertTrue(byStatusAndContest.getContent().isEmpty());

        byStatusAndContest = repo.findByStatusAndContest(new PageRequest(0, 2), IN_PROCESS, new Contest("fake_id"));
        assertThat(byStatusAndContest.getTotalPages(), is(0));
        assertTrue(byStatusAndContest.getContent().isEmpty());
    }
}
