/* @(#)PaletteColorChooserUI.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.gui.plaf.palette;
/**
 * PaletteColorChooserUI.
 */
public class PaletteColorChooserUI extends javax.swing.plaf.ColorChooserUI {
    protected org.jhotdraw.gui.plaf.palette.colorchooser.PaletteColorChooserMainPanel mainPanel;

    protected javax.swing.JColorChooser chooser;

    protected javax.swing.event.ChangeListener previewListener;

    protected java.beans.PropertyChangeListener propertyChangeListener;

    protected javax.swing.colorchooser.AbstractColorChooserPanel[] defaultChoosers;

    protected javax.swing.JComponent previewPanel;

    private static javax.swing.TransferHandler defaultTransferHandler = new org.jhotdraw.gui.plaf.palette.PaletteColorChooserUI.ColorTransferHandler();

    private java.awt.event.MouseListener previewMouseListener;

    public static javax.swing.plaf.ComponentUI createUI(javax.swing.JComponent c) {
        return new org.jhotdraw.gui.plaf.palette.PaletteColorChooserUI();
    }

    @java.lang.Override
    public void installUI(javax.swing.JComponent c) {
        chooser = ((javax.swing.JColorChooser) (c));
        javax.swing.colorchooser.AbstractColorChooserPanel[] oldPanels = chooser.getChooserPanels();
        installDefaults();
        chooser.setLayout(new java.awt.BorderLayout());
        mainPanel = new org.jhotdraw.gui.plaf.palette.colorchooser.PaletteColorChooserMainPanel();
        chooser.add(mainPanel);
        defaultChoosers = createDefaultChoosers();
        chooser.setChooserPanels(defaultChoosers);
        installPreviewPanel();
        javax.swing.colorchooser.AbstractColorChooserPanel[] newPanels = chooser.getChooserPanels();
        updateColorChooserPanels(oldPanels, newPanels);
        // Note: install listeners only after we have fully installed
        // all chooser panels. If we do it earlier, we send property
        // events too early.
        installListeners();
        chooser.applyComponentOrientation(c.getComponentOrientation());
    }

    protected javax.swing.colorchooser.AbstractColorChooserPanel[] createDefaultChoosers() {
        java.lang.String[] defaultChooserNames = ((java.lang.String[]) (org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.getInstance().get("ColorChooser.defaultChoosers")));
        java.util.ArrayList<javax.swing.colorchooser.AbstractColorChooserPanel> panels = new java.util.ArrayList<>(defaultChooserNames.length);
        for (java.lang.String defaultChooserName : defaultChooserNames) {
            try {
                panels.add(((javax.swing.colorchooser.AbstractColorChooserPanel) (java.lang.Class.forName(defaultChooserName).newInstance())));
            } catch (java.security.AccessControlException e) {
                // suppress
                java.lang.System.err.println("PaletteColorChooserUI warning: unable to instantiate " + defaultChooserName);
                e.printStackTrace();
            } catch (java.lang.Exception e) {
                // throw new InternalError("Unable to instantiate "+defaultChoosers[i]);
                // suppress
                java.lang.System.err.println("PaletteColorChooserUI warning: unable to instantiate " + defaultChooserName);
                e.printStackTrace();
            } catch (java.lang.UnsupportedClassVersionError e) {
                // suppress
                java.lang.System.err.println("PaletteColorChooserUI warning: unable to instantiate " + defaultChooserName);
                // e.printStackTrace();
            } catch (java.lang.Throwable t) {
                java.lang.System.err.println("PaletteColorChooserUI warning: unable to instantiate " + defaultChooserName);
            }
        }
        // AbstractColorChooserPanel[] panels = new AbstractColorChooserPanel[defaultChoosers.length];
        return panels.toArray(new javax.swing.colorchooser.AbstractColorChooserPanel[panels.size()]);
    }

    @java.lang.Override
    public void uninstallUI(javax.swing.JComponent c) {
        chooser.remove(mainPanel);
        uninstallListeners();
        uninstallDefaultChoosers();
        uninstallDefaults();
        mainPanel.setPreviewPanel(null);
        if (previewPanel instanceof javax.swing.plaf.UIResource) {
            chooser.setPreviewPanel(null);
        }
        mainPanel = null;
        previewPanel = null;
        defaultChoosers = null;
        chooser = null;
    }

    protected void installDefaults() {
        org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel.installColorsAndFont(chooser, "ColorChooser.background", "ColorChooser.foreground", "ColorChooser.font");
        javax.swing.TransferHandler th = chooser.getTransferHandler();
        if ((th == null) || (th instanceof javax.swing.plaf.UIResource)) {
            chooser.setTransferHandler(org.jhotdraw.gui.plaf.palette.PaletteColorChooserUI.defaultTransferHandler);
        }
    }

    protected void uninstallDefaults() {
        if (chooser.getTransferHandler() instanceof javax.swing.plaf.UIResource) {
            chooser.setTransferHandler(null);
        }
    }

