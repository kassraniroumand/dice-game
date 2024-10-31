public class Player {
	private int order;
	private String name;
	private int score;
	private boolean isFirst;

	public Player(int order, String name) {
		this.order = order;
		this.name = name;
		this.score = 0;
		this.isFirst = true;
	}

	public int getOrder() {
		return order;
	}

	public String getName() {
		return name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public boolean isFirst() {
		return isFirst;
	}

	public void setFirst(boolean isFirst) {
		this.isFirst = isFirst;
	}
}
