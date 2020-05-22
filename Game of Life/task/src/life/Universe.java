package life;

import java.util.List;

public class Universe {
    // if true cell is alive, otherwise cell is dead
    private List<List<Boolean>> universe;
    private int generation;
    private int alive;

    public Universe() {
        this.universe = null;
        this.generation = 0;
        this.alive = 0;
    }

    public Universe(List<List<Boolean>> universe, int generation) {
        this.universe = universe;
        this.generation = generation;
        if (universe == null) {
            this.alive = 0;
        } else {
            this.alive = getAliveCount();
        }
    }

    public int getAliveCount() {
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

    public void setUniverse(List<List<Boolean>> universe) {
        this.universe = universe;
        this.alive = getAliveCount();
    }

    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    public int getAlive() {
        return alive;
    }
}


