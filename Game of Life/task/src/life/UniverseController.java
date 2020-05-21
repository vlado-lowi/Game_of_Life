package life;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UniverseController {
    public static Universe createUniverse(int n, Long seed) {
        Random random;
        if(seed != null) {
            random = new Random(seed);
        } else {
            random = new Random();
        }

        List<List<Boolean>> universeArray = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            universeArray.add(new ArrayList<>());
            for (int j = 0; j < n; j++) {
                universeArray.get(i).add(random.nextBoolean());
            }
        }
        return new Universe(universeArray);
    }

    public static void getNextGeneration(Universe universe) {
        List<List<Boolean>> thisGeneration = universe.getUniverse();
        List<List<Boolean>> nextGenUniverseArr = new ArrayList<>();
        for (int i = 0; i < thisGeneration.size(); i++) {
            nextGenUniverseArr.add(new ArrayList<>());
            for (int j = 0; j < thisGeneration.size(); j++) {
                nextGenUniverseArr.get(i).add(getNextCellState(i, j, thisGeneration));
            }
        }
        universe.setUniverse(nextGenUniverseArr);
    }

    /*
    We consider the universe to be periodic: border cells also have eight neighbors. For example:
    If cell is right-border, its right (east) neighbor is leftmost cell in the same row.
    If cell is bottom-border, its bottom (south) neighbor is topmost cell in the same column.
    An alive cell survives if has two or three alive neighbors; otherwise, it dies of boredom (<2) or overpopulation (>3)
    A dead cell is reborn if it has exactly three alive neighbors
     */
    private static Boolean getNextCellState(int iIndex, int jIndex, List<List<Boolean>> matrix) {
        int size = matrix.size();
        int iPlus = iIndex + 1 >= size ? 0 : iIndex + 1;
        int jPlus = jIndex + 1 >= size ? 0 : jIndex + 1;
        int iMinus = iIndex - 1 < 0 ? size - 1 : iIndex - 1;
        int jMinus = jIndex - 1 < 0 ? size - 1 : jIndex - 1;
        boolean cellState = matrix.get(iIndex).get(jIndex);
        int aliveNeighbors = 0;
        // checking all neighbors around cell starting from north going clockwise
        aliveNeighbors += matrix.get(iMinus).get(jIndex) ? 1 : 0;
        aliveNeighbors += matrix.get(iMinus).get(jPlus) ? 1 : 0;
        aliveNeighbors += matrix.get(iIndex).get(jPlus) ? 1 : 0;
        aliveNeighbors += matrix.get(iPlus).get(jPlus) ? 1 : 0;
        aliveNeighbors += matrix.get(iPlus).get(jIndex) ? 1 : 0;
        aliveNeighbors += matrix.get(iPlus).get(jMinus) ? 1 : 0;
        aliveNeighbors += matrix.get(iIndex).get(jMinus) ? 1 : 0;
        aliveNeighbors += matrix.get(iMinus).get(jMinus) ? 1 : 0;
        if (cellState) { // cell is alive
            if (aliveNeighbors < 2) { // cell dies of boredom
                return false;
            } else if (aliveNeighbors > 3) { // cell dies of overpopulation
                return false;
            } else { // cell survives
                return true;
            }
        } else { // cell is dead
            if (aliveNeighbors == 3) { // cell is reborn
                return true;
            } else { // cell remains dead
                return false;
            }
        }
    }
}
