package co.edu.uniandes.cloud.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface ScalingService {
    WorkerQueueSize qSize();

    @Getter
    @AllArgsConstructor
    class WorkerQueueSize {
        private String name;
        private int quantity;
    }
}
