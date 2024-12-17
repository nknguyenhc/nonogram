package cli;

import solver.Board;
import solver.exceptions.InvalidConstraintException;

import java.util.Scanner;

public class Cli {
    private final Scanner scanner = new Scanner(System.in);

    public void run() {
        int rowCount = this.getNumber("Number of rows: ");
        int colCount = this.getNumber("Number of columns: ");
        boolean[][] board = new boolean[rowCount][];
        for (int i = 0; i < rowCount; i++) {
            board[i] = this.getRow(i, colCount);
        }

        int[][] rowConstraints = new int[rowCount][];
        for (int i = 0; i < rowCount; i++) {
            System.out.printf("Constraint on row %d: ", i + 1);
            rowConstraints[i] = this.getConstraint();
        }

        int[][] colConstraints = new int[colCount][];
        for (int i = 0; i < colCount; i++) {
            System.out.printf("Constraint on column %d: ", i + 1);
            colConstraints[i] = this.getConstraint();
        }

        Board solver;
        try {
            solver = new Board(board, rowConstraints, colConstraints);
        } catch (InvalidConstraintException e) {
            this.printSolution(null);
            return;
        }
        boolean[][] solution = solver.solve();
        this.printSolution(solution);
    }

    private int getNumber(String prompt) {
        System.out.print(prompt);
        String response = this.scanner.nextLine();
        while (true) {
            try {
                return Integer.parseInt(response);
            } catch (NumberFormatException e) {
                System.out.println("Not a number!");
                System.out.print(prompt);
                response = this.scanner.nextLine();
            }
        }
    }

    private boolean[] getRow(int i, int colCount) {
        System.out.printf("Crosses on line %d, in binary format: ", i + 1);
        String response = this.scanner.nextLine();
        while (true) {
            boolean[] row = this.parseRow(response, colCount);
            if (row != null) {
                return row;
            }
            System.out.print("Invalid! Try again: ");
            response = this.scanner.nextLine();
        }
    }

    private boolean[] parseRow(String response, int colCount) {
        if (response.length() != colCount) {
            return null;
        }

        boolean[] result = new boolean[colCount];
        for (int i = 0; i < colCount; i++) {
            switch (response.charAt(i)) {
                case '0':
                    result[i] = false;
                    break;
                case '1':
                    result[i] = true;
                    break;
                default:
                    return null;
            }
        }
        return result;
    }

    private int[] getConstraint() {
        String response = this.scanner.nextLine();
        int[] constraint = this.parseConstraint(response);
        while (constraint == null) {
            System.out.print("Invalid, please try again: ");
            response = this.scanner.nextLine();
            constraint = this.parseConstraint(response);
        }
        return constraint;
    }

    private int[] parseConstraint(String response) {
        if (response.length() == 0) {
            return null;
        }
        String[] parts = response.split(" ");
        int[] numbers = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            try {
                numbers[i] = Integer.parseInt(parts[i]);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return numbers;
    }

    private void printSolution(boolean[][] solution) {
        if (solution == null) {
            System.out.println("No solution");
            return;
        }
        for (boolean[] booleans : solution) {
            for (int j = 0; j < solution[0].length; j++) {
                System.out.print(booleans[j] ? 'X' : '.');
                System.out.print(' ');
            }
            System.out.println();
        }
    }
}
