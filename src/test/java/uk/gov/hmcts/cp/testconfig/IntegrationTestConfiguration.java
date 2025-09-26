package uk.gov.hmcts.cp.testconfig;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
@EnableAutoConfiguration(exclude = {
        org.springframework.boot.micrometer.tracing.autoconfigure.OpenTelemetryTracingAutoConfiguration.class
})
public class IntegrationTestConfiguration {}
