package life;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        long seed = scanner.nextLong();
        int generations = scanner.nextInt();
        scanner.close();
        Universe universe = UniverseController.createUniverse(n, seed); // Gen #0
        for (int i = 0; i < generations; i++) { // get requested generation
            universe = UniverseController.getNextGeneration(universe);
        }
        universe.print();
    }
}
