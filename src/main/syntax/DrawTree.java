package syntax;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextAttribute;
import java.awt.geom.Line2D;
import java.awt.geom.QuadCurve2D;
import java.text.AttributedString;
import java.util.ArrayList;

enum Alignment {
    CENTER,
    LEFT,
    RIGHT
}

public class DrawTree {
    private int fontSize;
    private int spacingX;
    private int spacingY;
    private int border;
    private BasicStroke stroke;
    private Font font;
	private boolean curved_movement;

    private int height;
    private int width;

    private Node NS;

    public DrawTree(Node NS, int fontSize, int spacingX, int spacingY, int border, String font_name,
            float strokeWeight, boolean curved_movement) {
        this.NS = NS;
        this.font = new Font(font_name, Font.PLAIN, fontSize);
        this.fontSize = fontSize;
        this.spacingX = spacingX;
        this.spacingY = spacingY;
        this.border = border;
        this.stroke = new BasicStroke(strokeWeight);
		this.curved_movement = curved_movement;

        height = NS.getDepth(this) + border * 2 + spacingY;
        width = NS.getWidth(this, null) + border * 2;
    }

    public void paintStatic(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setPaint(Color.BLACK);

        NS.resetPasses();

        paintNode(border, border + fontSize / 2, g2, NS);
    }

