package controller;

import simu.model.MyEngine;
import view.View;

public class Controller {


    private MyEngine engine;
    private View view;


    public Controller(MyEngine engine, View view) {
        this.view = view;
        this.engine = engine;
        initView();
    }

    public void setView(View view) {
        this.view = view;
    }

    private void initView() {
        view.setVisible(true);
    }

    public void finishSimulation(int customersServiced){
        view.finishSimulation(customersServiced);
    }

    public void updateServicedCustomers(int customersServiced){
        view.updateServicedCustomers(customersServiced);
    }

    public void pushDataToPoint(double[] data){
        view.pushDataToPoint(data);
    }

    public void setEngineSimulationTime(double simulationTime) {
        engine.setSimulationTime(simulationTime);
    }

    public void  updateTime(double time){
        view.updateTime(time);
    }

    public void updateTotalServiceAvgTime(double time){
        view.updateTotalServiceAvgTime(time);
    }

    public void runSimulation() {
        engine.run();
    }

    public void reset() {
        engine = new MyEngine(this);
    }

    public double getDelay() {
        return engine.getDelay();
    }

    public void setDelay(double delay) {
        engine.setDelay(delay);
    }
}