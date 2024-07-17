/* @(#)TextAreaFigure.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.figure;
/**
 * A {@code TextHolderFigure} which holds multiple lines of text in a rectangular area.
 *
 * <p>It automatically rearranges the text to fit its allocated display area, breaking the lines at
 * word boundaries whenever possible.<br>
 * The text can contain either LF or CRLF sequences to separate paragraphs, as well as tab
 * characters for table like formatting and alignment.<br>
 * Currently the tabs are distributed at regular intervals as determined by the TabSize property.
 * Tabs align correctly with either fixed or variable fonts.<br>
 * If, when resizing, the vertical size of the display box is not enough to display all the text,
 * TextAreaFigure displays a dashed red line at the bottom of the figure to indicate there is hidden
 * text.<br>
 * TextAreFigure uses all standard attributes for the area Rectangle2D.Double, ie: FillColor,
 * PenColor for the border, FontSize, FontStyle, and FontName, as well as four additional attributes
 * LeftMargin, RightMargin, TopMargin, and TabSize.<br>
 *
 * <p>A DrawingEditor should provide the TextAreaCreationTool to create a TextAreaFigure.
 *
 * <p>FIXME - TextAreaFigure should not draw a rectangle on its own but rather rely on a decorator.
 * We probably need a DecoratorConnector for this and we need a way to specify the inner bounds of
 * the decorator. We also need a way to center the text of the TextAreaFigure verticaly and
 * horizontaly.
 *
 * @author Eduardo Francos - InContext (original version), Werner Randelshofer (this derived
version)
 * @version $Id$
 */
public class TextAreaFigure extends org.jhotdraw.draw.figure.AbstractAttributedDecoratedFigure implements org.jhotdraw.draw.figure.TextHolderFigure {
    private static final long serialVersionUID = 1L;

    protected java.awt.geom.Rectangle2D.Double bounds = new java.awt.geom.Rectangle2D.Double();

    protected boolean editable = true;

    private static final java.awt.BasicStroke DASHES = new java.awt.BasicStroke(1.0F, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_BEVEL, 0.0F, new float[]{ 4.0F, 4.0F }, 0.0F);

    /**
     * This is a cached value to improve the performance of method isTextOverflow();
     */
    private java.lang.Boolean isTextOverflow;

