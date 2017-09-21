package bcccp.carpark.exit;

import bcccp.carpark.CarSensor;
import bcccp.carpark.Carpark;
import bcccp.carpark.IGate;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author jmz_0
 */
public class NullConstructorExitTest {
    
    Carpark mockCarpark;
    IGate mockGate;
    CarSensor mockIS;
    CarSensor mockOS;
    IExitUI mockUI;
    
    @Test(expected=NullPointerException.class)
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
