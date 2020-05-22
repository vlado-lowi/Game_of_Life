package life;

import javax.swing.*;
//import java.io.IOException;
//import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        int n = 20; // scanner.nextInt();
//        long seed = scanner.nextLong();
//        int generations = 12; //scanner.nextInt();
//        scanner.close();
        final Universe universe = new Universe();//UniverseController.createUniverse(n, null); // Gen #1
        SwingUtilities.invokeLater(() -> new GameOfLife(universe));
    }
}
