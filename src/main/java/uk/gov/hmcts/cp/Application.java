package uk.gov.hmcts.cp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@SuppressWarnings("HideUtilityClassConstructor")
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(final String[] args) {
        logger.info("Starting Spring Boot application...");
        SpringApplication.run(Application.class, args);
    }
}
