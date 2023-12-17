package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import controller.Controller;

public class View extends JFrame {

    ArrayList<double[]> dataSet = new ArrayList<>();

    private JPanel mainPanel;

    JPanel distributionPanel;


    private static double[][] arrayListTo2DArray(ArrayList<double[]> arrayList) {
        if (!arrayList.isEmpty()) {
            int numRows = arrayList.size();
            int numCols = arrayList.getFirst().length;
            double[][] resultArray = new double[numCols][numRows];

            for (int i = 0; i < numRows; i++) {
                double[] rowArray = arrayList.get(i);

                for (int j = 0; j < numCols; j++) {
                    resultArray[j][i] = rowArray[j];
                }
            }

            return resultArray;
        }

        return null;
    }

    private void updateDistributionPanel() {

        JFreeChart chart = ((ChartPanel) distributionPanel.getComponent(0)).getChart();

        CategoryDataset newDataset = createDataset(dataSet.getLast());
        ((CategoryPlot) chart.getPlot()).setDataset(newDataset);

        distributionPanel.revalidate();
        distributionPanel.repaint();
    }

    JButton startButton;
    JButton pauseButton;
    JButton speedUpButton;
    JButton slowDownButton;

    JTextField simulationTimeTextField;
    JLabel totalCustomerServiced;

    private JPanel totalCustomersServicedPanel, simulationCurrentTimePanel,totalServiceAvgTimePanel;

    JLabel simulationCurrentTime;
    JLabel totalServiceAvgTime;

    private JLabel speedLabel;


    private Controller controller;

    public void setController(Controller controller) {
        this.controller = controller;
        setStartButtonListener();
        setSpeedUpButtonListener();
        setSlowDownButtonListener();
        setPauseButtonListener();
    }

    public View(Controller controller) {

        double[] data = {0,0,0,0};

        distributionPanel = createDistributionPanel(data);

        distributionPanel.setVisible(false);


        this.controller = controller;

        setTitle("My simulation");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        simulationTimeTextField = new JTextField();
        simulationTimeTextField.setPreferredSize(new Dimension(100, 25));
        registerNumericKeyListener(simulationTimeTextField);


        totalCustomerServiced = new JLabel("0");
        totalCustomerServiced.setPreferredSize(new Dimension(150, 25));

        simulationCurrentTime = new JLabel("0");
        simulationCurrentTime.setPreferredSize(new Dimension(150, 25));

        totalServiceAvgTime = new JLabel("0");
        totalServiceAvgTime.setPreferredSize(new Dimension(150, 25));


        startButton = new JButton("Start");
        pauseButton = new JButton("Pause");
        speedUpButton = new JButton("Speed Up");
        slowDownButton = new JButton("Slow Down");


        startButton.setEnabled(false);
        pauseButton.setEnabled(false);
        speedUpButton.setEnabled(true);
        slowDownButton.setEnabled(true);

        JPanel buttonsPanel = new JPanel(new FlowLayout());

        buttonsPanel.add(startButton);
        buttonsPanel.add(pauseButton);
        buttonsPanel.add(speedUpButton);
        buttonsPanel.add(slowDownButton);

        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Enter simulation time:"));
        inputPanel.add(simulationTimeTextField);

        simulationCurrentTimePanel = new JPanel(new FlowLayout());
        simulationCurrentTimePanel.add(new JLabel("Current simulation time:"));
        simulationCurrentTimePanel.add(simulationCurrentTime);
        simulationCurrentTimePanel.setVisible(false);

        totalCustomersServicedPanel = new JPanel(new FlowLayout());
        totalCustomersServicedPanel.add(new JLabel("Total Customers Serviced:"));
        totalCustomersServicedPanel.add(totalCustomerServiced);
        totalCustomersServicedPanel.setVisible(false);

        totalServiceAvgTimePanel = new JPanel(new FlowLayout());
        totalServiceAvgTimePanel.add(new JLabel("Total service avg time:"));
        totalServiceAvgTimePanel.add(totalServiceAvgTime);
        totalServiceAvgTimePanel.setVisible(false);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(inputPanel);
        mainPanel.add(simulationCurrentTimePanel);
        mainPanel.add(totalCustomersServicedPanel);
        mainPanel.add(totalServiceAvgTimePanel);
        mainPanel.add(distributionPanel);
        mainPanel.add(buttonsPanel);

        speedLabel = new JLabel("Speed: 1x");
        speedLabel.setPreferredSize(new Dimension(150, 25));
        mainPanel.add(speedLabel);

        add(mainPanel, BorderLayout.CENTER);

    }

