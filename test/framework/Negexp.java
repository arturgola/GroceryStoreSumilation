package framework;

import eduni.distributions.ContinuousGenerator;

public class Negexp implements ContinuousGenerator {
    public Negexp() {
    }

    @Override
    public double sample() {
        return 0;
    }

    @Override
    public void setSeed(long seed) {

    }

    @Override
    public long getSeed() {
        return 0;
    }

    @Override
    public void reseed() {

    }
}
