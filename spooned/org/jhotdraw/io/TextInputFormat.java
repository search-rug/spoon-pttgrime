/* @(#)TextInputFormat.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.io;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.TextHolderFigure;
/**
 * An input format for importing text into a drawing.
 *
 * <p>This class uses the prototype design pattern. A TextHolderFigure figure is used as a prototype
 * for creating a figure that holds the imported text.
 *
 * <p>For text that spans multiple lines, TextInputFormat can either add all the text to the same
 * Figure, or it can create a new Figure for each line.
 *
 * <p><hr> <b>Design Patterns</b>
 *
 * <p><em>Prototype</em><br>
 * The text input format creates new text holder figures by cloning a prototype figure object and
 * assigning an image to it, which was read from data input. That's the reason why {@code Figure}
 * extends the {@code Cloneable} interface. <br>
 * Prototype: {@link TextHolderFigure}; Client: {@link org.jhotdraw.io.TextInputFormat}. <hr>
 */
public class TextInputFormat implements org.jhotdraw.draw.io.InputFormat {
    /**
     * The prototype for creating a figure that holds the imported text.
     */
    private org.jhotdraw.draw.figure.TextHolderFigure prototype;

    /**
     * Format description used for the file filter.
     */
    private java.lang.String description;

    /**
     * File name extension used for the file filter.
     */
    private java.lang.String fileExtension;

    /**
     * Image IO image format name.
     */
    private java.lang.String formatName;

    /**
     * This should be set to true for ImageHolderFigures that can hold multiple lines of text.
     */
    private boolean isMultiline;

    /**
     * Creates a new image output format for text, for a figure that can not. hold multiple lines of
     * text.
     */
    public TextInputFormat(org.jhotdraw.draw.figure.TextHolderFigure prototype) {
        this(prototype, "Text", "Text", "txt", false);
    }

    /**
     * Creates a new image output format for the specified image format.
     *
     * @param formatName
     * 		The format name for the javax.imageio.ImageIO object.
     * @param description
     * 		The format description to be used for the file filter.
     * @param fileExtension
     * 		The file extension to be used for file filter.
     * @param isMultiline
     * 		Set this to true, if the TextHolderFigure can hold multiple lines of text.
     * 		If this is true, multiple lines of text are added to the same figure. If this is false, a
     * 		new Figure is created for each line of text.
     */
    public TextInputFormat(org.jhotdraw.draw.figure.TextHolderFigure prototype, java.lang.String formatName, java.lang.String description, java.lang.String fileExtension, boolean isMultiline) {
        this.prototype = prototype;
        this.formatName = formatName;
        this.description = description;
        this.fileExtension = fileExtension;
        this.isMultiline = isMultiline;
    }

    @java.lang.Override
    public javax.swing.filechooser.FileFilter getFileFilter() {
        return new javax.swing.filechooser.FileNameExtensionFilter(description, fileExtension);
    }

    public java.lang.String getFileExtension() {
        return fileExtension;
    }

    @java.lang.Override
    public void read(java.io.InputStream in, org.jhotdraw.draw.Drawing drawing, boolean replace) throws java.io.IOException {
        if (replace) {
            drawing.removeAllChildren();
        }
        drawing.basicAddAll(0, createTextHolderFigures(in));
    }

    public java.util.List<org.jhotdraw.draw.figure.Figure> createTextHolderFigures(java.io.InputStream in) throws java.io.IOException {
        java.util.List<org.jhotdraw.draw.figure.Figure> list = new java.util.ArrayList<>();
        java.io.BufferedReader r = new java.io.BufferedReader(new java.io.InputStreamReader(in, "UTF8"));
        if (isMultiline) {
            org.jhotdraw.draw.figure.TextHolderFigure figure = ((org.jhotdraw.draw.figure.TextHolderFigure) (prototype.clone()));
            java.lang.StringBuilder buf = new java.lang.StringBuilder();
            for (java.lang.String line = null; line != null; line = r.readLine()) {
                if (buf.length() != 0) {
                    buf.append('\n');
                }
                buf.append(line);
            }
            figure.setText(buf.toString());
            org.jhotdraw.geom.Dimension2DDouble s = figure.getPreferredSize(1.0);
            figure.setBounds(new java.awt.geom.Point2D.Double(0, 0), new java.awt.geom.Point2D.Double(s.width, s.height));
        } else {
            double y = 0;
            for (java.lang.String line = null; line != null; line = r.readLine()) {
                org.jhotdraw.draw.figure.TextHolderFigure figure = ((org.jhotdraw.draw.figure.TextHolderFigure) (prototype.clone()));
                figure.setText(line);
                org.jhotdraw.geom.Dimension2DDouble s = figure.getPreferredSize(1.0);
                figure.setBounds(new java.awt.geom.Point2D.Double(0, y), new java.awt.geom.Point2D.Double(s.width, s.height));
                list.add(figure);
                y += s.height;
            }
        }
        if (list.size() == 0) {
            throw new java.io.IOException("No text found");
        }
        return list;
    }

    @java.lang.Override
    public boolean isDataFlavorSupported(java.awt.datatransfer.DataFlavor flavor) {
        return flavor.equals(java.awt.datatransfer.DataFlavor.stringFlavor);
    }

    @java.lang.Override
    public void read(java.awt.datatransfer.Transferable t, org.jhotdraw.draw.Drawing drawing, boolean replace) throws java.awt.datatransfer.UnsupportedFlavorException, java.io.IOException {
        java.lang.String text = ((java.lang.String) (t.getTransferData(java.awt.datatransfer.DataFlavor.stringFlavor)));
        java.util.List<org.jhotdraw.draw.figure.Figure> list = new java.util.ArrayList<>();
        if (isMultiline) {
            org.jhotdraw.draw.figure.TextHolderFigure figure = ((org.jhotdraw.draw.figure.TextHolderFigure) (prototype.clone()));
            figure.setText(text);
            org.jhotdraw.geom.Dimension2DDouble s = figure.getPreferredSize(1.0);
            figure.willChange();
            figure.setBounds(new java.awt.geom.Point2D.Double(0, 0), new java.awt.geom.Point2D.Double(s.width, s.height));
            figure.changed();
            list.add(figure);
        } else {
            double y = 0;
            for (java.lang.String line : text.split("\n")) {
                org.jhotdraw.draw.figure.TextHolderFigure figure = ((org.jhotdraw.draw.figure.TextHolderFigure) (prototype.clone()));
                figure.setText(line);
                org.jhotdraw.geom.Dimension2DDouble s = figure.getPreferredSize(1.0);
                y += s.height;
                figure.willChange();
                figure.setBounds(new java.awt.geom.Point2D.Double(0, 0 + y), new java.awt.geom.Point2D.Double(s.width, s.height + y));
                figure.changed();
                list.add(figure);
            }
        }
        if (replace) {
            drawing.removeAllChildren();
        }
        drawing.addAll(list);
    }
}