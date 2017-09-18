package bcccp.carpark.exit;

import bcccp.carpark.CarSensor;
import bcccp.carpark.Carpark;
import bcccp.carpark.IGate;
import bcccp.tickets.adhoc.IAdhocTicket;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
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
    public void stateTransitionTestForAdhoc() {        //IDLE to WAITING, WAITING to REJECTED, WAITING to PROCESSED, PROCESSED to TAKEN, TAKEN to EXITING, EXITING to EXITED
        assertEquals(mockExit.getState(), "IDLE");  //checks initial state
        
        when(mockIS.carIsDetected()).thenReturn(true);
        when(mockIS.getId()).thenReturn("Exit Inside Sensor");      //sets up conditions necessary for transition from IDLE to WAITING
        mockExit.carEventDetected("Exit Inside Sensor", true);
        assertEquals(mockExit.getState(), "WAITING");
        
        when(mockExit.isAdhocTicket("Test")).thenReturn(true);
        when(mockCarpark.getAdhocTicket("Test")).thenReturn(null);      //sets up conditions necessary for transition from WAITNG to REJECTED
        mockExit.ticketInserted("A");
        assertEquals(mockExit.getState(), "REJECTED");
        
        mockExit.setState(mockExit.state.valueOf("WAITING"));       //sets state back to WAITING and checks it
        assertEquals(mockExit.getState(), "WAITING");
        
        when(mockCarpark.getAdhocTicket("A")).thenReturn(mockTicket);
        when(mockTicket.isPaid()).thenReturn(true);                     //sets up conditions necessary for transition from WAITING to PROCESSED
        mockExit.ticketInserted("A");
        assertEquals(mockExit.getState(), "PROCESSED");
        
        mockExit.ticketTaken();
        assertEquals(mockExit.getState(), "TAKEN");     //checks transition from PROCESSED to TAKEN
        
        when(mockOS.carIsDetected()).thenReturn(true);
        when(mockOS.getId()).thenReturn("Exit Outside Sensor");      //sets up conditions necessary for transition from TAKEN to EXITING
        mockExit.carEventDetected("Exit Outside Sensor", true);
        assertEquals(mockExit.getState(), "EXITING");
        
        when(mockIS.carIsDetected()).thenReturn(false);
        when(mockIS.getId()).thenReturn("Exit Inside Sensor");      //sets up conditions necessary for transition from EXITING to EXITED
        mockExit.carEventDetected("Exit Inside Sensor", false);
        assertEquals(mockExit.getState(), "EXITED");
    }
    
    
    @Test(expected=AssertionError.class)
    public void testNullConstructorParam1() {
        ExitController testExit1 = new ExitController(null, mockGate, mockIS, mockOS, mockUI);
        fail("Exception should be thrown.");
    }
    
    @Test(expected=NullPointerException.class)
    public void testNullConstructorParam2() {
        ExitController testExit2 = new ExitController(mockCarpark, null, mockIS, mockOS, mockUI);
        fail("Exception should be thrown.");
    }
    
    @Test(expected=NullPointerException.class)
    public void testNullConstructorParam3() {
        ExitController testExit3 = new ExitController(mockCarpark, mockGate, null, mockOS, mockUI);
        fail("Exception should be thrown.");
    }
    
    @Test(expected=NullPointerException.class)
    public void testNullConstructorParam4() {
        ExitController testExit4 = new ExitController(mockCarpark, mockGate, mockIS, null, mockUI);
        fail("Exception should be thrown.");
    }
    
    @Test(expected=NullPointerException.class)
    public void testNullConstructorParam5() {
        ExitController testExit5 = new ExitController(mockCarpark, mockGate, mockIS, mockOS, null);
        fail("Exception should be thrown.");
    }
}
