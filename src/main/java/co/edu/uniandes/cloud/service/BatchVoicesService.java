package co.edu.uniandes.cloud.service;

import co.edu.uniandes.cloud.domain.Application;
import co.edu.uniandes.cloud.domain.enumeration.ApplicationState;
import co.edu.uniandes.cloud.repository.def.ApplicationRepository;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

import static co.edu.uniandes.cloud.config.ApplicationProperties.CLOICE_PROFILE_WORKER;

@Service
@Profile(CLOICE_PROFILE_WORKER)
public class BatchVoicesService {

    private final Logger log = LoggerFactory.getLogger(BatchVoicesService.class);
    private final ApplicationRepository applicationRepository;
    private final VoiceEncoderService voiceEncoderService;
    private MailService mailService;
    private int count;

    public BatchVoicesService(ApplicationRepository applicationRepository, VoiceEncoderService voiceEncoderService, MailService mailService) {
        this.applicationRepository = applicationRepository;
        this.voiceEncoderService = voiceEncoderService;
        this.mailService = mailService;
    }

    @Scheduled(fixedRate = 60000)
    public void processPendingVoices() {
        count = 0;
        log.info("Start encoding pending applications");
        final List<Application> statusInProcess = applicationRepository.findByStatus(ApplicationState.IN_PROCESS);
        statusInProcess.stream()
            .filter(application -> application.getOriginalRecordLocation() != null)
            .forEach(this::wrapProcessing);
    }

    private void wrapProcessing(Application app) {
        try {
            log.info("Processed {}", count);
            voiceEncoderService.processAppOriginalRecord(app);
            log.debug("Sending published voice email to '{}'", app.getEmail());
            mailService.sendStaticEmailFromTemplate(app.getEmail(),
                "stateless_publishApplication",
                "application.voice.published.title",
                ImmutableMap.of("app", app));
        } catch (Exception e) {
            log.error("Unexpected error processing application voice {} {}", app.getId(), e);
        }
        count++;
    }

}
