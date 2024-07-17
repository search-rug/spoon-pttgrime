/**
 *
 * @(#)PaletteFontChooserUI.java <p>Copyright (c) 2008 The authors and contributors of JHotDraw. You may not use, copy or modify
this file, except in compliance with the accompanying license terms.
 */
package org.jhotdraw.gui.plaf.palette;
/**
 * PaletteFontChooserUI.
 */
public class PaletteFontChooserUI extends org.jhotdraw.gui.plaf.FontChooserUI {
    private org.jhotdraw.gui.fontchooser.FontCollectionNode familyListParent;

    private org.jhotdraw.gui.fontchooser.FontFamilyNode faceListParent;

    private org.jhotdraw.gui.JFontChooser fontChooser;

    private org.jhotdraw.gui.plaf.palette.PaletteFontChooserSelectionPanel selectionPanel;

    private org.jhotdraw.gui.plaf.palette.PaletteFontChooserPreviewPanel previewPanel;

    private org.jhotdraw.gui.plaf.palette.PaletteFontChooserUI.SelectionPanelHandler selectionPanelHandler;

    private org.jhotdraw.gui.plaf.palette.PaletteFontChooserUI.FontChooserHandler chooserHandler;

    /**
     * The value of this counter is greater 0, if the palette font chooser is updating, and should
     * ignore incoming events.
     */
    private int isUpdating;

    public PaletteFontChooserUI(org.jhotdraw.gui.JFontChooser fontChooser) {
        this.fontChooser = fontChooser;
    }

    /**
     * Returns an instance of the UI delegate for the specified component. Each subclass must provide
     * its own static <code>createUI</code> method that returns an instance of that UI delegate
     * subclass. If the UI delegate subclass is stateless, it may return an instance that is shared by
     * multiple components. If the UI delegate is stateful, then it should return a new instance per
     * component. The default implementation of this method throws an error, as it should never be
     * invoked.
     */
    public static javax.swing.plaf.ComponentUI createUI(javax.swing.JComponent c) {
        org.jhotdraw.gui.plaf.palette.PaletteFontChooserUI ui = new org.jhotdraw.gui.plaf.palette.PaletteFontChooserUI(((org.jhotdraw.gui.JFontChooser) (c)));
        return ui;
    }

    /**
     * Configures the specified component appropriate for the look and feel. This method is invoked
     * when the <code>ComponentUI</code> instance is being installed as the UI delegate on the
     * specified component. This method should completely configure the component for the look and
     * feel, including the following:
     *
     * <ol>
     *   <li>Install any default property values for color, fonts, borders, icons, opacity, etc. on
     *       the component. Whenever possible, property values initialized by the client program
     *       should <i>not</i> be overridden.
     *   <li>Install a <code>LayoutManager</code> on the component if necessary.
     *   <li>Create/add any required sub-components to the component.
     *   <li>Create/install event listeners on the component.
     *   <li>Create/install a <code>PropertyChangeListener</code> on the component in order to detect
     *       and respond to component property changes appropriately.
     *   <li>Install keyboard UI (mnemonics, traversal, etc.) on the component.
     *   <li>Initialize any appropriate instance data.
     * </ol>
     *
     * @param c
     * 		the component where this UI delegate is being installed
     * @see #uninstallUI
     * @see javax.swing.JComponent#setUI
     * @see javax.swing.JComponent#updateUI
     */
    @java.lang.Override
    public void installUI(javax.swing.JComponent c) {
        installComponents(fontChooser);
        installListeners(fontChooser);
    }

    protected void installComponents(org.jhotdraw.gui.JFontChooser fc) {
        fc.removeAll();
        fc.setLayout(new java.awt.BorderLayout());
        selectionPanel = new org.jhotdraw.gui.plaf.palette.PaletteFontChooserSelectionPanel();
        fc.add(selectionPanel, java.awt.BorderLayout.CENTER);
        previewPanel = new org.jhotdraw.gui.plaf.palette.PaletteFontChooserPreviewPanel();
        fc.add(previewPanel, java.awt.BorderLayout.NORTH);
        updateCollectionList();
        updateFamilyList();
        updateFaceList();
        updatePreview();
    }

