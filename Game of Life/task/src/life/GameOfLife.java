package life;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GameOfLife extends JFrame {
    private Universe universe;
    private JLabel aliveLabel;
    private JLabel generationLabel;
    private DrawingPanel drawingPanel;
    private JToggleButton toggleButton;
    static final int MAX_SPEED = 1050;
    static final int MIN_SPEED = 50;
    static final int INIT_SPEED = 500;

    public GameOfLife() {
        this.universe = new Universe();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        SwingWorker<Void, Boolean> worker = initComponents();

        pack(); // makes the window correct size for its content ?
        setVisible(true);
        worker.execute();
    }

    private SwingWorker<Void, Boolean> initComponents() {

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
                        setToggleButton(true);
                    }
                    if (isPaused()) {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } else {
                        TimeUnit.MILLISECONDS.sleep(speed);
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
        this.toggleButton = new JToggleButton("Pause");
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


        // button than creates a new universe
        JButton resetButton = new JButton("Reset");
        resetButton.setName("ResetButton");
        resetButton.addActionListener(actionEvent -> worker.requestReset());

        // panel for info and tools
        JPanel sidePanel = new JPanel(new BorderLayout());

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 3, 5, 50));
        buttonsPanel.add(startButton);
        buttonsPanel.add(toggleButton);
        buttonsPanel.add(resetButton);

        sidePanel.add(buttonsPanel, BorderLayout.NORTH);

        // create label and add it to panel
        this.generationLabel = new JLabel();
        generationLabel.setName("GenerationLabel");
        generationLabel.setText("  Generation #0");
        generationLabel.setFont(new Font("Serif", Font.BOLD, 16));
        JPanel centerSidePanel = new JPanel();
        centerSidePanel.setLayout(new BoxLayout(centerSidePanel, BoxLayout.Y_AXIS));
        centerSidePanel.add(Box.createRigidArea(new Dimension(5, 20)));
        centerSidePanel.add(generationLabel);
//        sidePanel.add(generationLabel, BorderLayout.CENTER);

        // create label and add it to panel
        this.aliveLabel = new JLabel();
        aliveLabel.setName("AliveLabel");
        aliveLabel.setText("  Alive: 0");
        aliveLabel.setFont(new Font("Serif", Font.BOLD, 16));
//        sidePanel.add(aliveLabel, BorderLayout.CENTER);
        centerSidePanel.add(aliveLabel);
        centerSidePanel.add(Box.createRigidArea(new Dimension(5, 20)));
        JLabel speedLabel = new JLabel("Speed mode:");
        centerSidePanel.add(speedLabel);
        JSlider speedSlider =
                new JSlider(JSlider.HORIZONTAL, MIN_SPEED, MAX_SPEED, INIT_SPEED);
        speedSlider.addChangeListener(changeEvent -> {
            JSlider source = (JSlider)changeEvent.getSource();
            if (!source.getValueIsAdjusting()) {
                int speed = source.getValue();
                worker.setSpeed(speed);
            }
        });
        speedSlider.setMajorTickSpacing(200);
        speedSlider.setPaintLabels(true);
        centerSidePanel.add(speedSlider);
        sidePanel.add(centerSidePanel, BorderLayout.CENTER);



        // add control panel to frame (using borderLayout on frame)
        add(sidePanel, BorderLayout.WEST);

        // create new drawingPanel and add it to frame
        this.drawingPanel = new DrawingPanel(universe);
        drawingPanel.paint(drawingPanel.getGraphics());
        add(drawingPanel, BorderLayout.CENTER);
        return worker;
    }

    private void setToggleButton(boolean selected) {
        toggleButton.setSelected(selected);
    }

}