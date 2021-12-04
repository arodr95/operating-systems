// Name:        Rodrigue, Andrew
// Email:       arodr95@lsu.edu
// LSU ID:      891018227
// Project:     PA-1 (Multithreading)
// Instructor:  Feng Chen
// Class:       cs4103-fa21
// Login ID:    cs410393

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;

public class SudokuValidator {
    private static final int ROW_LENGTH = 9, COL_LENGTH = 9;
    private static final int[][] puzzle = new int[ROW_LENGTH][COL_LENGTH];
    private static final int THREAD_COUNT = 27;
    private static final boolean[] valid = new boolean[THREAD_COUNT];
    private static final ReentrantLock lock = new ReentrantLock();

    private static final class RowValidator implements Runnable {
        private final int row;
        private final int threadIndex;

        private RowValidator(int row, int threadIndex) {
            this.row = row;
            this.threadIndex = threadIndex;
        }

        @Override
        public void run() {
            valid[threadIndex] = true;
            boolean[] seen = new boolean[10];
            for (int column = 0; column < COL_LENGTH; column++) {
                int num = puzzle[row][column];
                if (num < 1 || num > 9 || seen[num]) {
                    valid[threadIndex] = false;
                    break;
                }
                seen[num] = true;
            }

            String threadInfo = Thread.currentThread().getName() + ",";
            String typeAndPosition = "Row " + (row + 1) + ",";
            String validity = valid[threadIndex] ? "Valid" : "Invalid";

            lock.lock();
            try {
                displayResults(threadInfo, typeAndPosition, validity);
            } finally {
                lock.unlock();
            }
        }

        public int row() {
            return row;
        }

        public int threadIndex() {
            return threadIndex;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (RowValidator) obj;
            return this.row == that.row &&
                    this.threadIndex == that.threadIndex;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, threadIndex);
        }

        @Override
        public String toString() {
            return "RowValidator[" +
                    "row=" + row + ", " +
                    "threadIndex=" + threadIndex + ']';
        }

    }

    private static final class ColumnValidator implements Runnable {
        private final int column;
        private final int threadIndex;

        private ColumnValidator(int column, int threadIndex) {
            this.column = column;
            this.threadIndex = threadIndex;
        }

        @Override
        public void run() {
            valid[threadIndex] = true;
            boolean[] seen = new boolean[10];
            for (int row = 0; row < ROW_LENGTH; row++) {
                int num = puzzle[row][column];
                if (num < 1 || num > 9 || seen[num]) {
                    valid[threadIndex] = false;
                    break;
                }
                seen[num] = true;
            }

            String threadInfo = Thread.currentThread().getName() + ",";
            String typeAndPosition = "Column " + (column + 1) + ",";
            String validity = valid[threadIndex] ? "Valid" : "Invalid";

            lock.lock();
            try {
                displayResults(threadInfo, typeAndPosition, validity);
            } finally {
                lock.unlock();
            }
        }

        public int column() {
            return column;
        }

        public int threadIndex() {
            return threadIndex;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (ColumnValidator) obj;
            return this.column == that.column &&
                    this.threadIndex == that.threadIndex;
        }

        @Override
        public int hashCode() {
            return Objects.hash(column, threadIndex);
        }

        @Override
        public String toString() {
            return "ColumnValidator[" +
                    "column=" + column + ", " +
                    "threadIndex=" + threadIndex + ']';
        }

    }

    private static final class GridValidator implements Runnable {
        private final int row;
        private final int column;
        private final int threadIndex;

        private GridValidator(int row, int column, int threadIndex) {
            this.row = row;
            this.column = column;
            this.threadIndex = threadIndex;
        }

        @Override
        public synchronized void run() {
            valid[threadIndex] = true;
            boolean[] seen = new boolean[10];
            loop:
            for (int i = row; i < row + 3; i++)
                for (int j = column; j < column + 3; j++) {
                    int num = puzzle[i][j];
                    if (num < 1 || num > 9 || seen[num]) {
                        valid[threadIndex] = false;
                        break loop;
                    }
                    seen[num] = true;
                }

            String threadInfo = Thread.currentThread().getName() + ",";
            String typeAndPosition = "Subgrid R" + (row + 1) + (row + 2) + (row + 3) + "-C" + (column + 1) + (column + 2) + (column + 3);
            String validity = (valid[threadIndex] ? "Valid" : "Invalid");

            lock.lock();
            try {
                displayResults(threadInfo, typeAndPosition, validity);
            } finally {
                lock.unlock();
            }
        }

        public int row() {
            return row;
        }

        public int column() {
            return column;
        }

        public int threadIndex() {
            return threadIndex;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (GridValidator) obj;
            return this.row == that.row &&
                    this.column == that.column &&
                    this.threadIndex == that.threadIndex;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, column, threadIndex);
        }

        @Override
        public String toString() {
            return "GridValidator[" +
                    "row=" + row + ", " +
                    "column=" + column + ", " +
                    "threadIndex=" + threadIndex + ']';
        }

    }

    private static void displayResults(String threadInfo, String typeAndPosition, String validity) {
        System.out.printf("%-14s%-21s%-7s\n", threadInfo, typeAndPosition, validity);
    }

    public static void main(String[] args) {
        String file = args[0];

        // open and scan input file
        try {
            System.out.println("Opening input file...");
            Scanner scanner = new Scanner(new File(file));
            System.out.println("Scanning input file...");
            for (int i = 0; i < ROW_LENGTH; i++)
                for (int j = 0; j < COL_LENGTH; j++)
                    puzzle[i][j] = scanner.nextInt();
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        // display provided sudoku solution
        System.out.println("Displaying scanned Sudoku solution...");
        System.out.println("-------------------------");
        for (int i = 0; i < ROW_LENGTH; i++) {
            for (int j = 0; j < COL_LENGTH; j++)
                System.out.printf("%s  ", puzzle[i][j]);
            System.out.println();
        }
        System.out.println("-------------------------");

        // MULTITHREADING
        Thread[] threads = new Thread[THREAD_COUNT];
        int threadIndex = 0;

        // row validator threads
        int row = 0;
        while (row < 9)
            threads[threadIndex] = new Thread(new RowValidator(row++, threadIndex++));

        // column validator threads
        int column = 0;
        while (column < 9)
            threads[threadIndex] = new Thread(new ColumnValidator(column++, threadIndex++));

        // grid validator threads
        for (row = 0; row < ROW_LENGTH; row += 3)
            for (column = 0; column < COL_LENGTH; column += 3)
                threads[threadIndex] = new Thread(new GridValidator(row, column, threadIndex++));

        // start threads
        for (Thread thread : threads)
            thread.start();
    }
}