    protected void installListeners() {
        propertyChangeListener = createPropertyChangeListener();
        chooser.addPropertyChangeListener(propertyChangeListener);
        previewListener = new org.jhotdraw.gui.plaf.palette.PaletteColorChooserUI.PreviewListener();
        chooser.getSelectionModel().addChangeListener(previewListener);
        previewMouseListener = new java.awt.event.MouseAdapter() {
            @java.lang.Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (chooser.getDragEnabled()) {
                    javax.swing.TransferHandler th = chooser.getTransferHandler();
                    th.exportAsDrag(chooser, e, javax.swing.TransferHandler.COPY);
                }
            }
        };
    }

    protected void uninstallListeners() {
        chooser.removePropertyChangeListener(propertyChangeListener);
        chooser.getSelectionModel().removeChangeListener(previewListener);
        previewPanel.removeMouseListener(previewMouseListener);
    }

    protected java.beans.PropertyChangeListener createPropertyChangeListener() {
        return new org.jhotdraw.gui.plaf.palette.PaletteColorChooserUI.PropertyHandler();
    }

    protected void installPreviewPanel() {
        if (previewPanel != null) {
            previewPanel.removeMouseListener(previewMouseListener);
        }
        if (previewPanel != null) {
            mainPanel.setPreviewPanel(null);
        }
        previewPanel = chooser.getPreviewPanel();
        if (((previewPanel != null) && (mainPanel != null)) && ((previewPanel.getSize().getHeight() + previewPanel.getSize().getWidth()) == 0)) {
            mainPanel.setPreviewPanel(null);
            return;
        }
        if ((previewPanel == null) || (previewPanel instanceof javax.swing.plaf.UIResource)) {
            // previewPanel = ColorChooserComponentFactory.getPreviewPanel(); // get from table?
            previewPanel = new org.jhotdraw.gui.plaf.palette.colorchooser.PaletteColorChooserPreviewPanel();
            chooser.setPreviewPanel(previewPanel);
        }
        previewPanel.setForeground(chooser.getColor());
        mainPanel.setPreviewPanel(previewPanel);
        previewPanel.addMouseListener(previewMouseListener);
    }

    class PreviewListener implements javax.swing.event.ChangeListener {
        @java.lang.Override
        public void stateChanged(javax.swing.event.ChangeEvent e) {
            javax.swing.colorchooser.ColorSelectionModel model = ((javax.swing.colorchooser.ColorSelectionModel) (e.getSource()));
            if (previewPanel != null) {
                previewPanel.setForeground(model.getSelectedColor());
                previewPanel.repaint();
            }
        }
    }

    protected void uninstallDefaultChoosers() {
        for (javax.swing.colorchooser.AbstractColorChooserPanel defaultChooser : defaultChoosers) {
            chooser.removeChooserPanel(defaultChooser);
        }
    }

    private void updateColorChooserPanels(javax.swing.colorchooser.AbstractColorChooserPanel[] oldPanels, javax.swing.colorchooser.AbstractColorChooserPanel[] newPanels) {
        for (javax.swing.colorchooser.AbstractColorChooserPanel oldPanel : oldPanels) {
            // remove old panels
            java.awt.Container wrapper = oldPanel.getParent();
            if (wrapper != null) {
                java.awt.Container parent = wrapper.getParent();
                if (parent != null) {
                    parent.remove(wrapper);// remove from hierarchy

                }
                oldPanel.uninstallChooserPanel(chooser);// uninstall

            }
        }
        mainPanel.removeAllColorChooserPanels();
        for (javax.swing.colorchooser.AbstractColorChooserPanel newPanel : newPanels) {
            if (newPanel != null) {
                mainPanel.addColorChooserPanel(newPanel);
            }
        }
        for (javax.swing.colorchooser.AbstractColorChooserPanel newPanel : newPanels) {
            if (newPanel != null) {
                newPanel.installChooserPanel(chooser);
            }
        }
    }

    public class PropertyHandler implements java.beans.PropertyChangeListener {
        @java.lang.Override
        public void propertyChange(java.beans.PropertyChangeEvent e) {
            java.lang.String name = e.getPropertyName();
            if (name.equals(javax.swing.JColorChooser.CHOOSER_PANELS_PROPERTY)) {
                javax.swing.colorchooser.AbstractColorChooserPanel[] oldPanels = ((javax.swing.colorchooser.AbstractColorChooserPanel[]) (e.getOldValue()));
                javax.swing.colorchooser.AbstractColorChooserPanel[] newPanels = ((javax.swing.colorchooser.AbstractColorChooserPanel[]) (e.getNewValue()));
                for (javax.swing.colorchooser.AbstractColorChooserPanel oldPanel : oldPanels) {
                    // remove old panels
                    if (oldPanel != null) {
                        java.awt.Container wrapper = oldPanel.getParent();
                        if (wrapper != null) {
                            java.awt.Container parent = wrapper.getParent();
                            if (parent != null) {
                                parent.remove(wrapper);// remove from hierarchy

                            }
                            oldPanel.uninstallChooserPanel(chooser);// uninstall

                        }
                    }
                }
                mainPanel.removeAllColorChooserPanels();
                for (javax.swing.colorchooser.AbstractColorChooserPanel newPanel : newPanels) {
                    if (newPanel != null) {
                        mainPanel.addColorChooserPanel(newPanel);
                    }
                }
                chooser.applyComponentOrientation(chooser.getComponentOrientation());
                for (javax.swing.colorchooser.AbstractColorChooserPanel newPanel : newPanels) {
                    if (newPanel != null) {
                        newPanel.installChooserPanel(chooser);
                    }
                }
            }
            if (name.equals(javax.swing.JColorChooser.PREVIEW_PANEL_PROPERTY)) {
                if (e.getNewValue() != previewPanel) {
                    installPreviewPanel();
                }
            }
            if ("componentOrientation".equals(name)) {
                java.awt.ComponentOrientation o = ((java.awt.ComponentOrientation) (e.getNewValue()));
                javax.swing.JColorChooser cc = ((javax.swing.JColorChooser) (e.getSource()));
                if (o != ((java.awt.ComponentOrientation) (e.getOldValue()))) {
                    cc.applyComponentOrientation(o);
                    cc.updateUI();
                }
            }
        }
    }

    static class ColorTransferHandler extends javax.swing.TransferHandler implements javax.swing.plaf.UIResource {
        private static final long serialVersionUID = 1L;

        ColorTransferHandler() {
            super("color");
        }
    }
}