    public TextAreaFigure() {
        this(org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels").getString("TextFigure.defaultText"));
    }

    public TextAreaFigure(java.lang.String text) {
        setText(text);
    }

    // DRAWING
    @java.lang.Override
    protected void drawText(java.awt.Graphics2D g) {
        if ((getText() != null) || isEditable()) {
            java.awt.Font font = getFont();
            boolean isUnderlined = attr().get(org.jhotdraw.draw.AttributeKeys.FONT_UNDERLINE);
            org.jhotdraw.geom.Insets2D.Double insets = getInsets();
            java.awt.geom.Rectangle2D.Double textRect = new java.awt.geom.Rectangle2D.Double(bounds.x + insets.left, bounds.y + insets.top, (bounds.width - insets.left) - insets.right, (bounds.height - insets.top) - insets.bottom);
            java.awt.Graphics2D g2 = ((java.awt.Graphics2D) (g.create()));
            if ((g2.getTransform().getScaleY() * g2.getTransform().getScaleX()) < 0) {
                java.awt.geom.AffineTransform at = new java.awt.geom.AffineTransform();
                at.translate(0, bounds.y - insets.bottom);
                at.scale(1, -1);
                at.translate(0, ((-bounds.y) - bounds.height) - insets.bottom);
                g2.transform(at);
            }
            float leftMargin = ((float) (textRect.x));
            float rightMargin = ((float) (java.lang.Math.max(leftMargin + 1, (textRect.x + textRect.width) + 1)));
            float verticalPos = ((float) (textRect.y));
            float maxVerticalPos = ((float) (textRect.y + textRect.height));
            if (leftMargin < rightMargin) {
                // float tabWidth = (float) (getTabSize() * g.getFontMetrics(font).charWidth('m'));
                float tabWidth = ((float) (getTabSize() * font.getStringBounds("m", getFontRenderContext()).getWidth()));
                float[] tabStops = new float[((int) (textRect.width / tabWidth))];
                for (int i = 0; i < tabStops.length; i++) {
                    tabStops[i] = ((float) (textRect.x + ((int) (tabWidth * (i + 1)))));
                }
                if (getText() != null) {
                    java.awt.Shape savedClipArea = g.getClip();
                    g2.clip(textRect);
                    java.lang.String[] paragraphs = getText().split("\n");// Strings.split(getText(), '\n');

                    for (int i = 0; i < paragraphs.length; i++) {
                        if (paragraphs[i].length() == 0) {
                            paragraphs[i] = " ";
                        }
                        java.text.AttributedString as = new java.text.AttributedString(paragraphs[i]);
                        as.addAttribute(java.awt.font.TextAttribute.FONT, font);
                        if (isUnderlined) {
                            as.addAttribute(java.awt.font.TextAttribute.UNDERLINE, java.awt.font.TextAttribute.UNDERLINE_LOW_ONE_PIXEL);
                        }
                        int tabCount = paragraphs[i].split("\t").length - 1;
                        java.awt.geom.Rectangle2D.Double paragraphBounds = drawParagraph(g2, as.getIterator(), verticalPos, maxVerticalPos, leftMargin, rightMargin, tabStops, tabCount);
                        verticalPos = ((float) (paragraphBounds.y + paragraphBounds.height));
                        if (verticalPos > maxVerticalPos) {
                            break;
                        }
                    }
                    g2.setClip(savedClipArea);
                }
            }
            g2.dispose();
        }
    }

    /**
     * Draws or measures a paragraph of text at the specified y location and the bounds of the
     * paragraph.
     *
     * @param g
     * 		Graphics object. This parameter is null, if we want to measure the size of the
     * 		paragraph.
     * @param styledText
     * 		the text of the paragraph.
     * @param verticalPos
     * 		the top bound of the paragraph
     * @param maxVerticalPos
     * 		the bottom bound of the paragraph
     * @param leftMargin
     * 		the left bound of the paragraph
     * @param rightMargin
     * 		the right bound of the paragraph
     * @param tabStops
     * 		an array with tab stops
     * @param tabCount
     * 		the number of entries in tabStops which contain actual values
     * @return Returns the actual bounds of the paragraph.
     */
    private java.awt.geom.Rectangle2D.Double drawParagraph(java.awt.Graphics2D g, java.text.AttributedCharacterIterator styledText, float verticalPos, float maxVerticalPos, float leftMargin, float rightMargin, float[] tabStops, int tabCount) {
        // This method is based on the code sample given
        // in the class comment of java.awt.font.LineBreakMeasurer,
        // assume styledText is an AttributedCharacterIterator, and the number
        // of tabs in styledText is tabCount
        java.awt.geom.Rectangle2D.Double paragraphBounds = new java.awt.geom.Rectangle2D.Double(leftMargin, verticalPos, 0, 0);
        int[] tabLocations = new int[tabCount + 1];
        int i = 0;
        for (char c = styledText.first(); c != java.text.AttributedCharacterIterator.DONE; c = styledText.next()) {
            if (c == '\t') {
                tabLocations[i++] = styledText.getIndex();
            }
        }
        tabLocations[tabCount] = styledText.getEndIndex() - 1;
        // Now tabLocations has an entry for every tab's offset in
        // the text.  For convenience, the last entry is tabLocations
        // is the offset of the last character in the text.
        java.awt.font.LineBreakMeasurer measurer = new java.awt.font.LineBreakMeasurer(styledText, getFontRenderContext());
        int currentTab = 0;
        while ((measurer.getPosition() < styledText.getEndIndex()) && (verticalPos <= maxVerticalPos)) {
            // Lay out and draw each line.  All segments on a line
            // must be computed before any drawing can occur, since
            // we must know the largest ascent on the line.
            // TextLayouts are computed and stored in a List;
            // their horizontal positions are stored in a parallel
            // List.
            // lineContainsText is true after first segment is drawn
            boolean lineContainsText = false;
            boolean lineComplete = false;
            float maxAscent = 0;
            float maxDescent = 0;
            float horizontalPos = leftMargin;
            java.util.List<java.awt.font.TextLayout> layouts = new java.util.ArrayList<>();
            java.util.List<java.lang.Float> penPositions = new java.util.ArrayList<>();
            int first = layouts.size();
            while ((!lineComplete) && (verticalPos <= maxVerticalPos)) {
                float wrappingWidth = rightMargin - horizontalPos;
                java.awt.font.TextLayout layout = null;
                layout = measurer.nextLayout(wrappingWidth, tabLocations[currentTab] + 1, lineContainsText);
                // layout can be null if lineContainsText is true
                if (layout != null) {
                    layouts.add(layout);
                    penPositions.add(horizontalPos);
                    horizontalPos += layout.getAdvance();
                    maxAscent = java.lang.Math.max(maxAscent, layout.getAscent());
                    maxDescent = java.lang.Math.max(maxDescent, layout.getDescent() + layout.getLeading());
                } else {
                    lineComplete = true;
                }
                lineContainsText = true;
                if (measurer.getPosition() == (tabLocations[currentTab] + 1)) {
                    currentTab++;
                }
                if (measurer.getPosition() == styledText.getEndIndex()) {
                    lineComplete = true;
                } else if ((tabStops.length == 0) || (horizontalPos >= tabStops[tabStops.length - 1])) {
                    lineComplete = true;
                }
                if (!lineComplete) {
                    // move to next tab stop
                    int j;
                    for (j = 0; horizontalPos >= tabStops[j]; j++) {
                    }
                    horizontalPos = tabStops[j];
                }
            } 
            // If there is only one layout element on the line, and we are
            // drawing, then honor alignment
            if ((first == (layouts.size() - 1)) && (g != null)) {
                switch (attr().get(org.jhotdraw.draw.AttributeKeys.TEXT_ALIGNMENT)) {
                    case TRAILING :
                        penPositions.set(first, (rightMargin - layouts.get(first).getVisibleAdvance()) - 1);
                        break;
                    case CENTER :
                        penPositions.set(first, ((((rightMargin - 1) - leftMargin) - layouts.get(first).getVisibleAdvance()) / 2) + leftMargin);
                        break;
                    case BLOCK :
                        // not supported
                        break;
                    case LEADING :
                    default :
                        break;
                }
            }
            verticalPos += maxAscent;
            java.util.Iterator<java.awt.font.TextLayout> layoutEnum = layouts.iterator();
            java.util.Iterator<java.lang.Float> positionEnum = penPositions.iterator();
            // now iterate through layouts and draw them
            while (layoutEnum.hasNext()) {
                java.awt.font.TextLayout nextLayout = layoutEnum.next();
                float nextPosition = positionEnum.next();
                if (g != null) {
                    nextLayout.draw(g, nextPosition, verticalPos);
                }
                java.awt.geom.Rectangle2D layoutBounds = nextLayout.getBounds();
                paragraphBounds.add(new java.awt.geom.Rectangle2D.Double(layoutBounds.getX() + nextPosition, layoutBounds.getY() + verticalPos, layoutBounds.getWidth(), layoutBounds.getHeight()));
            } 
            verticalPos += maxDescent;
        } 
        return paragraphBounds;
    }

    @java.lang.Override
    protected void drawFill(java.awt.Graphics2D g) {
        g.fill(bounds);
    }

    @java.lang.Override
    protected void drawStroke(java.awt.Graphics2D g) {
        g.draw(bounds);
    }

    // SHAPE AND BOUNDS
    @java.lang.Override
    public void setBounds(java.awt.geom.Point2D.Double anchor, java.awt.geom.Point2D.Double lead) {
        bounds.x = java.lang.Math.min(anchor.x, lead.x);
        bounds.y = java.lang.Math.min(anchor.y, lead.y);
        bounds.width = java.lang.Math.max(1, java.lang.Math.abs(lead.x - anchor.x));
        bounds.height = java.lang.Math.max(1, java.lang.Math.abs(lead.y - anchor.y));
    }

    @java.lang.Override
    public void transform(java.awt.geom.AffineTransform tx) {
        java.awt.geom.Point2D.Double anchor = getStartPoint();
        java.awt.geom.Point2D.Double lead = getEndPoint();
        setBounds(((java.awt.geom.Point2D.Double) (tx.transform(anchor, anchor))), ((java.awt.geom.Point2D.Double) (tx.transform(lead, lead))));
    }

    @java.lang.Override
    public boolean figureContains(java.awt.geom.Point2D.Double p, double scale) {
        return bounds.contains(p);
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getBounds(double scale) {
        return ((java.awt.geom.Rectangle2D.Double) (bounds.getBounds2D()));
    }

    @java.lang.Override
    public void restoreTransformTo(java.lang.Object geometry) {
        java.awt.geom.Rectangle2D.Double r = ((java.awt.geom.Rectangle2D.Double) (geometry));
        bounds.x = r.x;
        bounds.y = r.y;
        bounds.width = r.width;
        bounds.height = r.height;
    }

    @java.lang.Override
    public java.lang.Object getTransformRestoreData() {
        return bounds.clone();
    }

    // ATTRIBUTES
    /**
     * Gets the text shown by the text figure.
     */
    @java.lang.Override
    public java.lang.String getText() {
        return attr().get(org.jhotdraw.draw.AttributeKeys.TEXT);
    }

    /**
     * Returns the insets used to draw text.
     */
    @java.lang.Override
    public org.jhotdraw.geom.Insets2D.Double getInsets() {
        double sw = java.lang.Math.ceil(attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_WIDTH) / 2);
        org.jhotdraw.geom.Insets2D.Double insets = new org.jhotdraw.geom.Insets2D.Double(4, 4, 4, 4);
        return new org.jhotdraw.geom.Insets2D.Double(insets.top + sw, insets.left + sw, insets.bottom + sw, insets.right + sw);
    }

    @java.lang.Override
    public double getBaseline() {
        return getFont().getLineMetrics(getText(), getFontRenderContext()).getAscent() + getInsets().top;
    }

    @java.lang.Override
    public int getTabSize() {
        return 8;
    }

    /**
     * Sets the text shown by the text figure.
     */
    @java.lang.Override
    public void setText(java.lang.String newText) {
        attr().set(org.jhotdraw.draw.AttributeKeys.TEXT, newText);
    }

    @java.lang.Override
    public int getTextColumns() {
        return getText() == null ? 4 : java.lang.Math.max(getText().length(), 4);
    }

    @java.lang.Override
    public java.awt.Font getFont() {
        return org.jhotdraw.draw.AttributeKeys.getFont(this);
    }

    @java.lang.Override
    public java.awt.Color getTextColor() {
        return attr().get(org.jhotdraw.draw.AttributeKeys.TEXT_COLOR);
    }

    @java.lang.Override
    public java.awt.Color getFillColor() {
        return attr().get(org.jhotdraw.draw.AttributeKeys.FILL_COLOR);
    }

    @java.lang.Override
    public void setFontSize(float size) {
        attr().set(org.jhotdraw.draw.AttributeKeys.FONT_SIZE, java.lang.Double.valueOf(size));
    }

    @java.lang.Override
    public float getFontSize() {
        return attr().get(org.jhotdraw.draw.AttributeKeys.FONT_SIZE).floatValue();
    }

    // EDITING
    @java.lang.Override
    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean b) {
        this.editable = b;
    }

