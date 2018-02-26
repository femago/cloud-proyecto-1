package co.edu.uniandes.cloud.service;

import co.edu.uniandes.cloud.domain.Application;
import co.edu.uniandes.cloud.domain.enumeration.ApplicationState;
import co.edu.uniandes.cloud.repository.ApplicationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BatchVoicesService {

    private final Logger log = LoggerFactory.getLogger(BatchVoicesService.class);
    private final ApplicationRepository applicationRepository;
    private final VoiceEncoderService voiceEncoderService;

    public BatchVoicesService(ApplicationRepository applicationRepository, VoiceEncoderService voiceEncoderService) {
        this.applicationRepository = applicationRepository;
        this.voiceEncoderService = voiceEncoderService;
    }

    @Scheduled(fixedRate = 60000)
    public void processPendingVoices() {
        log.info("Start encoding pending applications");
        final List<Application> statusInProcess = applicationRepository.findByStatusEquals(ApplicationState.IN_PROCESS);
        statusInProcess.stream()
            .filter(application -> application.getOriginalRecordLocation() != null)
            .forEach(this::wrapProcessing);
    }

    private void wrapProcessing(Application app) {
        try {
            voiceEncoderService.processAppOriginalRecord(app);
        } catch (Exception e) {
            log.error("Unexpected error processing application voice {}", app.getId());
        }
    }
}
