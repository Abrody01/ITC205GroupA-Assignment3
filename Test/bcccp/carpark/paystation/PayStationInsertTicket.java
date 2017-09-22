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
    IAdhocTicket paidTicket;
    IAdhocTicketDAO adhoc;
    ISeasonTicketDAO season;
    ICarpark cp;
    PaystationController PayController;
    IPaystationUI ui;
    
    public PayStationInsertTicket() {
        
    }
    
  
    
    @Before
    public void setUp() {
        cp = mock(Carpark.class);
        ui = mock(PaystationUI.class);      
        testTicket = mock(IAdhocTicket.class);
        paidTicket = mock(IAdhocTicket.class);
        
        when(testTicket.getBarcode()).thenReturn("123ABC");
        when(paidTicket.isPaid()).thenReturn(Boolean.TRUE);
        when(cp.getAdhocTicket(testTicket.getBarcode())).thenReturn(testTicket);
        when(cp.getAdhocTicket(paidTicket.getBarcode())).thenReturn(paidTicket);
        when(paidTicket.isCurrent()).thenReturn(Boolean.FALSE);
        when(paidTicket.getBarcode()).thenReturn("CBA321");
        when(testTicket.isPaid()).thenReturn(Boolean.FALSE);
        
        PayController = new PaystationController(cp, mock(PaystationUI.class));
        
    }
    
    
    @After
    public void tearDown() {
        PayController = null;
        
    }
    
    @Test
    public void testTicketInserted() {
        System.out.println("ticketInserted");
        
        PayController.ticketInserted(testTicket.getBarcode());
        
        assertEquals(PayController.getState(),"WAITING");
        
    }
    @Test
    public void testNullTicketInserted() {
        //can also be used for unreadable ticket.
        System.out.println("ticketInserted");
        String testNull = "";
        PayController.ticketInserted(testNull);
        
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
    
    @Test
    public void testPAIDTicketInserted() {
        
        System.out.println("ticketInserted");
        String testNull = "";
        PayController.ticketInserted(testNull);
        
        assertEquals(PayController.getState(),"REJECTED");
        
    }

    
}
