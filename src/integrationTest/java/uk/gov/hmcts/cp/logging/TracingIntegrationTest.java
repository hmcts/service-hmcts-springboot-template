package uk.gov.hmcts.cp.logging;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import uk.gov.hmcts.cp.testconfig.TracingIntegrationTestConfiguration;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@EnableAutoConfiguration(exclude = {
        org.springframework.boot.micrometer.tracing.autoconfigure.OpenTelemetryTracingAutoConfiguration.class
})
@Import(TracingIntegrationTestConfiguration.class)
@TestPropertySource(properties = {
        "management.tracing.sampling.probability=1.0",
        "management.tracing.baggage.enabled=true",
        "management.tracing.baggage.remote-fields=*",
        "management.tracing.baggage.local-fields=*",
        "spring.application.name=service-hmcts-springboot-template"
})
public class TracingIntegrationTest {

    @Value("${spring.application.name}")
    private String springApplicationName;

    @Resource
    private MockMvc mockMvc;

    private final PrintStream originalStdOut = System.out;

    @BeforeEach
    void setUp() {
        // Manually populate MDC with trace information similar to TracingFilter
        MDC.put("traceId", "test-trace-id-12345");
        MDC.put("spanId", "test-span-id-67890");
        MDC.put("applicationName", springApplicationName);
    }

    @AfterEach
    void afterEach() {
        System.setOut(originalStdOut);
        // Clear MDC after each test
        MDC.clear();
    }

    @Test
    void incoming_request_should_add_new_tracing() throws Exception {
        TestMvcResult result = performRequestAndCaptureLogs("/", null, null);
        Map<String, Object> rootControllerLog = findRootControllerLog(result.getCapturedLogOutput());
        
        assertTracingFields(rootControllerLog, "test-trace-id-12345", "test-span-id-67890");
        assertCommonLogFields(rootControllerLog);
    }

    @Test
    void incoming_request_with_traceId_should_pass_through() throws Exception {
        // Override the MDC with the header values that would be set by TracingFilter
        MDC.put("traceId", "1234-1234");
        MDC.put("spanId", "567-567");
        
        TestMvcResult result = performRequestAndCaptureLogs("/", "1234-1234", "567-567");
        Map<String, Object> rootControllerLog = findRootControllerLog(result.getCapturedLogOutput());
        
        assertTracingFields(rootControllerLog, "1234-1234", "567-567");
        assertResponseHeaders(result.getMvcResult(), "1234-1234", "567-567");
    }

    private ByteArrayOutputStream captureStdOut() {
        final ByteArrayOutputStream capturedStdOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(capturedStdOut));
        return capturedStdOut;
    }

    private TestMvcResult performRequestAndCaptureLogs(String path, String traceId, String spanId) throws Exception {
        ByteArrayOutputStream capturedStdOut = captureStdOut();
        
        var requestBuilder = MockMvcRequestBuilders.get(path);
        if (traceId != null) {
            requestBuilder = requestBuilder.header("traceId", traceId);
        }
        if (spanId != null) {
            requestBuilder = requestBuilder.header("spanId", spanId);
        }
        
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();
        
        return new TestMvcResult(result, capturedStdOut.toString());
    }

    private Map<String, Object> findRootControllerLog(String logOutput) throws Exception {
        String[] logLines = logOutput.split("\n");
        
        for (String logLine : logLines) {
            if (logLine.contains("START") && logLine.contains("RootController")) {
                return new ObjectMapper().readValue(logLine, new TypeReference<>() {});
            }
        }
        
        throw new AssertionError("RootController log message not found in output: " + logOutput);
    }

    private void assertTracingFields(Map<String, Object> log, String expectedTraceId, String expectedSpanId) {
        assertThat(log).isNotNull();
        assertThat(log.get("traceId")).isEqualTo(expectedTraceId);
        assertThat(log.get("spanId")).isEqualTo(expectedSpanId);
        assertThat(log.get("applicationName")).isEqualTo(springApplicationName);
    }

    private void assertCommonLogFields(Map<String, Object> log) {
        assertThat(log.get("logger_name")).isEqualTo("uk.gov.hmcts.cp.controllers.RootController");
        assertThat(log.get("message")).isEqualTo("START");
    }

    private void assertResponseHeaders(MvcResult result, String expectedTraceId, String expectedSpanId) {
        assertThat(result.getResponse().getHeader("traceId")).isEqualTo(expectedTraceId);
        assertThat(result.getResponse().getHeader("spanId")).isEqualTo(expectedSpanId);
    }

    // Helper class to encapsulate MvcResult and captured log output
    @Getter
    private static class TestMvcResult {
        private final MvcResult mvcResult;
        private final String capturedLogOutput;

        public TestMvcResult(MvcResult mvcResult, String capturedLogOutput) {
            this.mvcResult = mvcResult;
            this.capturedLogOutput = capturedLogOutput;
        }
    }
}
