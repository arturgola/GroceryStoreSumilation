package simu.model;

import simu.framework.Clock;

/**
 * Class for customer objects.
 */
public class Customer {
    private static final String YELLOW = "\033[0;33m";
    private static final String WHITE = "\033[0;37m";
    private double arrivalTime;
    private double removalTime;
    private static double serviceTimeSum;
    private int id;
    private static int i = 1;

    /**
     * Customer class instance constructor. Customer object's id and arrival time are created.
     */
    public Customer() {
        id = i++;

        arrivalTime = Clock.getInstance().getClock();
        System.out.printf("New customer #%d arrived at %.2f. ", id, arrivalTime);
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    public double getRemovalTime() {
        return removalTime;
    }

    /**
     * Set the time the customer leaves the service point.
     * @param removalTime value of the time of customer removal.
     */
    public void setRemovalTime(double removalTime) {
        this.removalTime = removalTime;
    }

    public int getId() {
        return id;
    }

    public static void resetI() {
        Customer.i = 1;
    }

    public static void resetServiceTimeSum() {
        Customer.serviceTimeSum = 0;
    }

    /**
     * Print the results after the customer has been serviced.
     */
    public void reportResults() {
        serviceTimeSum += (removalTime - arrivalTime);
        double meanServiceTime = serviceTimeSum / id;   // id is the number of customers serviced

        System.out.printf("%sCustomer #%d has been serviced. Customer arrived: %.2f, removed: %.2f, stayed: %.2f, mean %.2f%s.",
                YELLOW, id, arrivalTime, removalTime, (removalTime-arrivalTime), meanServiceTime, WHITE);
    }
}