    public int paintNode(int _x, int _y, Graphics2D g2, Node n) {
        int center_x = _x + n.getWidth(this, g2) / 2;

        ArrayList<Integer> x_vals = new ArrayList<Integer>();

        if (!n.subNodes.isEmpty()) {
            int x = _x;
            for (Node sub : n.subNodes) {
                g2.setStroke(stroke);
                x_vals.add(paintNode(x, _y + spacingY, g2, sub));
                x += sub.getWidth(this, g2);
            }
        } else if (!n.metadata.isEmpty()) {
            int bracket_y = 0;
            switch (n.mode) {
            case TRIANGLE_:
                g2.setStroke(stroke);
                g2.setPaint(n.connector_color);
                g2.draw(new Line2D.Float(center_x, _y + (int) ((float) fontSize),
                        center_x - n.getWidth(this, g2) / 2 + spacingX / 2,
                        _y + spacingY - (int) ((float) fontSize / 1.25)));
                g2.draw(new Line2D.Float(center_x, _y + (int) ((float) fontSize),
                        center_x + n.getWidth(this, g2) / 2 - spacingX / 2,
                        _y + spacingY - (int) ((float) fontSize / 1.25)));
                g2.draw(new Line2D.Float(center_x - n.getWidth(this, g2) / 2 + spacingX / 2,
                        _y + spacingY - (int) ((float) fontSize / 1.25), center_x + n.getWidth(this, g2) / 2 - spacingX / 2,
                        _y + spacingY - (int) ((float) fontSize / 1.25)));
                g2.setPaint(n.content_color);
                drawAlignedString(center_x, _y + spacingY, g2, n.metadata, Alignment.CENTER);
                bracket_y = _y + spacingY;
                break;
            case BAR_:
                g2.setStroke(stroke);
                g2.setPaint(n.connector_color);
                g2.draw(new Line2D.Float(center_x, _y + fontSize, center_x,
                        _y + spacingY - (int) ((float) fontSize / 1.25)));
                g2.setPaint(n.content_color);
                drawAlignedString(center_x, _y + spacingY, g2, n.metadata, Alignment.CENTER);
                bracket_y = _y + spacingY;
                break;
            case NONE_:
                g2.setPaint(n.content_color);
                drawAlignedString(center_x, (int) (_y + fontSize * 1.5), g2, n.metadata, Alignment.CENTER);
                bracket_y = (int) (_y + fontSize * 1.5);
                break;
            }

            if (n.bracket == BRACKET.SQUARE_BRACKET) {
                switch (n.metadata.split("\\\\n").length){
                    case 1:
                        drawAlignedString(center_x - n.getWidth(this, g2) / 2 + (int)(JSyntaxTree.GetWidthOfAttributedString(getTrig("["), g2) * 0.5), bracket_y, g2, "[", Alignment.LEFT);
                        drawAlignedString(center_x + n.getWidth(this, g2) / 2 - (int)(JSyntaxTree.GetWidthOfAttributedString(getTrig("]"), g2) * 1.5), bracket_y, g2, "]", Alignment.LEFT);
                        break;
                    case 2:
                        drawAlignedString(center_x - n.getWidth(this, g2) / 2 + (int)(JSyntaxTree.GetWidthOfAttributedString(getTrig("\u23a1"), g2) * 0.5), bracket_y, g2, "\u23a1\\n\u23a3", Alignment.LEFT);
                        drawAlignedString(center_x + n.getWidth(this, g2) / 2 - (int)(JSyntaxTree.GetWidthOfAttributedString(getTrig("\u23a4"), g2) * 1.5), bracket_y, g2, "\u23a4\\n\u23a6", Alignment.LEFT);
                        break;
                    default:
                        String bl = "\u23a1\\n";
                        String br = "\u23a4\\n";
                        for (int i = 2; i < n.metadata.split("\\\\n").length; i++) {
                            bl += "\u23a2\\n";
                            br += "\u23a5\\n";
                        }
                        bl += "\u23a3\\n";
                        br += "\u23a6\\n";
                        drawAlignedString(center_x - n.getWidth(this, g2) / 2 + (int)(JSyntaxTree.GetWidthOfAttributedString(getTrig("\u23a1"), g2) * 0.5), (int) (_y + fontSize * 1.5), g2, bl, Alignment.LEFT);
                        drawAlignedString(center_x + n.getWidth(this, g2) / 2 - (int)(JSyntaxTree.GetWidthOfAttributedString(getTrig("\u23a4"), g2) * 1.25), (int) (_y + fontSize * 1.5), g2, br, Alignment.LEFT);
                }
            }
            else if (n.bracket == BRACKET.PARENTHESIS) {
                switch (n.metadata.split("\\\\n").length){
                    case 1:
                        drawAlignedString(center_x - n.getWidth(this, g2) / 2 + (int)(JSyntaxTree.GetWidthOfAttributedString(getTrig("["), g2) * 0.5), bracket_y, g2, "(", Alignment.LEFT);
                        drawAlignedString(center_x + n.getWidth(this, g2) / 2 - (int)(JSyntaxTree.GetWidthOfAttributedString(getTrig("]"), g2) * 1.25), bracket_y, g2, ")", Alignment.LEFT);
                        break;
                    case 2:
                        drawAlignedString(center_x - n.getWidth(this, g2) / 2 + (int)(JSyntaxTree.GetWidthOfAttributedString(getTrig("\u239b"), g2) * 0.5), bracket_y, g2, "\u239b\\n\u239d", Alignment.LEFT);
                        drawAlignedString(center_x + n.getWidth(this, g2) / 2 - (int)(JSyntaxTree.GetWidthOfAttributedString(getTrig("\u239e"), g2) * 1.25), bracket_y, g2, "\u239e\\n\u23a0", Alignment.LEFT);
                        break;
                    default:
                        String bl = "\u239b\\n";
                        String br = "\u239e\\n";
                        for (int i = 2; i < n.metadata.split("\\\\n").length; i++) {
                            bl += "\u239c\\n";
                            br += "\u239f\\n";
                        }
                        bl += "\u239d\\n";
                        br += "\u23a0\\n";
                        drawAlignedString(center_x - n.getWidth(this, g2) / 2 + (int)(JSyntaxTree.GetWidthOfAttributedString(getTrig("\u239b"), g2) * 0.5), (int) (_y + fontSize * 1.5), g2, bl, Alignment.LEFT);
                        drawAlignedString(center_x + n.getWidth(this, g2) / 2 - (int)(JSyntaxTree.GetWidthOfAttributedString(getTrig("\u239e"), g2) * 1.25), (int) (_y + fontSize * 1.5), g2, br, Alignment.LEFT);
                }
            }
        }

        int avg_x = center_x;

        if (!x_vals.isEmpty()) {
            avg_x = (x_vals.get(0) + x_vals.get(x_vals.size() - 1)) / 2;
            for (int i = 0; i < x_vals.size(); i++) {
                int x = x_vals.get(i);
                g2.setPaint(n.subNodes.get(i).connector_color);
                g2.draw(new Line2D.Float(avg_x, _y + (int) ((float) fontSize), x,
                        _y + spacingY - (int) ((float) fontSize / 1.25)));
            }
        }

        if (n.raises.length > 0) {
            for (int i = 0; i < n.raises.length; i++) {
                Node end = n;
                for (int j = 0; j < Math.abs(n.raises[i]); j++) {
                    if (end == null)
                        System.err.println("Cannot find endnode!");
                    end = end.getNeighborLeft();
                }
                for (int j = 0; j < n.raisesSUB[i]; j++) {
                    if (end == null)
                        System.err.println("Cannot find endnode!");
                    end = end.parent;
                }
                drawMovement(n, end, g2, n.raises[i] > 0);
            }
        }

        g2.setPaint(n.color);
        drawAlignedString(avg_x, _y, g2, n.value, Alignment.CENTER);

        return avg_x;
    }

