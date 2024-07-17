/* @(#)JActivityWindow.java

Copyright (c) 2011 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
license agreement you entered into with the copyright holders. For details
see accompanying license terms.
 */
package org.jhotdraw.gui;
import org.jhotdraw.api.gui.ActivityModel;
import javax.swing.plaf.metal.*;
/**
 * The {@code JActivityWindow} displays all progress models registered in the progress manager.
 *
 * <p>Once created, {@code JActivityWindow} becomes visible automatically if the if a progress model
 * is added to the progress manager.
 *
 * <p>If an activity finishes successfully, it is automatically removed.
 *
 * <p>You typically only want to create a single instance. To do this, call {@code JActivityWindow.getInstance();}.
 *
 * <p><hr> <b>Design Patterns</b>
 *
 * <p><em>Framework</em><br>
 * The interfaces and classes listed below define a framework for progress management.<br>
 * Contract: {@link ActivityManager}, {@link ActivityModel}, {@link JActivityWindow}, {@link JActivityIndicator}.
 */
// End of variables declaration//GEN-END:variables
public class JActivityWindow extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;

    private static org.jhotdraw.gui.JActivityWindow instance;

    private javax.swing.JPanel progressPanel;

    private org.jhotdraw.gui.ActivityManager manager;

    private java.lang.Object activityOwner;

    /**
     * Delay for automatic removal of successfully completed activities. Specify 0 for immediate
     * removal. Specify -1 for no removal.
     *
     * <p>FIXME - Changing this value to -1 requires changing code in JActivityView.
     */
    private int normalRemovalDelay = 1500;

    /**
     * Delay for automatic removal of completed activities with a warning. Specify 0 for immediate
     * removal. Specify -1 for no removal.
     *
     * <p>FIXME - Changing this value to -1 requires changing code in JActivityView.
     */
    private int warningRemovalDelay = 3000;

    /**
     * Delay for automatic removal of completed activities with an error. Specify 0 for immediate
     * removal. Specify -1 for no removal.
     *
     * <p>FIXME - Changing this value to -1 requires changing code in JActivityView.
     */
    private int errorRemovalDelay = -1;

    private java.util.HashMap<org.jhotdraw.api.gui.ActivityModel, org.jhotdraw.gui.JActivityView> views = new java.util.HashMap<>();

    private class Handler implements org.jhotdraw.gui.event.ActivityManagerListener , java.beans.PropertyChangeListener {
        @java.lang.Override
        public void activityModelAdded(org.jhotdraw.gui.event.ActivityManagerEvent evt) {
            org.jhotdraw.api.gui.ActivityModel pm = evt.getActivityModel();
            if ((activityOwner == null) || activityOwner.equals(pm.getOwner())) {
                addActivityModel(pm);
            }
        }

        @java.lang.Override
        public void activityModelRemoved(org.jhotdraw.gui.event.ActivityManagerEvent evt) {
            final org.jhotdraw.api.gui.ActivityModel pm = evt.getActivityModel();
            int delay = (pm.getError() != null) ? errorRemovalDelay : pm.getWarning() != null ? warningRemovalDelay : normalRemovalDelay;
            if (delay == (-1)) {
                JActivityWindow.this.setVisible(true);
                return;
            }
            java.awt.event.ActionListener tt = new java.awt.event.ActionListener() {
                @java.lang.Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    removeActivityModel(pm);
                }
            };
            if (delay == 0) {
                tt.actionPerformed(null);
            } else {
                javax.swing.Timer t = new javax.swing.Timer(pm.getError() != null ? delay : delay, tt);
                t.setRepeats(false);
                t.start();
            }
        }

        @java.lang.Override
        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            if (((evt.getPropertyName() == null) && (org.jhotdraw.gui.JActivityView.REQUEST_REMOVE_PROPERTY == null)) || ((evt.getPropertyName() != null) && evt.getPropertyName().equals(org.jhotdraw.gui.JActivityView.REQUEST_REMOVE_PROPERTY))) {
                removeActivityModel(((org.jhotdraw.gui.JActivityView) (evt.getSource())).getModel());
            }
        }
    }

    private org.jhotdraw.gui.JActivityWindow.Handler handler = new org.jhotdraw.gui.JActivityWindow.Handler();

    private org.jhotdraw.util.ResourceBundleUtil labels;

    /**
     * Creates new form JActivityWindow
     */
    public JActivityWindow() {
        this(org.jhotdraw.gui.ActivityManager.getInstance());
    }

    public JActivityWindow(org.jhotdraw.gui.ActivityManager pm) {
        labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.gui.Labels");
        initComponents();
        setFocusable(false);// needed for Mac OS X

        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        progressPanel = new javax.swing.JPanel() {
            private static final long serialVersionUID = 1L;

            @java.lang.Override
            public java.awt.Dimension getPreferredSize() {
                java.awt.Dimension d = super.getPreferredSize();
                d.width = 300;
                return d;
            }
        };
        progressPanel.setLayout(new javax.swing.BoxLayout(progressPanel, javax.swing.BoxLayout.Y_AXIS));
        // getContentPane().add(progressPanel, java.awt.BorderLayout.CENTER);
        scrollPane.setViewportView(progressPanel);
        disclosureToggle.setIcon(javax.swing.UIManager.getIcon("Tree.collapsedIcon"));
        disclosureToggle.setSelectedIcon(javax.swing.UIManager.getIcon("Tree.expandedIcon"));
        disclosureToggle.setUI(((javax.swing.plaf.metal.MetalToggleButtonUI) (javax.swing.plaf.metal.MetalToggleButtonUI.createUI(disclosureToggle))));
        org.jhotdraw.util.prefs.PreferencesUtil.installFramePrefsHandler(java.util.prefs.Preferences.userNodeForPackage(org.jhotdraw.gui.JActivityWindow.class), "progressFrame", this);
        setActivityManager(pm);
    }

    public void setActivityManager(org.jhotdraw.gui.ActivityManager newValue) {
        if (manager != null) {
            manager.removeActivityManagerListener(handler);
        }
        this.manager = newValue;
        if (manager != null) {
            manager.addActivityManagerListener(handler);
        }
        updateActivityModels();
        updateInfoPanel();
    }

    public org.jhotdraw.gui.ActivityManager getActivityManager() {
        return manager;
    }

    @java.lang.SuppressWarnings("unchecked")
    private void updateActivityModels() {
        for (org.jhotdraw.gui.JActivityView pv : views.values()) {
            pv.setModel(null);
            progressPanel.remove(pv);
        }
        for (org.jhotdraw.api.gui.ActivityModel am : ((java.util.HashMap<org.jhotdraw.api.gui.ActivityModel, org.jhotdraw.gui.JActivityView>) (views.clone())).keySet()) {
            removeActivityModel(am);
        }
        if (manager != null) {
            java.util.ArrayList<org.jhotdraw.api.gui.ActivityModel> pms = manager.getActivityModels();
            for (org.jhotdraw.api.gui.ActivityModel pm : pms) {
                addActivityModel(pm);
            }
            if (!views.isEmpty()) {
                // setVisible(true);
            }
        }
    }

    public static org.jhotdraw.gui.JActivityWindow getInstance() {
        if (org.jhotdraw.gui.JActivityWindow.instance == null) {
            org.jhotdraw.gui.JActivityWindow.instance = new org.jhotdraw.gui.JActivityWindow();
        }
        return org.jhotdraw.gui.JActivityWindow.instance;
    }

    public void addActivityModel(final org.jhotdraw.api.gui.ActivityModel model) {
        if (!views.containsKey(model)) {
            org.jhotdraw.gui.JActivityView viewer = new org.jhotdraw.gui.JActivityView(model);
            viewer.addPropertyChangeListener(handler);
            progressPanel.add(viewer);
            views.put(model, viewer);
            updateInfoPanel();
            pack();
            if (!isVisible()) {
                // setVisible(true);
            }
            viewer.repaint();
        }
    }

    /**
     * Set to a non-null value to only display progress models of a specific owner. Set to null to
     * display all models.
     *
     * @param newValue
     */
    public void setActivityOwner(java.lang.Object newValue) {
        java.lang.Object oldValue = this.activityOwner;
        this.activityOwner = newValue;
        if (oldValue != newValue) {
            updateActivityModels();
        }
    }

    /**
     * Owner is used to filter the progress models.
     *
     * @return The owner of the progress models being shown or null, if all models are shown.
     */
    public java.lang.Object getActivityOwner() {
        return activityOwner;
    }

    @java.lang.Override
    public java.awt.Dimension getPreferredSize() {
        java.awt.Dimension d = super.getPreferredSize();
        d.height = java.lang.Math.min(600, d.height);
        return d;
    }

    public void removeActivityModel(final org.jhotdraw.api.gui.ActivityModel model) {
        if (views.containsKey(model)) {
            org.jhotdraw.gui.JActivityView viewer = views.get(model);
            viewer.removePropertyChangeListener(handler);
            progressPanel.remove(viewer);
            views.remove(model);
            updateInfoPanel();
            pack();
        }
    }

    /**
     * Updates the info label and the cancel all button on the info panel.
     */
    private void updateInfoPanel() {
        int count = views.size();
        switch (count) {
            case 0 :
                infoLabel.setText(labels.getString("ActivityWindow.noActivities.text"));
                cancelAllButton.setEnabled(false);
                break;
            case 1 :
                infoLabel.setText(labels.getString("ActivityWindow.oneActivity.text"));
                cancelAllButton.setEnabled(true);
                break;
            default :
                infoLabel.setText(labels.getFormatted("ActivityWindow.nActivities.text", count));
                cancelAllButton.setEnabled(true);
                break;
        }
    }

    private static void invokeAndWait(java.lang.Runnable r) {
        if (javax.swing.SwingUtilities.isEventDispatchThread()) {
            r.run();
        } else {
            try {
                javax.swing.SwingUtilities.invokeAndWait(r);
            } catch (java.lang.InterruptedException | java.lang.reflect.InvocationTargetException ex) {
                ex.printStackTrace();
            }
        }
    }

    @java.lang.Override
    public void dispose() {
        super.dispose();
        setActivityManager(null);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        infoPanel = new javax.swing.JPanel();
        disclosureToggle = new javax.swing.JToggleButton();
        infoLabel = new javax.swing.JLabel();
        cancelAllButton = new javax.swing.JButton();
        strutPanel = new javax.swing.JPanel();
        viewPanel = new javax.swing.JPanel();
        separator = new javax.swing.JSeparator();
        scrollPane = new javax.swing.JScrollPane();
        org.jhotdraw.gui.JActivityWindow.FormListener formListener = new org.jhotdraw.gui.JActivityWindow.FormListener();
        setTitle("Activity");
        infoPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12));
        infoPanel.setLayout(new java.awt.GridBagLayout());
        disclosureToggle.setSelected(true);
        disclosureToggle.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        disclosureToggle.setFocusPainted(false);
        disclosureToggle.setContentAreaFilled(false);
        disclosureToggle.setBorderPainted(false);
        disclosureToggle.setRequestFocusEnabled(false);
        disclosureToggle.addItemListener(formListener);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        infoPanel.add(disclosureToggle, gridBagConstraints);
        infoLabel.setFont(new java.awt.Font("Dialog", 0, 11));// NOI18N

        infoLabel.setText(labels.getString("ActivityWindow.noActivities.text"));// NOI18N

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        infoPanel.add(infoLabel, gridBagConstraints);
        cancelAllButton.setText(labels.getString("ActivityWindow.cancelAll.text"));// NOI18N

        cancelAllButton.setEnabled(false);
        cancelAllButton.addActionListener(formListener);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        infoPanel.add(cancelAllButton, gridBagConstraints);
        getContentPane().add(infoPanel, java.awt.BorderLayout.NORTH);
        strutPanel.setPreferredSize(new java.awt.Dimension(400, 0));
        strutPanel.setLayout(null);
        getContentPane().add(strutPanel, java.awt.BorderLayout.SOUTH);
        viewPanel.setLayout(new java.awt.BorderLayout());
        viewPanel.add(separator, java.awt.BorderLayout.NORTH);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        viewPanel.add(scrollPane, java.awt.BorderLayout.CENTER);
        getContentPane().add(viewPanel, java.awt.BorderLayout.CENTER);
        pack();
    }

    // Code for dispatching events from components to event handlers.
    private class FormListener implements java.awt.event.ActionListener , java.awt.event.ItemListener {
        FormListener() {
        }

        public void actionPerformed(java.awt.event.ActionEvent evt) {
            if (evt.getSource() == cancelAllButton) {
                JActivityWindow.this.cancelAll(evt);
            }
        }

        public void itemStateChanged(java.awt.event.ItemEvent evt) {
            if (evt.getSource() == disclosureToggle) {
                JActivityWindow.this.disclosureStateChanged(evt);
            }
        }
    }

    private void disclosureStateChanged(java.awt.event.ItemEvent evt) {
        // GEN-FIRST:event_disclosureStateChanged
        viewPanel.setVisible(disclosureToggle.isSelected());
        pack();
    }// GEN-LAST:event_disclosureStateChanged


    private void cancelAll(java.awt.event.ActionEvent evt) {
        // GEN-FIRST:event_cancelAll
        java.awt.Component[] components = progressPanel.getComponents();
        for (java.awt.Component component : components) {
            if (component instanceof org.jhotdraw.gui.JActivityView) {
                ((org.jhotdraw.gui.JActivityView) (component)).getModel().cancel();
            }
        }
    }// GEN-LAST:event_cancelAll


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelAllButton;

    private javax.swing.JToggleButton disclosureToggle;

    private javax.swing.JLabel infoLabel;

    private javax.swing.JPanel infoPanel;

    private javax.swing.JScrollPane scrollPane;

    private javax.swing.JSeparator separator;

    private javax.swing.JPanel strutPanel;

    private javax.swing.JPanel viewPanel;
}