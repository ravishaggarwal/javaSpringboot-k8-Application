package au.com.auspost.health;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    private final String version;
    private final String environment;
    private final String message;

    public HealthController(
            @Value("${app.version:unknown}") String version,
            @Value("${app.environment:local}") String environment,
            @Value("${app.message:application is healthy}") String message) {
        this.version = version;
        this.environment = environment;
        this.message = message;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of(
                "status", "success",
                "message", message,
                "version", version,
                "environment", environment
        );
    }
}
