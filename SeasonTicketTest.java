package bcccp.tickets.season;

import bcccp.tickets.season.ISeasonTicket;
import bcccp.tickets.season.IUsageRecord;
import bcccp.tickets.season.SeasonTicket;
import bcccp.tickets.season.UsageRecord;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.mockito.Mockito.mock;

/**
 *
 * @author James Nocevski
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SeasonTicketTest {
    
    ISeasonTicket testTicket;
    IUsageRecord mockRecord;
    String mockCarparkID, mockTicketID;
    long mockStartPeriod, mockEndPeriod, mockTime;
    
    @Before
    public void setUp() {
        mockCarparkID = "test carpark ID";
        mockTicketID = "test ticket ID";
        mockStartPeriod = 123456;
        mockEndPeriod = 654321;
        mockTime = 456321;
        testTicket = new SeasonTicket(mockTicketID, mockCarparkID, mockStartPeriod, mockEndPeriod);
        mockRecord = mock(UsageRecord.class);
    }
    
    @Test
    public void dataRetrievalTest() {
        assertEquals(testTicket.getId(), "test ticket ID");
        assertEquals(testTicket.getCarparkId(), "test carpark ID");
        assertEquals(testTicket.getStartValidPeriod(), 123456);
        assertEquals(testTicket.getEndValidPeriod(), 654321);
    }
    
    @Test
    public void inUseTest() {
        testTicket.recordUsage(mockRecord);
        assertTrue(mockRecord instanceof UsageRecord);
        assertEquals(testTicket.inUse(), true);
        assertTrue(testTicket.getCurrentUsageRecord() instanceof UsageRecord);
        testTicket.endUsage(mockTime);
        assertEquals(testTicket.getCurrentUsageRecord(), null);
    }
    
    @Test(expected=AssertionError.class)
    public void testNullConstructorParam1() {
        testTicket = new SeasonTicket(null, mockCarparkID, mockStartPeriod, mockEndPeriod);
        fail("Exception should be thrown.");
    }
    
    @Test(expected=AssertionError.class)
    public void testNullConstructorParam2() {
        testTicket = new SeasonTicket(mockTicketID, null, mockStartPeriod, mockEndPeriod);
        fail("Exception should be thrown.");
    }
        
}
