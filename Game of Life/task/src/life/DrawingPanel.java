package life;

import javax.swing.*;
import java.awt.*;

public class DrawingPanel extends JPanel {
    private Universe universe;
    private Dimension preferredSize;
    private final int OFFSET = 5;

    public DrawingPanel(Universe universe) {
        this.universe = universe;
        this.preferredSize = new Dimension(universe.getGridSize() +  2 * OFFSET,
                universe.getGridSize() + 2 * OFFSET);
    }

    public void setUniverse(Universe universe) {
        this.universe = universe;
        this.preferredSize = new Dimension(universe.getGridSize() + 2 * OFFSET,
                universe.getGridSize() + 2 * OFFSET);
    }

    @Override
    public Dimension getPreferredSize() {
        return preferredSize;
    }

    @Override
    public void setPreferredSize(Dimension preferredSize) {
        this.preferredSize = preferredSize;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int cellSize = universe.getCellSize();
        int gridSize = universe.getGridSize();
        int universeSize =
                universe.getUniverse() == null ? 20 : universe.getUniverse().size();
        g.setColor(Color.lightGray);
        for(int i = 0 ; i <= universeSize; i++) {
            // draw the grid of empty cells
            g.drawLine(OFFSET, i * (cellSize + 1) + OFFSET
                    , gridSize + OFFSET, i * (cellSize + 1) + OFFSET);
            g.drawLine(i * (cellSize + 1) + OFFSET, OFFSET,
                    i * (cellSize + 1) + OFFSET, gridSize + OFFSET);
        }

        g.setColor(Color.BLUE);
        // there should be no logic in paint method
        //  - getAliveCells returns a List of Cells that should be printed
        for (Cell cell : universe.getAliveCells()) {
            // fill in the alive cells
            g.fillRect((cellSize + 1) * cell.getX() + 1 + OFFSET,
                    (cellSize + 1) * cell.getY() + 1 + OFFSET, cellSize, cellSize);
        }
    }
}
