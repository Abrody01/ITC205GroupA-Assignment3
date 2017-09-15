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
public class PayStationTicketPaid {
     
    String barcode;
    IAdhocTicket testTicket;
    IAdhocTicketDAO adhoc;
    ISeasonTicketDAO season;
    ICarpark cp;
    IPaystationController PayController;
    IPaystationUI ui;
    public PayStationTicketPaid() {
       
        
    }
    
   
    
    @Before
    public void setUp() {
        cp = mock(Carpark.class);
        ui = mock(PaystationUI.class);      
        testTicket = mock(IAdhocTicket.class);
        
        barcode = testTicket.getBarcode();
        when(testTicket.getBarcode()).thenReturn("123ABC");
        when(cp.getAdhocTicket(testTicket.getBarcode())).thenReturn(testTicket);
        when(testTicket.isCurrent()).thenReturn(Boolean.TRUE);
       
        PayController = new PaystationController(cp, ui);
        PayController.ticketInserted(testTicket.getBarcode());
        
    }
    
    @After
    public void tearDown() {
        
        PayController = null;
        
        
    }
    
    @Test
    public void testTicketPaid() {
        System.out.println("ticketPaid");
        
        PayController.ticketPaid();
        
        assertEquals(PayController.getState(),"PAID");
    }
    
   @Test
    public void testTicketPaidNotWaiting(){
        //Make the state of the carpark not "WAITING"
        PayController.ticketTaken();
        
        PayController.ticketPaid();
        
        assertEquals(PayController.getState(),"IDLE");
        
    }
    
}
