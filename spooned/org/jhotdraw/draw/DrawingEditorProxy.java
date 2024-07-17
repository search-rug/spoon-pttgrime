/* @(#)DrawingEditorProxy.java

Copyright (c) 2007-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw;
/**
 * DrawingEditorProxy. <hr> <b>Design Patterns</b>
 *
 * <p><em>Proxy</em><br>
 * To remove the need for null-handling, {@code AbstractTool} makes use of a proxy for {@code DrawingEditor}. Subject: {@link DrawingEditor}; Proxy: {@link DrawingEditorProxy}; Client: {@link org.jhotdraw.draw.tool.AbstractTool}. <hr>
 */
public class DrawingEditorProxy extends org.jhotdraw.beans.AbstractBean implements org.jhotdraw.draw.DrawingEditor {
    private static final long serialVersionUID = 1L;

    private org.jhotdraw.draw.DrawingEditor target;

    private class Forwarder implements java.beans.PropertyChangeListener , java.io.Serializable {
        private static final long serialVersionUID = 1L;

        @java.lang.Override
        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
        }
    }

    private org.jhotdraw.draw.DrawingEditorProxy.Forwarder forwarder;

    public DrawingEditorProxy() {
        forwarder = new org.jhotdraw.draw.DrawingEditorProxy.Forwarder();
    }

    /**
     * Sets the target of the proxy.
     */
    public void setTarget(org.jhotdraw.draw.DrawingEditor newValue) {
        if (target != null) {
            target.removePropertyChangeListener(forwarder);
        }
        this.target = newValue;
        if (target != null) {
            target.addPropertyChangeListener(forwarder);
        }
    }

    /**
     * Gets the target of the proxy.
     */
    public org.jhotdraw.draw.DrawingEditor getTarget() {
        return target;
    }

    @java.lang.Override
    public void add(org.jhotdraw.draw.DrawingView view) {
        target.add(view);
    }

    @java.lang.Override
    public void remove(org.jhotdraw.draw.DrawingView view) {
        target.remove(view);
    }

    @java.lang.Override
    public java.util.Collection<org.jhotdraw.draw.DrawingView> getDrawingViews() {
        return target.getDrawingViews();
    }

    @java.lang.Override
    public org.jhotdraw.draw.DrawingView getActiveView() {
        return target == null ? null : target.getActiveView();
    }

    @java.lang.Override
    public void setActiveView(org.jhotdraw.draw.DrawingView newValue) {
        target.setActiveView(newValue);
    }

    public org.jhotdraw.draw.DrawingView getFocusedView() {
        return target == null ? null : target.getActiveView();
    }

    @java.lang.Override
    public void setTool(org.jhotdraw.draw.tool.Tool t) {
        target.setTool(t);
    }

    @java.lang.Override
    public org.jhotdraw.draw.tool.Tool getTool() {
        return target.getTool();
    }

    @java.lang.Override
    public void setCursor(java.awt.Cursor c) {
        target.setCursor(c);
    }

    @java.lang.Override
    public org.jhotdraw.draw.DrawingView findView(java.awt.Container c) {
        return target.findView(c);
    }

    @java.lang.Override
    public <T> void setDefaultAttribute(org.jhotdraw.draw.AttributeKey<T> key, T value) {
        target.setDefaultAttribute(key, value);
    }

    @java.lang.Override
    public <T> T getDefaultAttribute(org.jhotdraw.draw.AttributeKey<T> key) {
        return target.getDefaultAttribute(key);
    }

    @java.lang.Override
    public void applyDefaultAttributesTo(org.jhotdraw.draw.figure.Figure f) {
        target.applyDefaultAttributesTo(f);
    }

    @java.lang.Override
    public java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> getDefaultAttributes() {
        return target.getDefaultAttributes();
    }

    @java.lang.Override
    public void setEnabled(boolean newValue) {
        target.setEnabled(newValue);
    }

    @java.lang.Override
    public boolean isEnabled() {
        return target.isEnabled();
    }

    @java.lang.Override
    public <T> void setHandleAttribute(org.jhotdraw.draw.AttributeKey<T> key, T value) {
        target.setHandleAttribute(key, value);
    }

    @java.lang.Override
    public <T> T getHandleAttribute(org.jhotdraw.draw.AttributeKey<T> key) {
        return target.getHandleAttribute(key);
    }

    @java.lang.Override
    public void setInputMap(javax.swing.InputMap newValue) {
        target.setInputMap(newValue);
    }

    @java.lang.Override
    public javax.swing.InputMap getInputMap() {
        return target.getInputMap();
    }

    @java.lang.Override
    public void setActionMap(javax.swing.ActionMap newValue) {
        target.setActionMap(newValue);
    }

    @java.lang.Override
    public javax.swing.ActionMap getActionMap() {
        return target.getActionMap();
    }
}