package bcccp.carpark.exit;

import bcccp.carpark.CarSensor;
import bcccp.carpark.Carpark;
import bcccp.carpark.Gate;
import bcccp.carpark.IGate;
import bcccp.tickets.adhoc.AdhocTicket;
import bcccp.tickets.adhoc.AdhocTicketDAO;
import bcccp.tickets.adhoc.IAdhocTicketDAO;
import bcccp.tickets.season.ISeasonTicketDAO;
import bcccp.tickets.season.SeasonTicket;
import bcccp.tickets.season.SeasonTicketDAO;
import bcccp.tickets.season.UsageRecord;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author James Nocevski
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ExitIntegrationTest {
    
    IGate testGate;
    CarSensor testIS;
    CarSensor testOS; 
    ExitUI testUI;
    Carpark testCarpark;
    AdhocTicket  testAdhoc;
    SeasonTicket testSeason;
    IAdhocTicketDAO mockAdhocDAO;
    ISeasonTicketDAO mockSeasonDAO;
    ExitController testExit;
    String seasonTicketId, cParkName;
    UsageRecord mockRecord;
    int cParkCap;
    
    @Before
    public void setUp() {
        cParkName = "test name";
        cParkCap = 5;
        mockAdhocDAO = mock(AdhocTicketDAO.class);
        mockSeasonDAO = mock(SeasonTicketDAO.class);
        mockRecord = mock(UsageRecord.class);
        testCarpark = new Carpark(cParkName, cParkCap, mockAdhocDAO, mockSeasonDAO);
        testGate = new Gate(0, 0);
        testIS = new CarSensor("test inside", 100, 100);
        testOS = new CarSensor("test outside", 200, 200);
        testUI = mock(ExitUI.class);
        testAdhoc = new AdhocTicket(cParkName, 1, "Atest");
        testSeason = new SeasonTicket("test", cParkName, 1234, 4321);
        testExit = new ExitController(testCarpark, testGate, testIS, testOS, testUI);
    }
    
    @Test
    public void initialisationTest() {
        assertTrue(testCarpark instanceof Carpark);
        assertTrue(testIS instanceof CarSensor);
        assertTrue(testOS instanceof CarSensor);
        assertTrue(testExit instanceof ExitController);
    }
    
    @Test
    public void testNormalFlowSeason() {
        assertEquals(testExit.getState(), "IDLE");  //checks initial state
        testIS.setCarDetected(true);
        testExit.carEventDetected(testIS.getId(), true);
        
        assertEquals(testExit.getState(), "WAITING");
        verify(testUI).display("Insert Ticket");
        
        when(mockSeasonDAO.findTicketById(testSeason.getId())).thenReturn(testSeason);
        testSeason.recordUsage(mockRecord);
        testExit.ticketInserted(testSeason.getId());
        assertEquals(testExit.getState(), "PROCESSED");
        
        testExit.ticketTaken();
        verify(testUI).display("Take Processed Ticket");
        assertEquals(testExit.getState(), "TAKEN");     
        
        testOS.setCarDetected(true);
        testExit.carEventDetected(testOS.getId(), true);
        assertEquals(testExit.getState(), "EXITING");
        
        testIS.setCarDetected(false);
        testExit.carEventDetected(testIS.getId(), false);
        assertEquals(testExit.getState(), "EXITED");
    }
    
    @Test
    public void testNormalFlowAdhoc() {
        assertEquals(testExit.getState(), "IDLE");  //checks initial state
        testIS.setCarDetected(true);
        testExit.carEventDetected(testIS.getId(), true);
        
        assertEquals(testExit.getState(), "WAITING");
        verify(testUI).display("Insert Ticket");            //checks state transition and message display
        
        when(mockAdhocDAO.findTicketByBarcode("Atest")).thenReturn(testAdhoc);
        testAdhoc.pay(1234, 1234);
        testExit.ticketInserted(testAdhoc.getBarcode());
        assertEquals(testExit.getState(), "PROCESSED");
        
        testExit.ticketTaken();
        verify(testUI).display("Take Processed Ticket");
        assertEquals(testExit.getState(), "TAKEN");     
        
        testOS.setCarDetected(true);
        testExit.carEventDetected(testOS.getId(), true);
        assertEquals(testExit.getState(), "EXITING");
        
        testIS.setCarDetected(false);
        testExit.carEventDetected(testIS.getId(), false);
        assertEquals(testExit.getState(), "EXITED");
    }
    
    @Test
    public void testAlternateFlowAdhoc() {          //test accurate detection of rejected adhoc ticket
        assertEquals(testExit.getState(), "IDLE");  //checks initial state
        
        testIS.setCarDetected(true);
        testExit.carEventDetected(testIS.getId(), true);
        assertEquals(testExit.getState(), "WAITING");
        
        when(mockAdhocDAO.findTicketByBarcode("Atest")).thenReturn(null);
        testAdhoc.pay(1234, 1234);
        testExit.ticketInserted(testAdhoc.getBarcode());
        assertEquals(testExit.getState(), "REJECTED");
    }
    
    @Test
    public void testAlternateFlowSeason() {          //test accurate detection of rejected season ticket
        assertEquals(testExit.getState(), "IDLE");  //checks initial state
        
        testIS.setCarDetected(true);
        testExit.carEventDetected(testIS.getId(), true);
        assertEquals(testExit.getState(), "WAITING");
        
        when(mockSeasonDAO.findTicketById(testSeason.getId())).thenReturn(testSeason);
        testExit.ticketInserted(testSeason.getId());
        assertEquals(testExit.getState(), "REJECTED");
    }
    
    @Test(expected=RuntimeException.class)
    public void testAlternateFlowNullSeason() {          //test null season ticket
        assertEquals(testExit.getState(), "IDLE");  //checks initial state
        
        testIS.setCarDetected(true);
        testExit.carEventDetected(testIS.getId(), true);
        assertEquals(testExit.getState(), "WAITING");
        
        when(mockSeasonDAO.findTicketById(testSeason.getId())).thenReturn(null);
        testExit.ticketInserted(testSeason.getId());
        assertEquals(testExit.getState(), "REJECTED");
    }
    
    @Test
    public void testAlternateFlowNullAdhoc() {          //test null adhoc ticket
        assertEquals(testExit.getState(), "IDLE");  //checks initial state
        
        testIS.setCarDetected(true);
        testExit.carEventDetected(testIS.getId(), true);
        assertEquals(testExit.getState(), "WAITING");
        
        when(mockAdhocDAO.findTicketByBarcode(testSeason.getId())).thenReturn(null);
        testExit.ticketInserted(testAdhoc.getBarcode());
        assertEquals(testExit.getState(), "REJECTED");
    }
}
