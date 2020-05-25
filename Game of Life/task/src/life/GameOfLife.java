package life;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.concurrent.TimeUnit;

import static life.PausableSwingWorker.*;

/**
 * Custom UI for the 0 player game of life
 */
public class GameOfLife extends JFrame {

    private Universe universe;
    private JLabel aliveLabel;
    private JLabel generationLabel;
    private DrawingPanel drawingPanel;
    private JToggleButton toggleButton;


    public GameOfLife() {

        universe = new Universe();
        setTitle("Game of Life");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);
        setLayout(new BorderLayout());

        // initialize all components of this frame and get our worker
        SwingWorker<Void, Boolean> worker = initComponents();

        // makes the window correct size
        // decides by looking at components preferred size i think ?
        pack();
        // setLocation relative to null opens the window in the middle of screen
        // must be called after pack() to work properly
        setLocationRelativeTo(null);
        setVisible(true);
        worker.execute();
    }

    private SwingWorker<Void, Boolean> initComponents() {

        // worker calculates state of universe and draws it on drawingPanel
        PausableSwingWorker<Void, Boolean> worker = new PausableSwingWorker<>() {

            /**
             * called after execute() is called on instance of this
             */
            @Override
            public Void doInBackground() throws Exception {
                // this method is called right after gui is drawn

                // pause the application at start
                pause();

                // we never cancel execution of the worker so this is effectively while(true) {...}
                while(!isCancelled()) {
                    // did user request another size of universe ?
                    if (isResizeRequested()) {
                        System.out.println("Resizing to: " + getRequestedSize());

                        removeResizeRequest();
                        // reset is needed to create a new universe of requested size
                        requestReset();
                    }
                    // did user request reset of universe state ?
                    if (isResetRequested()) {
                        removeResetRequest();
                        System.out.println("Creating new universe");

                        // create new universe with random state and requested(or default) size
                        universe = UniverseController.createUniverse(getRequestedSize());
                        // assign universe to drawing panel
                        // changes the preferred size of panel
                        drawingPanel.setUniverse(universe);
                        // changes windows size based on preferred size of components
                        pack();
                        // move the window after changing it's size
                        // keeping it in middle of screen
                        setLocationRelativeTo(null);
                        // update the user interface to reflect changes
                        updateUI();
                        // pause the application after creating new start state
                        pause();
                        // make the pause/resume button pressed
                        toggleButton.setSelected(true);
                    }
                    if (isPaused()) {
                        // if application is paused wait 100 ms before checking again
                        TimeUnit.MILLISECONDS.sleep(100);
                    } else {// no change requests or pause
                        // update universe to next generation
                        UniverseController.getNextGeneration(universe);
                        // wait user selected time in ms before drawing next generation
                        TimeUnit.MILLISECONDS.sleep(speed);
                        // update the user interface to reflect changes
                        updateUI();
                    }
                }
                return null;
            }

            /**
             * Called after execution of doInBackground() has finished
             */
            @Override
            protected void done() {
                updateUI();
            }

            private void updateUI() { // update labels
                String genText = String.format("  Generation #%d", universe.getGeneration());
                generationLabel.setText(genText);

                String aliveText = String.format("  Alive: %d", universe.getAlive());
                aliveLabel.setText(aliveText);

                drawingPanel.repaint();
            }
        };

        // buttons

        // toggle button pause/resume
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
            toggleButton.setSelected(false);
            worker.resume();
        });

        // button than creates a new universe
        JButton resetButton = new JButton("Reset");
        resetButton.setName("ResetButton");
        resetButton.addActionListener(actionEvent -> worker.requestReset());

        // labels

        // generation label
        generationLabel = new JLabel("  Generation #0");
        generationLabel.setName("GenerationLabel");
        generationLabel.setFont(new Font("Serif", Font.BOLD, 16));

        // alive label
        aliveLabel = new JLabel("  Alive: 0");
        aliveLabel.setName("AliveLabel");
        aliveLabel.setFont(new Font("Serif", Font.BOLD, 16));

        // set speed label
        JLabel speedLabel = new JLabel("Set time in ms between generations:");

        // set size label
        JLabel sizeLabel = new JLabel("Set size:");

        // set color label
        JLabel colorLabel = new JLabel("Select color of cells: ");

        // application properties

        // speed slider
        // select time in milliseconds between drawing distinct generations
        JSlider speedSlider =
                new JSlider(JSlider.HORIZONTAL, MIN_SPEED, MAX_SPEED, INIT_SPEED);
        // changes the speed of worker when the slider position changes
        speedSlider.addChangeListener(changeEvent -> {
            JSlider source = (JSlider)changeEvent.getSource();
            int speed = source.getValue();
            worker.setSpeed(speed);

        });
        speedSlider.setMajorTickSpacing(100);
        speedSlider.setMinorTickSpacing(20);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);

        // integer number format
        NumberFormat numberFormat = NumberFormat.getIntegerInstance();
        // input text field that only takes integers
        JFormattedTextField sizeField = new JFormattedTextField(numberFormat);
        // on change reset worker with requested size
        sizeField.addActionListener(actionEvent -> {
            JFormattedTextField source = (JFormattedTextField) actionEvent.getSource();
            int size = ((Number) source.getValue()).intValue();
            worker.requestResize(size);
        });
        sizeField.setSize(100, 30);

        //todo create a JColorChooser
        //todo apply color from JColorChooser to cells

        // panels

        // button panel with grid layout
        JPanel buttonsPanel =
                new JPanel(new GridLayout(1, 3, 5, 50));
        // add buttons to the panel
        buttonsPanel.add(startButton);
        buttonsPanel.add(toggleButton);
        buttonsPanel.add(resetButton);

        // panel with controls and info
        JPanel controlPanel = new JPanel(new BorderLayout());
        // add button panel on top of control panel (NORTH)
        controlPanel.add(buttonsPanel, BorderLayout.NORTH);

        // universe set size panel
        JPanel setSizePanel = new JPanel(
                new GridLayout(1, 2, 5, 30));
        setSizePanel.setMaximumSize(new Dimension(220, 30));

        // center part of control panel
        JPanel centerSidePanel = new JPanel();
        centerSidePanel.setLayout(new BoxLayout(centerSidePanel, BoxLayout.Y_AXIS));

        // drawing panel - for painting
        drawingPanel = new DrawingPanel(universe);
        drawingPanel.paint(drawingPanel.getGraphics());

        // adding components to panels

        setSizePanel.add(sizeLabel);
        setSizePanel.add(sizeField);
        // empty box for spacing
        centerSidePanel.add(Box.createRigidArea(new Dimension(5, 20)));
        centerSidePanel.add(generationLabel);
        centerSidePanel.add(aliveLabel);
        // empty box for spacing
        centerSidePanel.add(Box.createRigidArea(new Dimension(5, 20)));
        centerSidePanel.add(speedLabel);
        centerSidePanel.add(speedSlider);
        // empty box for spacing
        centerSidePanel.add(Box.createRigidArea(new Dimension(5, 20)));
        centerSidePanel.add(setSizePanel);
        controlPanel.add(centerSidePanel, BorderLayout.CENTER);

        // add control panel to left(WEST) part of frame
        add(controlPanel, BorderLayout.WEST);
        // add drawing panel to center part of frame
        add(drawingPanel, BorderLayout.CENTER);
        return worker;
    }
}
