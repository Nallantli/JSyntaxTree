package syntax;

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

	public int getDepth(DrawTree tree) {
		int depth = 0;
		for (Node n : subNodes) {
			int tent_depth = n.getDepth(tree) + tree.getSpacingY();
			if (tent_depth > depth)
				depth = tent_depth;
		}
		if (subNodes.isEmpty()) {
			depth = metadata.split("\\\\n").length * tree.getFontSize();
			if (metadata.charAt(metadata.length() - 1) == '^' || metadata.charAt(metadata.length() - 1) == '|')
				depth += tree.getSpacingY();
			depth += tree.getFontSize() * 1.5;
		}
		return depth;
	}

	public int getWidth() {
		return getWidth(null);
	}

	public int getWidth(DrawTree tree) {
		if (subNodes.isEmpty()) {
			if (tree != null) {
				int largest = (int)JSyntaxTree.GetWidthOfAttributedString(tree.getTrig(value));
				String[] arr = metadata.split("\\\\n");
				if (metadata.charAt(metadata.length() - 1) == '^' || metadata.charAt(metadata.length() - 1) == '|')
					arr = metadata.substring(0, metadata.length() - 1).split("\\\\n");
				for (String s : arr) {
					int temp = (int)JSyntaxTree.GetWidthOfAttributedString(tree.getTrig(s));
					if (largest < temp)
						largest = temp;
				}
				return largest + tree.getSpacingX();
			}
			return 1;
		}
		int width = 0;
		for (Node n : subNodes)
			width += n.getWidth(tree);
		return width;
	}
}