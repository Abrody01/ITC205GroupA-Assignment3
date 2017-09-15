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
public class PayStationControllerConstructor {
    
    
    
    
    ICarpark cp;
   
    IPaystationUI ui;
    
    public PayStationControllerConstructor() {
    }
    
    
    
    @Before
    public void setUp() {
        
        
        ui = mock(PaystationUI.class);
        cp = mock(Carpark.class);
        
        
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testControllerConstructor() {
        System.out.println("Generating Paystation Controller");

        IPaystationController PayController;
        PayController = new PaystationController(cp, ui);
        
        assertEquals(PayController.getState(),"IDLE"); 
        
      
    }
    
    @Test(expected=AssertionError.class)
    public void testControllerConstructorNullCarPark() {
        System.out.println("Generating Paystation Controller Null Car Park");

        IPaystationController PayController;
        PayController = new PaystationController(null, ui);
        
        
       fail("Should have thrown exception");
    }
    
    @Test(expected=NullPointerException.class)
    public void testControllerConstructorNullUi() {
        System.out.println("Generating Paystation Controller Null UI");

        IPaystationController PayController;
        PayController = new PaystationController(cp, null);
        
        
        fail("Should have thrown exception");
    }
    
    @Test(expected=NullPointerException.class)
    public void testControllerConstructorNullall() {
        System.out.println("Generating Paystation Controller Null both");

        IPaystationController PayController;
        PayController = new PaystationController(null, null);
        
        
        fail("Should have thrown exception");
    }
    
}
