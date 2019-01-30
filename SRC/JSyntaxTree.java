import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.font.TextAttribute;
import java.text.*;
import java.awt.font.*;

import javax.swing.*;

public class JSyntaxTree extends JPanel {
    static int fontSize = 48;
    static int spacingX = 50;
    static int spacingY = 200;
    static int border = 50;

    static String output_file = "OUTPUT.png";
    static String font_name = "Doulos SIL";
    
    static int height = 0;
    static int width = 0;
    static boolean in_color = false;
    static boolean auto_subscript = false;
 
    static BasicStroke stroke = new BasicStroke(3.0f);
    public static Font font;

    public static Node NS;

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.scale(0.5, 0.5);
        g2.setPaint(Color.WHITE);
        g2.fillRect(0, 0, width, height);
        paintStatic(g);
    }
 
    static public void paintStatic(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setPaint(Color.BLACK);

        paintNode(border, border + fontSize / 2, g2, NS);
    }

    public static void paintNode(int _x, int _y, Graphics g, Node n) {
        Graphics2D g2 = (Graphics2D) g;
        if (in_color)
            g2.setPaint(Color.BLUE);

        int center_x = _x + n.getWidth(true) / 2;
        drawCenteredString(center_x, _y, g, n.value);
        if (!n.subNodes.isEmpty()) {
            int x = _x;
            for (Node sub : n.subNodes) {
                g2.setStroke(stroke);
                if (in_color)
                    g2.setPaint(Color.BLACK);
                g2.draw(new Line2D.Float(center_x, _y + font.getSize2D(), x + sub.getWidth(true) / 2, _y + spacingY - font.getSize2D()));
                paintNode(x, _y + spacingY, g, sub);
                x+=sub.getWidth(true);
            }
        } else if (!n.metadata.isEmpty()) {
            if (n.metadata.charAt(n.metadata.length() - 1) == '^') {
                g2.setStroke(stroke);
                if (in_color)
                    g2.setPaint(Color.BLACK);
                g2.draw(new Line2D.Float(center_x, _y + font.getSize2D(), center_x - n.getWidth(true) / 2 + spacingX / 2, _y + spacingY - font.getSize2D()));               
                g2.draw(new Line2D.Float(center_x, _y + font.getSize2D(), center_x + n.getWidth(true) / 2 - spacingX / 2, _y + spacingY - font.getSize2D()));
                g2.draw(
                    new Line2D.Float(center_x - n.getWidth(true) / 2 + spacingX / 2, _y + spacingY - font.getSize2D(), center_x + n.getWidth(true) / 2 - spacingX / 2, _y + spacingY - font.getSize2D()));
                if (in_color)
                        g2.setPaint(Color.RED);
                drawCenteredString(center_x, _y + spacingY, g, n.metadata.substring(0, n.metadata.length() - 1));
            } else if (n.metadata.charAt(n.metadata.length() - 1) == '|') {
                g2.setStroke(stroke);
                if (in_color)
                    g2.setPaint(Color.BLACK);
                g2.draw(new Line2D.Float(center_x, _y + font.getSize2D(), center_x, _y + spacingY - font.getSize2D()));
                if (in_color)
                        g2.setPaint(Color.RED);
                drawCenteredString(center_x, _y + spacingY, g, n.metadata.substring(0, n.metadata.length() - 1));                
            } else {
                if (in_color)
                    g2.setPaint(Color.RED);
                drawCenteredString(center_x, (int)(_y + font.getSize2D() * 1.5), g, n.metadata);
            }
        }
    }

    static double GetWidthOfAttributedString(AttributedString attributedString) {
        AttributedCharacterIterator characterIterator = attributedString.getIterator();
        FontRenderContext fontRenderContext = new FontRenderContext(new AffineTransform(), true, true);
        LineBreakMeasurer lbm = new LineBreakMeasurer(characterIterator, fontRenderContext);
        TextLayout textLayout = lbm.nextLayout(Integer.MAX_VALUE);
        return textLayout.getBounds().getWidth();
    }
    
    static AttributedString getTrig(String text) {
        String s = text.replaceAll("\\_", "");
        s = s.replaceAll("\\*", "");
        s = s.replaceAll("\\%", "");
        s = s.replaceAll("\\$", "");
        s = s.replaceAll("\\@", "");
        s = s.replaceAll("\\#", "");
        AttributedString trig = new AttributedString(s);
        trig.addAttribute(TextAttribute.FAMILY, font.getFamily());
        trig.addAttribute(TextAttribute.SIZE, font.getSize());

        boolean sub = false;
        boolean bold = false;
        boolean ital = false;
        boolean smal = false;
        boolean und = false;
        boolean green = false;
        int b = 0;
        for (int i = 0; i < text.length(); i++) {

            if (text.charAt(i) == '_') {
                sub = !sub;
            } else if (text.charAt(i) == '*') {
                bold = !bold;
            } else if (text.charAt(i) == '%') {
                ital = !ital;
            } else if (text.charAt(i) == '$') {
                smal = !smal;
            } else if (text.charAt(i) == '@') {
                und = !und;
            } else if (text.charAt(i) == '#') {
                green = !green;
            } else {
                if (s.length() <= b)
                    continue;
                if (sub)
                    trig.addAttribute(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUB, b, b + 1);
                if (bold)
                    trig.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD, b, b + 1);
                if (ital)
                    trig.addAttribute(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE, b, b + 1);
                if (smal)
                    trig.addAttribute(TextAttribute.SIZE, (int)((float)fontSize * 0.70), b, b + 1);
                if (und)
                    trig.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON, b, b + 1);
                if (green)
                    trig.addAttribute(TextAttribute.SWAP_COLORS, TextAttribute.SWAP_COLORS_ON, b, b + 1);
                b++;
            }
        }

        if (sub || bold || ital || smal || und || green)
            System.err.println("Warning: unclosed formatting\n" + text);

        return trig;
    }

    public static void drawCenteredString(int _x, int _y, Graphics g, String text) {
        String[] arr = text.split("\\\\n");
        if (arr.length > 1) {
            for (int i = 0; i < arr.length; i++){
                drawCenteredString(_x, _y + i * (int)(font.getSize2D() * 1), g, arr[i]);
            }
            return;
        }

        while (text.charAt(0) == ' ')
            text = text.substring(1);

        AttributedString trig = getTrig(text);
        
        FontMetrics metrics = g.getFontMetrics(font);
        int x = _x - (int)(GetWidthOfAttributedString(trig) / 2);
        int y = _y - ((metrics.getHeight()) / 2) + metrics.getAscent();
        g.drawString(trig.getIterator(), x, y);
    }

    static public void save()
    {
        BufferedImage bImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D cg = bImg.createGraphics();
        paintStatic(cg);
        try {
                if (ImageIO.write(bImg, output_file.split("\\.")[output_file.split("\\.").length - 1], new File("./" + output_file)))
                {
                    System.out.println("Saved as: " + output_file);
                }
        } catch (IOException e) {
                e.printStackTrace();
        }
    }

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

    public static void initialize(String args[]) {
        String filename = getOption(args, "-i");
        if (filename == null) {
            System.err.println("File not found!");
            return;
        }
        boolean instant_quit = false;

        if (getOption(args, "-o") != null)
            output_file = getOption(args, "-o");
        if (getOption(args, "-c") != null)
            in_color = true;
        if (getOption(args, "-q") != null)
            instant_quit = true;
        if (getOption(args, "-a") != null)
            auto_subscript = true;
        if (getOption(args, "-f") != null)
            font_name = getOption(args, "-f");
        if (getOption(args, "-fs") != null)
            fontSize = Integer.valueOf(getOption(args, "-fs"));
        if (getOption(args, "-l") != null)
            stroke = new BasicStroke(Float.valueOf(getOption(args, "-l")));
        if (getOption(args, "-sx") != null)
            spacingX = Integer.valueOf(getOption(args, "-sx"));
        if (getOption(args, "-sy") != null)
            spacingY = Integer.valueOf(getOption(args, "-sy"));
        if (getOption(args, "-b") != null)
            border = Integer.valueOf(getOption(args, "-b"));

        font = new Font(font_name, Font.PLAIN, fontSize);
        NS = Interpreter.interpret(filename);
        height = NS.getDepth() + border * 2;
        width = NS.getWidth(true) + border * 2;

        if (!instant_quit) {
            JFrame f = new JFrame("JSyntaxTree");
            f.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {System.exit(0);}
            });
            JPanel jp = new JSyntaxTree();
            f.getContentPane().add(jp);
            jp.setPreferredSize(new Dimension(width / 2, height / 2));
            f.pack();
            f.setVisible(true);
            f.setResizable(false);
        }
        save();
    }
 
    public static void main(String args[]) {
        initialize(args);
    }
 
}