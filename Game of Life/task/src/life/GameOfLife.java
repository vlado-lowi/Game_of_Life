package life;

import javax.swing.*;
import java.awt.*;

public class GameOfLife {
    Universe universe;
    public GameOfLife(Universe universe) {
        this.universe = universe;
        System.out.println("Created GUI on EDT? "+
                SwingUtilities.isEventDispatchThread());
        JFrame f = new JFrame("Game of Life");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(300, 300);
//        setTitle("Game of Life");
        f.setLocationRelativeTo(null);
        f.setLayout(new BorderLayout());

        initComponents(f);
        f.pack();

        f.setVisible(true);
    }

    private void initComponents(JFrame frame) {
        JPanel labelsPanel = new JPanel(new GridLayout(2, 1, 0,2));

        JLabel genLabel = new JLabel();
        genLabel.setName("GenerationLabel");
        genLabel.setText("  Generation #0");
//        labelsPanel.setBorder(BorderFactory.createDashedBorder(Color.BLUE));
        labelsPanel.add(genLabel);

        JLabel aliveLabel = new JLabel();
        aliveLabel.setName("AliveLabel");
        aliveLabel.setText("  Alive: 0");
        labelsPanel.add(aliveLabel);

        frame.add(labelsPanel, BorderLayout.NORTH);

        JPanel drawingPanel = new DrawingPanel();
        drawingPanel.paint(drawingPanel.getGraphics());
//        drawingPanel.setBorder(BorderFactory.createDashedBorder(Color.RED));
        frame.add(drawingPanel, BorderLayout.CENTER);
    }

    // todo create updateAliveAndGeneration method
    //  that changes generation and alive based on universe reference
}