    /**
     * Returns a specialized tool for the given coordinate.
     *
     * <p>Returns null, if no specialized tool is available.
     */
    @java.lang.Override
    public org.jhotdraw.draw.tool.Tool getTool(java.awt.geom.Point2D.Double p) {
        if (isEditable() && contains(p)) {
            org.jhotdraw.draw.tool.TextAreaEditingTool tool = new org.jhotdraw.draw.tool.TextAreaEditingTool(this);
            return tool;
        }
        return null;
    }

    @java.lang.Override
    public org.jhotdraw.draw.figure.TextHolderFigure getLabelFor() {
        return this;
    }

    // CONNECTING
    // COMPOSITE FIGURES
    // CLONING
    @java.lang.Override
    public org.jhotdraw.draw.figure.TextAreaFigure clone() {
        org.jhotdraw.draw.figure.TextAreaFigure that = ((org.jhotdraw.draw.figure.TextAreaFigure) (super.clone()));
        that.bounds = ((java.awt.geom.Rectangle2D.Double) (this.bounds.clone()));
        return that;
    }

    // EVENT HANDLING
    @java.lang.Override
    public java.util.Collection<org.jhotdraw.draw.handle.Handle> createHandles(int detailLevel) {
        java.util.Collection<org.jhotdraw.draw.handle.Handle> handles = super.createHandles(detailLevel);
        if (detailLevel == 0) {
            handles.add(new org.jhotdraw.draw.handle.FontSizeHandle(this));
            handles.add(new org.jhotdraw.draw.handle.TextOverflowHandle(this));
        }
        return handles;
    }

