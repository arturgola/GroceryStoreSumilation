package simu.model;

import eduni.distributions.*;
import controller.Controller;
import simu.framework.*;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Class for the engine model used in this particular supermarket queue simulation. It inherits the more general Engine class.
 */
public class MyEngine extends Engine {
    /**
     * Arrival process attribute for generating customer arrival events.
     */
    private ArrivalProcess arrivalProcess;
    /**
     * Array of service points used in the simulation.
     */
    private ServicePoint[] servicePoint;
    /**
     * Controller connecting Model and View.
     */
    private Controller controller;


    /**
     * Constructor for new MyEngine instance that utilizes a Controller object.
     * @param controller
     */
    public MyEngine(Controller controller) {
        super();
        this.controller = controller;

        servicePoint = new ServicePoint[4];     // just an array of one element
        servicePoint[0] = new ServicePoint("S1", new Normal(10, 3), eventList, EventType.DEP1);
        servicePoint[1] = new ServicePoint("S2", new Normal(14, 7), eventList, EventType.DEP2);
        servicePoint[2] = new ServicePoint("S3", new Normal(12, 6), eventList, EventType.DEP3);
        servicePoint[3] = new ServicePoint("S4", new Normal(9, 4), eventList, EventType.DEP4);
        arrivalProcess = new ArrivalProcess(new Negexp(2), eventList, EventType.ARR);
    }

    /**
     * Method to initialize the simulation by creating new event (arrival).
     */
    protected void initialize() {
        arrivalProcess.generateNextEvent();

    }

    /**
     * Logic for B-event processing.
     * @param e Event object which will be processed by its attribute (time and type).
     */
    protected void runEvent(Event e) {
        Customer a;

        controller.updateTime(e.getTime());

        switch ((EventType) e.getType()) {
            case ARR:
                minQueueServicePoint().addToQueue(new Customer());

                arrivalProcess.generateNextEvent();
                break;

            case DEP1:
                a = servicePoint[0].removeFromQueue();
                a.setRemovalTime(Clock.getInstance().getClock());
                a.reportResults();
                updateView();
                break;

            case DEP2:
                a = servicePoint[1].removeFromQueue();
                a.setRemovalTime(Clock.getInstance().getClock());
                a.reportResults();
                updateView();
                break;

            case DEP3:
                a = servicePoint[2].removeFromQueue();
                a.setRemovalTime(Clock.getInstance().getClock());
                a.reportResults();
                updateView();
                break;

            case DEP4:
                a = servicePoint[3].removeFromQueue();
                a.setRemovalTime(Clock.getInstance().getClock());
                a.reportResults();
                updateView();
                break;
        }
    }

    /**
     * Method for C-event processing attempt (conditional events).
     */
    protected void tryCEvents() {
        for (ServicePoint sp : servicePoint) {
            if (!sp.isReserved() && sp.isOnQueue()) {
                sp.beginService();
            }
        }
    }

    /**
     * @return Service point object that has the shortest queue length.
     */
    private ServicePoint minQueueServicePoint() {
        List<ServicePoint> servicePointList = Arrays.asList(servicePoint);
        return Collections.min(servicePointList);
    }

    /**
     * @return Total number of customers serviced.
     */
    private int getTotalCustomerServiced() {
        int total = 0;
        for (ServicePoint sp : servicePoint) {
            total += sp.getCustomerServiced();
        }
        return total;
    }

    public ArrivalProcess getArrivalProcess() {
        return arrivalProcess;
    }

    public ServicePoint[] getServicePoint() {
        return servicePoint;
    }

    public Controller getController() {
        return controller;
    }


    /**
     * @return Total service time during the simulation run.
     */
    private double getTotalAvgServiceTime() {
        double total = 0;
        for (ServicePoint sp : servicePoint) {
            total += sp.getMeanServiceTime();
        }
        return total / servicePoint.length;
    }

    /**
     * Update content shown in GUI View.
     */
    private void updateView() {
        double[] sampleData = new double[]{
                servicePoint[0].getMeanServiceTime(),
                servicePoint[1].getMeanServiceTime(),
                servicePoint[2].getMeanServiceTime(),
                servicePoint[3].getMeanServiceTime()
        };
        controller.pushDataToPoint(sampleData);

        controller.updateTotalServiceAvgTime(getTotalAvgServiceTime());

        controller.updateServicedCustomers(getTotalCustomerServiced());
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Print and update the results of the simulation run in terminal and on GUI.
     */
    protected void results() {
        updateView();
        controller.finishSimulation(getTotalCustomerServiced());


        System.out.printf("\nSimulation ended at %.2f\n", Clock.getInstance().getClock());
        System.out.println("Total customers serviced: " + getTotalCustomerServiced());

        for (int i = 0; i < servicePoint.length; i++) {
            System.out.printf("Average service time for %s: %.2f, served: %d\n", servicePoint[i].getName(),
                    servicePoint[i].getMeanServiceTime(),
                    servicePoint[i].getCustomerServiced());
        }

        System.out.printf("Average service time for all service points: %.2f\n", getTotalAvgServiceTime());

        writeResults();
    }

    /**
     * Write the results of the simulation run to a .csv file.
     */
    private void writeResults() {
        String fileName = generateFileName();
        try (FileWriter writer = new FileWriter(fileName)) {
            // header
            writer.append("Simulation Time,Total Customers Serviced,Avg Service Time S1,Avg Service Time S2,Avg " +
                    "Service Time S3,Avg Service Time S4,Avg Service Time All\n");

            // data
            writer.append(String.format("%.2f,%d,%.2f,%.2f,%.2f,%.2f,%.2f\n",
                    Clock.getInstance().getClock(),
                    getTotalCustomerServiced(),
                    servicePoint[0].getMeanServiceTime(),
                    servicePoint[1].getMeanServiceTime(),
                    servicePoint[2].getMeanServiceTime(),
                    servicePoint[3].getMeanServiceTime(),
                    getTotalAvgServiceTime()));

            System.out.printf("Results written to %s%n", fileName);
        } catch (IOException e) {
            System.err.println("Error writing results to CSV file: " + e.getMessage());
        }
    }

    /**
     * @return a file name for new .csv file exported that includes current time stamp.
     */
    private String generateFileName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss");
        String dateTime = dateFormat.format(new Date());
        return "SimLog_" + dateTime + ".csv";
    }
}
