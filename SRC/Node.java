import java.util.ArrayList;

public class Node {
	public String metadata;
	public String value;
	public ArrayList<Node> subNodes;

	Node(String value) {
		this.value = value;
		this.subNodes = new ArrayList<Node>();
		this.metadata = "";
	}

	Node(String value, ArrayList<Node> subNodes) {
		this.value = value;
		this.subNodes = subNodes;
		this.metadata = "";
	}
	
	public String toString() {
		String s = "[" + value + " ";
		if (subNodes.isEmpty())
			s += ": " + metadata;
		else {
			for (Node n : subNodes)
				s += n.toString();
		}
		return s + "]";
	}

	public int getDepth() {
		int depth = 0;
		for (Node n : subNodes) {
			int tent_depth = n.getDepth() + 1;
			if (tent_depth > depth)
				depth = tent_depth;
		}
		return depth;
	}

	private int countLevel(int i) {
		if (i == 0)
			return subNodes.size();
		int r = 0;
		for (Node n : subNodes) {
			r += n.countLevel(i - 1);
		}
		return r;
	}

	public int getWidth() {
		if (subNodes.isEmpty())
			return 1;
		int width = 0;
		int i = 0;
		for (Node n : subNodes)
			width += n.getWidth();
		return width;
	}
}