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
 
/* 
 * This is like the FontDemo applet in volume 1, except that it 
 * uses the Java 2D APIs to define and render the graphics and text.
 */
 
public class Main extends JApplet {
    final static int maxCharHeight = 24;
    final static int minFontSize = 24;
    final static int spacingX = 150;
    final static int spacingY = 100;

    boolean saved = false;
    static int height = 0;
    static int width = 0;
 
    final static Color bg = Color.white;
    final static Color fg = Color.black;
    final static Color red = Color.red;
    final static Color white = Color.white;
 
    final static BasicStroke stroke = new BasicStroke(2.0f);
    final static BasicStroke wideStroke = new BasicStroke(8.0f);
 
    final static float dash1[] = {10.0f};
    final static BasicStroke dashed = new BasicStroke(1.0f, 
                                                      BasicStroke.CAP_BUTT, 
                                                      BasicStroke.JOIN_MITER, 
                                                      10.0f, dash1, 0.0f);
    Dimension totalSize;
    FontMetrics fontMetrics;

    public static Node NS;
 
    public void init() {
        //Initialize drawing colors
        setBackground(bg);
        setForeground(fg);
    }
 
    public void paint(Graphics g) {
        Font font = new Font("Doulos SIL", Font.PLAIN, 24);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Dimension d = getSize();
 
        Color fg3D = Color.white;
 
        g2.setPaint(fg3D);
        //g2.fillRect(0, 0, width, height);
        paintNode(spacingX, spacingY, g2, NS, font);
        if (!saved)
            save();
    }

