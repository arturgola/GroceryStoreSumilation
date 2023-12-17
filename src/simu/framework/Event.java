package simu.framework;

/**
 * Class for events in the simulation.
 */
public class Event implements Comparable<Event> {
    private IEventType type;
    private double time;

    /**
     * Event instance constructor.
     * @param type type of the Event instance.
     * @param time time of the Event instance.
     */
    public Event(IEventType type, double time) {
        this.type = type;
        this.time = time;
    }

    public double getTime() {
        return time;
    }

    public IEventType getType() {
        return type;
    }

    @Override
    public String toString() {
        return time + " [" + type + "]";
    }

    /**
     * Compare 2 events based on their time on the event list.
     * @param e the object to be compared.
     * @return numerical value based on the result of the comparison.
     */
    @Override
    public int compareTo(Event e) {
        if (time < e.time)
            return -1;
        else if (time > e.time)
            return 1;
        return 0;
    }
}
