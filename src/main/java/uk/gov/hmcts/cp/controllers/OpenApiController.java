package uk.gov.hmcts.cp.controllers;

import com.microsoft.applicationinsights.TelemetryClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.cp.openapi.api.DefaultApi;
import uk.gov.hmcts.cp.openapi.model.CourtHousesschema;
import uk.gov.hmcts.cp.services.OpenApiService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class OpenApiController implements DefaultApi {

    private final OpenApiService openApiService;
    private final TelemetryClient telemetryClient;

    @Override
    public ResponseEntity<CourtHousesschema> courthousesCourtIdGet(String courtId) {
        telemetryClient.trackEvent("GETCourthouseCourt", Map.of("courtId", courtId), null);
        CourtHousesschema courtHousesschema = openApiService.getCourtHouse(courtId);
        return new ResponseEntity<>(courtHousesschema, HttpStatus.OK);
    }

}