    public void drawMovement(Node start, Node end, Graphics2D g2, boolean inOut) {
        int shiftY = 0;
        Node max_current = end;
        int depth_max = 0;
        if (end.subNodes.isEmpty())
            depth_max = end.getTotalY(this) + end.getDepth(this);
        Node current = start;

        if (!end.subNodes.isEmpty()) {
            boolean crossed = false;
            while (!crossed || (crossed && current.hasAncestor(end))) {
                if (depth_max < current.getTotalY(this) + current.getDepth(this)) {
                    depth_max = current.getTotalY(this) + current.getDepth(this);
                    max_current = current;
                }
                if (!crossed && current.hasAncestor(end))
                    crossed = true;
                if (current.getNeighborLeft() == null)
                    break;
                current = current.getNeighborLeft();
            }
        } else {
            while (current != end) {
                if (depth_max < current.getTotalY(this) + current.getDepth(this)) {
                    depth_max = current.getTotalY(this) + current.getDepth(this);
                    max_current = current;
                }
                current = current.getNeighborLeft();
            }
        }

        shiftY = max_current.passes * fontSize / 2;
        max_current.passes++;
        depth_max += shiftY + fontSize;

        int startX = start.getTotalX(this, g2) - fontSize / 4;
        int endX = end.getTotalX(this, g2) + fontSize / 4;
        int startY = start.getTotalY(this) + start.getDepth(this);// + (int)((float)fontSize * 1.5);
        int endY = end.getTotalY(this) + end.getDepth(this);// + (int)((float)fontSize * 1.5);

        if (inOut)
            g2.setPaint(start.move_color);
        else
            g2.setPaint(end.move_color);

        if (!start.subNodes.isEmpty()) {
            g2.draw(new Line2D.Float(startX - start.getWidth(this, g2) / 2, startY, startX - start.getWidth(this, g2) / 2,
                    startY - fontSize / 2));
            g2.draw(new Line2D.Float(startX + start.getWidth(this, g2) / 2, startY, startX + start.getWidth(this, g2) / 2,
                    startY - fontSize / 2));
            g2.draw(new Line2D.Float(startX - start.getWidth(this, g2) / 2, startY, startX + start.getWidth(this, g2) / 2,
                    startY));
        }

        if (!end.subNodes.isEmpty()) {
            g2.draw(new Line2D.Float(endX - end.getWidth(this, g2) / 2, endY, endX - end.getWidth(this, g2) / 2,
                    endY - fontSize / 2));
            g2.draw(new Line2D.Float(endX + end.getWidth(this, g2) / 2, endY, endX + end.getWidth(this, g2) / 2,
                    endY - fontSize / 2));
            g2.draw(new Line2D.Float(endX - end.getWidth(this, g2) / 2, endY, endX + end.getWidth(this, g2) / 2, endY));
        }
		
		if (curved_movement)
        	g2.draw(new QuadCurve2D.Float(startX, startY, (startX + endX) / 2, depth_max + Math.max(spacingY, Math.abs(startY - endY)), endX, endY));
        else {
			g2.draw(new Line2D.Float(startX, startY + fontSize / 7, startX, depth_max));
        	g2.draw(new Line2D.Float(startX, depth_max, endX, depth_max));
        	g2.draw(new Line2D.Float(endX, endY + fontSize / 7, endX, depth_max));
		}

        if (inOut) {
            int[] x_points = { endX, endX - fontSize / 6, endX + fontSize / 6 };
            int[] y_points = { endY, endY + fontSize / 4, endY + fontSize / 4 };

            g2.fillPolygon(x_points, y_points, 3);
        } else {
            int[] x_points = { startX, startX - fontSize / 6, startX + fontSize / 6 };
            int[] y_points = { startY, startY + fontSize / 4, startY + fontSize / 4 };

            g2.fillPolygon(x_points, y_points, 3);
        }
    }

