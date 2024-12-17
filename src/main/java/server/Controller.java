package server;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import solver.Board;
import solver.exceptions.InvalidConstraintException;

@RestController
public class Controller {
    private static class PuzzleInput {
        public boolean[][] board;
        public int[][] rowConstraints;
        public int[][] colConstraints;
    }

    private static class PuzzleOutput {
        public boolean success;
        public boolean[][] solution;
        public String error;

        public PuzzleOutput(boolean[][] solution) {
            this.success = true;
            this.solution = solution;
        }

        public  PuzzleOutput(String error) {
            this.success = false;
            this.error = error;
        }
    }

    public static class PuzzleVerificationException extends Exception {
        public PuzzleVerificationException(String message) {
            super(message);
        }
    }

    @PostMapping("/solve")
    PuzzleOutput solve(@RequestBody PuzzleInput input)
            throws PuzzleVerificationException, InvalidConstraintException {
        this.verifyPuzzleInput(input);
        Board board = new Board(input.board, input.rowConstraints, input.colConstraints);
        boolean[][] solution = board.solve();
        if (solution == null) {
            return new PuzzleOutput("Unsolvable puzzle");
        } else {
            return new PuzzleOutput(solution);
        }
    }

    private void verifyPuzzleInput(PuzzleInput input) throws PuzzleVerificationException {
        if (input.board == null) {
            throw new PuzzleVerificationException("Board cannot be blank");
        }
        if (input.board.length == 0) {
            throw new PuzzleVerificationException("Board must have at least one row");
        }
        if (input.board[0].length == 0) {
            throw new PuzzleVerificationException("Board must have at least one column");
        }
        int rowCount = input.board.length;
        int colCount = input.board[0].length;
        for (boolean[] row: input.board) {
            if (row.length != colCount) {
                throw new PuzzleVerificationException("Board has inconsistent number of columns");
            }
        }

        if (input.rowConstraints == null) {
            throw new PuzzleVerificationException("Row constraints cannot be blank");
        }
        if (input.rowConstraints.length != rowCount) {
            throw new PuzzleVerificationException("Must have the same number of row constraints as number of rows");
        }

        if (input.colConstraints == null) {
            throw new PuzzleVerificationException("Col constraints cannot be blank");
        }
        if (input.colConstraints.length != colCount) {
            throw new PuzzleVerificationException("Must have the same number of col constraints as number of cols");
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PuzzleVerificationException.class)
    public PuzzleOutput puzzleVerificationExceptionHandler(Exception e) {
        return new PuzzleOutput(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidConstraintException.class)
    public PuzzleOutput invalidConstraintExceptionHandler() {
        return new PuzzleOutput("At least one of the constraints is invalid");
    }
}
