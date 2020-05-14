package life;

import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        long seed = scanner.nextLong();
        scanner.close();
        populateUniverse(n, seed);
    }

    private static void populateUniverse(int n, long seed) {
        Random random = new Random(seed);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (random.nextBoolean()){
                    System.out.print("O");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }
}
