package life;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
//        long seed = scanner.nextLong();
        int generations = 12; //scanner.nextInt();
        scanner.close();

        Universe universe = UniverseController.createUniverse(n, null); // Gen #1
        int generation = 1;
        printGeneration(universe, generation);

        while (generation <= generations) {
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            universe = UniverseController.getNextGeneration(universe);
            generation++;
            printGeneration(universe, generation);
        }
    }

    private static void printGeneration(Universe universe, int generation) {
        clearConsole();
        System.out.printf("Generation #%d%n", generation);
        System.out.printf("Alive: %d%n", universe.getAliveCount());
        System.out.println();
        universe.print();
    }

    private static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
            else
                Runtime.getRuntime().exec("clear");
        }
        catch (IOException | InterruptedException e) {
            System.err.println("Exception occurred when trying to clear console.");
            System.err.println(e.getMessage());
        }
    }
}
