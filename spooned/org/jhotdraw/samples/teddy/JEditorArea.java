/* @(#)JEditorArea.java

Copyright (c) 2006 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.teddy;
/**
 * A happy marriage between JTextArea and JEditorPane.
 */
// </editor-fold>//GEN-END:initComponents
// Variables declaration - do not modify//GEN-BEGIN:variables
// End of variables declaration//GEN-END:variables
public class JEditorArea extends javax.swing.JEditorPane {
    private static final long serialVersionUID = 1L;

    private boolean wrap;

    public JEditorArea() {
        setEditorKit(new javax.swing.text.StyledEditorKit());
        initComponents();
    }

    public javax.swing.text.StyledEditorKit getStyledEditorKit() {
        return ((javax.swing.text.StyledEditorKit) (getEditorKit()));
    }

    /**
     * Sets the line-wrapping policy of the text area. If set to true the lines will be wrapped if
     * they are too long to fit within the allocated width. If set to false, the lines will always be
     * unwrapped. A {@code PropertyChange} event ("lineWrap") is fired when the policy is changed. By
     * default this property is false.
     *
     * @param newValue
     * 		indicates if lines should be wrapped
     * @see #getLineWrap
     */
    public void setLineWrap(boolean newValue) {
        boolean oldValue = wrap;
        if (oldValue != newValue) {
            this.wrap = newValue;
            firePropertyChange("lineWrap", oldValue, newValue);
            rebuildView();
        }
    }

    /**
     * Gets the line-wrapping policy of the text area. If set to true the lines will be wrapped if
     * they are too long to fit within the allocated width. If set to false, the lines will always be
     * unwrapped.
     *
     * @return if lines will be wrapped
     */
    public boolean getLineWrap() {
        return wrap;
    }

    /**
     * Returns true if a viewport should always force the width of this Scrollable to match the width
     * of the viewport. This is implemented to return true if the line wrapping policy is true, and
     * false if lines are not being wrapped.
     *
     * @return true if a viewport should force the Scrollables width to match its own.
     */
    @java.lang.Override
    public boolean getScrollableTracksViewportWidth() {
        return wrap;
    }

    public void rebuildView() {
        revalidate();
        repaint();
    }

    /**
     * Replaces text from the indicated start to end position with the new text specified. Does
     * nothing if the model is null. Simply does a delete if the new string is null or empty.
     *
     * <p>This method is thread safe, although most Swing methods are not. Please see <A
     * HREF="http://java.sun.com/products/jfc/swingdoc-archive/threads.html">Threads and Swing</A> for
     * more information.
     *
     * @param str
     * 		the text to use as the replacement
     * @param start
     * 		the start position &gt;= 0
     * @param end
     * 		the end position &gt;= start
     * @exception IllegalArgumentException
     * 		if part of the range is an invalid position in the model
     */
    public void replaceRange(java.lang.String str, int start, int end) {
        if (end < start) {
            throw new java.lang.IllegalArgumentException("end before start");
        }
        javax.swing.text.Document doc = getDocument();
        if (doc != null) {
            try {
                if (doc instanceof javax.swing.text.AbstractDocument) {
                    ((javax.swing.text.AbstractDocument) (doc)).replace(start, end - start, str, null);
                } else {
                    doc.remove(start, end - start);
                    doc.insertString(start, str, null);
                }
            } catch (javax.swing.text.BadLocationException e) {
                throw new java.lang.IllegalArgumentException(e.getMessage());
            }
        }
    }

    /**
     * Translates an offset into the components text to a line number.
     *
     * @param offset
     * 		the offset &gt;= 0
     * @return the line number &gt;= 0
     * @exception BadLocationException
     * 		thrown if the offset is less than zero or greater than the
     * 		document length.
     */
    public int getLineOfOffset(int offset) throws javax.swing.text.BadLocationException {
        javax.swing.text.Document doc = getDocument();
        if (offset < 0) {
            throw new javax.swing.text.BadLocationException("Can't translate offset to line", -1);
        } else if (offset > doc.getLength()) {
            throw new javax.swing.text.BadLocationException("Can't translate offset to line", doc.getLength() + 1);
        } else {
            javax.swing.text.Element map = getDocument().getDefaultRootElement();
            return map.getElementIndex(offset);
        }
    }

    /**
     * Determines the offset of the start of the given line.
     *
     * @param line
     * 		the line number to translate &gt;= 0
     * @return the offset &gt;= 0
     * @exception BadLocationException
     * 		thrown if the line is less than zero or greater or equal to the
     * 		number of lines contained in the document (as reported by getLineCount).
     */
    public int getLineStartOffset(int line) throws javax.swing.text.BadLocationException {
        javax.swing.text.Document doc = getDocument();
        javax.swing.text.Element map = doc.getDefaultRootElement();
        int lineCount = map.getElementCount();
        // int lineCount = getLineCount();
        if (line < 0) {
            throw new javax.swing.text.BadLocationException("Negative line", -1);
        } else if (line >= lineCount) {
            throw new javax.swing.text.BadLocationException("No such line", doc.getLength() + 1);
        } else {
            javax.swing.text.Element lineElem = map.getElement(line);
            return lineElem.getStartOffset();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
    }
}