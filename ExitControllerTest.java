package bcccp.carpark.exit;

import bcccp.carpark.CarSensor;
import bcccp.carpark.Carpark;
import bcccp.carpark.IGate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
/**
 *
 * @author James Nocevski
 */
public class ExitControllerTest {
    
            Carpark mockCarpark;
            IGate mockGate;
            CarSensor mockIS;
            CarSensor mockOS;
            IExitUI mockUI;
            ExitController mockExit;
    
      
    @Before
	public void setUp() {
            mockCarpark = mock(Carpark.class);
            mockGate = mock(IGate.class);
            mockIS = mock(CarSensor.class);
            mockOS = mock(CarSensor.class);
            mockUI = mock(IExitUI.class);
            mockExit = new ExitController(mockCarpark, mockGate, mockIS, mockOS, mockUI);
	}
    
    @Test    
    public void initialisationTest() {
        assertTrue(mockExit instanceof ExitController);
        assertTrue(mockIS instanceof CarSensor);
        assertEquals(mockExit.getState(), "IDLE");
    }
    
    @Test
    public void idleToWaitingTest() {
        when(mockIS.carIsDetected()).thenReturn(true);
        when(mockIS.getId()).thenReturn("Exit Inside Sensor");
        assertEquals(mockExit.getState(), "IDLE");
        mockExit.carEventDetected("Exit Inside Sensor", true);
        assertEquals(mockExit.getState(), "WAITING");
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
