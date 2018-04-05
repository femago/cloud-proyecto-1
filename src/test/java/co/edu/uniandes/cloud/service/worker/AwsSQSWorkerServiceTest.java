package co.edu.uniandes.cloud.service.worker;

import co.edu.uniandes.cloud.CloiceApp;
import co.edu.uniandes.cloud.domain.Application;
import co.edu.uniandes.cloud.domain.enumeration.ApplicationState;
import co.edu.uniandes.cloud.repository.ApplicationRepository;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageBatchRequest;
import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static co.edu.uniandes.cloud.config.ApplicationProperties.CLOICE_PROFILE_SQS_WORKER;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CloiceApp.class)
@ActiveProfiles(CLOICE_PROFILE_SQS_WORKER)
public class AwsSQSWorkerServiceTest {

    final AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
    final String queueUrl = sqs.getQueueUrl(AwsSQSWorkerService.QUEUE_NAME).getQueueUrl();

    @Autowired
    private AwsSQSWorkerService sqsWorkerService;
    private ApplicationRepository mockedRepo;

    @Before
    public void setUp() throws Exception {
        mockedRepo = Mockito.mock(ApplicationRepository.class);
        Whitebox.setInternalState(sqsWorkerService, "applicationRepository", mockedRepo);
    }

    @Test
    public void processPendingVoices() throws InterruptedException {

        final CountDownLatch latch = new CountDownLatch(19);

        Mockito.doAnswer(invocation -> {
            latch.countDown();
            return new Application().status(ApplicationState.CONVERTED);
        }).when(mockedRepo).findOne(Mockito.anyLong());

        SendMessageBatchRequest send_batch_request = new SendMessageBatchRequest()
            .withQueueUrl(queueUrl)
            .withEntries(
                new SendMessageBatchRequestEntry("msg_1", "1"),
                new SendMessageBatchRequestEntry("msg_2", "2"),
                new SendMessageBatchRequestEntry("msg_3", "3"),
                new SendMessageBatchRequestEntry("msg_4", "4"),
                new SendMessageBatchRequestEntry("msg_5", "5"),
                new SendMessageBatchRequestEntry("msg_6", "6"),
                new SendMessageBatchRequestEntry("msg_7", "7"),
                new SendMessageBatchRequestEntry("msg_8", "8"),
                new SendMessageBatchRequestEntry("msg_9", "9"),
                new SendMessageBatchRequestEntry("msg_10", "10"));
        sqs.sendMessageBatch(send_batch_request);
        send_batch_request = new SendMessageBatchRequest()
            .withQueueUrl(queueUrl)
            .withEntries(
                new SendMessageBatchRequestEntry("msg_12", "12"),
                new SendMessageBatchRequestEntry("msg_22", "22"),
                new SendMessageBatchRequestEntry("msg_32", "32"),
                new SendMessageBatchRequestEntry("msg_42", "42"),
                new SendMessageBatchRequestEntry("msg_52", "52"),
                new SendMessageBatchRequestEntry("msg_62", "62"),
                new SendMessageBatchRequestEntry("msg_72", "72"),
                new SendMessageBatchRequestEntry("msg_82", "82"),
                new SendMessageBatchRequestEntry("msg_92", "92"));
        sqs.sendMessageBatch(send_batch_request);


        latch.await(2, TimeUnit.MINUTES);
        assertThat(latch.getCount(), is(0L));
    }
}
