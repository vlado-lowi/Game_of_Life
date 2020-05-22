package life;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DrawingPanel extends JPanel {
    Universe universe;
    List<List<Boolean>> universeArr;

    public DrawingPanel(Universe universe) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.universe = universe;
        this.universeArr = universe.getUniverse();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 500);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
//        g.drawString("This is my custom Panel!",10,20);
        for(int i = 0 ; i < 20; i++) {
            g.setColor(Color.BLACK);
            g.drawLine(0, i * 25, 500, i*25);
            g.drawLine(i *25, 0, i * 25, 500);
        }
        // fill the alive cells
//        if (universeArr != null) { // if universe is created
            g.setColor(Color.DARK_GRAY);
            for (Cell cell : UniverseController.getIndicesOfAliveCells(universe)) {
                g.fillRect(25 * cell.getX() + 1, 25 * cell.getY() + 1, 24, 24);
            }
//        }
    }

    // todo create paintGeneration method that takes universe as parameter
    public void repaintCells() {
        repaint();
    }
}
