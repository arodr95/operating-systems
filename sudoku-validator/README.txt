Name:        Rodrigue, Andrew
Project:     PA-1 (Multithreading)

Description:
    This program uses 27 threads to run a sudoku validator program. There are 9 threads to validate each row,
    9 threads to validate each column, and 9 threads to validate each 3x3 grid in the puzzle. There is a possible race
    condition when printing the result of each thread to the console. To solve this race condition, I created a Java
    ReentrantLock object which uses mutual exclusion to ensure that only one thread is displaying its results at a time.
    The reentrant lock disables the thread until the lock becomes available again. The lock is available when the lock's
    hold count is 0, and it is immediately set to 1 upon lock acquisition.

To run code:
    1. Verify JDK is installed
        java -version

    2. Navigate to prog1
        cd ~/prog1

    3. Compile SudokuValidator.java
        javac SudokuValidator.java

    4. Run SudokuValidator.class with argument of .txt file containing solution to verify
        For example, to run the SudokuValidator on the sudoku.txt sample input provided in the project description, run:
            java SudokuValidator ./sudoku.txt

Expected input:
    - some txt file containing a 9x9 sudoku puzzle solution to verify
    - for example, the provided sudoku.txt is located below

    // sample input of sudoku.txt
    6 2 4 5 3 9 1 8 7
    5 1 9 7 2 8 6 3 4
    8 3 7 6 1 4 2 9 5
    1 4 3 8 6 5 7 2 9
    9 5 8 2 4 7 3 6 1
    7 6 2 3 9 1 4 5 8
    3 7 1 9 5 6 8 4 2
    4 9 6 1 8 2 5 7 3
    2 8 5 4 7 3 9 1 6
    //

Expected output:
    - logs of file actions (open, scanning)
    - sudoku solution from file formatted in a grid
    - thread results (see format below)
        Thread ID, Thread Type & Location, Validity Result

    // sample output from validating sudoku.txt
    Opening input file...
    Scanning input file...
    Displaying scanned Sudoku solution...
    -------------------------
    6  2  4  5  3  9  1  8  7
    5  1  9  7  2  8  6  3  4
    8  3  7  6  1  4  2  9  5
    1  4  3  8  6  5  7  2  9
    9  5  8  2  4  7  3  6  1
    7  6  2  3  9  1  4  5  8
    3  7  1  9  5  6  8  4  2
    4  9  6  1  8  2  5  7  3
    2  8  5  4  7  3  9  1  6
    -------------------------
    Thread-12,    Column 4,            Valid
    Thread-1,     Row 2,               Valid
    Thread-2,     Row 3,               Valid
    Thread-10,    Column 2,            Valid
    Thread-9,     Column 1,            Valid
    Thread-5,     Row 6,               Valid
    Thread-0,     Row 1,               Valid
    Thread-7,     Row 8,               Valid
    Thread-16,    Column 8,            Valid
    Thread-6,     Row 7,               Valid
    Thread-15,    Column 7,            Valid
    Thread-11,    Column 3,            Valid
    Thread-14,    Column 6,            Valid
    Thread-4,     Row 5,               Valid
    Thread-17,    Column 9,            Valid
    Thread-8,     Row 9,               Valid
    Thread-3,     Row 4,               Valid
    Thread-13,    Column 5,            Valid
    Thread-21,    Subgrid R456-C123    Valid
    Thread-20,    Subgrid R123-C789    Valid
    Thread-25,    Subgrid R789-C456    Valid
    Thread-26,    Subgrid R789-C789    Valid
    Thread-18,    Subgrid R123-C123    Valid
    Thread-19,    Subgrid R123-C456    Valid
    Thread-22,    Subgrid R456-C456    Valid
    Thread-23,    Subgrid R456-C789    Valid
    Thread-24,    Subgrid R789-C123    Valid
    //

Interpretation of output:
    - if any threads are invalid, the solution is invalid
    - if all threads are valid, the solution is valid