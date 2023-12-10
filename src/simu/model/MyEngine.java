package simu.model;

import eduni.distributions.*;
import simu.Controller;
import simu.framework.*;


/* Simulate a queueing system with one service point and one queue.
 * The service time is normally distributed with mean 10.
 * The interarrival time is exponentially distributed with mean 15.
 * The simulation runs until the number of arrivals exceeds 1000.
 * The program prints the average waiting time.
 */
public class MyEngine extends Engine {
    private ArrivalProcess arrivalProcess;
    private ServicePoint[] servicePoint;
    private Controller controller;


   public MyEngine(Controller controller) {
        super();
        this.controller = controller;

        servicePoint = new ServicePoint[4];     // just an array of one element
        servicePoint[0] = new ServicePoint("S1", new Normal(10, 6), eventList, EventType.DEP1);
        servicePoint[1] = new ServicePoint("S2", new Normal(10, 10), eventList, EventType.DEP2);
       servicePoint[2] = new ServicePoint("S3", new Normal(10, 6), eventList, EventType.DEP3);
       servicePoint[3] = new ServicePoint("S4", new Normal(10, 10), eventList, EventType.DEP4);
        arrivalProcess = new ArrivalProcess(new Negexp(15), eventList, EventType.ARR);
    }

    protected void initialize() {
        arrivalProcess.generateNextEvent();

    }

    protected void runEvent(Event e) {
       Customer a;

       controller.updateTime(e.getTime());


       switch ((EventType) e.getType()) {
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

                double totalServiceAvgTime = servicePoint[0].getMeanServiceTime() + servicePoint[1].getMeanServiceTime()+servicePoint[2].getMeanServiceTime()+servicePoint[3].getMeanServiceTime();


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
        }
    }

    protected void tryCEvents() {
        for (ServicePoint sp : servicePoint) {
            if (!sp.isReserved() && sp.isOnQueue()) {
                sp.beginService();
            }
        }
    }



    public void setController(Controller controller){
       this.controller = controller;
    }

    protected void results() {
//        System.out.printf("\nSimulation ended at %.2f\n", Clock.getInstance().getClock());
        controller.finishSimulation(servicePoint[3].getCustomerServiced());
//        System.out.println("Total customers serviced: " + servicePoint[3].getCustomerServiced());
//        System.out.printf("Average service time for S1: %.2f\n", servicePoint[0].getMeanServiceTime());
//        System.out.printf("Average service time for S2: %.2f\n", servicePoint[1].getMeanServiceTime());
//        System.out.printf("Average service time for S3: %.2f\n", servicePoint[2].getMeanServiceTime());
//        System.out.printf("Average service time for S4: %.2f\n", servicePoint[3].getMeanServiceTime());

//        double totalServiceTime = servicePoint[0].getMeanServiceTime() + servicePoint[1].getMeanServiceTime()+servicePoint[2].getMeanServiceTime()+servicePoint[3].getMeanServiceTime();

//        System.out.printf("Average service time for all service points: %.2f\n", totalServiceTime);

    }
}
