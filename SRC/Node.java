import java.util.ArrayList;
import java.awt.*;

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
		else
			for (Node n : subNodes)
				s += n.toString();
		return s + "]";
	}

	public int getDepth() {
		int depth = 0;
		for (Node n : subNodes) {
			int tent_depth = n.getDepth() + JSyntaxTree.spacingY;
			if (tent_depth > depth)
				depth = tent_depth;
		}
		if (subNodes.isEmpty()) {
			depth = metadata.split("\\\\n").length * JSyntaxTree.fontSize;
			if (metadata.charAt(metadata.length() - 1) == '^')
				depth += JSyntaxTree.spacingY;
		}
		return depth;
	}

	public int getWidth() {
		return getWidth(false);
	}

	public int getWidth(boolean g) {
		if (subNodes.isEmpty()) {
			if (g) {
				int largest = (int)JSyntaxTree.GetWidthOfAttributedString(JSyntaxTree.getTrig(value));
				String[] arr = metadata.split("\\\\n");
				if (metadata.charAt(metadata.length() - 1) == '^')
					arr = metadata.substring(0, metadata.length() - 1).split("\\\\n");
				for (String s : arr) {
					int temp = (int)JSyntaxTree.GetWidthOfAttributedString(JSyntaxTree.getTrig(s));
					if (largest < temp)
						largest = temp;
				}
				return largest + JSyntaxTree.spacingX;
			}
			return 1;
		}
		int width = 0;
		for (Node n : subNodes)
			width += n.getWidth(g);
		return width;
	}
}