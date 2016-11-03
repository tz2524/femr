package femr.utd.tests;

import com.google.inject.Inject;
import femr.business.services.core.IMedicationService;
import femr.business.services.core.IResearchService;
import femr.common.dtos.ServiceResponse;
import femr.common.models.ResearchFilterItem;
import femr.common.models.ResearchResultSetItem;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Shadow on 11/2/16.
 */
public class ResearchServiceTest extends BaseTest {

    private static IResearchService service;
    private static ResearchFilterItem filters;

    @Inject
    public void setService(IResearchService service) {
        this.service = service;
    }

    @Before
    public void setup() {
        //  Config the filters.
        filters = new ResearchFilterItem();
        filters.setGraphType("stacked-bar");
        filters.setStartDate("2002-07-05");
        filters.setEndDate("2016-11-02");
        filters.setGroupPrimary(false);
        filters.setGroupFactor(10);
        filters.setFilterRangeStart((float) 1);
        filters.setFilterRangeEnd((float) 10000);
        filters.setMedicationName(null);
        filters.setOrderBy(null);
        filters.setMissionTripId(1);
    }

    @Test
    public void ageGenderTest() {

        //  In this case, `buildAgeResultSet` is gonna be invoked.
        filters.setPrimaryDataset("age");

        //  The case secondary is "gender".
        filters.setSecondaryDataset("gender");
        ServiceResponse<ResearchResultSetItem> response = service.retrieveGraphData(filters);

        //  Make sure the secondary data was retrieved.
        assertNotNull(response.getResponseObject().getSecondaryValueMap());

    }

    @Test
    public void agePregnancyTest() {

        //  In this case, `buildAgeResultSet` is gonna be invoked.
        filters.setPrimaryDataset("age");

        //  The case secondary is "pregnancyStatus".
        filters.setSecondaryDataset("pregnancyStatus");
        ServiceResponse<ResearchResultSetItem> response = service.retrieveGraphData(filters);

        //  Make sure the secondary data was retrieved.
        assertNotNull(response.getResponseObject().getSecondaryValueMap());

    }

    @Test
    public void heightGenderTest() {

        //  In this case, `buildHeightResultSet` is gonna be invoked.
        filters.setPrimaryDataset("height");

        //  The case secondary is "gender".
        filters.setSecondaryDataset("gender");
        ServiceResponse<ResearchResultSetItem> response = service.retrieveGraphData(filters);

        //  Make sure the secondary data was retrieved.
        assertNotNull(response.getResponseObject().getSecondaryValueMap());

    }

    @Test
    public void heightPregnancyTest() {

        //  In this case, `buildHeightResultSet` is gonna be invoked.
        filters.setPrimaryDataset("height");

        //  The case secondary is "pregnancyStatus".
        filters.setSecondaryDataset("pregnancyStatus");
        ServiceResponse<ResearchResultSetItem> response = service.retrieveGraphData(filters);

        //  Make sure the secondary data was retrieved.
        assertNotNull(response.getResponseObject().getSecondaryValueMap());
    }


    @Test
    public void vitalGenderTest() {

        //  In this case, `buildVitalResultSet` is gonna be invoked.
        filters.setPrimaryDataset("weight");

        //  The case secondary is "gender".
        filters.setSecondaryDataset("gender");
        ServiceResponse<ResearchResultSetItem> response = service.retrieveGraphData(filters);

        //  Make sure the secondary data was retrieved.
        assertNotNull(response.getResponseObject().getSecondaryValueMap());
    }


    @Test
    public void vitalPregnancyTest() {

        //  In this case, `buildVitalResultSet` is gonna be invoked.
        filters.setPrimaryDataset("weight");

        //  The case secondary is "pregnancyStatus".
        filters.setSecondaryDataset("pregnancyStatus");
        ServiceResponse<ResearchResultSetItem> response = service.retrieveGraphData(filters);

        //  Make sure the secondary data was retrieved.
        assertNotNull(response.getResponseObject().getSecondaryValueMap());
    }


}
