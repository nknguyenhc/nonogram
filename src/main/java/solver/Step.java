package solver;

public class Step {
    public static class ConstraintInfo {
        public boolean isRow;
        public int index;

        public ConstraintInfo(boolean isRow, int index) {
            this.isRow = isRow;
            this.index = index;
        }
    }

    public ConstraintInfo constraintInfo;
    public int[][] board;

    public Step(int[][] board) {
        this.constraintInfo = null;
        this.board = board;
    }

    public Step(int[][] board, boolean isRow, int index) {
        this.constraintInfo = new ConstraintInfo(isRow, index);
        this.board = board;
    }
}
