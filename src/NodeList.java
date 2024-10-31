import java.util.ArrayList;
import java.util.List;

public class NodeList {

	private Node head;
	private int rows;
	private int cols;

	public NodeList() {
		head = null;
		rows = 0;
		cols = 0;
	}

	public void add(int data) {
		Node node = new Node(data);
		if (head == null) {
			head = node;
			rows = 1;
			cols = 1;
		} else {
			Node curr = head;
			while (curr.right != null) {
				curr = curr.right;
			}
			curr.right = node;
			cols++;
		}
	}

	public void set(int row, int col, int data) {
		if (row > rows || col > cols) {
			throw new IndexOutOfBoundsException();
		}
		Node curr = head;
		for (int i = 0; i < row; i++) {
			curr = curr.down;
		}
		for (int i = 0; i < col; i++) {
			curr = curr.right;
		}
		curr.data = data;
	}

	public int get(int row, int col) {
		if (row > rows || col > cols) {
			throw new IndexOutOfBoundsException();
		}
		Node curr = head;
		for (int i = 0; i < row; i++) {
			curr = curr.down;
		}
		for (int i = 0; i < col; i++) {
			curr = curr.right;
		}
		return (int) curr.data;
	}

	public List<Node> getColumn(int col) {
		if (col > cols) {
			throw new IndexOutOfBoundsException();
		}
		List<Node> column = new ArrayList<>();
		Node curr = head;
		for (int i = 0; i < col; i++) {
			curr = curr.right;
		}
		while (curr != null) {
			column.add(curr);
			curr = curr.down;
		}
		return column;
	}

	public Node getNode(int row, int col) {
		if (row > rows || col > cols) {
			throw new IndexOutOfBoundsException();
		}
		Node curr = head;
		for (int i = 0; i < row; i++) {
			curr = curr.down;
		}
		for (int i = 0; i < col; i++) {
			curr = curr.right;
		}
		return curr;
	}

	public void initialize(int rows, int cols, Integer defaultValue) {
		if (rows <= 0 || cols <= 0) {
			throw new IllegalArgumentException("Rows and columns must be positive");
		}

		this.rows = rows;
		this.cols = cols;
		head = new Node(defaultValue);

		// Create the first row
		Node current = head;
		for (int j = 1; j < cols; j++) {
			current.right = new Node(defaultValue);
			current = current.right;
		}

		// Create subsequent rows and link them vertically
		Node previousRowStart = head;
		for (int i = 1; i < rows; i++) {
			Node rowStart = new Node(defaultValue);
			previousRowStart.down = rowStart;

			Node previousInRow = previousRowStart;
			current = rowStart;

			for (int j = 1; j < cols; j++) {
				current.right = new Node(defaultValue);
				previousInRow.right.down = current.right;

				current = current.right;
				previousInRow = previousInRow.right;
			}

			previousRowStart = rowStart;
		}
	}

	public Node getHead(){
		return head;
	}

	// check if all the cells are not 0
	public boolean checkAllValueIsNotZero() {
		Node currentRow = head;
		while (currentRow != null) {
			Node currentCol = currentRow;
			while (currentCol != null) {
				if ((int) currentCol.data == 0) {
					return false; // Found a zero value
				}
				currentCol = currentCol.right;
			}
			currentRow = currentRow.down;
		}
		return true; // No zero values found
	}



	public void printList() {
		Node currentRow = head;
		while (currentRow != null) {
			Node currentCol = currentRow;
			while (currentCol != null) {
				if ((int) currentCol.data != 0) {
					System.out.printf("%2d   ", (int) currentCol.data);
				} else {
					System.out.print("__   "); // Print two spaces if currentCol is null
				}
				currentCol = currentCol.right;
			}
			System.out.println();
			currentRow = currentRow.down;
		}
	}
}