    protected void installListeners(org.jhotdraw.gui.JFontChooser fc) {
        selectionPanelHandler = new org.jhotdraw.gui.plaf.palette.PaletteFontChooserUI.SelectionPanelHandler();
        selectionPanel.getCollectionList().addListSelectionListener(selectionPanelHandler);
        selectionPanel.getFamilyList().addListSelectionListener(selectionPanelHandler);
        selectionPanel.getFaceList().addListSelectionListener(selectionPanelHandler);
        selectionPanel.getCollectionList().addKeyListener(selectionPanelHandler);
        selectionPanel.getFamilyList().addKeyListener(selectionPanelHandler);
        selectionPanel.getFaceList().addKeyListener(selectionPanelHandler);
        selectionPanel.getCollectionList().addMouseListener(selectionPanelHandler);
        selectionPanel.getFamilyList().addMouseListener(selectionPanelHandler);
        selectionPanel.getFaceList().addMouseListener(selectionPanelHandler);
        chooserHandler = new org.jhotdraw.gui.plaf.palette.PaletteFontChooserUI.FontChooserHandler();
        fontChooser.addPropertyChangeListener(chooserHandler);
        if (fontChooser.getModel() != null) {
            fontChooser.getModel().addTreeModelListener(chooserHandler);
        }
    }

    /**
     * Reverses configuration which was done on the specified component during <code>installUI</code>.
     * This method is invoked when this <code>UIComponent</code> instance is being removed as the UI
     * delegate for the specified component. This method should undo the configuration performed in
     * <code>installUI</code>, being careful to leave the <code>JComponent</code> instance in a clean
     * state (no extraneous listeners, look-and-feel-specific property objects, etc.). This should
     * include the following:
     *
     * <ol>
     *   <li>Remove any UI-set borders from the component.
     *   <li>Remove any UI-set layout managers on the component.
     *   <li>Remove any UI-added sub-components from the component.
     *   <li>Remove any UI-added event/property listeners from the component.
     *   <li>Remove any UI-installed keyboard UI from the component.
     *   <li>Nullify any allocated instance data objects to allow for GC.
     * </ol>
     *
     * @param c
     * 		the component from which this UI delegate is being removed; this argument is often
     * 		ignored, but might be used if the UI object is stateless and shared by multiple components
     * @see #installUI
     * @see javax.swing.JComponent#updateUI
     */
    @java.lang.Override
    public void uninstallUI(javax.swing.JComponent c) {
        uninstallListeners(fontChooser);
        uninstallComponents(fontChooser);
    }

    protected void uninstallComponents(org.jhotdraw.gui.JFontChooser fc) {
        fontChooser.removeAll();
    }

    protected void uninstallListeners(org.jhotdraw.gui.JFontChooser fc) {
        fontChooser.removePropertyChangeListener(chooserHandler);
        selectionPanel.getCollectionList().removeListSelectionListener(selectionPanelHandler);
        selectionPanel.getFamilyList().removeListSelectionListener(selectionPanelHandler);
        selectionPanel.getFaceList().removeListSelectionListener(selectionPanelHandler);
        selectionPanel.getCollectionList().removeKeyListener(selectionPanelHandler);
        selectionPanel.getFamilyList().removeKeyListener(selectionPanelHandler);
        selectionPanel.getFaceList().removeKeyListener(selectionPanelHandler);
        selectionPanel.getCollectionList().removeMouseListener(selectionPanelHandler);
        selectionPanel.getFamilyList().removeMouseListener(selectionPanelHandler);
        selectionPanel.getFaceList().removeMouseListener(selectionPanelHandler);
        if (fontChooser.getModel() != null) {
            fontChooser.getModel().removeTreeModelListener(chooserHandler);
        }
        chooserHandler = null;
        selectionPanelHandler = null;
    }

