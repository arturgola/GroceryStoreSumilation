package framework;

import org.junit.jupiter.api.Test;
import simu.framework.ArrivalProcess;
import simu.framework.EventList;
import simu.model.EventType;

import static org.junit.jupiter.api.Assertions.*;

class ArrivalProcessTest {

    @Test
    void testGenerateNextEvent() {
        EventList eventList = new EventList();
        ArrivalProcess arrivalProcess = new ArrivalProcess(new Negexp(), eventList, EventType.ARR);

        assertNull(eventList.getNextEventTime());
        arrivalProcess.generateNextEvent();
        assertNotNull(eventList.getNextEventTime());
    }

}