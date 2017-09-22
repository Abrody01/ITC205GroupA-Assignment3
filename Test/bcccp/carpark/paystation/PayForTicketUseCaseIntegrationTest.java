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

import bcccp.tickets.season.ISeasonTicketDAO;

import bcccp.tickets.season.SeasonTicketDAO;
import bcccp.tickets.season.UsageRecordFactory;

import org.junit.After;

import org.junit.Before;

import org.junit.Test;
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;


/**
 *
 * @author Aaron
 */
public class PayForTicketUseCaseIntegrationTest {
    
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
            
    public PayForTicketUseCaseIntegrationTest() {
        
       
        
    }
    
   
    
    @Before
    public void setUp() throws Exception {
        //Builds all assets required for testing the various flows of the Pay for ticket use case 
        season = new SeasonTicketDAO(new UsageRecordFactory());
        adhoc = new AdhocTicketDAO(new AdhocTicketFactory());
        
        cp = new Carpark("TestCarPark", 5, adhoc, season);
        
        ui = mock(PaystationUI.class);      
        testTicket = cp.issueAdhocTicket();
        testTicket.enter(System.currentTimeMillis());
        
        notCurrent = cp.issueAdhocTicket();
        notCurrent.enter(System.currentTimeMillis());
        notCurrent.pay(System.currentTimeMillis(), 1.0f);
        
               
        PayController = new PaystationController(cp, ui);
        
    }
    
    @After
    public void tearDown() {
        testTicket = null;
        PayController = null;
        notCurrent = null;
        
    }

  
    @Test
    public void testTicketNormalFlow(){
        /*this tests the normal flow of the Pay for ticket use case:
        A ticket is inserted in to the machine, the use is asked to pay for the ticket
        The ticket is converted to "paid" and the user takes the ticket.
        */
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
        /* This tests the first alternate flow for the Pay for ticket use case
        The user enters a ticket, the ticket is not registered with the carpark,
        the user can then remove the rejected ticket and go to the office to have it
        inspected
        */
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
        /* This tests the Second alternate flow for the Pay for ticket use case
        The user enters a ticket, the ticket is already paid,
        the user can then remove the rejected ticket 
        */
        System.out.println("Alt-Flow2");
        PayController.ticketInserted(notCurrent.getBarcode());
        
        assertEquals("REJECTED",PayController.getState());
                
        PayController.ticketTaken();
        assertEquals("IDLE",PayController.getState());
        
        System.out.println("Alt-Flow2-Complete");
    }
    
    @Test
    public void testTicketAlternateFlow3(){
        /* This tests the Third alternate flow for the Pay for ticket use case
        The user enters a ticket,
        the user can then remove the ticket.
        */
        System.out.println("Alt-Flow3");
        PayController.ticketInserted(testTicket.getBarcode());
        
        assertEquals("WAITING",PayController.getState());
                
        PayController.ticketTaken();
        assertEquals("IDLE",PayController.getState());
        
        System.out.println("Alt-Flow3-Complete");
    }
    
    
    
    
}