    private void updateCollectionList() {
        isUpdating++;
        javax.swing.JList list = selectionPanel.getCollectionList();
        javax.swing.DefaultListModel lm = ((javax.swing.DefaultListModel) (list.getModel()));
        lm.removeAllElements();
        org.jhotdraw.gui.fontchooser.FontChooserModel model = fontChooser.getModel();
        java.lang.Object parent = model.getRoot();
        for (int i = 0, n = model.getChildCount(parent); i < n; i++) {
            lm.addElement(model.getChild(parent, i));
        }
        javax.swing.tree.TreePath path = fontChooser.getSelectionPath();
        if ((path == null) || (path.getPathCount() < 2)) {
            list.clearSelection();
        } else {
            list.setSelectedIndex(((javax.swing.tree.TreeNode) (path.getPathComponent(0))).getIndex(((javax.swing.tree.TreeNode) (path.getPathComponent(1)))));
            list.scrollRectToVisible(list.getCellBounds(list.getSelectedIndex(), list.getSelectedIndex()));
        }
        isUpdating--;
    }

    private void updateFamilyList() {
        isUpdating++;
        javax.swing.JList list = selectionPanel.getFamilyList();
        org.jhotdraw.gui.fontchooser.FontChooserModel model = fontChooser.getModel();
        org.jhotdraw.gui.fontchooser.FontCollectionNode newParent = null;
        javax.swing.tree.TreePath path = fontChooser.getSelectionPath();
        if ((path != null) && (path.getPathCount() > 1)) {
            newParent = ((org.jhotdraw.gui.fontchooser.FontCollectionNode) (path.getPathComponent(1)));
        }
        if (newParent != familyListParent) {
            javax.swing.DefaultListModel lm = ((javax.swing.DefaultListModel) (list.getModel()));
            lm.removeAllElements();
            familyListParent = newParent;
            if (familyListParent != null) {
                for (int i = 0, n = model.getChildCount(familyListParent); i < n; i++) {
                    lm.addElement(model.getChild(familyListParent, i));
                }
            }
        }
        if ((path == null) || (path.getPathCount() < 3)) {
            list.clearSelection();
        } else {
            list.setSelectedIndex(((javax.swing.tree.TreeNode) (path.getPathComponent(1))).getIndex(((javax.swing.tree.TreeNode) (path.getPathComponent(2)))));
            list.scrollRectToVisible(list.getCellBounds(list.getSelectedIndex(), list.getSelectedIndex()));
        }
        isUpdating--;
    }

    private void updateFaceList() {
        isUpdating++;
        javax.swing.JList list = selectionPanel.getFaceList();
        org.jhotdraw.gui.fontchooser.FontChooserModel model = fontChooser.getModel();
        org.jhotdraw.gui.fontchooser.FontFamilyNode newParent = null;
        javax.swing.tree.TreePath path = fontChooser.getSelectionPath();
        if ((path != null) && (path.getPathCount() > 2)) {
            newParent = ((org.jhotdraw.gui.fontchooser.FontFamilyNode) (path.getPathComponent(2)));
        }
        if (newParent != faceListParent) {
            javax.swing.DefaultListModel lm = ((javax.swing.DefaultListModel) (list.getModel()));
            lm.removeAllElements();
            faceListParent = newParent;
            if (faceListParent != null) {
                for (int i = 0, n = model.getChildCount(faceListParent); i < n; i++) {
                    lm.addElement(model.getChild(faceListParent, i));
                }
            }
        }
        if ((path == null) || (path.getPathCount() < 4)) {
            list.clearSelection();
        } else {
            list.setSelectedIndex(((javax.swing.tree.TreeNode) (path.getPathComponent(2))).getIndex(((javax.swing.tree.TreeNode) (path.getPathComponent(3)))));
            list.scrollRectToVisible(list.getCellBounds(list.getSelectedIndex(), list.getSelectedIndex()));
        }
        isUpdating--;
    }

    private void updatePreview() {
        isUpdating++;
        previewPanel.setSelectedFont(fontChooser.getSelectedFont());
        isUpdating--;
    }

