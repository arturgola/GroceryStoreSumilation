package simu.framework;

import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Negexp;
import simu.model.EventType;


/**
 * Generate new arrival events into the event list.
 */
public class ArrivalProcess {
    private ContinuousGenerator generator;
    private EventList eventList;
    private IEventType type;

    /**
     * Create new instance of the class
     * @param g random number generator
     * @param tl event list
     * @param type type of event generated (arrival)
     */
    public ArrivalProcess(ContinuousGenerator g, EventList tl, IEventType type) {
        this.generator = g;
        this.eventList = tl;
        this.type = type;
    }

    /**
     * Create new event into the event list. New event has a type and time for the event to happen.
     * @return time of event created.
     */
    public double generateNextEvent() {
        double eventTime = Clock.getInstance().getClock() + generator.sample();
        Event t = new Event(type, eventTime);
        eventList.add(t);

        return eventTime;
    }

    public static void main(String[] args) {
        EventList eventList = new EventList();
        ArrivalProcess arrivalProcess = new ArrivalProcess(new Negexp(10), eventList, EventType.ARR);

        for (int i = 0; i < 10; i++) {
            Clock.getInstance().setClock(arrivalProcess.generateNextEvent());
        }
        eventList.print();
    }
}
