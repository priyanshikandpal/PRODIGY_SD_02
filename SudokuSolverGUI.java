import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SudokuSolverGUI {
    private static final int GRID_SIZE = 9;
    private static final int SUBGRID_SIZE = 3;
    private static final int CELL_SIZE = 60;
    private static final int CANVAS_WIDTH = CELL_SIZE * GRID_SIZE;
    private static final int CANVAS_HEIGHT = CELL_SIZE * GRID_SIZE;

    private static JTextField[][] cells = new JTextField[GRID_SIZE][GRID_SIZE];
    private static int[][] board = {
        {7, 0, 2, 0, 5, 0, 6, 0, 0},
        {0, 0, 0, 0, 0, 3, 0, 0, 0},
        {1, 0, 0, 0, 0, 9, 5, 0, 0},
        {8, 0, 0, 0, 0, 0, 0, 9, 0},
        {0, 4, 3, 0, 0, 0, 7, 5, 0},
        {0, 9, 0, 0, 0, 0, 0, 0, 8},
        {0, 0, 9, 7, 0, 0, 0, 0, 5},
        {0, 0, 0, 2, 0, 0, 0, 0, 0},
        {0, 0, 7, 0, 4, 0, 2, 0, 3}
    };

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Sudoku Solver");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(CANVAS_WIDTH, CANVAS_HEIGHT + 40);
            frame.setResizable(false);

            Container cp = frame.getContentPane();
            cp.setLayout(new BorderLayout());

            JPanel panel = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE));
            cp.add(panel, BorderLayout.CENTER);

            for (int row = 0; row < GRID_SIZE; row++) {
                for (int col = 0; col < GRID_SIZE; col++) {
                    cells[row][col] = new JTextField();
                    cells[row][col].setHorizontalAlignment(JTextField.CENTER);
                    if (board[row][col] != 0) {
                        cells[row][col].setText(String.valueOf(board[row][col]));
                        cells[row][col].setEditable(false);
                        cells[row][col].setBackground(Color.LIGHT_GRAY);
                    }
                    panel.add(cells[row][col]);
                }
            }

            JPanel controlPanel = new JPanel();
            cp.add(controlPanel, BorderLayout.SOUTH);

            JButton solveButton = new JButton("Solve");
            solveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    solveAndDisplay();
                }
            });
            controlPanel.add(solveButton);

            JButton resetButton = new JButton("Reset");
            resetButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    resetBoard();
                }
            });
            controlPanel.add(resetButton);

            frame.setVisible(true);
        });
    }

    private static void solveAndDisplay() {
        if (solveBoard(board)) {
            for (int row = 0; row < GRID_SIZE; row++) {
                for (int col = 0; col < GRID_SIZE; col++) {
                    cells[row][col].setText(String.valueOf(board[row][col]));
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Unsolvable Sudoku puzzle.");
        }
    }

    private static void resetBoard() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (board[row][col] != 0) {
                    cells[row][col].setText(String.valueOf(board[row][col]));
                } else {
                    cells[row][col].setText("");
                }
            }
        }
    }

    private static boolean solveBoard(int[][] board) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= GRID_SIZE; num++) {
                        if (isValidPlacement(board, num, row, col)) {
                            board[row][col] = num;

                            if (solveBoard(board)) {
                                return true;
                            } else {
                                board[row][col] = 0;
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isValidPlacement(int[][] board, int num, int row, int col) {
        return !isInRow(board, num, row) && !isInCol(board, num, col) && !isInBox(board, num, row, col);
    }

    private static boolean isInRow(int[][] board, int num, int row) {
        for (int col = 0; col < GRID_SIZE; col++) {
            if (board[row][col] == num) {
                return true;
            }
        }
        return false;
    }

    private static boolean isInCol(int[][] board, int num, int col) {
        for (int row = 0; row < GRID_SIZE; row++) {
            if (board[row][col] == num) {
                return true;
            }
        }
        return false;
    }

    private static boolean isInBox(int[][] board, int num, int row, int col) {
        int localBoxRow = row - row % SUBGRID_SIZE;
        int localBoxCol = col - col % SUBGRID_SIZE;

        for (int i = localBoxRow; i < localBoxRow + SUBGRID_SIZE; i++) {
            for (int j = localBoxCol; j < localBoxCol + SUBGRID_SIZE; j++) {
                if (board[i][j] == num) {
                    return true;
                }
            }
        }
        return false;
    }
}