    private void doCollectionChanged() {
        javax.swing.JList list = selectionPanel.getCollectionList();
        javax.swing.tree.TreePath path = fontChooser.getSelectionPath();
        org.jhotdraw.gui.fontchooser.FontCollectionNode oldCollection = ((path != null) && (path.getPathCount() > 1)) ? ((org.jhotdraw.gui.fontchooser.FontCollectionNode) (path.getPathComponent(1))) : null;
        org.jhotdraw.gui.fontchooser.FontFamilyNode oldFamily = ((path != null) && (path.getPathCount() > 2)) ? ((org.jhotdraw.gui.fontchooser.FontFamilyNode) (path.getPathComponent(2))) : null;
        org.jhotdraw.gui.fontchooser.FontFaceNode oldFace = ((path != null) && (path.getPathCount() > 3)) ? ((org.jhotdraw.gui.fontchooser.FontFaceNode) (path.getPathComponent(3))) : null;
        org.jhotdraw.gui.fontchooser.FontCollectionNode newCollection = ((org.jhotdraw.gui.fontchooser.FontCollectionNode) (list.getSelectedValue()));
        org.jhotdraw.gui.fontchooser.FontFamilyNode newFamily = null;
        org.jhotdraw.gui.fontchooser.FontFaceNode newFace = null;
        if (((oldFamily == null) || (oldFace == null)) && (fontChooser.getSelectedFont() != null)) {
            oldFace = new org.jhotdraw.gui.fontchooser.FontFaceNode(fontChooser.getSelectedFont());
            oldFamily = new org.jhotdraw.gui.fontchooser.FontFamilyNode(fontChooser.getSelectedFont().getFamily());
        }
        if ((newCollection != null) && (oldFamily != null)) {
            for (int i = 0, n = newCollection.getChildCount(); i < n; i++) {
                org.jhotdraw.gui.fontchooser.FontFamilyNode aFamily = newCollection.getChildAt(i);
                if (aFamily.compareTo(oldFamily) == 0) {
                    newFamily = aFamily;
                    break;
                }
            }
        }
        if ((newFamily != null) && (oldFace != null)) {
            // search in the new family for the face
            for (org.jhotdraw.gui.fontchooser.FontFaceNode aFace : newFamily.faces()) {
                if (aFace.compareTo(oldFace) == 0) {
                    newFace = aFace;
                    break;
                }
            }
        } else if (((newFace == null) && (oldFamily != null)) && (oldFace != null)) {
            OuterLoop : for (org.jhotdraw.gui.fontchooser.FontFamilyNode aFamily : newCollection.families()) {
                for (org.jhotdraw.gui.fontchooser.FontFaceNode aFace : aFamily.faces()) {
                    if (aFace.compareTo(oldFace) == 0) {
                        newFace = aFace;
                        newFamily = ((org.jhotdraw.gui.fontchooser.FontFamilyNode) (aFace.getParent()));
                        break OuterLoop;
                    }
                }
            }
        }
        if (newCollection != null) {
            if ((newFamily == null) && (newCollection.getChildCount() > 0)) {
                newFamily = newCollection.getChildAt(0);
            }
            if (newFamily != null) {
                if ((newFace == null) && (newFamily.getChildCount() > 0)) {
                    newFace = newFamily.getChildAt(0);
                }
            }
        }
        setNewSelectionPath(newCollection, newFamily, newFace);
    }

