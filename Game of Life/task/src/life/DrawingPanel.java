package life;

import javax.swing.*;
import java.awt.*;

public class DrawingPanel extends JPanel {
    public DrawingPanel() {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
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
            g.drawLine(0, i * 25, 500, i*25);
            g.drawLine(i *25, 0, i * 25, 500);
        }
        // fill a random rectangle
        g.setColor(Color.DARK_GRAY);
        g.fillRect(25, 25, 25, 25);
    }

    // todo create paintGeneration method that takes universe as parameter
}
