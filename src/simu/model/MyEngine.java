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

public class MyEngine extends Engine {
    public ArrivalProcess arrivalProcess;
    public ServicePoint[] servicePoint;
    public Controller controller;


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

    protected void initialize() {
        arrivalProcess.generateNextEvent();

    }

    public void runEvent(Event e) {
        Customer a;

        controller.updateTime(e.getTime());


       /*switch ((EventType) e.getType()) {
            case ARR:
                servicePoint[0].addToQueue(new Customer());
                arrivalProcess.generateNextEvent();
                break;

           case DEP1:
                a = servicePoint[0].removeFromQueue();
                servicePoint[1].addToQueue(a);
                break;

           case DEP2:
               a = servicePoint[1].removeFromQueue();
               servicePoint[2].addToQueue(a);
               break;

           case DEP3:
               a = servicePoint[2].removeFromQueue();
               servicePoint[3].addToQueue(a);
               break;

            case DEP4:
                a = servicePoint[3].removeFromQueue();
                a.setRemovalTime(Clock.getInstance().getClock());
                controller.updateServicedCustomers(servicePoint[3].getCustomerServiced());

                double totalServiceAvgTime = servicePoint[0].getMeanServiceTime() + servicePoint[1]
                .getMeanServiceTime()+servicePoint[2].getMeanServiceTime()+servicePoint[3].getMeanServiceTime();


                double[] sampleData = {
                        servicePoint[0].getMeanServiceTime(),
                        servicePoint[1].getMeanServiceTime(),
                        servicePoint[2].getMeanServiceTime(),
                        servicePoint[3].getMeanServiceTime()
                };

                controller.pushDataToPoint(sampleData);


                controller.updateTotalServiceAvgTime(totalServiceAvgTime);
                a.reportResults();
                break;
        }*/

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

//                double totalServiceAvgTime = servicePoint[0].getMeanServiceTime() + servicePoint[1]
//                .getMeanServiceTime()+servicePoint[2].getMeanServiceTime()+servicePoint[3].getMeanServiceTime();


        /*double[] sampleData = new double[] {
                servicePoint[0].getMeanServiceTime(),
                servicePoint[1].getMeanServiceTime(),
                servicePoint[2].getMeanServiceTime(),
                servicePoint[3].getMeanServiceTime()
        };
        controller.pushDataToPoint(sampleData);*/


//                controller.updateTotalServiceAvgTime(totalServiceAvgTime);

    }

    public void tryCEvents() {
        for (ServicePoint sp : servicePoint) {
            if (!sp.isReserved() && sp.isOnQueue()) {
                sp.beginService();
            }
        }
    }

    private ServicePoint minQueueServicePoint() {
        List<ServicePoint> servicePointList = Arrays.asList(servicePoint);
        return Collections.min(servicePointList);
    }

    private int getTotalCustomerServiced() {
        int total = 0;
        for (ServicePoint sp : servicePoint) {
            total += sp.getCustomerServiced();
        }
        return total;
    }

    private double getTotalAvgServiceTime() {
        double total = 0;
        for (ServicePoint sp : servicePoint) {
            total += sp.getMeanServiceTime();
        }
        return total / servicePoint.length;
    }

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

    private String generateFileName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss");
        String dateTime = dateFormat.format(new Date());
        return "SimLog_" + dateTime + ".csv";
    }
}
