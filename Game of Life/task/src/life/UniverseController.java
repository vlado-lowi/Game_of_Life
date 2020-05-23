package life;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UniverseController {
    /**
     *      Create a new universe (2D boolean array) of cells
     *      true = alive cell; false = dead cell
     *      initial state of cells (dead or alive) is random
     * @param size universe will be size * size 2D array
     * @param seed specify the seed for {@see java.util.Random} instance
     *             or null to not use seed
     * @return new instance of required size and seed
     */
    public static Universe createUniverse(int size, Long seed) {
        Random random;
        // use seed if its not null
        if(seed != null) {
            random = new Random(seed);
        } else {
            random = new Random();
        }

        // create empty 2D arr
        List<List<Boolean>> universeArray = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            // add inner arr
            universeArray.add(new ArrayList<>(size));
            for (int j = 0; j < size; j++) {
                // fill inner arr with random bool
                universeArray.get(i).add(random.nextBoolean());
            }
        }
        return new Universe(universeArray, 1);
    }

    /**
     * Calculates the next state of universe this means going over the 2D array of booleans
     * one by one, checking the number of neighbors and deciding whether the cell will be alive
     * or dead in the next generation
     * After iterating over all cells update the universe to reflect the next generation
     * @param universe calculates next generation for this universe
     */
    public static void getNextGeneration(Universe universe) {
        // if the universe 2D bool array is null this is generation 0
        // create generation 1 with size and seed
        if (universe.getUniverse() == null) {
            int size = 20; // size of universe
            Long seed = null; // seed for Random
            // create generation 1 with size and seed
            universe.setUniverse(createUniverse(size, seed));
        } else { // calculate next generation
            int universeSize = universe.getUniverse().size();
            // new 2D list for next generation
            List<List<Boolean>> resultArray = new ArrayList<>();

            for (int i = 0; i < universeSize; i++) { // iterate over rows
                // for each row add a new List
                resultArray.add(new ArrayList<>());
                for (int j = 0; j < universeSize; j++) { // iterate over members of row
                    // calculate next state of cell (i,j) and add it to result at (i,j)
                    resultArray.get(i).add(
                            getNextCellState(i, j, universe.getUniverse()));
                }
            }
            // update the universe to next generation and increment generation
            universe.setUniverse(new Universe(
                    resultArray, universe.getGeneration() + 1));
        }
    }

    /**
     * This method is used to get List of all alive cells {@see life.Cell} in universe
     * @param universe cells of this universe
     * @return List of the alive cells
     */
    public static List<Cell> getIndicesOfAliveCells(Universe universe) {
        List<Cell> resultList = new ArrayList<>();
        if (universe == null || universe.getUniverse() == null) { // dead universe
            return resultList; // return empty list
        }

        for (int i = 0; i < universe.getUniverse().size(); i++) { // iterate over rows
            for (int j = 0; j < universe.getUniverse().get(0).size(); j++) { // iterate over row members
                if (universe.getUniverse().get(i).get(j)) { // cell is alive
                    resultList.add(new Cell(i, j)); // add new cell with i,j indices
                }
            }
        }
        return resultList;
    }

    /**
     *     We consider the universe to be periodic:
     *     border cells also have eight neighbors.
     *     For example: If cell is right-border,
     *          its right (east) neighbor is leftmost cell in the same row.
     *     If cell is bottom-border,
     *          its bottom (south) neighbor is topmost cell in the same column.
     *     An alive cell survives if has two or three alive neighbors;
     *     otherwise, it dies of boredom (<2) or overpopulation (>3)
     *     A dead cell is reborn if it has exactly three alive neighbors
     * @param iIndex row index of this cell in matrix
     * @param jIndex column index of this cell in matrix
     * @param matrix 2D array of all cells
     * @return next state (alive/dead) depending on the rules
     */
    private static Boolean getNextCellState(int iIndex, int jIndex,
                                            List<List<Boolean>> matrix) {
        int size = matrix.size();

        // helper variables to account for periodic nature of matrix
        int iPlus = iIndex + 1 >= size ? 0 : iIndex + 1;
        int jPlus = jIndex + 1 >= size ? 0 : jIndex + 1;
        int iMinus = iIndex - 1 < 0 ? size - 1 : iIndex - 1;
        int jMinus = jIndex - 1 < 0 ? size - 1 : jIndex - 1;

        int aliveNeighbors = 0;
        // check all 8 neighbours of cell and count alive ones
        aliveNeighbors += matrix.get(iMinus).get(jIndex) ? 1 : 0;
        aliveNeighbors += matrix.get(iMinus).get(jPlus) ? 1 : 0;
        aliveNeighbors += matrix.get(iIndex).get(jPlus) ? 1 : 0;
        aliveNeighbors += matrix.get(iPlus).get(jPlus) ? 1 : 0;
        aliveNeighbors += matrix.get(iPlus).get(jIndex) ? 1 : 0;
        aliveNeighbors += matrix.get(iPlus).get(jMinus) ? 1 : 0;
        aliveNeighbors += matrix.get(iIndex).get(jMinus) ? 1 : 0;
        aliveNeighbors += matrix.get(iMinus).get(jMinus) ? 1 : 0;

        if (matrix.get(iIndex).get(jIndex)) { // true if cell is alive
            // cell survives
            if (aliveNeighbors < 2) {
                return false; // cell dies of boredom
            } else {
                // alive neighbors > 3 dies of over population
                // alive neighbors == 2 or 3 survive
                return aliveNeighbors <= 3;
            }
        } else { // cell is dead
            // cell is reborn if it had 3 alive neighbours
            // else remains dead
            return aliveNeighbors == 3;
        }
    }
}
