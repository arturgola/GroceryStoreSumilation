package simu.framework;

/**
 * Singleton class of system clock
 */
public class Clock {
    private static Clock instance;
    private double clock;

    /**
     * Constructor of the singleton
     */
    private Clock() { }

    /**
     * Retrieve the Clock instance. If there is no instance created the constructor method will be called.
     * @return Return the Clock instance.
     */
    public static Clock getInstance() {
        if (instance == null)
            instance = new Clock();
        return instance;
    }

    /**
     * Set the time of the clock.
     * @param clock time to be set.
     */
    public void setClock(double clock) {
        this.clock = clock;
    }

    /**
     * Get the time of the clock.
     * @return Time of the clock.
     */
    public double getClock() {
        return clock;
    }
}
