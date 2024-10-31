public class Node {
	int data;
	Node right, down, up;

	Node(int data) {
		this.data = data;
		this.right = null;
		this.down = null;
	}

	@Override
	public String toString() {
		return "Node{" +
				"data=" + data +
				'}';
	}
}