    private void doFamilyChanged() {
        javax.swing.JList list = selectionPanel.getFamilyList();
        javax.swing.tree.TreePath path = fontChooser.getSelectionPath();
        org.jhotdraw.gui.fontchooser.FontCollectionNode oldCollection = ((path != null) && (path.getPathCount() > 1)) ? ((org.jhotdraw.gui.fontchooser.FontCollectionNode) (path.getPathComponent(1))) : null;
        org.jhotdraw.gui.fontchooser.FontFamilyNode oldFamily = ((path != null) && (path.getPathCount() > 2)) ? ((org.jhotdraw.gui.fontchooser.FontFamilyNode) (path.getPathComponent(2))) : null;
        org.jhotdraw.gui.fontchooser.FontFaceNode oldFace = ((path != null) && (path.getPathCount() > 3)) ? ((org.jhotdraw.gui.fontchooser.FontFaceNode) (path.getPathComponent(3))) : null;
        org.jhotdraw.gui.fontchooser.FontCollectionNode newCollection = oldCollection;
        org.jhotdraw.gui.fontchooser.FontFamilyNode newFamily = ((org.jhotdraw.gui.fontchooser.FontFamilyNode) (list.getSelectedValue()));
        org.jhotdraw.gui.fontchooser.FontFaceNode newFace = null;
        if ((newFamily != null) && (oldFace != null)) {
            for (int i = 0, n = newFamily.getChildCount(); i < n; i++) {
                org.jhotdraw.gui.fontchooser.FontFaceNode aFace = newFamily.getChildAt(i);
                if (aFace.compareTo(oldFace) == 0) {
                    newFace = aFace;
                    break;
                }
            }
        }
        if (newCollection != null) {
            if ((newFamily == null) && (newCollection.getChildCount() > 0)) {
                newFamily = newCollection.getChildAt(0);
            }
            if (newFamily != null) {
                if ((newFace == null) && (newFamily.getChildCount() > 0)) {
                    newFace = newFamily.getChildAt(0);
                }
            }
        }
        setNewSelectionPath(newCollection, newFamily, newFace);
    }

    private void doFaceChanged() {
        javax.swing.JList list = selectionPanel.getFaceList();
        javax.swing.tree.TreePath path = fontChooser.getSelectionPath();
        org.jhotdraw.gui.fontchooser.FontCollectionNode oldCollection = ((path != null) && (path.getPathCount() > 1)) ? ((org.jhotdraw.gui.fontchooser.FontCollectionNode) (path.getPathComponent(1))) : null;
        org.jhotdraw.gui.fontchooser.FontFamilyNode oldFamily = ((path != null) && (path.getPathCount() > 2)) ? ((org.jhotdraw.gui.fontchooser.FontFamilyNode) (path.getPathComponent(2))) : null;
        org.jhotdraw.gui.fontchooser.FontFaceNode oldFace = ((path != null) && (path.getPathCount() > 3)) ? ((org.jhotdraw.gui.fontchooser.FontFaceNode) (path.getPathComponent(3))) : null;
        org.jhotdraw.gui.fontchooser.FontCollectionNode newCollection = oldCollection;
        org.jhotdraw.gui.fontchooser.FontFamilyNode newFamily = oldFamily;
        org.jhotdraw.gui.fontchooser.FontFaceNode newFace = ((org.jhotdraw.gui.fontchooser.FontFaceNode) (list.getSelectedValue()));
        setNewSelectionPath(newCollection, newFamily, newFace);
    }

    private void setNewSelectionPath(org.jhotdraw.gui.fontchooser.FontCollectionNode newCollection, org.jhotdraw.gui.fontchooser.FontFamilyNode newFamily, org.jhotdraw.gui.fontchooser.FontFaceNode newFace) {
        org.jhotdraw.gui.fontchooser.FontChooserModel model = fontChooser.getModel();
        javax.swing.tree.TreePath newPath;
        if (newFace != null) {
            newPath = new javax.swing.tree.TreePath(new java.lang.Object[]{ model.getRoot(), newCollection, newFamily, newFace });
        } else if (newFamily != null) {
            newPath = new javax.swing.tree.TreePath(new java.lang.Object[]{ model.getRoot(), newCollection, newFamily });
        } else if (newCollection != null) {
            newPath = new javax.swing.tree.TreePath(new java.lang.Object[]{ model.getRoot(), newCollection });
        } else {
            newPath = new javax.swing.tree.TreePath(model.getRoot());
        }
        fontChooser.setSelectionPath(newPath);
    }

    private class SelectionPanelHandler implements java.awt.event.KeyListener , java.awt.event.MouseListener , javax.swing.event.ListSelectionListener {
        @java.lang.Override
        public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
            if (isUpdating == 0) {
                java.lang.Object src = evt.getSource();
                if (src == selectionPanel.getCollectionList()) {
                    doCollectionChanged();
                } else if (src == selectionPanel.getFamilyList()) {
                    doFamilyChanged();
                } else if (src == selectionPanel.getFaceList()) {
                    doFaceChanged();
                }
            }
        }

