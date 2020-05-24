package life;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GameOfLife {
    private Universe universe;
    private final JFrame frame;
    private JLabel aliveLabel;
    private JLabel generationLabel;
    private DrawingPanel drawingPanel;
    private JToggleButton toggleButton;

    public GameOfLife() {
        this.frame = new JFrame("Game of Life");
    }

    public void startGUI( ) {
        this.universe = new Universe();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        SwingWorker<Void, Boolean> worker = initComponents();

        frame.pack(); // makes the window correct size for its content ?
        frame.setVisible(true);
        worker.execute();
    }

    private SwingWorker<Void, Boolean> initComponents() {
        // panel for info and tools
        JPanel controlPanel = new JPanel(new GridLayout(2, 1, 0,2));

        // create label and add it to panel
        this.generationLabel = new JLabel();
        generationLabel.setName("GenerationLabel");
        generationLabel.setText("  Generation #0");
        controlPanel.add(generationLabel);

        // create label and add it to panel
        this.aliveLabel = new JLabel();
        aliveLabel.setName("AliveLabel");
        aliveLabel.setText("  Alive: 0");
        controlPanel.add(aliveLabel);

        // worker calculates state of universe and draws it on drawingPanel
        PausableSwingWorker<Void, Boolean> worker = new PausableSwingWorker<>() {
            @Override
            public Void doInBackground() throws Exception {
                pause();
                while(!isCancelled()) {
                    if (isResetRequested()) {
                        removeResetRequest();
                        System.out.println("Creating universe with new seed");
                        universe = UniverseController.createUniverse(20, null);
                        drawingPanel.setUniverse(universe);
                        publish(true);
                        pause();
                    }
                    if (isPaused()) {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } else {
                        TimeUnit.MILLISECONDS.sleep(500);
                        UniverseController.getNextGeneration(universe);
                        publish(true);
                    }
                }
                return null;
            }

            @Override
            protected void process(List<Boolean> ignored) {
                drawingPanel.repaint();
                updateUI();
            }

            @Override
            protected void done() {
                updateUI();
                drawingPanel.repaint();
            }
            private void updateUI() { // update labels
                String genText = String.format("  Generation #%d", universe.getGeneration());
                generationLabel.setText(genText);

                String aliveText = String.format("  Alive: %d", universe.getAlive());
                aliveLabel.setText(aliveText);
            }
        };



        // toggle button pause and resume
        this.toggleButton = new JToggleButton("Pause/Resume");
        toggleButton.setName("PlayToggleButton");
        toggleButton.addActionListener(actionEvent -> {
            // if button is pressed pause
            if (((JToggleButton) actionEvent.getSource()).isSelected()) {
                worker.pause();
            } else {
                worker.resume();
            }
        });

        // start button
        JButton startButton = new JButton("Start");
        startButton.addActionListener(actionEvent -> {
            setToggleButton(false);
            worker.resume();
        });
        controlPanel.add(startButton);

        controlPanel.add(toggleButton);

        // button than creates a new universe
        JButton resetButton = new JButton("ResetButton");
        resetButton.setName("ResetButton");
        resetButton.addActionListener(actionEvent -> worker.requestReset());
        controlPanel.add(resetButton);

        // add control panel to frame (using borderLayout on frame)
        frame.add(controlPanel, BorderLayout.NORTH);

        // create new drawingPanel and add it to frame
        this.drawingPanel = new DrawingPanel(universe);
        drawingPanel.paint(drawingPanel.getGraphics());
        frame.add(drawingPanel, BorderLayout.CENTER);
        return worker;
    }

    private void setToggleButton(boolean selected) {
        toggleButton.setSelected(selected);
    }
}