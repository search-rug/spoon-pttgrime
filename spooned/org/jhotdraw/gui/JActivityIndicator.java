/* @(#)JActivityIndicator.java

Copyright (c) 2011 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
license agreement you entered into with the copyright holders. For details
see accompanying license terms.
 */
package org.jhotdraw.gui;
import org.jhotdraw.api.gui.ActivityModel;
/**
 * This indicator displays a progress bar when an {@code ActivityModel} is active.
 *
 * <p>The indicator can indicate all activities or only those belonging to a specific owner, see
 * {@link #setActivityOwner}. <hr> <b>Design Patterns</b>
 *
 * <p><em>Framework</em><br>
 * The interfaces and classes listed below define a framework for progress management.<br>
 * Contract: {@link ActivityManager}, {@link ActivityModel}, {@link JActivityWindow}, {@link JActivityIndicator}.
 *
 * @author Werner Randelshofer
 * @version 1.0 2011-09-08 Created.
 */
// End of variables declaration//GEN-END:variables
public class JActivityIndicator extends javax.swing.JPanel {
    private static final long serialVersionUID = 1L;

    private java.util.ArrayList<org.jhotdraw.api.gui.ActivityModel> models = new java.util.ArrayList<>();

    private class Handler implements org.jhotdraw.gui.event.ActivityManagerListener , java.beans.PropertyChangeListener {
        @java.lang.Override
        public void activityModelAdded(org.jhotdraw.gui.event.ActivityManagerEvent evt) {
            org.jhotdraw.api.gui.ActivityModel pm = evt.getActivityModel();
            if ((progressOwner == null) || progressOwner.equals(pm.getOwner())) {
                addActivityModel(pm);
            }
        }

        @java.lang.Override
        public void activityModelRemoved(org.jhotdraw.gui.event.ActivityManagerEvent evt) {
            org.jhotdraw.api.gui.ActivityModel pm = evt.getActivityModel();
            removeActivityModel(pm);
        }

        @java.lang.Override
        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            if (((evt.getPropertyName() == null) && (org.jhotdraw.api.gui.ActivityModel.INDETERMINATE_PROPERTY == null)) || ((evt.getPropertyName() != null) && evt.getPropertyName().equals(org.jhotdraw.api.gui.ActivityModel.INDETERMINATE_PROPERTY))) {
                updateIndeterminate();
            }
            if (((evt.getPropertyName() == null) && (org.jhotdraw.api.gui.ActivityModel.NOTE_PROPERTY == null)) || ((evt.getPropertyName() != null) && evt.getPropertyName().equals(org.jhotdraw.api.gui.ActivityModel.NOTE_PROPERTY))) {
                updateToolTip();
            }
        }
    }

    private org.jhotdraw.gui.JActivityIndicator.Handler handler = new org.jhotdraw.gui.JActivityIndicator.Handler();

    private org.jhotdraw.gui.ActivityManager manager;

    private java.lang.Object progressOwner;

    private org.jhotdraw.util.ResourceBundleUtil labels;

    /**
     * Creates new form JActivityIndicator
     */
    public JActivityIndicator() {
        labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.gui.Labels");
        initComponents();
        setActivityManager(org.jhotdraw.gui.ActivityManager.getInstance());
    }

    public org.jhotdraw.gui.ActivityManager getActivityManager() {
        return manager;
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
        updateProgressBar();
        updateToolTip();
    }

    public java.lang.Object getActivityOwner() {
        return progressOwner;
    }

    public void setActivityOwner(java.lang.Object progressOwner) {
        this.progressOwner = progressOwner;
        updateActivityModels();
    }

    public void addActivityModel(final org.jhotdraw.api.gui.ActivityModel model) {
        if ((progressOwner == null) || (model.getOwner() == progressOwner)) {
            models.add(model);
            model.addPropertyChangeListener(handler);
            updateProgressBar();
            updateToolTip();
            updateIndeterminate();
        }
    }

    public void removeActivityModel(final org.jhotdraw.api.gui.ActivityModel model) {
        if (models.remove(model)) {
            model.removePropertyChangeListener(handler);
            updateProgressBar();
            updateToolTip();
            updateIndeterminate();
        }
    }

    private void updateActivityModels() {
        for (org.jhotdraw.api.gui.ActivityModel pm : models) {
            pm.removePropertyChangeListener(handler);
        }
        models.clear();
        if (manager != null) {
            for (org.jhotdraw.api.gui.ActivityModel pm : manager.getActivityModels()) {
                if ((progressOwner == null) || (pm.getOwner() == progressOwner)) {
                    models.add(pm);
                }
            }
        }
        updateProgressBar();
        updateIndeterminate();
        updateToolTip();
    }

    @java.lang.Override
    public java.awt.Dimension getPreferredSize() {
        return progressBar.getPreferredSize();
    }

    private void updateProgressBar() {
        if (models.isEmpty()) {
            progressBar.setModel(new javax.swing.DefaultBoundedRangeModel());
            progressBar.setEnabled(false);
            progressBar.setVisible(false);
        } else if (models.size() == 1) {
            progressBar.setModel(models.get(0));
            progressBar.setEnabled(true);
            progressBar.setVisible(true);
        } else {
            progressBar.setModel(new javax.swing.DefaultBoundedRangeModel());
            progressBar.setEnabled(true);
            progressBar.setVisible(true);
        }
    }

    private void updateToolTip() {
        if (models.isEmpty()) {
            setToolTipText(labels.getString("ActivityIndicator.noActivities.toolTipText"));
        } else if (models.size() == 1) {
            setToolTipText(models.get(0).getTitle());
            // setToolTipText(labels.getString("ActivityIndicator.oneActivity.toolTipText"));
        } else {
            setToolTipText(labels.getFormatted("ActivityIndicator.nActivities.toolTipText", models.size()));
        }
    }

    private void updateIndeterminate() {
        if (models.size() == 0) {
            progressBar.setIndeterminate(false);
        } else if (models.size() == 1) {
            progressBar.setIndeterminate(models.get(0).isIndeterminate());
        } else {
            progressBar.setIndeterminate(true);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    @java.lang.SuppressWarnings("unchecked")
    private void initComponents() {
        progressBar = new javax.swing.JProgressBar();
        setLayout(new java.awt.BorderLayout());
        add(progressBar, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar progressBar;
}