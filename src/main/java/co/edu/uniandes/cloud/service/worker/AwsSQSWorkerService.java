package co.edu.uniandes.cloud.service.worker;

import co.edu.uniandes.cloud.domain.Application;
import co.edu.uniandes.cloud.repository.ApplicationRepository;
import co.edu.uniandes.cloud.service.MailService;
import co.edu.uniandes.cloud.service.VoiceEncoderService;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.google.common.base.Stopwatch;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static co.edu.uniandes.cloud.config.Constants.CLOICE_PROFILE_SQS_WORKER;

@Service
@Profile(CLOICE_PROFILE_SQS_WORKER)
public class AwsSQSWorkerService extends WorkerTriggerService implements Runnable {

    public static final String QUEUE_NAME = "uniandes-cloud-cloice";

    private final ApplicationRepository applicationRepository;

    private final AmazonSQS sqs;
    private final String queueUrl;
    private final ReceiveMessageRequest rq;

    public AwsSQSWorkerService(VoiceEncoderService voiceEncoderService, MailService mailService, ApplicationRepository applicationRepository,
                               @Value("${amazon.sqs.reconnection-rate-seconds}") int reconnection_rate_seconds) {
        super(LoggerFactory.getLogger(AwsSQSWorkerService.class), voiceEncoderService, mailService);
        this.applicationRepository = applicationRepository;
        sqs = AmazonSQSClientBuilder.defaultClient();
        queueUrl = sqs.getQueueUrl(QUEUE_NAME).getQueueUrl();
        rq = new ReceiveMessageRequest()
            .withQueueUrl(queueUrl)
            .withWaitTimeSeconds(20)
            .withMaxNumberOfMessages(10);

        final ScheduledThreadPoolExecutor schThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
        schThreadPoolExecutor.scheduleAtFixedRate(this, 0, reconnection_rate_seconds, TimeUnit.SECONDS);
    }

    public void processPendingVoices() {
        final ReceiveMessageResult receiveMessageResult = sqs.receiveMessage(rq);
        log.info("Listening to SQS");
        final List<Message> messages = receiveMessageResult.getMessages();
        log.info("Received from SQS {} mssgs", messages.size());

        Stopwatch stopwatch = Stopwatch.createStarted();
        messages.parallelStream().forEach(this::processMessage);
        log.info("-*-*-*-*- Processing {} took {} seconds -*-*-*-*-", messages.size(), stopwatch.elapsed(TimeUnit.SECONDS));
    }

    private void processMessage(Message msg) {
        try {
            log.info("Processing SQS Message {}", msg);
            // Garantizar que ninguna excepcion sale para evitar romper el thread que se queda escuchando por mensajes
            final Application app = applicationRepository.findOne(msg.getBody());
            if (!super.isApplicationTbp(app)) {
                log.info("Message with an already processed application {}", app);
                removeFromSQS(msg);
                return;
            }
            if (super.processApplication(app)) {
                removeFromSQS(msg);
            }
        } catch (Exception e) {
            super.handleFailedApplicationProcessing(e, msg);
        }
    }

    private void removeFromSQS(Message msg) {
        sqs.deleteMessage(queueUrl, msg.getReceiptHandle());
        log.info("Message removed from SQS {}", msg.getMessageId());
    }

    @Override
    public void run() {
        processPendingVoices();
    }
}
