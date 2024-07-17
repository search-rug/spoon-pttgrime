/* @(#)SVGTextArea.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.svg.figures;
/**
 * SVGTextArea.
 */
public class SVGTextAreaFigure extends org.jhotdraw.samples.svg.figures.SVGAttributedFigure implements org.jhotdraw.samples.svg.figures.SVGFigure , org.jhotdraw.draw.figure.TextHolderFigure {
    private static final long serialVersionUID = 1L;

    private java.awt.geom.Rectangle2D.Double bounds = new java.awt.geom.Rectangle2D.Double();

    private boolean editable = true;

    private static final java.awt.BasicStroke DASHES = new java.awt.BasicStroke(1.0F, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_BEVEL, 0.0F, new float[]{ 4.0F, 4.0F }, 0.0F);

    /**
     * This is a cached value to improve the performance of method isTextOverflow();
     */
    private java.lang.Boolean isTextOverflow;

    /**
     * This is used to perform faster drawing and hit testing.
     */
    private transient java.awt.geom.Rectangle2D.Double cachedDrawingArea;

    private transient java.awt.Shape cachedTextShape;

    public SVGTextAreaFigure() {
        this("Text");
    }

    public SVGTextAreaFigure(java.lang.String text) {
        setText(text);
        org.jhotdraw.samples.svg.SVGAttributeKeys.setDefaults(this);
        setConnectable(false);
    }

    // DRAWING
    @java.lang.Override
    protected void drawText(java.awt.Graphics2D g) {
    }

    @java.lang.Override
    protected void drawFill(java.awt.Graphics2D g) {
        g.fill(getTextShape());
    }

    @java.lang.Override
    protected void drawStroke(java.awt.Graphics2D g) {
        g.draw(getTextShape());
    }

