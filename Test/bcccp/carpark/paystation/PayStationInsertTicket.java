/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bcccp.carpark.paystation;

import bcccp.carpark.Carpark;
import bcccp.carpark.ICarpark;
import bcccp.tickets.adhoc.IAdhocTicketDAO;
import bcccp.tickets.adhoc.AdhocTicketDAO;
import bcccp.tickets.adhoc.AdhocTicketFactory;
import bcccp.tickets.adhoc.IAdhocTicket;
import bcccp.tickets.adhoc.AdhocTicket;
import bcccp.tickets.season.ISeasonTicketDAO;
import bcccp.tickets.season.IUsageRecordFactory;
import bcccp.tickets.season.SeasonTicketDAO;
import bcccp.tickets.season.UsageRecordFactory;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.Mockito.*;
import org.mockito.Mock;
import static org.junit.Assert.*;
import org.mockito.junit.MockitoJUnitRunner;

/**
 *
 * @author Aaron
 */
public class PayStationInsertTicket {
    
    String barcode;
    IAdhocTicket testTicket;
    IAdhocTicketDAO adhoc;
    ISeasonTicketDAO season;
    ICarpark cp;
    PaystationController PayController;
    
    public PayStationInsertTicket() {
        
    }
    
  
    
    @Before
    public void setUp() {
        season = new SeasonTicketDAO(new UsageRecordFactory());
        adhoc = new AdhocTicketDAO(new AdhocTicketFactory());
          
        cp = new Carpark("TestCarPark", 5, adhoc, season); 
        
        testTicket = cp.issueAdhocTicket();
       
        barcode = testTicket.getBarcode();
        
        PayController = new PaystationController(cp, mock(PaystationUI.class));
        
    }
    
    
    @After
    public void tearDown() {
        PayController = null;
        
    }
    
    @Test
    public void testTicketInserted() {
        System.out.println("ticketInserted");
        
        PayController.ticketInserted(barcode);
        
        assertEquals(PayController.getState(),"WAITING");
        
    }
    @Test
    public void testNullTicketInserted() {
        System.out.println("ticketInserted");
        String testNull = "";
        PayController.ticketInserted(testNull);
        
        assertEquals(PayController.getState(),"REJECTED");
        
    }
    @Test
    public void testInvalidTicketInserted() {
        System.out.println("ticketInserted");
        String testInvalid = "12a5";
        PayController.ticketInserted(testInvalid);
        
        assertEquals(PayController.getState(),"REJECTED");
        
    }
    
    @Test(expected=AssertionError.class) 
    public void testTicketInsertedTwice() {
        System.out.println("ticketInserted");
        PayController.ticketInserted(barcode);
        
        PayController.ticketInserted(barcode);
        fail("Should have thrown exception");
        //assertEquals(PayController.getState(),"WAITING");
        
        
    }
    @Test(expected=AssertionError.class) 
    public void testTicketInsertedNotWaiting(){
       PayController.ticketInserted(barcode);
       PayController.ticketPaid();
       
       PayController.ticketInserted(barcode);
        fail("Should have thrown exception");
    }

    
}
