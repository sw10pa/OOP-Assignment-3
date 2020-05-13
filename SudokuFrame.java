import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

public class SudokuFrame extends JFrame {

	private final JTextArea puzzleText;
	private final JTextArea solutionText;

	private final JButton checkButton;
	private final JCheckBox autoCheckBox;

	public SudokuFrame() {
		super("Sudoku Solver");

		setLayout(new BorderLayout(4, 4));

		puzzleText = new JTextArea(15, 20);
		puzzleText.setBorder(new TitledBorder("Puzzle"));
		add(puzzleText, BorderLayout.CENTER);

		solutionText = new JTextArea(15, 20);
		solutionText.setBorder(new TitledBorder("Solution"));
		add(solutionText, BorderLayout.EAST);

		Box controlsBox = new Box(BoxLayout.X_AXIS);
		add(controlsBox, BorderLayout.SOUTH);

		checkButton = new JButton("Check");
		controlsBox.add(checkButton);

		autoCheckBox = new JCheckBox("Auto Check", true);
		controlsBox.add(autoCheckBox);

		pack();
		addActionListeners();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void addActionListeners() {
		puzzleText.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				if (autoCheckBox.isSelected()) {
					redrawSolutions();
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				if (autoCheckBox.isSelected()) {
					redrawSolutions();
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				if (autoCheckBox.isSelected()) {
					redrawSolutions();
				}
			}
		});

		checkButton.addActionListener(e -> redrawSolutions());
	}

	private void redrawSolutions() {
		String solution = "";

		try {
			Sudoku sudoku = new Sudoku(Sudoku.textToGrid(puzzleText.getText()));
			int solutions = sudoku.solve();
			if (solutions > 0) {
				solution = sudoku.getSolutionText();
				solution += "Solutions: " + solutions + "\n";
				solution += "Elapsed: " + sudoku.getElapsed() + " ms" + "\n";
			}
		} catch (Exception exception) {
			solution = "Parsing problem";
		}

		solutionText.setText(solution);
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception ignored) { }
		
		SudokuFrame sudokuFrame = new SudokuFrame();
		sudokuFrame.setVisible(true);
	}

}
