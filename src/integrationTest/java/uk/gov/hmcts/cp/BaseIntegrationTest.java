package uk.gov.hmcts.cp;

import uk.gov.hmcts.cp.testconfig.IntegrationTestConfiguration;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(IntegrationTestConfiguration.class)
public abstract class BaseIntegrationTest {
    // Base class for integration tests that need to exclude tracing dependencies
}
