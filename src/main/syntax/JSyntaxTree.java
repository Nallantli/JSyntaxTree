package syntax;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Main class for directing the generation of the trees - functions as an
 * embeddable panel.
 */

public class JSyntaxTree extends JPanel {
	// Scale that makes sure to keep the
	// generated graphic (as the GUI, not the
	// output file) under 500x500 pixels.
	static double scale = 1;

	// The tree object, generates the actual
	// underlying graphical visualization
	public static DrawTree tree;

	// JPanel automatically called method
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.scale(scale, scale);
		// Fill the background of the window
		g2.setPaint(Color.WHITE);
		g2.fillRect(0, 0, tree.getWidth(), tree.getHeight());
		// Begin to draw the tree onto the window
		tree.paintStatic(g2);
	}

	// Finds the length of a string having
	// undergone textual effects
	public static double GetWidthOfAttributedString(AttributedString attributedString, Graphics2D g2) {
		AttributedCharacterIterator characterIterator = attributedString.getIterator();
		FontRenderContext fontRenderContext = new FontRenderContext(new AffineTransform(), true, true);
		if (g2 != null)
			fontRenderContext = g2.getFontRenderContext();
		LineBreakMeasurer lbm = new LineBreakMeasurer(characterIterator, fontRenderContext);
		TextLayout textLayout = lbm.nextLayout(Integer.MAX_VALUE);
		return textLayout.getBounds().getWidth();
	}

	// Saves an image file of the resulting tree
	static public void save(String output_file) {
		BufferedImage bImg = new BufferedImage(tree.getWidth(), tree.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D cg = bImg.createGraphics();
		tree.paintStatic(cg);
		try {
			if (ImageIO.write(bImg, output_file.split("\\.")[output_file.split("\\.").length - 1],
					new File(output_file))) {
				System.out.println("Saved as: " + output_file);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Quick method to read and analyze the
	// inputted parameters on the CLI
	public static String getOption(String[] args, String o) {
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals(o)) {
				if (i < args.length - 1)
					return args[i + 1];
				else
					return "";
			}
		}
		return null;
	}

	// Parse the CLI options and begin tree
	// generation
	public static void initialize(String args[], JPanel f) throws Exception {
		String filename = getOption(args, "-i");
		String raw = null;
		if (filename == null) {
			if (getOption(args, "-r") != null) {
				raw = getOption(args, "-r");
			} else
				throw new Exception("File not found!");
		} else
			raw = Interpreter.loadFile(filename);

		String output_file = null;
		if (filename != null) {
			String[] sp = filename.split("\\.");
			output_file = filename.substring(0, filename.length() - sp[sp.length - 1].length()) + "png";
		}

		boolean in_color = false;
		boolean auto_subscript = false;
		boolean curved_movement = false;
		int fontSize = 26;
		float strokeWeight = 1.5F;
		int border = 25;
		int spacingX = 25;
		int spacingY = 75;
		String font_name = "Doulos SIL";

		if (getOption(args, "-o") != null)
			output_file = getOption(args, "-o");
		if (getOption(args, "-c") != null)
			in_color = true;
		if (getOption(args, "-a") != null)
			auto_subscript = true;
		if (getOption(args, "-cm") != null)
			curved_movement = true;
		if (getOption(args, "-f") != null)
			font_name = getOption(args, "-f");
		if (getOption(args, "-fs") != null)
			fontSize = Integer.valueOf(getOption(args, "-fs"));
		if (getOption(args, "-l") != null)
			strokeWeight = Float.valueOf(getOption(args, "-l"));
		if (getOption(args, "-sx") != null)
			spacingX = Integer.valueOf(getOption(args, "-sx"));
		if (getOption(args, "-sy") != null)
			spacingY = Integer.valueOf(getOption(args, "-sy"));
		if (getOption(args, "-b") != null)
			border = Integer.valueOf(getOption(args, "-b"));

		// Call the interpreter to read a file and
		// generate the parent node of the tree
		Node NS = Interpreter.interpret(raw, auto_subscript, in_color);

		// Send the parameters specified in the
		// options to a new tree object
		tree = new DrawTree(NS, fontSize, spacingX, spacingY, border, font_name, strokeWeight, curved_movement);

		// If graphics is requested, let the tree
		// be drawn as a new panel
		if (f != null) {
			JPanel jp = new JSyntaxTree();
			scale = 400.0 / (float) tree.getHeight();
			jp.setPreferredSize(
					new Dimension((int) ((double) tree.getWidth() * scale), (int) ((double) tree.getHeight() * scale)));
			f.add(jp);
		}

		// Lastly, process the output file
		if (output_file != null)
			save(output_file);
	}

	public static void main(String args[]) {
		try {
			JPanel jp = new JPanel();
			initialize(args, jp);
			if (getOption(args, "-q") == null) {
				JFrame f = new JFrame("JSyntaxTree");
				f.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent e) {
						System.exit(0);
					}
				});
				f.getContentPane().add(jp);
				f.setResizable(false);
				f.pack();
				f.setVisible(true);
			}
		} catch (Exception e) {
			System.err.println(e);
		}
	}

}