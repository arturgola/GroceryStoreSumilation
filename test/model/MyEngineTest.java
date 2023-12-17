package model;

import controller.Controller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import simu.framework.Event;
import simu.model.Customer;
import simu.model.EventType;
import simu.model.MyEngine;

import static org.junit.jupiter.api.Assertions.*;

class MyEngineTest {

    private MyEngine myEngine;

    @BeforeEach
    void setUp() {
        Controller controller = new Controller();
        myEngine = new MyEngine(controller);
    }

    @Test
    void testInitialize() {
        assertNotNull(myEngine.getArrivalProcess());
        assertNotNull(myEngine.getServicePoint());
        assertNotNull(myEngine.getController());
        assertNotNull(myEngine.getEventList());
    }

    @Test
    void testRunEvent_ARR() {
        Event arrivalEvent = new Event(EventType.ARR, 10.0);
        myEngine.runEvent(arrivalEvent);
        assertNotNull(myEngine.servicePoint[0].getQueue().peek());
    }

    @Test
    void testRunEvent_DEP1() {
        Customer customer = new Customer();
        myEngine.servicePoint[0].addToQueue(customer);
        Event dep1Event = new Event(EventType.DEP1, 10.0);
        myEngine.runEvent(dep1Event);
        assertNull(myEngine.servicePoint[0].getQueue().peek());
        assertEquals(customer, myEngine.servicePoint[1].getQueue().peek());
    }

    @Test
    void testTryCEvents() {
        Customer customer = new Customer();
        myEngine.servicePoint[0].addToQueue(customer);
        myEngine.tryCEvents();
        assertEquals(1, myEngine.servicePoint[0].getQueue().size());
        myEngine.servicePoint[0].beginService();
        myEngine.tryCEvents();
        assertEquals(0, myEngine.servicePoint[0].getQueue().size());
    }
}