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
public class PaystationControllerTest {
    
    //@Mock PaystationController PayController;
    String barcode;
    String notBarcode;
    IAdhocTicket testTicket;
    IAdhocTicketDAO adhoc;
    ISeasonTicketDAO season;
    ICarpark cp;
    PaystationController PayController;
    IAdhocTicket notCurrent;
    IPaystationUI ui;
            
    public PaystationControllerTest() {
        
       
        
    }
    
   
    
    @Before
    public void setUp() throws Exception {
        cp = mock(Carpark.class);
        ui = mock(PaystationUI.class);      
        testTicket = mock(IAdhocTicket.class);
        notCurrent = mock(IAdhocTicket.class);
        notBarcode = notCurrent.getBarcode();
        barcode = testTicket.getBarcode();
        when(testTicket.getBarcode()).thenReturn("123ABC");
        when(notCurrent.isCurrent()).thenReturn(Boolean.TRUE);
        when(cp.getAdhocTicket(testTicket.getBarcode())).thenReturn(testTicket);
        when(notCurrent.isCurrent()).thenReturn(Boolean.FALSE);
        when(notCurrent.getBarcode()).thenReturn("CBA321");
        
        PayController = new PaystationController(cp, ui);
        
    }
    
    @After
    public void tearDown() {
        testTicket = null;
        PayController = null;
        barcode = null;
        
    }

  
    @Test
    public void testTicketNormalFlow(){
        System.out.println("NormalFlow");
        System.out.println("TicketInserted");
        PayController.ticketInserted(testTicket.getBarcode());
        assertEquals("WAITING",PayController.getState());
        System.out.println("ticketPaid");
        PayController.ticketPaid();
        assertEquals("PAID",PayController.getState());
        System.out.println("ticketTaken");
        PayController.ticketTaken();
        assertEquals("IDLE",PayController.getState());
        System.out.println("norm-Flow-Complete");
        
    }
    @Test
    public void testTicketAlternateFlow1(){
        
        System.out.println("Alt-Flow1");
        PayController.ticketInserted("");
        assertEquals("REJECTED",PayController.getState());
        System.out.println("ticketTaken");
        PayController.ticketTaken();
        assertEquals("IDLE",PayController.getState());
        
        System.out.println("Alt-Flow1-Complete");
    }
    @Test
    public void testTicketAlternateFlow2(){
        
        System.out.println("Alt-Flow2");
        PayController.ticketInserted(notCurrent.getBarcode());
        System.out.println(notCurrent.isPaid());
        assertEquals("REJECTED",PayController.getState());
                
        PayController.ticketTaken();
        assertEquals("IDLE",PayController.getState());
        
        System.out.println("Alt-Flow2-Complete");
    }
    
    
    
    
    
}
