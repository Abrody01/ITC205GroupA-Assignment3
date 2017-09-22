/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bcccp.carpark.paystation;

import bcccp.carpark.Carpark;
import bcccp.carpark.ICarpark;
import bcccp.tickets.adhoc.IAdhocTicket;
import bcccp.tickets.adhoc.IAdhocTicketDAO;
import bcccp.tickets.season.ISeasonTicketDAO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author Aaron
 */
public class TestTicketTaken {
    
    IAdhocTicket testTicket;
    IAdhocTicketDAO adhoc;
    ISeasonTicketDAO season;
    ICarpark cp;
    IPaystationController PayController;
    IPaystationUI ui;
    public TestTicketTaken() {
         
    }
    
   
    @Before
    public void setUp() {
        System.out.println("------Start Set-up------");
         cp = mock(Carpark.class);
         ui = mock(PaystationUI.class);
         
         testTicket = mock(IAdhocTicket.class);
         when(testTicket.getBarcode()).thenReturn("123ABC");
         when(cp.getAdhocTicket(testTicket.getBarcode())).thenReturn(testTicket);
         
         PayController = new PaystationController(cp,ui);
     
         PayController.ticketInserted(testTicket.getBarcode());
         PayController.ticketPaid();
         
         System.out.println("------end Set-up------");
    }
    
    @After
    public void tearDown() {
        PayController = null;
    }
    
     @Test    
    public void initialisationTest() {
        
        assertEquals("PAID",PayController.getState());
    }
    @Test
    public void testTicketTaken(){
        System.out.println("ticket Taken");
        PayController.ticketTaken();
        
        assertEquals("IDLE",PayController.getState());
        System.out.println("End Test ticket Taken");
    }

   @Test
    public void testTicketTakenWrongState(){
        PayController.ticketTaken();
        System.out.println("ticket Taken Wrong State");
        PayController.ticketTaken();
        
        assertEquals("IDLE",PayController.getState());
        System.out.println("end Test ticket Taken Wrong State");
    }
    
     @Test
    public void testRejectedTicketTaken(){
        System.out.println("ticket Taken Rejected State");
        PayController.ticketTaken();
        PayController.ticketInserted(null);
        assertEquals("REJECTED",PayController.getState());
        System.out.println("ticket Taken");
        PayController.ticketTaken();
        
        assertEquals("IDLE",PayController.getState());
         System.out.println("End Test ticket Taken Rejected State");
        
        
    }
}