    private void registerNumericKeyListener(JTextField textField) {
        textField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) {
                    e.consume();


                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                updateStartButtonState();


            }
        });
    }

    private void updateStartButtonState() {
        startButton.setEnabled(!simulationTimeTextField.getText().isEmpty());
    }

    private void setStartButtonListener() {
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Button clicked on EDT? " + SwingUtilities.isEventDispatchThread());

                controller.setEngineSimulationTime(Double.parseDouble(simulationTimeTextField.getText()));

                simulationCurrentTimePanel.setVisible(true);
                totalCustomersServicedPanel.setVisible(true);
                totalServiceAvgTimePanel.setVisible(true);

                distributionPanel.setVisible(true);

                startButton.setEnabled(false);
                pauseButton.setEnabled(true);
                speedUpButton.setEnabled(true);
                slowDownButton.setEnabled(true);

                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        System.out.println("Running on background thread? " + SwingUtilities.isEventDispatchThread());
                        controller.runSimulation();

                        return null;
                    }
                };

                worker.execute();
            }
        });
    }

    private void setSpeedUpButtonListener() {
        speedUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                controller.setDelay(Math.min((controller.getDelay() + 0.5), 2));
                updateSpeedLabel();
            }
        });
    }

    private void setSlowDownButtonListener() {
        slowDownButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                controller.setDelay(Math.max((controller.getDelay() - 0.5), 0.5));
                updateSpeedLabel();
            }
        });
    }

    private void updateSpeedLabel() {

        speedLabel.setText("Speed: " + controller.getDelay() + "x");
    }

    private void setPauseButtonListener() {
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.togglePause();
            }
        });
    }

    public void finishSimulation(int servicedCustomers) {
        controller.reset();
        updateSpeedLabel();
        startButton.setEnabled(true);
        pauseButton.setEnabled(false);
        speedUpButton.setEnabled(true);
        slowDownButton.setEnabled(true);
    }

    public void updateTime(double time) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                simulationCurrentTime.setText(String.format("%.2f", time));
            }
        });
    }

    public void updateServicedCustomers(int customersServiced){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                totalCustomerServiced.setText(String.valueOf(customersServiced));
            }
        });
    }

    public void updateTotalServiceAvgTime(double time){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                totalServiceAvgTime.setText(String.format("%.2f", time));
            }
        });
    }

    public void pushDataToPoint(double[] newData) {
        dataSet.add(newData);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                updateDistributionPanel();
            }
        });
    }

    private JPanel createDistributionPanel(double[] data) {
        JPanel panel = new JPanel(new BorderLayout());

        CategoryDataset dataset = createDataset(data);

        JFreeChart chart = ChartFactory.createBarChart(
                "Average Time Distribution",
                "",
                "Average Time",
                dataset
        );

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(400, 200));

        panel.add(chartPanel, BorderLayout.CENTER);
        return panel;
    }

    private CategoryDataset createDataset(double[] data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i = 0; i < data.length; i++) {

            dataset.addValue(data[i], "S" + (i + 1), "");
        }

        return dataset;
    }

    private double calculateAverage(double[] values) {
        double sum = 0.0;
        for (double value : values) {
            if(value>0){
                sum += value;
            }
        }
        return sum / values.length;
    }
}

