package syntax;

import java.util.ArrayList;

enum MODE {
	NONE_, BAR_, TRIANGLE_
}

enum BRACKET {
	NO_BRACKET, SQUARE_BRACKET
}

public class Node {
	public String metadata;
	public String value;
	public ArrayList<Node> subNodes;
	public Node parent;
	public int[] raises = new int[0];
	public int[] raisesSUB = new int[0];
	public int passes = 0;
	public MODE mode = MODE.NONE_;
	public BRACKET bracket = BRACKET.NO_BRACKET;

	Node(String value) {
		this.value = value;
		this.subNodes = new ArrayList<Node>();
		this.metadata = "";
	}

	Node(String value, ArrayList<Node> subNodes) {
		this.value = value;
		this.subNodes = subNodes;
		this.metadata = "";
		for (Node n : subNodes)
			n.parent = this;
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
			depth += metadata.split("\\\\n").length * tree.getFontSize();
			if (mode != MODE.NONE_)
				depth += tree.getSpacingY();
			else
				depth += tree.getFontSize() * 1.5;
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
				int largest = (int) JSyntaxTree.GetWidthOfAttributedString(tree.getTrig(value));
				String[] arr = metadata.split("\\\\n");
				for (String s : arr) {
					int temp = (int) JSyntaxTree.GetWidthOfAttributedString(tree.getTrig(s));
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

	public int getTotalX(DrawTree tree) {
		int pos = 0;
		Node n = getNeighborLeft();
		while (n != null) {
			pos += n.getWidth(tree);
			n = n.getNeighborLeft();
		}
		return pos + getWidth(tree) / 2 + tree.getBorder();
	}

	public int getTotalY(DrawTree tree) {
		int depth = 0;
		/*
		 * if (subNodes.isEmpty()) { depth = metadata.split("\\\\n").length *
		 * tree.getFontSize(); if (metadata.charAt(metadata.length() - 1) == '^' ||
		 * metadata.charAt(metadata.length() - 1) == '|') depth += tree.getSpacingY();
		 * else depth += tree.getFontSize() * 1.5; }
		 */
		Node current = parent;
		while (current != null) {
			depth += tree.getSpacingY();
			current = current.parent;
		}

		return depth;
	}

	public Node getNeighborLeft() {
		Node current = this;

		while (current.parent != null) {
			int i;
			for (i = 0; i < current.parent.subNodes.size(); i++) {
				if (current.parent.subNodes.get(i) == current)
					break;
			}
			if (i == 0) {
				current = current.parent;
			} else {
				Node n = current.parent.subNodes.get(i - 1);
				while (!n.subNodes.isEmpty()) {
					n = n.subNodes.get(n.subNodes.size() - 1);
				}
				return n;
			}
		}
		return null;
	}

	public void resetPasses() {
		passes = 0;
		for (Node n : subNodes)
			n.resetPasses();
	}

	public boolean hasAncestor(Node n) {
		if (parent == n)
			return true;
		else if (parent != null)
			return parent.hasAncestor(n);
		return false;
	}
}