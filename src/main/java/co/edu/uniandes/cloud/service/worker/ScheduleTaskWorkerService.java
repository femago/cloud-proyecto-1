package co.edu.uniandes.cloud.service.worker;

import co.edu.uniandes.cloud.domain.Application;
import co.edu.uniandes.cloud.domain.enumeration.ApplicationState;
import co.edu.uniandes.cloud.repository.ApplicationRepository;
import co.edu.uniandes.cloud.service.MailService;
import co.edu.uniandes.cloud.service.VoiceEncoderService;
import com.google.common.base.Stopwatch;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static co.edu.uniandes.cloud.config.Constants.CLOICE_PROFILE_SCH_WORKER;

@Service
@Profile(CLOICE_PROFILE_SCH_WORKER)
public class ScheduleTaskWorkerService extends WorkerTriggerService {

    private final ApplicationRepository applicationRepository;

    public ScheduleTaskWorkerService(ApplicationRepository applicationRepository, VoiceEncoderService voiceEncoderService, MailService mailService) {
        super(LoggerFactory.getLogger(ScheduleTaskWorkerService.class), voiceEncoderService, mailService);
        this.applicationRepository = applicationRepository;
    }

    @Scheduled(fixedRate = 60000)
    public void processPendingVoices() {
        log.info("Start encoding pending applications");
        final List<Application> statusInProcess = applicationRepository.findByStatus(ApplicationState.IN_PROCESS);
        Stopwatch stopwatch = Stopwatch.createStarted();
        statusInProcess.parallelStream()
            .filter(super::isApplicationTbp)
            .forEach(super::processApplication);
        log.info("-*-*-*-*- Processing took {} seconds -*-*-*-*-", stopwatch.elapsed(TimeUnit.SECONDS));
    }


}
