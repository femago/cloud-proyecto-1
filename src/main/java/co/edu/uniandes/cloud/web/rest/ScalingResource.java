package co.edu.uniandes.cloud.web.rest;

import co.edu.uniandes.cloud.service.ScalingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScalingResource {

    private ScalingService scalingService;

    public ScalingResource(ScalingService scalingService) {
        this.scalingService = scalingService;
    }

    @GetMapping("/hirefire/7754431f-0c47-435c-9087-407688d20860/info")
    public ResponseEntity<ScalingService.WorkerQueueSize> get() {
        return ResponseEntity.ok(scalingService.qSize());
    }
}
