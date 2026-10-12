package au.com.appk8deployment.health;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class HealthControllerTest {

    @Test
    void healthResponseContainsSuccessAndVersion() {
        HealthController controller =
                new HealthController("1.2.3", "unit-test", "healthy");

        Map<String, String> result = controller.health();

        assertThat(result.get("status")).isEqualTo("success");
        assertThat(result.get("version")).isEqualTo("1.2.3");
        assertThat(result.get("environment")).isEqualTo("unit-test");
    }
}
