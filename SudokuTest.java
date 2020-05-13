import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.util.*;

public class SudokuTest {

    @Test
    public void mainTest() {
        Sudoku.main(new String[0]);
    }

    @Test
    public void stringsToGridTest() {
        int[][] actual;
        int[][] expected;

        actual = Sudoku.stringsToGrid("");
        expected = new int[][] {{}};
        assertTrue(Arrays.deepEquals(expected, actual));

        actual = Sudoku.stringsToGrid("0");
        expected = new int[][] {{0}};
        assertTrue(Arrays.deepEquals(expected, actual));

        actual = Sudoku.stringsToGrid("", "");
        expected = new int[][] {{}, {}};
        assertTrue(Arrays.deepEquals(expected, actual));

        actual = Sudoku.stringsToGrid("0", "0");
        expected = new int[][] {{0}, {0}};
        assertTrue(Arrays.deepEquals(expected, actual));
    }

    @Test
    public void textToGridTest() {
        int[][] actual;
        int[][] expected;

        assertThrows(RuntimeException.class, () -> Sudoku.textToGrid(""));

        actual = Sudoku.textToGrid("123456789" +
                                   "123456789" +
                                   "123456789" +
                                   "123456789" +
                                   "123456789" +
                                   "123456789" +
                                   "123456789" +
                                   "123456789" +
                                   "123456789");

        expected = new int[][] {{1, 2, 3, 4, 5, 6, 7, 8, 9},
                                {1, 2, 3, 4, 5, 6, 7, 8, 9},
                                {1, 2, 3, 4, 5, 6, 7, 8, 9},
                                {1, 2, 3, 4, 5, 6, 7, 8, 9},
                                {1, 2, 3, 4, 5, 6, 7, 8, 9},
                                {1, 2, 3, 4, 5, 6, 7, 8, 9},
                                {1, 2, 3, 4, 5, 6, 7, 8, 9},
                                {1, 2, 3, 4, 5, 6, 7, 8, 9},
                                {1, 2, 3, 4, 5, 6, 7, 8, 9}};

        assertTrue(Arrays.deepEquals(expected, actual));
    }

    @Test
    public void stringToIntsTest() {
        int[] actual;
        int[] expected;

        actual = Sudoku.stringToInts("");
        expected = new int[] {};
        assertArrayEquals(expected, actual);

        actual = Sudoku.stringToInts("0");
        expected = new int[] {0};
        assertArrayEquals(expected, actual);

        actual = Sudoku.stringToInts("123456789");
        expected = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void intsToStringTest() {
        String actual;
        String expected;

        actual = Sudoku.intsToString(new int[][] {{1, 2, 3, 4, 5, 6, 7, 8, 9},
                                                  {1, 2, 3, 4, 5, 6, 7, 8, 9},
                                                  {1, 2, 3, 4, 5, 6, 7, 8, 9},
                                                  {1, 2, 3, 4, 5, 6, 7, 8, 9},
                                                  {1, 2, 3, 4, 5, 6, 7, 8, 9},
                                                  {1, 2, 3, 4, 5, 6, 7, 8, 9},
                                                  {1, 2, 3, 4, 5, 6, 7, 8, 9},
                                                  {1, 2, 3, 4, 5, 6, 7, 8, 9},
                                                  {1, 2, 3, 4, 5, 6, 7, 8, 9}});

        expected = "1 2 3 4 5 6 7 8 9 \n" +
                   "1 2 3 4 5 6 7 8 9 \n" +
                   "1 2 3 4 5 6 7 8 9 \n" +
                   "1 2 3 4 5 6 7 8 9 \n" +
                   "1 2 3 4 5 6 7 8 9 \n" +
                   "1 2 3 4 5 6 7 8 9 \n" +
                   "1 2 3 4 5 6 7 8 9 \n" +
                   "1 2 3 4 5 6 7 8 9 \n" +
                   "1 2 3 4 5 6 7 8 9 \n";

        assertEquals(expected, actual);
    }

    @Test
    public void integrationTest() {
        Sudoku sudoku;
        int solutions;

        sudoku = new Sudoku(Sudoku.zeroesGrid);
        solutions = sudoku.solve();
        assertEquals(100, solutions);

        sudoku = new Sudoku(Sudoku.easyGrid);
        solutions = sudoku.solve();
        assertEquals(1, solutions);

        sudoku = new Sudoku(Sudoku.mediumGrid);
        solutions = sudoku.solve();
        assertEquals(1, solutions);

        sudoku = new Sudoku(Sudoku.hardGrid);
        solutions = sudoku.solve();
        assertEquals(1, solutions);
    }


}
