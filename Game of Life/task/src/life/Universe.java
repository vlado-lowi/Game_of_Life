package life;

import java.util.List;

public class Universe {
    // if true cell is alive, otherwise cell is dead
    private List<List<Boolean>> universe;

    public Universe(List<List<Boolean>> universe) {
        this.universe = universe;
    }

    public void print() {
        for (List<Boolean> row : universe) {
            for (Boolean cellState : row) {
                System.out.print(cellState ? "O" : " ");
            }
            System.out.println();
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
    }
}

