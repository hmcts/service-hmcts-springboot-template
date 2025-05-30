package uk.gov.hmcts.cp.services;

import org.springframework.stereotype.Service;
import uk.gov.hmcts.cp.openapi.model.CourtHouse;

@Service
public class OpenApiService {

    public CourtHouse getCourtHouse(String courtId) {
        CourtHouse courtHouse = new CourtHouse();
        courtHouse.courtHouseName("House name 221B");
        courtHouse.courtHouseCode(courtId);
        return courtHouse;
    }
}
