import java.util.*;

public class Sudoku {

	public static final int[][] zeroesGrid = Sudoku.stringsToGrid(
			"0 0 0 0 0 0 0 0 0",
			"0 0 0 0 0 0 0 0 0",
			"0 0 0 0 0 0 0 0 0",
			"0 0 0 0 0 0 0 0 0",
			"0 0 0 0 0 0 0 0 0",
			"0 0 0 0 0 0 0 0 0",
			"0 0 0 0 0 0 0 0 0",
			"0 0 0 0 0 0 0 0 0",
			"0 0 0 0 0 0 0 0 0");

	public static final int[][] easyGrid = Sudoku.stringsToGrid(
			"1 6 4 0 0 0 0 0 2",
			"2 0 0 4 0 3 9 1 0",
			"0 0 5 0 8 0 4 0 7",
			"0 9 0 0 0 6 5 0 0",
			"5 0 0 1 0 2 0 0 8",
			"0 0 8 9 0 0 0 3 0",
			"8 0 9 0 4 0 2 0 0",
			"0 7 3 5 0 9 0 0 1",
			"4 0 0 0 0 0 6 7 9");

	public static final int[][] mediumGrid = Sudoku.stringsToGrid(
			"530070000",
			"600195000",
			"098000060",
			"800060003",
			"400803001",
			"700020006",
			"060000280",
			"000419005",
			"000080079");

	public static final int[][] hardGrid = Sudoku.stringsToGrid(
			"3 7 0 0 0 0 0 8 0",
			"0 0 1 0 9 3 0 0 0",
			"0 4 0 7 8 0 0 0 3",
			"0 9 3 8 0 0 0 1 2",
			"0 0 0 0 4 0 0 0 0",
			"5 2 0 0 0 6 7 9 0",
			"6 0 0 0 2 1 0 4 0",
			"0 0 0 5 3 0 9 0 0",
			"0 3 0 0 0 0 0 5 1");

	public static final int SIZE = 9;
	public static final int PART = 3;
	public static final int MAX_SOLUTIONS = 100;

	public static int[][] stringsToGrid(String... rows) {
		int[][] result = new int[rows.length][];
		for (int row = 0; row < rows.length; row++) {
			result[row] = stringToInts(rows[row]);
		}
		return result;
	}

	public static int[][] textToGrid(String text) {
		int[] nums = stringToInts(text);
		if (nums.length != SIZE * SIZE) {
			throw new RuntimeException("Needed 81 numbers, but got: " + nums.length);
		}
		
		int[][] result = new int[SIZE][SIZE];
		int count = 0;
		for (int row = 0; row < SIZE; row++) {
			for (int col = 0; col < SIZE; col++) {
				result[row][col] = nums[count];
				count++;
			}
		}
		return result;
	}

	public static int[] stringToInts(String string) {
		int[] a = new int[string.length()];
		int found = 0;
		for (int i = 0; i < string.length(); i++) {
			if (Character.isDigit(string.charAt(i))) {
				a[found] = Integer.parseInt(string.substring(i, i + 1));
				found++;
			}
		}
		int[] result = new int[found];
		System.arraycopy(a, 0, result, 0, found);
		return result;
	}

	public static String intsToString(int[][] ints) {
		StringBuilder result = new StringBuilder();

		for (int row = 0; row < SIZE; row++) {
			for (int col = 0; col < SIZE; col++) {
				result.append(ints[row][col]);
				result.append(" ");
			}
			result.append("\n");
		}

		return result.toString();
	}

	public static void main(String[] args) {
		debugSolve(zeroesGrid);
		debugSolve(easyGrid);
		debugSolve(mediumGrid);
		debugSolve(hardGrid);
	}

	private static void debugSolve(int[][] grid) {
		Sudoku sudoku = new Sudoku(grid);
		int solutions = sudoku.solve();

		System.out.println("Puzzle: \n" + sudoku.toString());
		System.out.println("Solutions: " + solutions);
		System.out.println("Elapsed: " + sudoku.getElapsed() + " ms\n");
		System.out.println("Solution: \n" + sudoku.getSolutionText());
		System.out.println("*****************\n");
	}

	private final int[][] grid;
	private final int[][] solution;
	private ArrayList<Spot> spots;

	private long elapsedTime;
	private int solutionsNumber;

	public Sudoku(int[][] ints) {
		grid = ints;
		createSpots();
		solution = new int[SIZE][SIZE];

		solutionsNumber = 0;
	}

	private void createSpots() {
		spots = new ArrayList<>();

		for (int row = 0; row < SIZE; row++) {
			for (int col = 0; col < SIZE; col++) {
				if (grid[row][col] == 0) {
					spots.add(new Spot(row, col));
				}
			}
		}

		spots.sort(Comparator.comparingInt((Spot spot) -> spot.getValidValues().size()));
	}

	public int solve() {
		long startTime = System.currentTimeMillis();

		solveRecursively(0);

		elapsedTime = System.currentTimeMillis() - startTime;

		return solutionsNumber;
	}

	private void solveRecursively(int curIndex) {
		if (solutionsNumber >= MAX_SOLUTIONS) return;

		if (curIndex == spots.size()) {
			solutionsNumber++;
			saveSolution();
			return;
		}

		Spot curSpot = spots.get(curIndex);
		HashSet<Integer> validValues = curSpot.getValidValues();

		for (int curValue : validValues) {
			curSpot.setValue(curValue);
			solveRecursively(curIndex + 1);
			curSpot.setValue(0);
		}
	}

	private void saveSolution() {
		if (solutionsNumber == 1) {
			for (int i = 0; i < SIZE; i++) {
				System.arraycopy(grid[i], 0, solution[i], 0, SIZE);
			}
		}
	}
	
	public String getSolutionText() {
		return intsToString(solution);
	}
	
	public long getElapsed() {
		return elapsedTime;
	}

	@Override
	public String toString() {
		return intsToString(grid);
	}

	public class Spot {

		private final int row;
		private final int col;

		public Spot(int row, int col) {
			this.row = row;
			this.col = col;
		}

		public void setValue(int value) {
			grid[row][col] = value;
		}

		public HashSet<Integer> getValidValues() {
			HashSet<Integer> validValues = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));

			for (int i = 0; i < SIZE; i++) {
				validValues.remove(grid[row][i]);
				validValues.remove(grid[i][col]);
				validValues.remove(grid[row - row % PART + i / PART][col - col % PART + i % PART]);
			}

			return validValues;
		}

	}

}
