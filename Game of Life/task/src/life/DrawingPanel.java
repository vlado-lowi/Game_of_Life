package life;

import javax.swing.*;
import java.awt.*;

public class DrawingPanel extends JPanel {
    private Universe universe;

    public DrawingPanel(Universe universe) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.universe = universe;
    }

    public void setUniverse(Universe universe) {
        this.universe = universe;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 500);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        for(int i = 0 ; i < 20; i++) {
            // draw the grid of empty cells
            g.drawLine(0, i * 25, 500, i*25);
            g.drawLine(i *25, 0, i * 25, 500);
        }

        g.setColor(Color.DARK_GRAY);
        // there should be no logic in paint method
        //  - getAliveCells returns a List of Cells that should be printed
        for (Cell cell : universe.getAliveCells()) {
            // fill in the alive cells
            g.fillRect(25 * cell.getX() + 1, 25 * cell.getY() + 1, 24, 24);
        }
    }
}