        @java.lang.Override
        public void keyReleased(java.awt.event.KeyEvent evt) {
            java.lang.Object src = evt.getSource();
            switch (evt.getKeyCode()) {
                case java.awt.event.KeyEvent.VK_LEFT :
                    if (src == selectionPanel.getCollectionList()) {
                    } else if (src == selectionPanel.getFamilyList()) {
                        selectionPanel.getCollectionList().requestFocus();
                    } else if (src == selectionPanel.getFaceList()) {
                        selectionPanel.getFamilyList().requestFocus();
                    }
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_RIGHT :
                    if (src == selectionPanel.getCollectionList()) {
                        selectionPanel.getFamilyList().requestFocus();
                    } else if (src == selectionPanel.getFamilyList()) {
                        selectionPanel.getFaceList().requestFocus();
                    } else if (src == selectionPanel.getFaceList()) {
                    }
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_ESCAPE :
                    fontChooser.cancelSelection();
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_ENTER :
                    fontChooser.approveSelection();
                    evt.consume();
                    break;
            }
        }

        @java.lang.Override
        public void keyPressed(java.awt.event.KeyEvent evt) {
        }

        @java.lang.Override
        public void keyTyped(java.awt.event.KeyEvent evt) {
        }

        @java.lang.Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            if ((evt.getClickCount() == 2) && (evt.getButton() == java.awt.event.MouseEvent.BUTTON1)) {
                fontChooser.approveSelection();
            }
        }

        @java.lang.Override
        public void mousePressed(java.awt.event.MouseEvent e) {
        }

        @java.lang.Override
        public void mouseReleased(java.awt.event.MouseEvent e) {
        }

        @java.lang.Override
        public void mouseEntered(java.awt.event.MouseEvent e) {
        }

        @java.lang.Override
        public void mouseExited(java.awt.event.MouseEvent e) {
        }
    }

    private class FontChooserHandler implements java.beans.PropertyChangeListener , javax.swing.event.TreeModelListener {
        @java.lang.Override
        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            java.lang.String name = evt.getPropertyName();
            if (((name == null) && (org.jhotdraw.gui.JFontChooser.SELECTION_PATH_PROPERTY == null)) || ((name != null) && name.equals(org.jhotdraw.gui.JFontChooser.SELECTION_PATH_PROPERTY))) {
                updateCollectionList();
                updateFamilyList();
                updateFaceList();
            } else if (((name == null) && (org.jhotdraw.gui.JFontChooser.SELECTED_FONT_PROPERTY == null)) || ((name != null) && name.equals(org.jhotdraw.gui.JFontChooser.SELECTED_FONT_PROPERTY))) {
                updatePreview();
            } else if (((name == null) && (org.jhotdraw.gui.JFontChooser.MODEL_PROPERTY == null)) || ((name != null) && name.equals(org.jhotdraw.gui.JFontChooser.MODEL_PROPERTY))) {
                org.jhotdraw.gui.fontchooser.FontChooserModel m = ((org.jhotdraw.gui.fontchooser.FontChooserModel) (evt.getOldValue()));
                if (m != null) {
                    m.removeTreeModelListener(this);
                }
                m = ((org.jhotdraw.gui.fontchooser.FontChooserModel) (evt.getNewValue()));
                if (m != null) {
                    m.addTreeModelListener(this);
                }
                updateCollectionList();
                updateFamilyList();
                updateFaceList();
            }
        }

        @java.lang.Override
        public void treeNodesChanged(javax.swing.event.TreeModelEvent e) {
            updateCollectionList();
            updateFamilyList();
            updateFaceList();
        }

        @java.lang.Override
        public void treeNodesInserted(javax.swing.event.TreeModelEvent e) {
            updateCollectionList();
            updateFamilyList();
            updateFaceList();
        }

        @java.lang.Override
        public void treeNodesRemoved(javax.swing.event.TreeModelEvent e) {
            updateCollectionList();
            updateFamilyList();
            updateFaceList();
        }

        @java.lang.Override
        public void treeStructureChanged(javax.swing.event.TreeModelEvent e) {
            updateCollectionList();
            updateFamilyList();
            updateFaceList();
        }
    }
}