    public void paintNode(int _x, int _y, Graphics g, Node n, Font font) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(fg);
        drawCenteredString(_x + n.getWidth() * spacingX / 2, _y, g, n.value, font);
        if (!n.subNodes.isEmpty()) {            
            int x = _x;
            for (Node sub : n.subNodes) {
                g2.setStroke(new BasicStroke(2f));
                g2.draw(new Line2D.Float(_x + n.getWidth() * spacingX / 2, _y + font.getSize2D(), x + sub.getWidth() * spacingX / 2, _y + spacingY - font.getSize2D()));
                paintNode(x, _y + spacingY, g, sub, font);
                x+=sub.getWidth() * spacingX;
            }
        } else {
            if (n.metadata.charAt(n.metadata.length() - 1) == '^') {
                g2.setStroke(new BasicStroke(2f));
                g2.draw(new Line2D.Float(_x + n.getWidth() * spacingX / 2, _y + font.getSize2D(), _x + n.getWidth() * spacingX / 2 - (int)GetWidthOfAttributedString(g2, new AttributedString(n.metadata.split("\\\\\n")[0].substring(0, n.metadata.split("\\\\n")[0].length() - 1))) / 2, _y + spacingY / 2 + font.getSize2D() / 2));               
                g2.draw(new Line2D.Float(_x + n.getWidth() * spacingX / 2, _y + font.getSize2D(), _x + n.getWidth() * spacingX / 2 + (int)GetWidthOfAttributedString(g2, new AttributedString(n.metadata.split("\\\\\n")[0].substring(0, n.metadata.split("\\\\n")[0].length() - 1))) / 2, _y + spacingY / 2 + font.getSize2D() / 2));
                g2.draw(
                    new Line2D.Float(
                        _x + n.getWidth() * spacingX / 2 - (int)GetWidthOfAttributedString(
                            g2, 
                            new AttributedString(n.metadata.split("\\\\\n")[0].substring(0, n.metadata.split("\\\\n")[0].length() - 1))
                            ) / 2, 
                        _y + spacingY / 2 + font.getSize2D() / 2,  
                        _x + n.getWidth() * spacingX / 2 + (int)GetWidthOfAttributedString(g2, new AttributedString(n.metadata.split("\\\\\n")[0].substring(0, n.metadata.split("\\\\n")[0].length() - 1))) / 2, 
                        _y + spacingY / 2 + font.getSize2D() / 2)
                    );

                drawCenteredString(_x + n.getWidth() * spacingX / 2, (int)(_y + font.getSize2D() * 1.5 + spacingY / 2), g, n.metadata.substring(0, n.metadata.length() - 1), font);
            } else
                drawCenteredString(_x + n.getWidth() * spacingX / 2, (int)(_y + font.getSize2D() * 1.5), g, n.metadata, font);
        }
    }

    double GetWidthOfAttributedString(Graphics2D graphics2D, AttributedString attributedString) {
        AttributedCharacterIterator characterIterator = attributedString.getIterator();
        FontRenderContext fontRenderContext = graphics2D.getFontRenderContext();
        LineBreakMeasurer lbm = new LineBreakMeasurer(characterIterator, fontRenderContext);
        TextLayout textLayout = lbm.nextLayout(Integer.MAX_VALUE);
        return textLayout.getBounds().getWidth();
    }

    public void drawCenteredString(int _x, int _y, Graphics g, String text, Font font) {
        String[] arr = text.split("\\\\n");
        if (arr.length > 1) {
            for (int i = 0; i < arr.length; i++){
                drawCenteredString(_x, _y + i * (int)(font.getSize2D() * 1), g, arr[i], font);
            }
            return;
        }

        if (text.charAt(0) == ' ')
            text = text.substring(1);

        String s = text.replaceAll("\\_", "");
        s = s.replaceAll("\\*", "");
        s = s.replaceAll("\\%", "");
        s = s.replaceAll("\\$", "");
        s = s.replaceAll("\\`", "");
        AttributedString trig = new AttributedString(s);
        trig.addAttribute(TextAttribute.FAMILY, font.getFamily());
        trig.addAttribute(TextAttribute.SIZE, font.getSize());

        boolean sub = false;
        boolean bold = false;
        boolean ital = false;
        boolean smal = false;
        boolean und = false;
        int b = 0;
        for (int i = 0; i < text.length(); i++) {
            if (s.length() <= b)
                break;

            if (text.charAt(i) == '_') {
                sub = !sub;
            } else if (text.charAt(i) == '*') {
                bold = !bold;
            } else if (text.charAt(i) == '%') {
                ital = !ital;
            } else if (text.charAt(i) == '$') {
                smal = !smal;
            } else if (text.charAt(i) == '`') {
                und = !und;
            } else {
                if (sub)
                    trig.addAttribute(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUB, b, b + 1);
                if (bold)
                    trig.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD, b, b + 1);
                if (ital)
                    trig.addAttribute(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE, b, b + 1);
                if (smal)
                    trig.addAttribute(TextAttribute.SIZE, 18, b, b + 1);
                if (und)
                    trig.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON, b, b + 1);
                b++;
            }
        }
        // Get the FontMetrics
        FontMetrics metrics = g.getFontMetrics(font);
        // Determine the X coordinate for the text
        int x = _x - (int)(GetWidthOfAttributedString((Graphics2D)g, trig) / 2);
        // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
        int y = _y - ((metrics.getHeight()) / 2) + metrics.getAscent();
        // Set the font
        //g.setFont(font);
        // Draw the String
        g.drawString(trig.getIterator(), x, y);
    }

    public void save()
    {
        saved = true;
        BufferedImage bImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D cg = bImg.createGraphics();
        paint(cg);
        try {
                if (ImageIO.write(bImg, "png", new File("./output_image.png")))
                {
                    System.out.println("-- saved");
                }
        } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }
    }
 
    public static void main(String args[]) {
        if (args.length == 0) {
            System.out.println("No input file found!");
            return;
        } else {
            System.out.println("Drawing tree for: " + args[0]);
        }

        NS = Interpreter.interpret(args[0]);
        height = (NS.getDepth() + 4) * spacingY;
        width = (NS.getWidth() + 2) * spacingX;

        JFrame f = new JFrame("JSyntaxTree");
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {System.exit(0);}
        });
        JApplet applet = new Main();
        f.getContentPane().add("Center", applet);
        applet.init();
        f.setSize(new Dimension(width,height));
        f.setVisible(true);
    }
 
}