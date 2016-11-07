package femr.utd.tests;

import com.google.inject.Inject;
import femr.business.services.core.IEncounterService;
import femr.common.dtos.ServiceResponse;
import femr.common.models.PatientEncounterItem;
import femr.common.models.PatientItem;
import femr.util.calculations.LocaleUnitConverter;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Shadow on 11/7/16.
 */
public class PatientItemMetricTest extends BaseTest {

    private PatientItem patientItem;

    @Before
    public void setup() {
        patientItem = new PatientItem();
        patientItem.setHeightFeet(5);
        patientItem.setHeightInches(7);
        patientItem.setWeight((float)150);
    }

    @Test
    public void toMetricTest() throws Exception {
        //  Use this line **before** refactoring.
        //patientItem = LocaleUnitConverter.toMetric(patientItem);

        //  Use this line **after** refactoring.
        patientItem.toMetric();

        assertEquals((Integer)1, patientItem.getHeightFeet());
        assertEquals((Integer)70, patientItem.getHeightInches());
        assertEquals((Integer)5, patientItem.getHeightFeetDual());
        assertEquals((Integer)7, patientItem.getHeightInchesDual());

        assertEquals((Float)(float)68.04, patientItem.getWeight());
        assertEquals(150, Math.round(patientItem.getWeightDual()));
    }

}
