/* @(#)JActivityView.java  2.1 2011-05-01

Copyright (c) 2011 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
license agreement you entered into with the copyright holders. For details
see accompanying license terms.
 */
package org.jhotdraw.gui;
import org.jhotdraw.api.gui.ActivityModel;
/**
 * A class to view an {@code ActivityModel}.
 *
 * <p>This view is used by {@code JActivityWindow}. <hr> <b>Design Patterns</b>
 *
 * <p><em>Framework</em><br>
 * The interfaces and classes listed below define a framework for progress management.<br>
 * Contract: {@link ActivityManager}, {@link ActivityModel}, {@link JActivityWindow}, {@link JActivityIndicator}.
 */
// End of variables declaration//GEN-END:variables
public class JActivityView extends javax.swing.JPanel {
    private static final long serialVersionUID = 1L;

    public static final java.lang.String REQUEST_REMOVE_PROPERTY = "requestRemove";

    private org.jhotdraw.api.gui.ActivityModel model;

    private class Handler implements java.beans.PropertyChangeListener , javax.swing.event.ChangeListener {
        @java.lang.Override
        public void stateChanged(javax.swing.event.ChangeEvent e) {
            /* if (model != null && model.getValue() >= model.getMaximum()) {
            close();
            }
             */
        }

        /**
         * Thread safe.
         */
        @java.lang.Override
        public void propertyChange(final java.beans.PropertyChangeEvent evt) {
            org.jhotdraw.gui.ActivityManager.invokeAndWait(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    updateProperties(evt);
                }
            });
        }
    }

    private org.jhotdraw.gui.JActivityView.Handler handler = new org.jhotdraw.gui.JActivityView.Handler();

    private org.jhotdraw.util.ResourceBundleUtil labels;

    /**
     * Creates a new JActivityView.
     */
    public JActivityView() {
        this(null);
    }

    /**
     * Creates a new JActivityView.
     */
    public JActivityView(org.jhotdraw.api.gui.ActivityModel model) {
        labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.gui.Labels");
        initComponents();
        closeButton.setVisible(false);
        setModel(model);
    }

    public void setModel(org.jhotdraw.api.gui.ActivityModel newValue) {
        if (model != null) {
            model.removeChangeListener(handler);
            model.removePropertyChangeListener(handler);
            progressBar.setModel(new javax.swing.DefaultBoundedRangeModel());
        }
        model = newValue;
        progressBar.setModel(newValue);
        if (model != null) {
            model.addChangeListener(handler);
            model.addPropertyChangeListener(handler);
            updateTitle();
            updateProperties(null);
            progressBar.setModel(model);
        }
    }

    public org.jhotdraw.api.gui.ActivityModel getModel() {
        return model;
    }

    private void updateProperties(java.beans.PropertyChangeEvent evt) {
        if ((evt == null) || (evt.getPropertyName() == null)) {
            updateNote();
            updateWarning();
            updateError();
            updateCancelable();
            updateCanceled();
            updateClosed();
            updateIndeterminate();
            return;
        }
        java.lang.String name = evt.getPropertyName();
        if (((name == null) && (org.jhotdraw.api.gui.ActivityModel.NOTE_PROPERTY == null)) || ((name != null) && name.equals(org.jhotdraw.api.gui.ActivityModel.NOTE_PROPERTY))) {
            updateNote();
        } else if (((name == null) && (org.jhotdraw.api.gui.ActivityModel.WARNING_PROPERTY == null)) || ((name != null) && name.equals(org.jhotdraw.api.gui.ActivityModel.WARNING_PROPERTY))) {
            updateWarning();
        } else if (((name == null) && (org.jhotdraw.api.gui.ActivityModel.ERROR_PROPERTY == null)) || ((name != null) && name.equals(org.jhotdraw.api.gui.ActivityModel.ERROR_PROPERTY))) {
            updateError();
        } else if (((name == null) && (org.jhotdraw.api.gui.ActivityModel.CANCELABLE_PROPERTY == null)) || ((name != null) && name.equals(org.jhotdraw.api.gui.ActivityModel.CANCELABLE_PROPERTY))) {
            updateCancelable();
        } else if (((name == null) && (org.jhotdraw.api.gui.ActivityModel.CANCELED_PROPERTY == null)) || ((name != null) && name.equals(org.jhotdraw.api.gui.ActivityModel.CANCELED_PROPERTY))) {
            updateCanceled();
            updateCancelable();
        } else if (((name == null) && (org.jhotdraw.api.gui.ActivityModel.INDETERMINATE_PROPERTY == null)) || ((name != null) && name.equals(org.jhotdraw.api.gui.ActivityModel.INDETERMINATE_PROPERTY))) {
            updateIndeterminate();
        } else if (((name == null) && (org.jhotdraw.api.gui.ActivityModel.CLOSED_PROPERTY == null)) || ((name != null) && name.equals(org.jhotdraw.api.gui.ActivityModel.CLOSED_PROPERTY))) {
            updateCancelable();
            updateCanceled();
            updateClosed();
        }
    }

    private void updateTitle() {
        titleLabel.setText(model.getTitle());
    }

    private void updateNote() {
        java.lang.String txt = model.getNote();
        noteLabel.setText(model.getNote());
    }

    private void updateIndeterminate() {
        progressBar.setIndeterminate(model.isIndeterminate());
    }

    private void updateWarning() {
        java.lang.String txt = model.getWarning();
        warningLabel.setText(txt);
        updateLabelVisibility();
    }

    private void updateError() {
        java.lang.String txt = model.getError();
        errorLabel.setText(txt);
        updateLabelVisibility();
    }

    private void updateLabelVisibility() {
        boolean isError = model.getError() != null;
        boolean isWarning = model.getWarning() != null;
        errorLabel.setVisible(isError);
        warningLabel.setVisible((!isError) && isWarning);
        noteLabel.setVisible((!isError) && (!isWarning));
        revalidate();
    }

    private void updateCancelable() {
        boolean b = model.isCancelable() && (!model.isClosed());
        if (cancelButton.isVisible() != b) {
            cancelButton.setVisible(b);
            revalidate();
        }
    }

    private void updateCanceled() {
        boolean b = (model.isCancelable() && (!model.isCanceled())) && (!model.isClosed());
        if (cancelButton.isEnabled() != b) {
            cancelButton.setEnabled(b);
            revalidate();
        }
    }

    private void updateClosed() {
        boolean b = model.isClosed();
        if (progressBar.isEnabled() == b) {
            closeButton.setVisible(model.getError() != null);
            progressBar.setEnabled(!b);
            revalidate();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        titleLabel = new javax.swing.JLabel();
        noteLabel = new javax.swing.JLabel();
        warningLabel = new javax.swing.JLabel();
        errorLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        cancelButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();
        separator = new javax.swing.JSeparator();
        setLayout(new java.awt.GridBagLayout());
        titleLabel.setFont(titleLabel.getFont().deriveFont(titleLabel.getFont().getStyle() | java.awt.Font.BOLD));
        titleLabel.setText("title");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 12, 0, 12);
        add(titleLabel, gridBagConstraints);
        noteLabel.setFont(noteLabel.getFont().deriveFont(noteLabel.getFont().getSize() - 2.0F));
        noteLabel.setText("note");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 12, 0, 12);
        add(noteLabel, gridBagConstraints);
        warningLabel.setFont(new java.awt.Font("Lucida Grande", 0, 11));// NOI18N

        warningLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jhotdraw/gui/images/ProgressView.warningIcon.png")));// NOI18N

        warningLabel.setText("warning");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 12, 0, 12);
        add(warningLabel, gridBagConstraints);
        errorLabel.setFont(new java.awt.Font("Lucida Grande", 0, 11));// NOI18N

        errorLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jhotdraw/gui/images/ProgressView.errorIcon.png")));// NOI18N

        errorLabel.setText("error");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 12, 0, 12);
        add(errorLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 12, 12, 12);
        add(progressBar, gridBagConstraints);
        cancelButton.setText(labels.getString("ActivityView.cancel.text"));// NOI18N

        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancel(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 12, 12);
        add(cancelButton, gridBagConstraints);
        closeButton.setText(labels.getString("ActivityView.close.text"));// NOI18N

        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                close(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 12, 12);
        add(closeButton, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 1.0;
        add(separator, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents


    private void close(java.awt.event.ActionEvent evt) {
        // GEN-FIRST:event_close
        closeButton.setEnabled(false);
        putClientProperty(org.jhotdraw.gui.JActivityView.REQUEST_REMOVE_PROPERTY, true);
    }// GEN-LAST:event_close


    private void cancel(java.awt.event.ActionEvent evt) {
        // GEN-FIRST:event_cancel
        model.cancel();
        model.setNote("Cancelling...");
    }// GEN-LAST:event_cancel


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;

    private javax.swing.JButton closeButton;

    private javax.swing.JLabel errorLabel;

    private javax.swing.JLabel noteLabel;

    private javax.swing.JProgressBar progressBar;

    private javax.swing.JSeparator separator;

    private javax.swing.JLabel titleLabel;

    private javax.swing.JLabel warningLabel;
}