package co.edu.uniandes.cloud.service.worker;

import co.edu.uniandes.cloud.service.ScalingService;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesResult;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import static co.edu.uniandes.cloud.service.worker.AwsSQSWorkerService.QUEUE_NAME;

@Service
public class AwsSQSScalingService implements ScalingService {

    private AmazonSQS sqs;
    private String queueUrl;
    private GetQueueAttributesRequest attributesRequest;

    @PostConstruct
    public void init() {
        sqs = AmazonSQSClientBuilder.defaultClient();
        queueUrl = sqs.getQueueUrl(QUEUE_NAME).getQueueUrl();
        attributesRequest = new GetQueueAttributesRequest()
            .withQueueUrl(queueUrl)
            .withAttributeNames("ApproximateNumberOfMessages");
    }

    @Override
    public WorkerQueueSize qSize() {
        GetQueueAttributesResult attributesResult = sqs.getQueueAttributes(attributesRequest);
        String approximateNumberOfMessages = attributesResult.getAttributes().getOrDefault("ApproximateNumberOfMessages", "0");
        return new WorkerQueueSize("worker", Integer.parseInt(approximateNumberOfMessages));
    }
}
