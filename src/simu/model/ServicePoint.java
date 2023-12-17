package simu.model;

import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Normal;
import simu.framework.Clock;
import simu.framework.Event;
import simu.framework.EventList;
import simu.model.Customer;

import java.util.LinkedList;

/**
 * Class for service point objects.
 */
public class ServicePoint implements Comparable<ServicePoint> {
    private static final String GREEN = "\033[0;32m";
    private static final String WHITE = "\033[0;37m";
    private static final String BLUE = "\033[0;34m";

    /**
     * Queue of the Service point instance.
     */
    private LinkedList<Customer> queue;
    private ContinuousGenerator generator;
    private EventList eventList;
    private EventType eventTypeScheduled;
    private String name;
    private double serviceTimeSum;
    private int customerServiced;
    private boolean reserved = false;

    /**
     * Service point instance constructor.
     * @param name name of the Service point object.
     * @param g random number generator.
     * @param tl event list that the service point access.
     * @param type type of the event that the service point will add to the event list.
     */
    public ServicePoint(String name, ContinuousGenerator g, EventList tl, EventType type) {
        this.name = name;
        this.generator = g;
        this.eventList = tl;
        this.eventTypeScheduled = type;

        this.serviceTimeSum = 0;
        this.customerServiced = 0;

        queue = new LinkedList<>();
    }

    /**
     * Add a customer object to its queue
     * @param a Customer object
     */
    public void addToQueue(Customer a) {
        queue.addFirst(a);
        System.out.printf("%sAdded customer #%d to queue of %s, in queue: %d%s. ", BLUE, a.getId(), name, queue.size(), WHITE);
    }

    /**
     * Remove next customer from its queue.
     * @return Customer object
     */
    public Customer removeFromQueue() {
        if (queue.size() > 0) {
            Customer a = queue.removeLast();
            customerServiced++;
            reserved = false;
            return a;
        } else
            return null;
    }

    /**
     * Begin serving the next customer from the queue. Add new service completion (departure) event into the event list.
     */
    public void beginService() {
        System.out.printf("%sStarting service %s for the customer #%d%s. ", GREEN, name, queue.peek().getId(), WHITE);

        reserved = true;
        double serviceTime = generator.sample();
        serviceTimeSum+=serviceTime;
        eventList.add(new Event(eventTypeScheduled, Clock.getInstance().getClock() + serviceTime));
    }

    /**
     * @return boolean value if the service point is serving a customer at the moment.
     */
    public boolean isReserved() {
        return reserved;
    }

    /**
     * @return boolean value if there is customer(s) on the service point's queue.
     */
    public boolean isOnQueue() {
        return queue.size() > 0;
    }

    public int getCustomerServiced() {
        return customerServiced;
    }

    public double getMeanServiceTime() {
        return serviceTimeSum / customerServiced;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(ServicePoint otherServicePoint) {
        return Integer.compare(this.queue.size(), otherServicePoint.queue.size());
    }

    //    public void serve() {
//        Customer a;
//        Normal normalGenerator = new Normal(5, 1);
//
//        a = removeFromQueue();
//        while (a != null) {
//            Clock.getInstance().setClock(Clock.getInstance().getClock() + normalGenerator.sample());
//
//            a.setRemovalTime(Clock.getInstance().getClock());
//            a.reportResults();
//            System.out.println(a.getRemovalTime());
//            System.out.println(a.getArrivalTime());
////            serviceTimeSum += a.getRemovalTime() - a.getArrivalTime();
//
//            a = removeFromQueue();
//        }
//    }
}