    // SHAPE AND BOUNDS
    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getBounds(double scale) {
        return ((java.awt.geom.Rectangle2D.Double) (bounds.clone()));
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double getDrawingArea(double scale) {
        if (cachedDrawingArea == null) {
            java.awt.geom.Rectangle2D.Double r = getBounds();
            double g = org.jhotdraw.samples.svg.SVGAttributeKeys.getPerpendicularHitGrowth(this, 1.0);
            org.jhotdraw.geom.Geom.grow(r, g, g);
            if (attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) == null) {
                cachedDrawingArea = r;
            } else {
                cachedDrawingArea = new java.awt.geom.Rectangle2D.Double();
                cachedDrawingArea.setRect(attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM).createTransformedShape(r).getBounds2D());
            }
        }
        return ((java.awt.geom.Rectangle2D.Double) (cachedDrawingArea.clone()));
    }

    /**
     * Checks if a Point2D.Double is inside the figure.
     */
    @java.lang.Override
    public boolean contains(java.awt.geom.Point2D.Double p, double scaleDenominator) {
        if (attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) {
            try {
                p = ((java.awt.geom.Point2D.Double) (attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM).inverseTransform(p, new java.awt.geom.Point2D.Double())));
            } catch (java.awt.geom.NoninvertibleTransformException ex) {
                ex.printStackTrace();
            }
        }
        java.awt.geom.Rectangle2D r = getTextShape().getBounds2D();
        return r.isEmpty() ? getBounds().contains(p) : r.contains(p);
    }

    private java.awt.Shape getTextShape() {
        if (cachedTextShape == null) {
            java.awt.geom.Path2D.Double shape;
            cachedTextShape = shape = new java.awt.geom.Path2D.Double();
            if ((getText() != null) || isEditable()) {
                java.awt.Font font = getFont();
                boolean isUnderlined = attr().get(org.jhotdraw.draw.AttributeKeys.FONT_UNDERLINE);
                org.jhotdraw.geom.Insets2D.Double insets = getInsets();
                java.awt.geom.Rectangle2D.Double textRect = new java.awt.geom.Rectangle2D.Double(bounds.x + insets.left, bounds.y + insets.top, (bounds.width - insets.left) - insets.right, (bounds.height - insets.top) - insets.bottom);
                float leftMargin = ((float) (textRect.x));
                float rightMargin = ((float) (java.lang.Math.max(leftMargin + 1, textRect.x + textRect.width)));
                float verticalPos = ((float) (textRect.y));
                float maxVerticalPos = ((float) (textRect.y + textRect.height));
                if (leftMargin < rightMargin) {
                    float tabWidth = ((float) (getTabSize() * font.getStringBounds("m", getFontRenderContext()).getWidth()));
                    float[] tabStops = new float[((int) (textRect.width / tabWidth))];
                    for (int i = 0; i < tabStops.length; i++) {
                        tabStops[i] = ((float) (textRect.x + ((int) (tabWidth * (i + 1)))));
                    }
                    if (getText() != null) {
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
                            java.awt.geom.Rectangle2D.Double paragraphBounds = appendParagraph(shape, as.getIterator(), verticalPos, maxVerticalPos, leftMargin, rightMargin, tabStops, tabCount);
                            verticalPos = ((float) (paragraphBounds.y + paragraphBounds.height));
                            if (verticalPos > (textRect.y + textRect.height)) {
                                break;
                            }
                        }
                    }
                }
            }
        }
        return cachedTextShape;
    }

    /**
     * Appends a paragraph of text at the specified y location and returns the bounds of the
     * paragraph.
     *
     * @param shape
     * 		Shape to which to add the glyphs of the paragraph. This parameter is null, if we
     * 		only want to measure the size of the paragraph.
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
    private java.awt.geom.Rectangle2D.Double appendParagraph(java.awt.geom.Path2D.Double shape, java.text.AttributedCharacterIterator styledText, float verticalPos, float maxVerticalPos, float leftMargin, float rightMargin, float[] tabStops, int tabCount) {
        // assume styledText is an AttributedCharacterIterator, and the number
        // of tabs in styledText is tabCount
        java.awt.geom.Rectangle2D.Double paragraphBounds = new java.awt.geom.Rectangle2D.Double(leftMargin, verticalPos, 0, 0);
        int[] tabLocations = new int[tabCount + 1];
        int i = 0;
        for (char c = styledText.first(); c != java.text.CharacterIterator.DONE; c = styledText.next()) {
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
        while (measurer.getPosition() < styledText.getEndIndex()) {
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
            java.util.LinkedList<java.awt.font.TextLayout> layouts = new java.util.LinkedList<java.awt.font.TextLayout>();
            java.util.LinkedList<java.lang.Float> penPositions = new java.util.LinkedList<java.lang.Float>();
            while (!lineComplete) {
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
            verticalPos += maxAscent;
            if (verticalPos > maxVerticalPos) {
                break;
            }
            java.util.Iterator<java.awt.font.TextLayout> layoutEnum = layouts.iterator();
            java.util.Iterator<java.lang.Float> positionEnum = penPositions.iterator();
            // now iterate through layouts and draw them
            while (layoutEnum.hasNext()) {
                java.awt.font.TextLayout nextLayout = layoutEnum.next();
                float nextPosition = positionEnum.next();
                java.awt.geom.AffineTransform tx = new java.awt.geom.AffineTransform();
                tx.translate(nextPosition, verticalPos);
                if (shape != null) {
                    java.awt.Shape outline = nextLayout.getOutline(tx);
                    shape.append(outline, false);
                }
                java.awt.geom.Rectangle2D layoutBounds = nextLayout.getBounds();
                paragraphBounds.add(new java.awt.geom.Rectangle2D.Double(layoutBounds.getX() + nextPosition, layoutBounds.getY() + verticalPos, layoutBounds.getWidth(), layoutBounds.getHeight()));
            } 
            verticalPos += maxDescent;
        } 
        return paragraphBounds;
    }

    @java.lang.Override
    public void setBounds(java.awt.geom.Point2D.Double anchor, java.awt.geom.Point2D.Double lead) {
        bounds.x = java.lang.Math.min(anchor.x, lead.x);
        bounds.y = java.lang.Math.min(anchor.y, lead.y);
        bounds.width = java.lang.Math.max(0.1, java.lang.Math.abs(lead.x - anchor.x));
        bounds.height = java.lang.Math.max(0.1, java.lang.Math.abs(lead.y - anchor.y));
        invalidate();
    }

    /**
     * Transforms the figure.
     *
     * @param tx
     * 		the transformation.
     */
    @java.lang.Override
    public void transform(java.awt.geom.AffineTransform tx) {
        if ((attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) || ((tx.getType() & java.awt.geom.AffineTransform.TYPE_TRANSLATION) != tx.getType())) {
            if (attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) == null) {
                attr().set(org.jhotdraw.draw.AttributeKeys.TRANSFORM, ((java.awt.geom.AffineTransform) (tx.clone())));
            } else {
                java.awt.geom.AffineTransform t = org.jhotdraw.draw.AttributeKeys.TRANSFORM.getClone(this);
                t.preConcatenate(tx);
                attr().set(org.jhotdraw.draw.AttributeKeys.TRANSFORM, t);
            }
        } else {
            java.awt.geom.Point2D.Double anchor = getStartPoint();
            java.awt.geom.Point2D.Double lead = getEndPoint();
            setBounds(((java.awt.geom.Point2D.Double) (tx.transform(anchor, anchor))), ((java.awt.geom.Point2D.Double) (tx.transform(lead, lead))));
            if ((attr().get(org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT) != null) && (!attr().get(org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT).isRelativeToFigureBounds())) {
                org.jhotdraw.samples.svg.Gradient g = org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT.getClone(this);
                g.transform(tx);
                attr().set(org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT, g);
            }
            if ((attr().get(org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_GRADIENT) != null) && (!attr().get(org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_GRADIENT).isRelativeToFigureBounds())) {
                org.jhotdraw.samples.svg.Gradient g = org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_GRADIENT.getClone(this);
                g.transform(tx);
                attr().set(org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_GRADIENT, g);
            }
        }
        invalidate();
    }

    @java.lang.Override
    public void restoreTransformTo(java.lang.Object geometry) {
        java.lang.Object[] restoreData = ((java.lang.Object[]) (geometry));
        bounds = ((java.awt.geom.Rectangle2D.Double) (((java.awt.geom.Rectangle2D.Double) (restoreData[0])).clone()));
        org.jhotdraw.draw.AttributeKeys.TRANSFORM.setClone(this, ((java.awt.geom.AffineTransform) (restoreData[1])));
        org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT.setClone(this, ((org.jhotdraw.samples.svg.Gradient) (restoreData[2])));
        org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_GRADIENT.setClone(this, ((org.jhotdraw.samples.svg.Gradient) (restoreData[3])));
        invalidate();
    }

    @java.lang.Override
    public java.lang.Object getTransformRestoreData() {
        return new java.lang.Object[]{ bounds.clone(), org.jhotdraw.draw.AttributeKeys.TRANSFORM.getClone(this), org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT.getClone(this), org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_GRADIENT.getClone(this) };
    }

    // ATTRIBUTES
    @java.lang.Override
    public java.lang.String getText() {
        return attr().get(org.jhotdraw.draw.AttributeKeys.TEXT);
    }

    @java.lang.Override
    public int getTextColumns() {
        return getText() == null ? 4 : java.lang.Math.max(getText().length(), 4);
    }

    @java.lang.Override
    protected <T> void fireAttributeChanged(org.jhotdraw.draw.AttributeKey<T> key, T oldValue, T newValue) {
        if (((((((key.equals(org.jhotdraw.samples.svg.SVGAttributeKeys.TRANSFORM) || key.equals(org.jhotdraw.samples.svg.SVGAttributeKeys.FONT_FACE)) || key.equals(org.jhotdraw.samples.svg.SVGAttributeKeys.FONT_BOLD)) || key.equals(org.jhotdraw.samples.svg.SVGAttributeKeys.FONT_ITALIC)) || key.equals(org.jhotdraw.samples.svg.SVGAttributeKeys.FONT_SIZE)) || key.equals(org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_WIDTH)) || key.equals(org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_COLOR)) || key.equals(org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_GRADIENT)) {
            invalidate();
        }
    }

    /**
     * Sets the text shown by the text figure.
     */
    @java.lang.Override
    public void setText(java.lang.String newText) {
        attr().set(org.jhotdraw.draw.AttributeKeys.TEXT, newText);
    }

    /**
     * Returns the insets used to draw text.
     */
    @java.lang.Override
    public org.jhotdraw.geom.Insets2D.Double getInsets() {
        double sw = (attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_COLOR) == null) ? 0 : java.lang.Math.ceil(attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_WIDTH) / 2);
        org.jhotdraw.geom.Insets2D.Double insets = new org.jhotdraw.geom.Insets2D.Double(0, 0, 0, 0);
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

    @java.lang.Override
    public org.jhotdraw.draw.figure.TextHolderFigure getLabelFor() {
        return this;
    }

    @java.lang.Override
    public java.awt.Font getFont() {
        return org.jhotdraw.samples.svg.SVGAttributeKeys.getFont(this);
    }

    @java.lang.Override
    public java.awt.Color getTextColor() {
        return attr().get(org.jhotdraw.draw.AttributeKeys.FILL_COLOR);
        // return TEXT_COLOR.get(this);
    }

    @java.lang.Override
    public java.awt.Color getFillColor() {
        return attr().get(org.jhotdraw.draw.AttributeKeys.FILL_COLOR).equals(java.awt.Color.white) ? java.awt.Color.black : java.awt.Color.WHITE;
    }

    @java.lang.Override
    public void setFontSize(float size) {
        java.awt.geom.Point2D.Double p = new java.awt.geom.Point2D.Double(0, size);
        java.awt.geom.AffineTransform tx = attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM);
        if (tx != null) {
            try {
                tx.inverseTransform(p, p);
                java.awt.geom.Point2D.Double p0 = new java.awt.geom.Point2D.Double(0, 0);
                tx.inverseTransform(p0, p0);
                p.y -= p0.y;
            } catch (java.awt.geom.NoninvertibleTransformException ex) {
                ex.printStackTrace();
            }
        }
        attr().set(org.jhotdraw.draw.AttributeKeys.FONT_SIZE, java.lang.Math.abs(p.y));
    }

    @java.lang.Override
    public float getFontSize() {
        java.awt.geom.Point2D.Double p = new java.awt.geom.Point2D.Double(0, attr().get(org.jhotdraw.draw.AttributeKeys.FONT_SIZE));
        java.awt.geom.AffineTransform tx = attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM);
        if (tx != null) {
            tx.transform(p, p);
            java.awt.geom.Point2D.Double p0 = new java.awt.geom.Point2D.Double(0, 0);
            tx.transform(p0, p0);
            p.y -= p0.y;
            /* try {
            tx.inverseTransform(p, p);
            } catch (NoninvertibleTransformException ex) {
            ex.printStackTrace();
            }
             */
        }
        return ((float) (java.lang.Math.abs(p.y)));
    }

    // EDITING
    @java.lang.Override
    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean b) {
        this.editable = b;
    }

    @java.lang.Override
    public java.util.Collection<org.jhotdraw.draw.handle.Handle> createHandles(int detailLevel) {
        java.util.LinkedList<org.jhotdraw.draw.handle.Handle> handles = new java.util.LinkedList<org.jhotdraw.draw.handle.Handle>();
        switch (detailLevel % 2) {
            case -1 :
                // Mouse hover handles
                handles.add(new org.jhotdraw.draw.handle.BoundsOutlineHandle(this, false, true));
                break;
            case 0 :
                org.jhotdraw.draw.handle.ResizeHandleKit.addResizeHandles(this, handles);
                handles.add(new org.jhotdraw.draw.handle.FontSizeHandle(this));
                handles.add(new org.jhotdraw.draw.handle.TextOverflowHandle(this));
                handles.add(new org.jhotdraw.samples.svg.figures.LinkHandle(this));
                break;
            case 1 :
                org.jhotdraw.draw.handle.TransformHandleKit.addTransformHandles(this, handles);
                break;
            default :
                break;
        }
        return handles;
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

    // CONNECTING
    // COMPOSITE FIGURES
    // CLONING
    // EVENT HANDLING
    /**
     * Gets the text shown by the text figure.
     */
    @java.lang.Override
    public boolean isEmpty() {
        return (getText() == null) || (getText().length() == 0);
    }

    @java.lang.Override
    public void invalidate() {
        super.invalidate();
        cachedDrawingArea = null;
        cachedTextShape = null;
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
                if (getText() != null) {
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
                        java.awt.geom.Rectangle2D.Double paragraphBounds = appendParagraph(null, as.getIterator(), verticalPos, maxVerticalPos, leftMargin, rightMargin, tabStops, tabCount);
                        verticalPos = ((float) (paragraphBounds.y + paragraphBounds.height));
                        textRect.add(paragraphBounds);
                    }
                }
            }
        }
        return new org.jhotdraw.geom.Dimension2DDouble(java.lang.Math.abs(textRect.x) + textRect.width, java.lang.Math.abs(textRect.y) + textRect.height);
    }

    @java.lang.Override
    public org.jhotdraw.samples.svg.figures.SVGTextAreaFigure clone() {
        org.jhotdraw.samples.svg.figures.SVGTextAreaFigure that = ((org.jhotdraw.samples.svg.figures.SVGTextAreaFigure) (super.clone()));
        that.bounds = ((java.awt.geom.Rectangle2D.Double) (this.bounds.clone()));
        return that;
    }
}