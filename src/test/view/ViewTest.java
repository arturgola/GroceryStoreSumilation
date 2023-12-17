package view;

import controller.Controller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ViewTest {

    private Controller controller;
    private View view;

    @BeforeEach
    void setUp() {
        controller = new Controller();
        view = new View(controller);
    }

    @Test
    void testUpdateTime() {
        double time = 5.0;
        view.updateTime(time);
        assertEquals(String.format("%.2f", time), view.simulationCurrentTime.getText());
    }

    @Test
    void testUpdateServicedCustomers() {
        int customersServiced = 10;
        view.updateServicedCustomers(customersServiced);
        assertEquals(String.valueOf(customersServiced), view.totalCustomerServiced.getText());
    }

    @Test
    void testUpdateTotalServiceAvgTime() {
        double time = 7.5;
        view.updateTotalServiceAvgTime(time);
        assertEquals(String.format("%.2f", time), view.totalServiceAvgTime.getText());
    }

    @Test
    void testPushDataToPoint() {
        double[] newData = {1.0, 2.0, 3.0, 4.0};
        view.pushDataToPoint(newData);
        assertTrue(view.distributionPanel.isVisible());
    }

    @Test
    void testFinishSimulation() {
        view.finishSimulation(15);
        assertTrue(view.startButton.isEnabled());
        assertFalse(view.pauseButton.isEnabled());
        assertTrue(view.speedUpButton.isEnabled());
        assertTrue(view.slowDownButton.isEnabled());
    }

    @Test
    void testStartButtonAction() {
        view.simulationTimeTextField.setText("10.0");
        view.startButton.doClick();
        assertFalse(view.startButton.isEnabled());
        assertTrue(view.pauseButton.isEnabled());
        assertTrue(view.speedUpButton.isEnabled());
        assertTrue(view.slowDownButton.isEnabled());
    }

    @Test
    void testSpeedUpButtonAction() {
        double initialDelay = controller.getDelay();
        view.speedUpButton.doClick();
        assertEquals(initialDelay + 0.5, controller.getDelay(), 0.001);
    }

    @Test
    void testSlowDownButtonAction() {
        double initialDelay = controller.getDelay();
        view.slowDownButton.doClick();
        assertEquals(Math.max(initialDelay - 0.5, 0.5), controller.getDelay(), 0.001);
    }

}