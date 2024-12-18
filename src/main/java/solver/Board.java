package solver;

import solver.exceptions.InvalidConstraintException;

import java.util.function.Consumer;

/**
 * Notes on the rows and cols:
 * {@code 0} means not decided.
 * {@code 1} means not chosen (crossed).
 * {@code 2} means chosen.
 */
public class Board {
    private final int[][] rows;
    private final int[][] cols;
    private final Constraint[] rowConstraints;
    private final Constraint[] colConstraints;

    /**
     * Instantiates a new puzzle.
     * @param cells An array represents the crosses already on the map,
     *              with {@code true} meaning a cross in the corresponding cell.
     * @param rowConstraints The list of constraints on the rows,
     *                       each element is an array corresponding to one row.
     * @param colConstraints The list of constraints on the columns,
     *                       each element is an array corresponding to one column.
     */
    public Board(boolean[][] cells, int[][] rowConstraints, int[][] colConstraints)
            throws InvalidConstraintException {
        int rowCount = cells.length;
        int colCount = cells[0].length;
        this.rows = this.toRows(cells);
        this.cols = this.toCols(cells);

        assert rowConstraints.length == rowCount;
        assert colConstraints.length == colCount;

        this.rowConstraints = new Constraint[rowCount];
        for (int i = 0; i < rowCount; i++) {
            this.rowConstraints[i] = new Constraint(rowConstraints[i], this.rows[i]);
        }

        this.colConstraints = new Constraint[colCount];
        for (int i = 0; i < colCount; i++) {
            this.colConstraints[i] = new Constraint(colConstraints[i], this.cols[i]);
        }
    }

