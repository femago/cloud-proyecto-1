package co.edu.uniandes.cloud.service.worker;

import co.edu.uniandes.cloud.domain.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import static co.edu.uniandes.cloud.config.Constants.CLOICE_PROFILE_NO_SQS;

@Component
@Profile(CLOICE_PROFILE_NO_SQS)
public class EmptyApplicationEventEmitter implements ApplicationEventEmitter {
    private final Logger log = LoggerFactory.getLogger(EmptyApplicationEventEmitter.class);

    @Override
    public void onSaved(Application app) {
        log.info("Application saved, no notification generated");
    }
}
