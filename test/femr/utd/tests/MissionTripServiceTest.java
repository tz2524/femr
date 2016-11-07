package femr.utd.tests;

import femr.common.dtos.ServiceResponse;
import org.junit.Before;
import org.junit.Test;
import femr.common.models.TripItem;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;


/**
 * Created by TZhang on 11/6/16.
 */
public class MissionTripServiceTest extends BaseTest {

    private TripItem item;
    private ServiceResponse<TripItem> responseItem = new ServiceResponse<>();



    @Before
    public void setUp(){

        //Trip Item setup
        item = new TripItem();
        item.setTeamName("Group 1");
        item.setTripCity("Richardson");
        item.setTripCountry("USA");
        item.setTripEndDate(new GregorianCalendar(2016, Calendar.DECEMBER, 31).getTime());
        item.setTripStartDate(new GregorianCalendar(2016, Calendar.OCTOBER, 1).getTime());
        responseItem.setResponseObject(item);

    }

    //In this section, Trip item is tested
    @Test
    public void teamNameTest() {
        //Test team name
        assertEquals("Group 1",responseItem.getResponseObject().getTeamName());

    }
    @Test
    public void countryNameTest() {
        //Test country name
        assertEquals("USA",responseItem.getResponseObject().getTripCountry());


    }

    @Test
    public void cityNameTest() {
        //test city name
        assertEquals("Richardson",responseItem.getResponseObject().getTripCity().toString());


    }
    @Test
    public void EndDateTest() {
        //test end date
        assertEquals(new GregorianCalendar(2016, Calendar.DECEMBER, 31).getTime(),responseItem.getResponseObject().getTripEndDate());


    } @Test
    public void StartDateTest() {
        //test start date
        assertEquals(new GregorianCalendar(2016, Calendar.OCTOBER, 1).getTime(),responseItem.getResponseObject().getTripStartDate());


    }

}
