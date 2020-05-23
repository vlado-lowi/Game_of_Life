package life;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GameOfLife {
    private final Universe universe;
    private final JFrame frame;
    private JLabel aliveLabel;
    private JLabel generationLabel;
    private JPanel drawingPanel;

    public GameOfLife(Universe universe) {
        this.universe = universe;

        // create new frame
        this.frame = new JFrame("Game of Life");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        initComponents();

        frame.pack(); // makes the window correct size for its content ?
        frame.setVisible(true);
    }

    private void initComponents() {
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
        SwingWorker<Void, Boolean> worker = new SwingWorker<>() {
            @Override
            public Void doInBackground() throws Exception {
                for(int i = 0; i < 50; i++) {
                    TimeUnit.MILLISECONDS.sleep(500);
                    UniverseController.getNextGeneration(universe);
                    publish(true);
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

        // create a button that starts the execution of SwingWorker
        JButton button = new JButton("Start");
        button.addActionListener(actionEvent -> worker.execute());
        // add button to panel
        controlPanel.add(button);

        // add control panel to frame (using borderLayout on frame)
        frame.add(controlPanel, BorderLayout.NORTH);

        // create new drawingPanel and add it to frame
        this.drawingPanel = new DrawingPanel(universe);
        drawingPanel.paint(drawingPanel.getGraphics());
        frame.add(drawingPanel, BorderLayout.CENTER);
    }
}