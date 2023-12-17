package simu.framework;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * Class for the event list used during the entire time of simulation.
 */
public class EventList {
    private PriorityQueue<Event> evenList;

    /**
     * Event list constructor. Create new priority queue as new event list.
     */
    public EventList() {
        evenList = new PriorityQueue<>();
    }

    /**
     * Add new event object to event list.
     * @param e event object.
     */
    public void add(Event e) {
        System.out.printf("Adding to the event list %s %.2f", e.getType(), e.getTime());
        evenList.add(e);
    }

    /**
     * Remove next event on the event list.
     * @return Return the event object removed from the event list.
     */
    public Event remove() {
        if (evenList.isEmpty())
            return null;

        System.out.printf("Removing from the event list %s %.2f. ", evenList.peek().getType(), evenList.peek().getTime());
        return evenList.remove();
    }

    /**
     * Retrieve the time of the next event object on the event list.
     * @return time value of the event object.
     */
    public double getNextEventTime() {
        if (evenList.isEmpty())
            return 0;
        return evenList.peek().getTime();
    }

    public void print() {
        Object[] tmp = evenList.toArray();
        Arrays.sort(tmp);
        for (Object e : tmp)
            System.out.println(e);
    }
}