    private int[][] toRows(boolean[][] cells) {
        int[][] rows = new int[cells.length][cells[0].length];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                rows[i][j] = cells[i][j] ? 1 : 0;
            }
        }
        return rows;
    }

    private int[][] toCols(boolean[][] cells) {
        int[][] cols = new int[cells[0].length][cells.length];
        for (int i = 0; i < cells[0].length; i++) {
            for (int j = 0; j < cells.length; j++) {
                cols[i][j] = cells[j][i] ? 1 : 0;
            }
        }
        return cols;
    }

    /**
     * Finds a solution
     * @return An array with {@code true} meaning the cell is filled,
     *         and {@code false} meaning the cell is not filled.
     */
    public boolean[][] solve() {
        boolean isSolved = this.solve(this.rowConstraints.length + this.colConstraints.length, null);
        return this.toSolutionBoard(isSolved);
    }

    public boolean[][] solve(Consumer<Step> consumer) {
        boolean isSolved = this.solve(this.rowConstraints.length + this.colConstraints.length, consumer);
        return this.toSolutionBoard(isSolved);
    }

    private boolean solve(int unsolvedCount, Consumer<Step> consumer) {
        if (unsolvedCount == 0) {
            return true;
        }

        int bestRowConstraintIndex = this.findBestRowConstraint();
        int bestColConstraintIndex = this.findBestColConstraint();
        assert bestRowConstraintIndex != -1 || bestColConstraintIndex != -1;

        if (bestRowConstraintIndex == -1) {
            return this.resolveCol(unsolvedCount, bestColConstraintIndex, consumer);
        } else if (bestColConstraintIndex == -1) {
            return this.resolveRow(unsolvedCount, bestRowConstraintIndex, consumer);
        } else if (this.rowConstraints[bestRowConstraintIndex].possibilityCount() <
                this.colConstraints[bestColConstraintIndex].possibilityCount()) {
            return this.resolveCol(unsolvedCount, bestColConstraintIndex, consumer);
        } else {
            return this.resolveRow(unsolvedCount, bestRowConstraintIndex, consumer);
        }
    }

    private int[][] cloneBoard() {
        int[][] board = new int[this.rows.length][this.rows[0].length];
        for (int i = 0; i < this.rows.length; i++) {
            for (int j = 0; j < this.rows[0].length; j++) {
                board[i][j] = this.rows[i][j];
            }
        }
        return board;
    }

    private Step getStep(boolean isRow, int index) {
        return new Step(this.cloneBoard(), isRow, index);
    }

    private int findBestRowConstraint() {
        int index = -1;
        for (int i = 0; i < this.rowConstraints.length; i++) {
            if (this.rowConstraints[i].isResolved()) {
                continue;
            }
            if (index == -1 ||
                    this.rowConstraints[i].possibilityCount() < this.rowConstraints[index].possibilityCount()) {
                index = i;
            }
        }
        return index;
    }

    private int findBestColConstraint() {
        int index = -1;
        for (int i = 0; i < this.colConstraints.length; i++) {
            if (this.colConstraints[i].isResolved()) {
                continue;
            }
            if (index == -1 ||
                    this.colConstraints[i].possibilityCount() < this.colConstraints[index].possibilityCount()) {
                index = i;
            }
        }
        return index;
    }

    private boolean resolveCol(int unsolvedCount, int colIndex, Consumer<Step> consumer) {
        int[] originalCol = this.cols[colIndex];
        this.colConstraints[colIndex].resolve();
        for (int[] colNumbers: this.colConstraints[colIndex].getPossibilities()) {
            if (consumer != null) {
                consumer.accept(this.getStep(false, colIndex));
            }
            boolean isUpdated = this.updateCol(colIndex, colNumbers);
            if (!isUpdated) {
                continue;
            }
            if (consumer != null) {
                consumer.accept(this.getStep(false, colIndex));
            }
            boolean isSolved = this.solve(unsolvedCount - 1, consumer);
            if (isSolved) {
                return true;
            }
            this.undoRowConstraints();
        }
        this.undoCol(colIndex, originalCol);
        this.colConstraints[colIndex].unresolve();
        return false;
    }

    private boolean updateCol(int colIndex, int[] colNumbers) {
        this.cols[colIndex] = colNumbers;
        boolean areAllConstraintsUpdated = true;
        int i;
        for (i = 0; i < this.rows.length; i++) {
            this.rows[i][colIndex] = colNumbers[i];
            this.rowConstraints[i].update(this.rows[i]);
            if (this.rowConstraints[i].possibilityCount() == 0) {
                areAllConstraintsUpdated = false;
                break;
            }
        }
        if (areAllConstraintsUpdated) {
            return true;
        }

        for (int j = 0; j <= i; j++) {
            this.rowConstraints[j].undo();
        }
        return false;
    }

    private void undoCol(int colIndex, int[] originalCol) {
        this.cols[colIndex] = originalCol;
        for (int i = 0; i < this.rows.length; i++) {
            this.rows[i][colIndex] = originalCol[i];
        }
    }

    private void undoRowConstraints() {
        for (Constraint constraint: this.rowConstraints) {
            constraint.undo();
            assert constraint.possibilityCount() > 0;
        }
    }

    private boolean resolveRow(int unresolvedCount, int rowIndex, Consumer<Step> consumer) {
        int[] originalRow = this.rows[rowIndex];
        this.rowConstraints[rowIndex].resolve();
        for (int[] rowNumbers: this.rowConstraints[rowIndex].getPossibilities()) {
            if (consumer != null) {
                consumer.accept(this.getStep(true, rowIndex));
            }
            boolean isUpdated = this.updateRow(rowIndex, rowNumbers);
            if (!isUpdated) {
                continue;
            }
            if (consumer != null) {
                consumer.accept(this.getStep(true, rowIndex));
            }
            boolean isSolved = this.solve(unresolvedCount - 1, consumer);
            if (isSolved) {
                return true;
            }
            this.undoColConstraints();
        }
        this.undoRow(rowIndex, originalRow);
        this.rowConstraints[rowIndex].unresolve();
        return false;
    }

    private boolean updateRow(int rowIndex, int[] rowNumbers) {
        this.rows[rowIndex] = rowNumbers;
        boolean areAllConstraintsUpdated = true;
        int i;
        for (i = 0; i < this.cols.length; i++) {
            this.cols[i][rowIndex] = rowNumbers[i];
            this.colConstraints[i].update(this.cols[i]);
            if (this.colConstraints[i].possibilityCount() == 0) {
                areAllConstraintsUpdated = false;
                break;
            }
        }
        if (areAllConstraintsUpdated) {
            return true;
        }

        for (int j = 0; j <= i; j++) {
            this.colConstraints[j].undo();
        }
        return false;
    }

    private void undoRow(int rowIndex, int[] originalRow) {
        this.rows[rowIndex] = originalRow;
        for (int i = 0; i < this.cols.length; i++) {
            this.cols[i][rowIndex] = originalRow[i];
        }
    }

    private void undoColConstraints() {
        for (Constraint constraint: this.colConstraints) {
            constraint.undo();
            assert constraint.possibilityCount() > 0;
        }
    }

    private boolean[][] toSolutionBoard(boolean isSolved) {
        if (!isSolved) {
            return null;
        }
        boolean[][] result = new boolean[this.rows.length][this.rows[0].length];
        for (int i = 0; i < this.rows.length; i++) {
            for (int j = 0; j < this.rows[0].length; j++) {
                int item = this.rows[i][j];
                assert item == 1 || item == 2;
                result[i][j] = item == 2;
            }
        }
        return result;
    }
}
