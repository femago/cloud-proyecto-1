package co.edu.uniandes.cloud.service.worker;

import co.edu.uniandes.cloud.domain.Application;

public interface ApplicationEventEmitter {
    void onSaved(Application app);
}
