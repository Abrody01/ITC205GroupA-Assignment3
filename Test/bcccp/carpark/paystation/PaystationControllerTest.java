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
    IAdhocTicket testTicket;
    IAdhocTicketDAO adhoc;
    ISeasonTicketDAO season;
    ICarpark cp;
    PaystationController PayController;
            
    public PaystationControllerTest() {
        
       
        
    }
    
   
    
    @Before
    public void setUp() throws Exception {
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

    /**
     * Test of ticketInserted method, of class PaystationController.
     */
    @Test
    public void testTicketInserted() {
        System.out.println("ticketInserted");
        
        PayController.ticketInserted(barcode);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
        
    }

    /**
     * Test of ticketPaid method, of class PaystationController.
     */
    @Test
    public void testTicketPaid() {
        System.out.println("ticketPaid");
        PayController.ticketInserted(barcode);
        PayController.ticketPaid();
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");

        assertEquals(testTicket.isPaid(),true);
        
    }

    /**
     * Test of ticketTaken method, of class PaystationController.
     */
    @Test
    public void testTicketTaken() {
        System.out.println("ticketTaken");
        PayController.ticketInserted(barcode);
        PayController.ticketPaid();
        PayController.ticketTaken();
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    
    
}