    @java.lang.Override
    public void invalidate() {
        super.invalidate();
        isTextOverflow = null;
    }

    @java.lang.Override
    public boolean isTextOverflow() {
        if (isTextOverflow == null) {
            org.jhotdraw.geom.Insets2D.Double insets = getInsets();
            isTextOverflow = getPreferredTextSize((getBounds().width - insets.left) - insets.right).height > ((getBounds().height - insets.top) - insets.bottom);
        }
        return isTextOverflow;
    }

    /**
     * Returns the preferred text size of the TextAreaFigure.
     *
     * <p>If you want to use this method to determine the bounds of the TextAreaFigure, you need to
     * add the insets of the TextAreaFigure to the size.
     *
     * @param maxWidth
     * 		the maximal width to use. Specify Double.MAX_VALUE if you want the width to be
     * 		unlimited.
     * @return width and height needed to lay out the text.
     */
    public org.jhotdraw.geom.Dimension2DDouble getPreferredTextSize(double maxWidth) {
        java.awt.geom.Rectangle2D.Double textRect = new java.awt.geom.Rectangle2D.Double();
        if (getText() != null) {
            java.awt.Font font = getFont();
            boolean isUnderlined = attr().get(org.jhotdraw.draw.AttributeKeys.FONT_UNDERLINE);
            float leftMargin = 0;
            float rightMargin = ((float) (maxWidth)) - 1;
            float verticalPos = 0;
            float maxVerticalPos = java.lang.Float.MAX_VALUE;
            if (leftMargin < rightMargin) {
                float tabWidth = ((float) (getTabSize() * font.getStringBounds("m", getFontRenderContext()).getWidth()));
                float[] tabStops = new float[((int) (textRect.width / tabWidth))];
                for (int i = 0; i < tabStops.length; i++) {
                    tabStops[i] = ((float) (textRect.x + ((int) (tabWidth * (i + 1)))));
                }
                java.lang.String[] paragraphs = getText().split("\n");// Strings.split(getText(), '\n');

                for (int i = 0; i < paragraphs.length; i++) {
                    if (paragraphs[i].length() == 0) {
                        paragraphs[i] = " ";
                    }
                    java.text.AttributedString as = new java.text.AttributedString(paragraphs[i]);
                    as.addAttribute(java.awt.font.TextAttribute.FONT, font);
                    if (isUnderlined) {
                        as.addAttribute(java.awt.font.TextAttribute.UNDERLINE, java.awt.font.TextAttribute.UNDERLINE_LOW_ONE_PIXEL);
                    }
                    int tabCount = paragraphs[i].split("\t").length - 1;
                    java.awt.geom.Rectangle2D.Double paragraphBounds = drawParagraph(null, as.getIterator(), verticalPos, maxVerticalPos, leftMargin, rightMargin, tabStops, tabCount);
                    verticalPos = ((float) (paragraphBounds.y + paragraphBounds.height));
                    textRect.add(paragraphBounds);
                }
            }
        }
        return new org.jhotdraw.geom.Dimension2DDouble((-java.lang.Math.min(textRect.x, 0)) + textRect.width, (-java.lang.Math.min(textRect.y, 0)) + textRect.height);
    }
}