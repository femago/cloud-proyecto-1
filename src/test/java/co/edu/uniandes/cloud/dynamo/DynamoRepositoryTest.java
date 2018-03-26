//package co.edu.uniandes.cloud.dynamo;
//
//import co.edu.uniandes.cloud.config.DynamoDBConfig;
//import co.edu.uniandes.cloud.domain.Application;
//import co.edu.uniandes.cloud.domain.Contest;
//import co.edu.uniandes.cloud.repository.dynamo.ApplicationDynamoRepository;
//import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
//import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
//import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
//import org.junit.*;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import java.util.List;
//
//import static org.hamcrest.Matchers.is;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = {DynamoDBConfig.class})
//@TestPropertySource(properties = {
//    "amazon.dynamodb.endpoint=http://localhost:8000/",
//    "amazon.aws.accesskey=test1",
//    "amazon.aws.secretkey=test231"})
//public class DynamoRepositoryTest {
//    private static final long CAPACITY = 5L;
//
//    @Autowired
//    private AmazonDynamoDB amazonDynamoDB;
//
//    @Autowired
//    ApplicationDynamoRepository repository;
//
//    private static DynamoTest.StartHTTPDynamo httpDynamo;
//
//    @BeforeClass
//    public static void beforeClass() throws Exception {
//        httpDynamo = new DynamoTest.StartHTTPDynamo();
//        httpDynamo.start();
//    }
//
//    @AfterClass
//    public static void afterClass() throws Exception {
//        httpDynamo.stop();
//    }
//
//    @Before
//    public void init() throws Exception {
//        // Delete User table in case it exists
//        amazonDynamoDB.listTables().getTableNames().stream().
//            filter(tableName -> tableName.equals(Application.TABLE_NAME)).forEach(tableName -> {
//            amazonDynamoDB.deleteTable(tableName);
//        });
//
//        //Create User table
//        amazonDynamoDB.createTable(new DynamoDBMapper(amazonDynamoDB)
//            .generateCreateTableRequest(Application.class)
//            .withProvisionedThroughput(new ProvisionedThroughput(CAPACITY, CAPACITY)));
//    }
//
//    @Test
//    public void sampleTestCase() {
//        Contest c1 = new Contest();
//        c1.setId("1");
//        Contest c2 = new Contest();
//        c2.setId("2");
//
//        Application app1 = new Application()
//            .email("f1@g.co")
//            .name("nombre")
//            .lastname("apellido")
//            ;
//        repository.save(app1);
//
//        Application app2 = new Application()
//            .email("f2@g.co")
//            .name("nombre1")
//            .lastname("apellido1")
//            ;
//        repository.save(app2);
//
//        new Application();
//        Application app3 = new Application()
//            .email("f3@g.co")
//            .name("nombre3")
//            .lastname("apellido3")
//            ;
//        repository.save(app3);
//
//        Application app4 = new Application()
//            .email("f4@g.co")
//            .name("nombre4")
//            .lastname("apellido4")
//            ;
//        repository.save(app4);
//
//
//        PageRequest pageRequest = new PageRequest(0, 1);
//        Page<Application> byContest_id = repository.findAll(pageRequest);
//        List<Application> content = byContest_id.getContent();
//        Assert.assertThat(content.size(), is(4));
//        System.out.println(content);
//    }
//
//
//}
