package life;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        final Universe universe = new Universe(); // generation 0
        SwingUtilities.invokeLater(() -> new GameOfLife(universe)); // start GUI
    }
}
