package life;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GameOfLife {
    Universe universe;
    JFrame f;
    JLabel aliveLabel;
    JLabel generationLabel;
    JPanel drawingPanel;

    public GameOfLife(Universe universe) {
        this.universe = universe;
        System.out.println("Created GUI on EDT? "+
                SwingUtilities.isEventDispatchThread());
        this.f = new JFrame("Game of Life");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(300, 300);
        f.setLocationRelativeTo(null);
        f.setLayout(new BorderLayout());

        initComponents(f);

        f.pack();
        f.setVisible(true);
    }

    private void initComponents(JFrame frame) {
        JPanel labelsPanel = new JPanel(new GridLayout(2, 1, 0,2));

        this.generationLabel = new JLabel();
        generationLabel.setName("GenerationLabel");
        generationLabel.setText("  Generation #0");
//        labelsPanel.setBorder(BorderFactory.createDashedBorder(Color.BLUE));
        labelsPanel.add(generationLabel);

        this.aliveLabel = new JLabel();
        aliveLabel.setName("AliveLabel");
        aliveLabel.setText("  Alive: 0");
        labelsPanel.add(aliveLabel);

        JButton button = new JButton("Start");
        SwingWorker<Void, List<Cell>> worker = new SwingWorker<>() {
            @Override
            public Void doInBackground() throws Exception {
                for(int i = 0; i < 50; i++) {
                    TimeUnit.MILLISECONDS.sleep(500);
                    UniverseController.getNextGeneration(universe);
                    publish(UniverseController.getIndicesOfAliveCells(universe));
                }
                return null;
            }

            @Override
            protected void process(List<List<Cell>> chunks) {
                List<Cell> aliveCells = chunks.get(chunks.size() - 1);
                drawingPanel.repaint();
                updateUI();
            }

            @Override
            protected void done() {
                updateUI();
//                DrawingPanel panel = (DrawingPanel) drawingPanel;
//                panel.repaintCells();
                drawingPanel.repaint();
                System.out.println("REDRAWING");
            }
            private void updateUI() { // update labels
                String genText = String.format("  Generation #%d", universe.getGeneration());
                generationLabel.setText(genText);

                String aliveText = String.format("  Alive: %d", universe.getAlive());
                aliveLabel.setText(aliveText);
            }
        };

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                worker.execute();
            }
        });
        labelsPanel.add(button);


        frame.add(labelsPanel, BorderLayout.NORTH);

        this.drawingPanel = new DrawingPanel(universe);
        drawingPanel.paint(drawingPanel.getGraphics());
//        drawingPanel.setBorder(BorderFactory.createDashedBorder(Color.RED));
        frame.add(drawingPanel, BorderLayout.CENTER);
    }

    // todo create updateAliveAndGeneration method
    //  that changes generation and alive based on universe reference
}