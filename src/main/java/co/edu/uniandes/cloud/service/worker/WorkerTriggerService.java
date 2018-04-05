package co.edu.uniandes.cloud.service.worker;

import co.edu.uniandes.cloud.domain.Application;
import co.edu.uniandes.cloud.domain.enumeration.ApplicationState;
import co.edu.uniandes.cloud.service.MailService;
import co.edu.uniandes.cloud.service.VoiceEncoderService;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;

public abstract class WorkerTriggerService {

    protected final Logger log;
    private final VoiceEncoderService voiceEncoderService;
    private final MailService mailService;

    public WorkerTriggerService(Logger log, VoiceEncoderService voiceEncoderService, MailService mailService) {
        this.log = log;
        this.voiceEncoderService = voiceEncoderService;
        this.mailService = mailService;
    }

    public abstract void processPendingVoices();


    protected final boolean processApplication(Application app) {
        try {
            log.info("Processing Application {}", app.getId());
            voiceEncoderService.processAppOriginalRecord(app);
            log.debug("Sending published voice email to '{}'", app.getEmail());
            mailService.sendStaticEmailFromTemplate(app.getEmail(),
                "stateless_publishApplication",
                "application.voice.published.title",
                ImmutableMap.of("app", app));
            return true;
        } catch (Exception e) {
            handleFailedApplicationProcessing(e, app);
            return false;
        }
    }

    protected final boolean isApplicationTbp(Application app) {
        return app != null
            && ApplicationState.IN_PROCESS.equals(app.getStatus())
            && app.getOriginalRecordLocation() != null;
    }

    protected void handleFailedApplicationProcessing(Exception e, Object applicationInfo) {
        String applicationId = applicationInfo.toString();
        if (applicationInfo instanceof Application) {
            applicationId = "with id: " + ((Application) applicationInfo).getId();
        }
        log.error("Unexpected error processing application voice " + applicationId, e);
    }
}
