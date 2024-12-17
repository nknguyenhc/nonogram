package solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class Constraint {
    private static class PossibleCombination {
        public final int[] realization;
        public boolean isSatisfiable;

        public PossibleCombination(int[] numbers, int length) {
            this.realization = new int[length];
            int i = 0;
            boolean isChosen = false;
            for (int number: numbers) {
                for (int j = 0; j < number; j++) {
                    this.realization[i] = isChosen ? 2 : 1;
                    i++;
                }
                isChosen = !isChosen;
            }
        }

        public void updateSatisfiability(int[] cells) {
            this.isSatisfiable = this.checkSatisfiability(cells);
        }

        private boolean checkSatisfiability(int[] cells) {
            assert cells.length == this.realization.length;
            for (int i = 0; i < cells.length; i++) {
                if (cells[i] != 0 && cells[i] != this.realization[i]) {
                    return false;
                }
            }
            return true;
        }

        public void undo() {
            this.isSatisfiable = true;
        }

        @Override
        public String toString() {
            return Arrays.toString(this.realization);
        }
    }

    private final List<PossibleCombination> possibleCombinations;
    private int possibleCombinationCount;
    private final Stack<List<Integer>> removedCombinations = new Stack<>();
    private final int length;

    public Constraint(int[] numbers, int[] cells) {
        this.length = cells.length;
        int[] combination = new int[2 * numbers.length + 1];
        combination[0] = 0;
        int sum = 0;
        for (int i = 0; i < numbers.length; i++) {
            combination[2 * i + 1] = numbers[i];
            if (i == numbers.length - 1) {
                combination[2 * i + 2] = 0;
                sum += numbers[i];
            } else {
                combination[2 * i + 2] = 1;
                sum += numbers[i] + 1;
            }
        }

        this.possibleCombinations = new ArrayList<>();
        this.addPossibleCombination(combination, cells, cells.length - sum, 0);
        this.possibleCombinationCount = this.possibleCombinations.size();
    }

    private void addPossibleCombination(int[] combination, int[] cells, int difference, int index) {
        assert difference >= 0;
        assert index % 2 == 0;
        if (index == combination.length - 1) {
            combination[index] = difference;
            PossibleCombination possibleCombination = new PossibleCombination(combination.clone(), cells.length);
            possibleCombination.updateSatisfiability(cells);
            if (possibleCombination.isSatisfiable) {
                this.possibleCombinations.add(possibleCombination);
            }
            return;
        }

        for (int increment = 0; increment <= difference; increment++) {
            addPossibleCombination(combination, cells, difference - increment, index + 2);
            combination[index]++;
        }
        combination[index] -= (difference + 1);
    }

    public void update(int[] cells) {
        List<Integer> unsatisfiableCombinations = new ArrayList<>();
        for (int i = 0; i < this.possibleCombinations.size(); i++) {
            PossibleCombination combination = this.possibleCombinations.get(i);
            if (!combination.isSatisfiable) {
                continue;
            }
            combination.updateSatisfiability(cells);
            if (!combination.isSatisfiable) {
                unsatisfiableCombinations.add(i);
            }
        }
        this.removedCombinations.add(unsatisfiableCombinations);
        this.possibleCombinationCount -= unsatisfiableCombinations.size();
    }

    public void undo() {
        List<Integer> toAddBack = this.removedCombinations.pop();
        for (int i: toAddBack) {
            this.possibleCombinations.get(i).undo();
        }
        this.possibleCombinationCount += toAddBack.size();
    }

    public int possibilityCount() {
        return this.possibleCombinationCount;
    }

    public List<int[]> getPossibilities() {
        List<int[]> possibilities = new ArrayList<>();
        for (PossibleCombination combination: this.possibleCombinations) {
            if (!combination.isSatisfiable) {
                continue;
            }
            possibilities.add(combination.realization);
        }
        return possibilities;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Combinations: { ");
        for (PossibleCombination combination: this.possibleCombinations) {
            if (combination.isSatisfiable) {
                stringBuilder.append(combination);
                stringBuilder.append(' ');
            }
        }
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}
