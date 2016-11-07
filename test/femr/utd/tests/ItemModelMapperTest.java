package femr.utd.tests;
import femr.common.dtos.ServiceResponse;
import femr.common.models.MissionTripItem;
import femr.data.models.core.IMissionCity;
import femr.data.models.core.IMissionCountry;
import femr.data.models.core.IMissionTeam;
import femr.data.models.core.IMissionTrip;
import femr.data.models.mysql.MissionCity;
import femr.data.models.mysql.MissionCountry;
import femr.data.models.mysql.MissionTeam;
import femr.data.models.mysql.MissionTrip;
import org.junit.Before;
import org.junit.Test;
import femr.common.models.TripItem;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;


/**
 * Created by TZhang on 11/7/16.
 */
public class ItemModelMapperTest extends BaseTest {

    private IMissionTrip MT;
    private IMissionTeam team;
    private IMissionCity city;
    private IMissionCountry country;
    private MissionTripItem responseItem;

    @Before
    public void setup(){
        //we set up the MissionTrip object first and then pass it to MissionTripItem where the message chain applied to.
        MT = new MissionTrip();
        team = new MissionTeam();
        city = new MissionCity();
        country = new MissionCountry();
        team.setName("Group 1");
        city.setName("Richardson");
        country.setName("USA");
        city.setMissionCountry(country);

        MT.setMissionTeam(team);
        MT.setMissionCity(city);
        MT.setEndDate(new GregorianCalendar(2016, Calendar.DECEMBER, 31).getTime());
        MT.setStartDate(new GregorianCalendar(2016, Calendar.OCTOBER, 1).getTime());
        responseItem = new MissionTripItem();
        responseItem.setId(MT.getId());
        responseItem.setTripStartDate(MT.getStartDate());
        responseItem.setTripEndDate(MT.getEndDate());
        responseItem.setTripCity(MT.getMissionCity().getName());
        responseItem.setTripCountry(MT.getMissionCity().getMissionCountry().getName());
        responseItem.setTeamName(MT.getMissionTeam().getName());
    }

    @Test
    public void teamNameTest() {
        //test team name on MissionTripItem
        assertEquals("Group 1",responseItem.getTeamName());

    }
    @Test
    public void countryNameTest() {
        //test counrty name
        assertEquals("USA",responseItem.getTripCountry());


    }

    @Test
    public void cityNameTest() {
        //test city name
        assertEquals("Richardson",responseItem.getTripCity().toString());


    }
    @Test
    public void EndDateTest() {
        //test end date
        assertEquals(new GregorianCalendar(2016, Calendar.DECEMBER, 31).getTime(),responseItem.getTripEndDate());


    } @Test
    public void StartDateTest() {
        //test start date
        assertEquals(new GregorianCalendar(2016, Calendar.OCTOBER, 1).getTime(),responseItem.getTripStartDate());


    }

}
