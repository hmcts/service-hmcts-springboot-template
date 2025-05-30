package uk.gov.hmcts.cp.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.cp.openapi.api.CourtHouseApi;
import uk.gov.hmcts.cp.openapi.model.CourtHouse;
import uk.gov.hmcts.cp.services.OpenApiService;

@RestController
@RequiredArgsConstructor
public class OpenApiController implements CourtHouseApi {

    private final OpenApiService openApiService;

    @Override
    public ResponseEntity<CourtHouse> getCourthouseByCourtId(String courtId) {
        CourtHouse courtHouse = openApiService.getCourtHouse(courtId);
        return new ResponseEntity<>(courtHouse, HttpStatus.OK);
    }

}
