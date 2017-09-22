package bcccp.carpark.exit;

import bcccp.carpark.CarSensor;
import bcccp.carpark.Carpark;
import bcccp.carpark.IGate;
import bcccp.tickets.adhoc.IAdhocTicket;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
/**
 *
 * @author James Nocevski
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ExitControllerTest {
    
            Carpark mockCarpark;
            IGate mockGate;
            CarSensor mockIS;
            CarSensor mockOS;
            IExitUI mockUI;
            IAdhocTicket mockTicket;
            ExitController mockExit;
    
    
            
    @Before
	public void setUp() {
            mockCarpark = mock(Carpark.class);
            mockGate = mock(IGate.class);
            mockIS = mock(CarSensor.class);
            mockOS = mock(CarSensor.class);
            mockUI = mock(IExitUI.class);
            mockTicket = mock(IAdhocTicket.class);
            mockExit = new ExitController(mockCarpark, mockGate, mockIS, mockOS, mockUI);
            
	}
    
    @Test    
    public void initialisationTest() {
        assertTrue(mockExit instanceof ExitController);
        assertTrue(mockIS instanceof CarSensor);
        assertTrue(mockOS instanceof CarSensor);
        assertEquals(mockExit.getState(), "IDLE");
    }
    
    @Test
    public void adhocIdleToWaitingTest() {        //IDLE to WAITING, WAITING to REJECTED, WAITING to PROCESSED, PROCESSED to TAKEN, TAKEN to EXITING, EXITING to EXITED
        assertEquals(mockExit.getState(), "IDLE");  //checks initial state
        
        when(mockIS.carIsDetected()).thenReturn(true);
        when(mockIS.getId()).thenReturn("Exit Inside Sensor");      //sets up conditions necessary for transition from IDLE to WAITING
        mockExit.carEventDetected("Exit Inside Sensor", true);
        assertEquals(mockExit.getState(), "WAITING");
    }
    
    @Test
    public void adhocWaitingToRejectedTest() {
        when(mockIS.carIsDetected()).thenReturn(true);
        mockExit.setTestState("WAITING");
        assertEquals(mockExit.getState(), "WAITING");
        when(mockExit.isAdhocTicket("Test")).thenReturn(true);
        when(mockCarpark.getAdhocTicket("Test")).thenReturn(null);      //sets up conditions necessary for transition from WAITNG to REJECTED
        mockExit.ticketInserted("A");
        assertEquals(mockExit.getState(), "REJECTED");
    }
    
    @Test
    public void adhocWaitingToProcessedTest() {
        when(mockIS.carIsDetected()).thenReturn(true);
        mockExit.setTestState("WAITING");
        assertEquals(mockExit.getState(), "WAITING");
        when(mockCarpark.getAdhocTicket("A")).thenReturn(mockTicket);
        when(mockTicket.isPaid()).thenReturn(true);                     //sets up conditions necessary for transition from WAITING to PROCESSED
        mockExit.ticketInserted("A");
        assertEquals(mockExit.getState(), "PROCESSED");
    }
    
    @Test
    public void adhocProcessedToTakenTest() {
        when(mockIS.carIsDetected()).thenReturn(true);
        mockExit.setTestState("PROCESSED");
        assertEquals(mockExit.getState(), "PROCESSED");
        mockExit.ticketTaken();
        assertEquals(mockExit.getState(), "TAKEN");     //checks transition from PROCESSED to TAKEN
    }
    
    @Test
    public void adhocTakenToExitingTest() {
        mockExit.setTestState("TAKEN");
        assertEquals(mockExit.getState(), "TAKEN");
        when(mockOS.carIsDetected()).thenReturn(true);
        when(mockOS.getId()).thenReturn("Exit Outside Sensor");      //sets up conditions necessary for transition from TAKEN to EXITING
        mockExit.carEventDetected("Exit Outside Sensor", true);
        assertEquals(mockExit.getState(), "EXITING");
    }
    
    @Test
    public void adhocExitingToExitedTest() {
        mockExit.setTestState("EXITING");
        assertEquals(mockExit.getState(), "EXITING");
        when(mockIS.carIsDetected()).thenReturn(false);
        when(mockIS.getId()).thenReturn("Exit Inside Sensor");      //sets up conditions necessary for transition from EXITING to EXITED
        mockExit.carEventDetected("Exit Inside Sensor", false);
        assertEquals(mockExit.getState(), "EXITED");
    }
       
    @Test
    public void seasonIdleToWaitingTest() {
        assertEquals(mockExit.getState(), "IDLE");  //checks initial state
        when(mockIS.carIsDetected()).thenReturn(true);
        when(mockIS.getId()).thenReturn("Exit Inside Sensor");      //sets up conditions necessary for transition from IDLE to WAITING
        mockExit.carEventDetected("Exit Inside Sensor", true);
        assertEquals(mockExit.getState(), "WAITING");
    }
    
    @Test
    public void seasonWaitingToRejectedTest() {
        when(mockIS.carIsDetected()).thenReturn(true);
        mockExit.setTestState("WAITING");
        assertEquals(mockExit.getState(), "WAITING");
        when(mockExit.isAdhocTicket("Test")).thenReturn(false);
        when(mockCarpark.isSeasonTicketValid("Test")).thenReturn(false);      //sets up conditions necessary for transition from WAITNG to REJECTED
        when(mockCarpark.isSeasonTicketInUse("Test")).thenReturn(false);
        when(mockIS.carIsDetected()).thenReturn(true);
        mockExit.ticketInserted("Test");
        System.out.println(mockExit.getState());
        assertEquals(mockExit.getState(), "REJECTED");
    }
    
    @Test
    public void seasonWaitingToProcessedTest() {
        when(mockIS.carIsDetected()).thenReturn(true);
        mockExit.setTestState("WAITING");
        assertEquals(mockExit.getState(), "WAITING");
        when(mockExit.isAdhocTicket("Test")).thenReturn(false);
        when(mockIS.carIsDetected()).thenReturn(true);
        when(mockCarpark.isSeasonTicketValid("Test")).thenReturn(true);                  //sets up conditions necessary for transition from WAITING to PROCESSED
        when(mockCarpark.isSeasonTicketInUse("Test")).thenReturn(true);
        mockExit.ticketInserted("Test");
        assertEquals(mockExit.getState(), "PROCESSED");
    }

}
