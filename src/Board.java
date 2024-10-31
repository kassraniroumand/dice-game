import java.util.ArrayList;
import java.util.List;

public class Board {
	// logic for the board 2d matrix
	NodeList nodeListSinglton;
	public Board() {
		this.nodeListSinglton = NodeListSinglton.getInstance();
	}

	public void initialize() {
		// add the data to the board
		NodeList nodeListSinglton = NodeListSinglton.getInstance();
		nodeListSinglton.initialize(3, 11, 0);
	}

	// is any other value in that column
	public boolean isColumnEmpty(int column) {
		List<Node> column1 = this.nodeListSinglton.getColumn(column);
		for (Node node : column1) {
			if (node.data != 0) {
				return false;
			}
		}
		return true;
	}

	public void set(int player,int column, int data) {
		Node node = this.nodeListSinglton.getNode(player, column);
		List<Node> column1 = this.nodeListSinglton.getColumn(column);
		// if data is bigger than value in the column
		for (Node node1 : column1) {
			if (node1.data < data && node1.data != 0) {
				node1.data = -1;
			} else if (node1.data > data && node1.data != 0) {
				data = -1;
			}
		}

		this.nodeListSinglton.set(player, column, data);
	}

}
