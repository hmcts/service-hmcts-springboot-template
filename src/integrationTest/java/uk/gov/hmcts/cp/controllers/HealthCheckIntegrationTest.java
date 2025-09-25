package uk.gov.hmcts.cp.controllers;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import uk.gov.hmcts.cp.BaseIntegrationTest;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class HealthCheckIntegrationTest extends BaseIntegrationTest {

    @Resource
    private MockMvc mockMvc;

    @DisplayName("Actuator health status should be UP")
    @Test
    void shouldCallActuatorAndGet200() throws Exception {
        mockMvc.perform(get("/health"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }
}
