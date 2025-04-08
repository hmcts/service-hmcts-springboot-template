package uk.gov.hmcts.cp.config;

import com.microsoft.applicationinsights.TelemetryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public TelemetryClient getTelemetryClient() {
        return new TelemetryClient();
    }

}
