package life;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents universe in game of life
 */
public class Universe {
    // 2D array representing grid of cells
    // true  = cell is alive
    // false = cell is dead
    private List<List<Boolean>> universe;
    // list of all alive cells
    // useful for paintComponent method
    private List<Cell> aliveCells;
    private int generation;
    private int alive;

    /**
     * Create dead universe; generation 0; empty universe
     */
    public Universe() {
        this.universe = null;
        this.generation = 0;
        this.alive = 0;
        this.aliveCells = new ArrayList<>();
    }

    /**
     * @param universe 2D Array of cell states true = alive; false = dead
     * @param generation number of generation this universe represents
     */
    public Universe(List<List<Boolean>> universe, int generation) {
        this.universe = universe;
        this.generation = generation;
        if (universe == null) {
            this.alive = 0;
            this.aliveCells = new ArrayList<>();
        } else {
            // goes over the newly set 2D array and counts alive cells
            this.alive = countAliveCells();
            this.aliveCells = UniverseController.getIndicesOfAliveCells(this);
        }
    }

    // return number of alive cells in universe
    private int countAliveCells() {
        int count = 0;
        for (List<Boolean> row : universe) {
            for (Boolean cellState : row) {
                if (cellState) {
                    count++;
                }
            }
        }
        return  count;
    }

    public List<List<Boolean>> getUniverse() {
        return universe;
    }

    public void setUniverse(Universe universe) {
        this.universe = universe.getUniverse();
        this.generation = universe.getGeneration();
        this.alive = universe.getAlive();
        this.aliveCells = UniverseController.getIndicesOfAliveCells(this);
    }

    public int getGeneration() {
        return generation;
    }

    public List<Cell> getAliveCells() {
        return aliveCells;
    }

    public int getAlive() {
        return alive;
    }
}


