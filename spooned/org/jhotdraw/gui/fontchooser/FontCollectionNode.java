/**
 *
 * @(#)FontCollectionNode.java <p>Copyright (c) 2008 The authors and contributors of JHotDraw. You may not use, copy or modify
this file, except in compliance with the accompanying license terms.
 */
package org.jhotdraw.gui.fontchooser;
/**
 * A FontCollectionNode is a MutableTreeNode which only allows FontFamilyNode as child nodes.
 */
public class FontCollectionNode implements javax.swing.tree.MutableTreeNode , java.lang.Comparable<org.jhotdraw.gui.fontchooser.FontCollectionNode> , java.lang.Cloneable {
    private javax.swing.tree.MutableTreeNode parent;

    private java.lang.String name;

    private java.util.ArrayList<org.jhotdraw.gui.fontchooser.FontFamilyNode> children;

    private boolean isEditable;

    public FontCollectionNode(java.lang.String name) {
        this.name = name;
        children = new java.util.ArrayList<>();
    }

    public FontCollectionNode(java.lang.String name, java.util.ArrayList<org.jhotdraw.gui.fontchooser.FontFamilyNode> families) {
        this.name = name;
        this.children = families;
    }

    @java.lang.Override
    public int compareTo(org.jhotdraw.gui.fontchooser.FontCollectionNode that) {
        return java.text.Collator.getInstance().compare(this.name, that.name);
    }

    public java.lang.String getName() {
        return name;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return name;
    }

    @java.lang.Override
    public org.jhotdraw.gui.fontchooser.FontCollectionNode clone() {
        org.jhotdraw.gui.fontchooser.FontCollectionNode that;
        try {
            that = ((org.jhotdraw.gui.fontchooser.FontCollectionNode) (super.clone()));
        } catch (java.lang.CloneNotSupportedException ex) {
            java.lang.InternalError error = new java.lang.InternalError("Clone failed");
            error.initCause(ex);
            throw error;
        }
        that.parent = null;
        that.children = new java.util.ArrayList<>();
        for (org.jhotdraw.gui.fontchooser.FontFamilyNode f : this.children) {
            that.insert(f.clone(), that.getChildCount());
        }
        return that;
    }

    public void add(org.jhotdraw.gui.fontchooser.FontFamilyNode newChild) {
        insert(newChild, getChildCount());
    }

    public void addAll(java.util.Collection<org.jhotdraw.gui.fontchooser.FontFamilyNode> c) {
        children.addAll(c);
    }

    @java.lang.Override
    public void insert(javax.swing.tree.MutableTreeNode newChild, int index) {
        org.jhotdraw.gui.fontchooser.FontCollectionNode oldParent = ((org.jhotdraw.gui.fontchooser.FontCollectionNode) (newChild.getParent()));
        if (oldParent != null) {
            oldParent.remove(newChild);
        }
        newChild.setParent(this);
        children.add(index, ((org.jhotdraw.gui.fontchooser.FontFamilyNode) (newChild)));
    }

    @java.lang.Override
    public void remove(int childIndex) {
        javax.swing.tree.MutableTreeNode child = ((javax.swing.tree.MutableTreeNode) (getChildAt(childIndex)));
        children.remove(childIndex);
        child.setParent(null);
    }

    @java.lang.Override
    public void remove(javax.swing.tree.MutableTreeNode aChild) {
        if (aChild == null) {
            throw new java.lang.IllegalArgumentException("argument is null");
        }
        if (!isNodeChild(aChild)) {
            throw new java.lang.IllegalArgumentException("argument is not a child");
        }
        remove(getIndex(aChild));// linear search

    }

    @java.lang.Override
    public void setUserObject(java.lang.Object object) {
        throw new java.lang.UnsupportedOperationException("Not supported.");
    }

    @java.lang.Override
    public void removeFromParent() {
        if (parent != null) {
            parent.remove(this);
        }
    }

    @java.lang.Override
    public void setParent(javax.swing.tree.MutableTreeNode newParent) {
        this.parent = newParent;
    }

    @java.lang.Override
    public org.jhotdraw.gui.fontchooser.FontFamilyNode getChildAt(int childIndex) {
        return children.get(childIndex);
    }

    @java.lang.Override
    public int getChildCount() {
        return children.size();
    }

    @java.lang.Override
    public javax.swing.tree.MutableTreeNode getParent() {
        return parent;
    }

    @java.lang.Override
    public int getIndex(javax.swing.tree.TreeNode node) {
        return children.indexOf(node);
    }

    @java.lang.Override
    public boolean getAllowsChildren() {
        return true;
    }

    @java.lang.Override
    public boolean isLeaf() {
        return children.isEmpty();
    }

    @java.lang.Override
    public java.util.Enumeration<org.jhotdraw.gui.fontchooser.FontFamilyNode> children() {
        return java.util.Collections.enumeration(children);
    }

    public java.util.List<org.jhotdraw.gui.fontchooser.FontFamilyNode> families() {
        return java.util.Collections.unmodifiableList(children);
    }

    // Child Queries
    /**
     * Returns true if <code>aNode</code> is a child of this node. If <code>aNode</code> is null, this
     * method returns false.
     *
     * @return true if <code>aNode</code> is a child of this node; false if <code>aNode</code> is null
     */
    public boolean isNodeChild(javax.swing.tree.TreeNode aNode) {
        boolean retval;
        if (aNode == null) {
            retval = false;
        } else if (getChildCount() == 0) {
            retval = false;
        } else {
            retval = aNode.getParent() == this;
        }
        return retval;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean newValue) {
        isEditable = newValue;
    }
}