    public void drawAlignedString(int _x, int _y, Graphics2D g2, String text, Alignment align) {
        String[] arr = text.split("\\\\n");
        if (arr.length > 1) {
            for (int i = 0; i < arr.length; i++) {
                drawAlignedString(_x, _y + i * (int) (fontSize), g2, arr[i], align);
            }
            return;
        }

        AttributedString trig = getTrig(text);

        FontMetrics metrics = g2.getFontMetrics(font);
        int x = _x - (int) (JSyntaxTree.GetWidthOfAttributedString(trig, g2) / 2);
        if (align == Alignment.LEFT)
            x = _x;
        if (align == Alignment.RIGHT)
            x = _x - (int) (JSyntaxTree.GetWidthOfAttributedString(trig, g2));
        int y = _y - ((metrics.getHeight()) / 2) + metrics.getAscent();
        g2.drawString(trig.getIterator(), x, y);
    }

    public AttributedString getTrig(String text) {
        String s = text;
        s = s.replaceAll("([^?=\\\\])\\_", "$1");
        s = s.replaceAll("^\\_", "");
        s = s.replaceAll("([^?=\\\\])\\*", "$1");
        s = s.replaceAll("^\\*", "");
        s = s.replaceAll("([^?=\\\\])\\%", "$1");
        s = s.replaceAll("^\\%", "");
        s = s.replaceAll("([^?=\\\\])\\$", "$1");
        s = s.replaceAll("^\\$", "");
        s = s.replaceAll("([^?=\\\\])\\-", "$1");
        s = s.replaceAll("^\\-", "");
        s = s.replaceAll("([^?=\\\\])\\#", "$1");
        s = s.replaceAll("^\\#", "");
        s = s.replaceAll("([^?=\\\\])\\=", "$1");
        s = s.replaceAll("^\\=", "");

        s = s.replaceAll("\\\\\\_", "_");
        s = s.replaceAll("\\\\\\*", "*");
        s = s.replaceAll("\\\\\\%", "%");
        s = s.replaceAll("\\\\\\$", "\\$");
        s = s.replaceAll("\\\\\\-", "\\-");
        s = s.replaceAll("\\\\\\#", "#");
        s = s.replaceAll("\\\\\\=", "=");
        s = s.replaceAll("\\\\\\\\", "\\\\");

        AttributedString trig = new AttributedString(s);
        trig.addAttribute(TextAttribute.FAMILY, font.getFamily());
        trig.addAttribute(TextAttribute.SIZE, font.getSize());

        boolean sub = false;
        boolean bold = false;
        boolean ital = false;
        boolean smal = false;
        boolean und = false;
        boolean green = false;
        boolean strike = false;
        int b = 0;
        char prev = 0;
        for (int i = 0; i < text.length(); i++) {
            if (prev != '\\' && text.charAt(i) == '_') {
                sub = !sub;
            } else if (prev != '\\' && text.charAt(i) == '*') {
                bold = !bold;
            } else if (prev != '\\' && text.charAt(i) == '%') {
                ital = !ital;
            } else if (prev != '\\' && text.charAt(i) == '$') {
                smal = !smal;
            } else if (prev != '\\' && text.charAt(i) == '-') {
                und = !und;
            } else if (prev != '\\' && text.charAt(i) == '#') {
                green = !green;
            } else if (prev != '\\' && text.charAt(i) == '=') {
                strike = !strike;
            } else if (text.charAt(i) != '\\' || (prev == '\\' && text.charAt(i) == '\\')) {
                if (s.length() <= b)
                    continue;
                if (sub)
                    trig.addAttribute(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUB, b, b + 1);
                if (bold)
                    trig.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD, b, b + 1);
                if (ital)
                    trig.addAttribute(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE, b, b + 1);
                if (smal)
                    trig.addAttribute(TextAttribute.SIZE, (int) ((float) fontSize * 0.70), b, b + 1);
                if (und)
                    trig.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON, b, b + 1);
                if (green)
                    trig.addAttribute(TextAttribute.SWAP_COLORS, TextAttribute.SWAP_COLORS_ON, b, b + 1);
                if (strike)
                    trig.addAttribute(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON, b, b + 1);
                b++;
            }
            prev = text.charAt(i);
        }

        if (sub || bold || ital || smal || und || green || strike)
            System.out.println("Warning: unclosed formatting\n" + text);

        return trig;
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public int getSpacingX() {
        return this.spacingX;
    }

    public int getSpacingY() {
        return this.spacingY;
    }

    public int getFontSize() {
        return this.fontSize;
    }

    public int getBorder() {
        return this.border;
    }
}