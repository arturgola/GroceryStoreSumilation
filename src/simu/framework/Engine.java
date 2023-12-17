package simu.framework;


import simu.model.Customer;

/**
 * Engine class with abstract methods for implementing event processing.
 */
public abstract class Engine {
    private static final String RED = "\033[0;31m"; // https://gist.github.com/fnky/458719343aabd01cfb17a3a4f7296797
    private static final String WHITE = "\033[0;37m"; // ANSI escape code for white color
    private double simulationTime = 0;
    protected EventList eventList;

    protected double delay = 1;

    protected boolean isPaused = false;

    /**
     * Engine instance constructor. Create event list that is used during the entire time of simulation.
     */
    public Engine() {
        eventList = new EventList();
        // Service Points are created in the subclass
    }

    /**
     * Set the duration of the simulation.
     * @param simulationTime duration that the simulation runs.
     */
    public void setSimulationTime(double simulationTime) {
        Clock.getInstance().setClock(0);
        Customer.resetI();
        Customer.resetServiceTimeSum();
        this.simulationTime = simulationTime;
    }

    public void setDelay(double delay) {
        this.delay = delay;
    }

    public double getDelay() {
        return delay;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void togglePause() {
        isPaused = !isPaused;
    }

    /**
     * Main loop of the simulation.
     */
    public void run() {
        initialize();

        while (simulate() && !isPaused) {
            System.out.printf("\n%sA-phase:%s time is %.2f\n", RED, WHITE, currentTime());
            Clock.getInstance().setClock(currentTime());

            System.out.printf("%sB-phase:%s ", RED, WHITE);
            runBEvents();

            System.out.printf("%s\nC-phase:%s ", RED, WHITE);
            tryCEvents();
            System.out.println("");

            try {
                Thread.sleep((int) (10/delay));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            while (isPaused) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        results();
    }

    /**
     * Check if simulation should still be running.
     * @return Return boolean value if the application is still running.
     */
    private boolean simulate() {
        return Clock.getInstance().getClock() < simulationTime;
    }

    /**
     * Get the time of the next event on the event list to be set as the new current time.
     * @return time of next event on the event list.
     */
    private double currentTime() {
        return eventList.getNextEventTime();
    }

    /**
     * Process next event on the event list.
     */
    private void runBEvents() {
        while (eventList.getNextEventTime() == Clock.getInstance().getClock()) {
            runEvent(eventList.remove());
        }
    }

    public EventList getEventList() {
        return eventList;
    }

    protected abstract void initialize();
    protected abstract void runEvent(Event e);
    protected abstract void tryCEvents();
    protected abstract void results();

}
