/**
 *
 * @(#)FontFaceNode.java <p>Copyright (c) 2008 The authors and contributors of JHotDraw. You may not use, copy or modify
this file, except in compliance with the accompanying license terms.
 */
package org.jhotdraw.gui.fontchooser;
/**
 * A FontFaceNode is a MutableTreeNode which does not allow children.
 */
public class FontFaceNode implements javax.swing.tree.MutableTreeNode , java.lang.Comparable<org.jhotdraw.gui.fontchooser.FontFaceNode> , java.lang.Cloneable {
    private org.jhotdraw.gui.fontchooser.FontFamilyNode parent;

    private java.awt.Font typeface;

    private java.lang.String name;

    public FontFaceNode(java.awt.Font typeface) {
        this.typeface = typeface;
        this.name = beautifyName(typeface.getPSName());
    }

    protected java.lang.String beautifyName(java.lang.String name) {
        // 'Beautify' the name
        int p = name.lastIndexOf('-');
        if (p != (-1)) {
            name = name.substring(p + 1);
            java.lang.String lcName = name.toLowerCase();
            if ("plain".equals(lcName)) {
                name = "Plain";
            } else if ("bolditalic".equals(lcName)) {
                name = "Bold Italic";
            } else if ("italic".equals(lcName)) {
                name = "Italic";
            } else if ("bold".equals(lcName)) {
                name = "Bold";
            }
        } else {
            java.lang.String lcName = name.toLowerCase();
            if (lcName.endsWith("plain")) {
                name = "Plain";
            } else if (lcName.endsWith("boldoblique")) {
                name = "Bold Oblique";
            } else if (lcName.endsWith("bolditalic")) {
                name = "Bold Italic";
            } else if (lcName.endsWith("bookita")) {
                name = "Book Italic";
            } else if (lcName.endsWith("bookit")) {
                name = "Book Italic";
            } else if (lcName.endsWith("demibold")) {
                name = "Demi Bold";
            } else if (lcName.endsWith("semiita")) {
                name = "Semi Italic";
            } else if (lcName.endsWith("italic")) {
                name = "Italic";
            } else if (lcName.endsWith("book")) {
                name = "Book";
            } else if (lcName.endsWith("bold")) {
                name = "Bold";
            } else if (lcName.endsWith("bol")) {
                name = "Bold";
            } else if (lcName.endsWith("oblique")) {
                name = "Oblique";
            } else if (lcName.endsWith("regular")) {
                name = "Regular";
            } else if (lcName.endsWith("semi")) {
                name = "Semi";
            } else {
                name = "Plain";
            }
        }
        java.lang.StringBuilder buf = new java.lang.StringBuilder();
        char prev = name.charAt(0);
        buf.append(prev);
        for (int i = 1; i < name.length(); i++) {
            char ch = name.charAt(i);
            if (((((prev != ' ') && (prev != '-')) && java.lang.Character.isUpperCase(ch)) && (!java.lang.Character.isUpperCase(prev))) || (java.lang.Character.isDigit(ch) && (!java.lang.Character.isDigit(prev)))) {
                buf.append(' ');
            }
            buf.append(ch);
            prev = ch;
        }
        name = buf.toString();
        return name;
    }

    public void setName(java.lang.String newValue) {
        this.name = newValue;
    }

    public java.lang.String getName() {
        return name;
    }

    public java.awt.Font getFont() {
        return typeface;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return name;
    }

    @java.lang.Override
    public void insert(javax.swing.tree.MutableTreeNode child, int index) {
        throw new java.lang.UnsupportedOperationException("Not allowed.");
    }

    @java.lang.Override
    public void remove(int index) {
        throw new java.lang.UnsupportedOperationException("Not allowed.");
    }

    @java.lang.Override
    public void remove(javax.swing.tree.MutableTreeNode node) {
        throw new java.lang.UnsupportedOperationException("Not allowed.");
    }

    @java.lang.Override
    public void setUserObject(java.lang.Object object) {
        throw new java.lang.UnsupportedOperationException("Not allowed.");
    }

    @java.lang.Override
    public void removeFromParent() {
        if (parent != null) {
            parent.remove(this);
        }
    }

    @java.lang.Override
    public void setParent(javax.swing.tree.MutableTreeNode newParent) {
        this.parent = ((org.jhotdraw.gui.fontchooser.FontFamilyNode) (newParent));
    }

    @java.lang.Override
    public javax.swing.tree.TreeNode getChildAt(int childIndex) {
        throw new java.lang.IndexOutOfBoundsException("" + childIndex);
    }

    @java.lang.Override
    public int getChildCount() {
        return 0;
    }

    @java.lang.Override
    public javax.swing.tree.TreeNode getParent() {
        return parent;
    }

    @java.lang.Override
    public int getIndex(javax.swing.tree.TreeNode node) {
        return -1;
    }

    @java.lang.Override
    public boolean getAllowsChildren() {
        return false;
    }

    @java.lang.Override
    public boolean isLeaf() {
        return true;
    }

    @java.lang.SuppressWarnings("unchecked")
    @java.lang.Override
    public java.util.Enumeration<javax.swing.tree.TreeNode> children() {
        return java.util.Collections.enumeration(java.util.Collections.EMPTY_LIST);
    }

    @java.lang.Override
    public int compareTo(org.jhotdraw.gui.fontchooser.FontFaceNode that) {
        return this.name.compareTo(that.name);
    }

    @java.lang.Override
    public org.jhotdraw.gui.fontchooser.FontFaceNode clone() {
        org.jhotdraw.gui.fontchooser.FontFaceNode that;
        try {
            that = ((org.jhotdraw.gui.fontchooser.FontFaceNode) (super.clone()));
        } catch (java.lang.CloneNotSupportedException ex) {
            java.lang.InternalError error = new java.lang.InternalError("Clone failed");
            error.initCause(ex);
            throw error;
        }
        that.parent = null;
        return that;
    }

    public boolean isEditable() {
        return false;
    }
}