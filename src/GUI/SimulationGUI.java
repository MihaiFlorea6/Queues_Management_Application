package GUI;

import BusinessLogic.SimulationManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class SimulationGUI extends JFrame {
    private final JTextField clientsField, queuesField, timeMaxField;
    private final JTextField minArrivalField, maxArrivalField, minServiceField, maxServiceField;
    private final JTextArea logArea;
    private JButton startButton;
    private final JButton exportButton;
    private JComboBox<String> strategyComboBox;

    public SimulationGUI() {
        setTitle("Queue Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 600));


        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Simulation Parameters"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;


        int row = 0;


        gbc.gridx = 0; gbc.gridy = row++;
        inputPanel.add(new JLabel("Strategy:"), gbc);
        gbc.gridx = 1;
        strategyComboBox = new JComboBox<>(new String[]{"Shortest Queue", "Time Strategy"});
        inputPanel.add(strategyComboBox, gbc);


        gbc.gridx = 0; gbc.gridy = row++;
        inputPanel.add(new JLabel("Number of Clients:"), gbc);
        gbc.gridx = 1;
        clientsField = new JTextField(10);
        inputPanel.add(clientsField, gbc);


        gbc.gridx = 0; gbc.gridy = row++;
        inputPanel.add(new JLabel("Number of Queues:"), gbc);
        gbc.gridx = 1;
        queuesField = new JTextField(10);
        inputPanel.add(queuesField, gbc);


        gbc.gridx = 0; gbc.gridy = row++;
        inputPanel.add(new JLabel("Time Max Simulation:"), gbc);
        gbc.gridx = 1;
        timeMaxField = new JTextField(10);
        inputPanel.add(timeMaxField, gbc);


        gbc.gridx = 0; gbc.gridy = row++;
        inputPanel.add(new JLabel("Min Time Arrival:"), gbc);
        gbc.gridx = 1;
        minArrivalField = new JTextField(10);
        inputPanel.add(minArrivalField, gbc);


        gbc.gridx = 0; gbc.gridy = row++;
        inputPanel.add(new JLabel("Max Time Arrival:"), gbc);
        gbc.gridx = 1;
        maxArrivalField = new JTextField(10);
        inputPanel.add(maxArrivalField, gbc);


        gbc.gridx = 0; gbc.gridy = row++;
        inputPanel.add(new JLabel("Min Time Service:"), gbc);
        gbc.gridx = 1;
        minServiceField = new JTextField(10);
        inputPanel.add(minServiceField, gbc);


        gbc.gridx = 0; gbc.gridy = row++;
        inputPanel.add(new JLabel("Max Time Service:"), gbc);
        gbc.gridx = 1;
        maxServiceField = new JTextField(10);
        inputPanel.add(maxServiceField, gbc);


        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setPreferredSize(new Dimension(750, 300));
        scrollPane.setBorder(BorderFactory.createTitledBorder("Simulation Log"));


        startButton = new JButton("Start Simulation");
        startButton.setPreferredSize(new Dimension(200, 40));
        startButton.addActionListener(new StartButtonListener());

        exportButton = new JButton("Export Log to File");
        exportButton.setPreferredSize(new Dimension(200, 40));
        exportButton.setEnabled(false);
        exportButton.addActionListener(e -> exportLogToFile());

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(startButton);
        buttonPanel.add(exportButton);


        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }

    private void exportLogToFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Log File");
        fileChooser.setSelectedFile(new File("simulation_log.txt"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try {
                Files.write(fileToSave.toPath(), logArea.getText().getBytes());
                JOptionPane.showMessageDialog(this,
                        "Log saved successfully to: " + fileToSave.getAbsolutePath(),
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error saving file: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class StartButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {

                if (queuesField.getText().isEmpty() || clientsField.getText().isEmpty() ||
                        timeMaxField.getText().isEmpty() || minArrivalField.getText().isEmpty() ||
                        maxArrivalField.getText().isEmpty() || minServiceField.getText().isEmpty() ||
                        maxServiceField.getText().isEmpty()) {
                    throw new NumberFormatException("Empty fields");
                }

                int numClients = Integer.parseInt(clientsField.getText());
                int numQueues = Integer.parseInt(queuesField.getText());
                int timeMax = Integer.parseInt(timeMaxField.getText());
                int minArrival = Integer.parseInt(minArrivalField.getText());
                int maxArrival = Integer.parseInt(maxArrivalField.getText());
                int minService = Integer.parseInt(minServiceField.getText());
                int maxService = Integer.parseInt(maxServiceField.getText());

                if (minArrival > maxArrival || minService > maxService) {
                    throw new IllegalArgumentException("Invalid time ranges");
                }


                logArea.setText("");
                exportButton.setEnabled(true);


                SimulationManager simulation = new SimulationManager(
                        numClients, numQueues, timeMax,
                        minArrival, maxArrival, minService, maxService,
                        logArea
                );
                new Thread(simulation).start();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        SimulationGUI.this,
                        "Invalid input! Please enter valid numbers in all fields.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(
                        SimulationGUI.this,
                        ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SimulationGUI gui = new SimulationGUI();
            gui.setVisible(true);
        });
    }
}