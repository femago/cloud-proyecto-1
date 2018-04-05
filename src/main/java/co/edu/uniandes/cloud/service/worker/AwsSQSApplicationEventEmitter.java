package co.edu.uniandes.cloud.service.worker;

import co.edu.uniandes.cloud.domain.Application;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import static co.edu.uniandes.cloud.config.ApplicationProperties.CLOICE_PROFILE_SQS;

@Component
@Profile(CLOICE_PROFILE_SQS)
public class AwsSQSApplicationEventEmitter implements ApplicationEventEmitter {
    private final Logger log = LoggerFactory.getLogger(AwsSQSApplicationEventEmitter.class);
    private final AmazonSQS sqs;
    private final String queueUrl;

    public AwsSQSApplicationEventEmitter() {
        sqs = AmazonSQSClientBuilder.defaultClient();
        queueUrl = sqs.getQueueUrl(AwsSQSWorkerService.QUEUE_NAME).getQueueUrl();
        log.info("Initialized AwsSQSApplicationEventEmitter");
    }

    @Override
    public void onSaved(Application app) {
        SendMessageRequest send_msg_request = new SendMessageRequest()
            .withQueueUrl(queueUrl)
            .withMessageBody(app.getId().toString())
            .withDelaySeconds(1);
        sqs.sendMessage(send_msg_request);
    }
}
