/* @(#)FontSizeHandle.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.handle;
import org.jhotdraw.draw.figure.TextHolderFigure;
/**
 * A {@link Handle} which can be used to change the font size of a {@link TextHolderFigure}.
 */
public class FontSizeHandle extends org.jhotdraw.draw.handle.LocatorHandle {
    private float oldSize;

    private float newSize;

    private java.lang.Object restoreData;

    public FontSizeHandle(org.jhotdraw.draw.figure.TextHolderFigure owner) {
        super(owner, new org.jhotdraw.draw.locator.FontSizeLocator());
    }

    public FontSizeHandle(org.jhotdraw.draw.figure.TextHolderFigure owner, org.jhotdraw.draw.locator.Locator locator) {
        super(owner, locator);
    }

    /**
     * Draws this handle.
     */
    @java.lang.Override
    public void draw(java.awt.Graphics2D g) {
        drawDiamond(g, getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.ATTRIBUTE_HANDLE_FILL_COLOR), getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.ATTRIBUTE_HANDLE_STROKE_COLOR));
    }

    @java.lang.Override
    public java.awt.Cursor getCursor() {
        return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.S_RESIZE_CURSOR);
    }

    @java.lang.Override
    public void trackStart(java.awt.Point anchor, int modifiersEx) {
        org.jhotdraw.draw.figure.TextHolderFigure textOwner = ((org.jhotdraw.draw.figure.TextHolderFigure) (getOwner()));
        oldSize = newSize = textOwner.getFontSize();
        restoreData = textOwner.attr().getAttributesRestoreData();
    }

    @java.lang.Override
    public void trackStep(java.awt.Point anchor, java.awt.Point lead, int modifiersEx) {
        org.jhotdraw.draw.figure.TextHolderFigure textOwner = ((org.jhotdraw.draw.figure.TextHolderFigure) (getOwner()));
        java.awt.geom.Point2D.Double anchor2D = view.viewToDrawing(anchor);
        java.awt.geom.Point2D.Double lead2D = view.viewToDrawing(lead);
        if (textOwner.attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) {
            try {
                textOwner.attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM).inverseTransform(anchor2D, anchor2D);
                textOwner.attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM).inverseTransform(lead2D, lead2D);
            } catch (java.awt.geom.NoninvertibleTransformException ex) {
                ex.printStackTrace();
            }
        }
        newSize = ((float) (java.lang.Math.max(1, (oldSize + lead2D.y) - anchor2D.y)));
        textOwner.willChange();
        textOwner.setFontSize(newSize);
        textOwner.changed();
    }

    @java.lang.Override
    public void trackEnd(java.awt.Point anchor, java.awt.Point lead, int modifiersEx) {
        final org.jhotdraw.draw.figure.TextHolderFigure textOwner = ((org.jhotdraw.draw.figure.TextHolderFigure) (getOwner()));
        final java.lang.Object editRestoreData = restoreData;
        final float editNewSize = newSize;
        javax.swing.undo.UndoableEdit edit = new javax.swing.undo.AbstractUndoableEdit() {
            private static final long serialVersionUID = 1L;

            @java.lang.Override
            public java.lang.String getPresentationName() {
                org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
                return labels.getString("attribute.fontSize.text");
            }

            @java.lang.Override
            public void undo() {
                super.undo();
                textOwner.willChange();
                textOwner.attr().restoreAttributesTo(editRestoreData);
                textOwner.changed();
            }

            @java.lang.Override
            public void redo() {
                super.redo();
                textOwner.willChange();
                textOwner.setFontSize(newSize);
                textOwner.changed();
            }
        };
        fireUndoableEditHappened(edit);
    }

    @java.lang.Override
    public void keyPressed(java.awt.event.KeyEvent evt) {
        final org.jhotdraw.draw.figure.TextHolderFigure textOwner = ((org.jhotdraw.draw.figure.TextHolderFigure) (getOwner()));
        oldSize = newSize = textOwner.getFontSize();
        switch (evt.getKeyCode()) {
            case java.awt.event.KeyEvent.VK_UP :
                if (newSize > 1) {
                    newSize -= 1.0F;
                }
                evt.consume();
                break;
            case java.awt.event.KeyEvent.VK_DOWN :
                newSize++;
                evt.consume();
                break;
            case java.awt.event.KeyEvent.VK_LEFT :
                evt.consume();
                break;
            case java.awt.event.KeyEvent.VK_RIGHT :
                evt.consume();
                break;
        }
        if (newSize != oldSize) {
            restoreData = textOwner.attr().getAttributesRestoreData();
            textOwner.willChange();
            textOwner.setFontSize(newSize);
            textOwner.changed();
            final java.lang.Object editRestoreData = restoreData;
            final float editNewSize = newSize;
            javax.swing.undo.UndoableEdit edit = new javax.swing.undo.AbstractUndoableEdit() {
                private static final long serialVersionUID = 1L;

                @java.lang.Override
                public java.lang.String getPresentationName() {
                    org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
                    return labels.getString("attribute.fontSize");
                }

                @java.lang.Override
                public void undo() {
                    super.undo();
                    textOwner.willChange();
                    textOwner.attr().restoreAttributesTo(editRestoreData);
                    textOwner.changed();
                }

                @java.lang.Override
                public void redo() {
                    super.redo();
                    textOwner.willChange();
                    textOwner.setFontSize(newSize);
                    textOwner.changed();
                }
            };
            fireUndoableEditHappened(edit);
        }
    }

    @java.lang.Override
    public java.lang.String getToolTipText(java.awt.Point p) {
        return org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels").getString("handle.fontSize.toolTipText");
    }
}