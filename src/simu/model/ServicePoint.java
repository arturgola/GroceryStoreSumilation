package simu.model;

import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Normal;
import simu.framework.Clock;
import simu.framework.Event;
import simu.framework.EventList;
import simu.model.Customer;

import java.util.LinkedList;

public class ServicePoint implements Comparable<ServicePoint> {
    private static final String GREEN = "\033[0;32m";
    private static final String WHITE = "\033[0;37m";
    private static final String BLUE = "\033[0;34m";
    private LinkedList<Customer> queue;
    private ContinuousGenerator generator;
    private EventList eventList;
    private EventType eventTypeScheduled;
    private String name;
    private double serviceTimeSum;
    private int customerServiced;
    private boolean reserved = false;

    public ServicePoint(String name, ContinuousGenerator g, EventList tl, EventType type) {
        this.name = name;
        this.generator = g;
        this.eventList = tl;
        this.eventTypeScheduled = type;

        this.serviceTimeSum = 0;
        this.customerServiced = 0;

        queue = new LinkedList<>();
    }

    public void addToQueue(Customer a) {
        queue.addFirst(a);
        System.out.printf("%sAdded customer #%d to queue of %s, in queue: %d%s. ", BLUE, a.getId(), name, queue.size(), WHITE);
    }

    public Customer removeFromQueue() {
        if (queue.size() > 0) {
            Customer a = queue.removeLast();
            customerServiced++;
            reserved = false;
            return a;
        } else
            return null;
    }

    public void beginService() {
        System.out.printf("%sStarting service %s for the customer #%d%s. ", GREEN, name, queue.peek().getId(), WHITE);

        reserved = true;
        double serviceTime = generator.sample();
        serviceTimeSum+=serviceTime;
        eventList.add(new Event(eventTypeScheduled, Clock.getInstance().getClock() + serviceTime));
    }

    public boolean isReserved() {
        return reserved;
    }

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
