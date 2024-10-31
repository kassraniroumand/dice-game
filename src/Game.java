import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Game {
	private int currentPlayerIndex = 0;
	private List<Player> players;
	private Board board;
	private static final int TURNS_PER_PLAYER = 12;
	private static final int DICE_SIDES = 6;
	private static final int MIN_CELL_NUMBER = 2;
	private static final int MAX_CELL_NUMBER = 12;
	private final Scanner scanner;

	public Game() {
		this.board = new Board();
		this.players = List.of(
				new Player(0, "Player 1"),
				new Player(1, "Player 2"),
				new Player(2, "Player 3")
		);
		this.scanner = new Scanner(System.in);
	}

	public void start() throws IOException {
		board.initialize();
		int totalTurns = players.size() * TURNS_PER_PLAYER;
		do {
			playTurn();
			updateScores();
			moveToNextPlayer();
			// if all cells are filled, terminate game
		} while (!NodeListSinglton.getInstance().checkAllValueIsNotZero());
		printFinalScores();
	}

	private void playTurn() {
		Player currentPlayer = players.get(currentPlayerIndex);
		int[] diceRolls = rollDice();
		int totalRoll = Arrays.stream(diceRolls).reduce(0, Integer::sum);

		System.out.printf("%s rolled %d and %d%n", currentPlayer.getName(), diceRolls[0], diceRolls[1]);
		printGameBoard();

		int cellNumber = getCellNumberFromUser() - 2;

		// if there is non-zero value, just return
		if (NodeListSinglton.getInstance().get(currentPlayerIndex, cellNumber) != 0) {
			System.out.println("You can only set the value in an empty cell. Please try again.");
			playTurn();
			return;
		}

		if (currentPlayer.isFirst()) {
			boolean columnEmpty = board.isColumnEmpty(cellNumber);
			if (columnEmpty) {
				currentPlayer.setFirst(false);
				board.set(currentPlayerIndex, cellNumber, totalRoll);
			} else {
				System.out.println("You can only set the value in an empty column. Please try again.");
				playTurn();
				return;
			}
		} else {
			board.set(currentPlayerIndex, cellNumber, totalRoll);
		}
		System.out.println("\nUpdated board:");
		printGameBoard();
	}

	private int[] rollDice() {
		return new int[]{new Dice(DICE_SIDES).roll(), new Dice(DICE_SIDES).roll()};
	}

	private void moveToNextPlayer() {
		currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
	}

	private int getCellNumberFromUser() {
		int cellNumber;
		while (true) {
			System.out.printf("Enter the cell number to set the value (between %d - %d): ", MIN_CELL_NUMBER, MAX_CELL_NUMBER);
			try {
				cellNumber = scanner.nextInt();
				if (cellNumber >= MIN_CELL_NUMBER && cellNumber <= MAX_CELL_NUMBER) {
					break;
				} else {
					System.out.printf("Invalid input. Please enter a number between %d and %d.%n", MIN_CELL_NUMBER, MAX_CELL_NUMBER);
				}
			} catch (InputMismatchException e) {
				System.out.println("Invalid input. Please enter a valid number.");
				scanner.nextLine(); // Clear the invalid input
			}
		}
		return cellNumber;
	}

	private void updateScores() {
		for (Player player : players) {
			// when the all columns are filled, calculate the score
			player.setScore(calculatePlayerScore(player));
		}
	}

	private int calculatePlayerScore(Player player) {
		int score = 0;
		NodeList nodeList = NodeListSinglton.getInstance();

		for (int col = 0; col < 11; col++) {
			int cellValue = nodeList.get(player.getOrder(), col);
			if (cellValue == 0) continue; // Skip empty cells

			List<Node> columnNodes = nodeList.getColumn(col);

			// Get all non-zero values in the column
			List<Integer> nonZeroValues = columnNodes.stream()
					.map(node -> node.data)
					.filter(val -> val != 0)
					.collect(Collectors.toList());

			// If column is completely filled (3 non-zero values)
			if (nonZeroValues.size() == 3) {
				// Count occurrences of the current player's value in the column
				long equalValuesCount = columnNodes.stream()
						.filter(node -> node.data == cellValue)
						.count();

				// Case 1: If the value is unique
				if (equalValuesCount == 1) {
					// Find the maximum value in the column
					int maxValue = Collections.max(nonZeroValues);
					// Award points only if this unique value is the maximum
					if (cellValue == maxValue) {
						score += col + 2;
					}
				}
				// Case 2: If all three values are equal
				else if (equalValuesCount == 3) {
					score += col + 2;
				}
				// Case 3: If there are exactly two equal values, no points are awarded
			}
		}
		return score;
	}

	private void printGameBoard() {
		final String HORIZONTAL_LINE =
				"+-----+-------------+--------+--------+--------+--------+--------+--------+--------+--------+--------+--------+--------+--------+";
		final String HEADER_FORMAT = "| %3s | %-11s|  %5d |  %5d |  %5d |  %5d |  %5d |  %5d |  %5d |  %5d |  %5d |" +
				" " +
				" %5d |  %5d | Score |";
		final String ROW_FORMAT = "| %s%d | %-11s | %6s | %6s | %6s | %6s | %6s | %6s | %6s | %6s | %6s | %6s | %6s " +
				"|" +
				" %5d |";

		System.out.println(HORIZONTAL_LINE);
		System.out.println(String.format(HEADER_FORMAT, "Turn", "Player", 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12));
		System.out.println(HORIZONTAL_LINE);

		Node currentRow = NodeListSinglton.getInstance().getHead();
		for (int rowNumber = 0; rowNumber < players.size(); rowNumber++) {
			printPlayerRow(currentRow, rowNumber, ROW_FORMAT);
			System.out.println(HORIZONTAL_LINE);
			currentRow = currentRow.down;
		}
	}

	private void printPlayerRow(Node currentRow, int rowNumber, String rowFormat) {
		Player player = players.get(rowNumber);
		String playerIndicator = (rowNumber == currentPlayerIndex) ? "->" : "  ";
		String[] cellValues = getCellValues(currentRow);

		// Highlight unique, non-zero values
		NodeList nodeList = NodeListSinglton.getInstance();
		for (int col = 0; col < 11; col++) {
			int cellValue = nodeList.get(player.getOrder(), col);
			if (cellValue > 0 && nodeList.getColumn(col).stream().filter(node -> node.data == cellValue).count() == 1) {
				cellValues[col] = "[" + cellValues[col] + "]";  // Highlight scored cells
			}
		}

		System.out.println(String.format(rowFormat,
				playerIndicator, (rowNumber + 1), player.getName(),
				cellValues[0], cellValues[1], cellValues[2], cellValues[3], cellValues[4],
				cellValues[5], cellValues[6], cellValues[7], cellValues[8], cellValues[9], cellValues[10],
				player.getScore())
		);
	}

	private String[] getCellValues(Node currentRow) {
		String[] cellValues = new String[11];
		Node currentCol = currentRow;
		for (int colIndex = 0; colIndex < 11 && currentCol != null; colIndex++) {
			if (currentCol.data == -1) {
				cellValues[colIndex] = "*";
			} else {
				cellValues[colIndex] = ((int) currentCol.data != 0) ? String.valueOf(currentCol.data) : "__";
			}
			currentCol = currentCol.right;
		}
		return cellValues;
	}

	private void printFinalScores() {
		System.out.println("\nFinal Scores:");
		for (Player player : players) {
			System.out.printf("%s: %d points\n", player.getName(), player.getScore());
		}
		Player winner = players.stream().max((p1, p2) -> Integer.compare(p1.getScore(), p2.getScore())).orElse(null);
		if (winner != null) {
			System.out.printf("\nThe winner is %s with %d points!\n", winner.getName(), winner.getScore());
		}
	}
}