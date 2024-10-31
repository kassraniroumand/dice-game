public class NodeListSinglton {
	private static NodeList instance = null;

	private NodeListSinglton() {}

	public static NodeList getInstance() {
		if (instance == null) {
			instance = new NodeList();
		}
		return instance;
	}
}
