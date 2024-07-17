/**
 *
 * @(#)DefaultHarmonicColorModel.java <p>Copyright (c) 2008 The authors and contributors of JHotDraw. You may not use, copy or modify
this file, except in compliance with the accompanying license terms.
 */
package org.jhotdraw.color;
/**
 * DefaultHarmonicColorModel.
 */
public class DefaultHarmonicColorModel extends javax.swing.AbstractListModel implements org.jhotdraw.color.HarmonicColorModel , java.lang.Cloneable {
    private static final long serialVersionUID = 1L;

    protected java.beans.PropertyChangeSupport propertySupport = new java.beans.PropertyChangeSupport(this);

    private java.util.ArrayList<java.awt.Color> colors;

    private org.jhotdraw.color.ColorSliderModel sliderModel;

    private int base;

    private java.util.ArrayList<org.jhotdraw.color.HarmonicRule> rules;

    private float customHueConstraint = 30.0F / 360.0F;

    private int adjusting;

    public DefaultHarmonicColorModel() {
        java.awt.color.ColorSpace sys = org.jhotdraw.color.HSLPhysiologicColorSpace.getInstance();
        sliderModel = new org.jhotdraw.color.DefaultColorSliderModel(sys);
        colors = new java.util.ArrayList<>();
        rules = new java.util.ArrayList<>();
        base = 0;
        add(java.awt.Color.RED);
        javax.swing.DefaultListModel x;
    }

    @java.lang.Override
    public void setSize(int newValue) {
        int oldSize = size();
        while (colors.size() > newValue) {
            colors.remove(colors.size() - 1);
        } 
        while (colors.size() < newValue) {
            colors.add(null);
        } 
        if (oldSize < newValue) {
            fireIntervalRemoved(this, oldSize, newValue - 1);
        } else if (oldSize > newValue) {
            fireIntervalRemoved(this, newValue, oldSize - 1);
        }
    }

    @java.lang.Override
    public int size() {
        return colors.size();
    }

    @java.lang.Override
    public boolean isAdjusting() {
        return adjusting > 0;
    }

    @java.lang.Override
    public void set(int index, java.awt.Color newValue) {
        adjusting++;
        java.awt.Color oldValue = colors.set(index, newValue);
        for (org.jhotdraw.color.HarmonicRule r : rules) {
            r.colorChanged(this, index, oldValue, newValue);
        }
        for (org.jhotdraw.color.HarmonicRule r : rules) {
            if (r.getBaseIndex() == index) {
                r.apply(this);
            }
        }
        adjusting--;
        fireContentsChanged(this, index, index);
    }

    @java.lang.Override
    public void applyRules() {
        for (org.jhotdraw.color.HarmonicRule r : rules) {
            if (r.getBaseIndex() == base) {
                r.apply(this);
            }
        }
    }

    @java.lang.Override
    public java.awt.Color get(int index) {
        return colors.get(index);
    }

    @java.lang.Override
    public boolean add(java.awt.Color c) {
        boolean b = colors.add(c);
        if (b) {
            fireIntervalAdded(this, size() - 1, size() - 1);
        }
        return b;
    }

    @java.lang.Override
    public void setBase(int newValue) {
        base = newValue;
    }

    @java.lang.Override
    public int getBase() {
        return base;
    }

    @java.lang.Override
    public float[] RGBtoComponent(int rgb, float[] hsb) {
        return org.jhotdraw.color.ColorUtil.fromColor(sliderModel.getColorSpace(), new java.awt.Color(rgb));
    }

    @java.lang.Override
    public int componentToRGB(float h, float s, float b) {
        return org.jhotdraw.color.ColorUtil.toRGB24(sliderModel.getColorSpace(), h, s, b);
    }

    @java.lang.Override
    public int getSize() {
        return size();
    }

    @java.lang.Override
    public java.lang.Object getElementAt(int index) {
        return get(index);
    }

    @java.lang.Override
    public java.awt.color.ColorSpace getColorSpace() {
        return sliderModel.getColorSpace();
    }

    @java.lang.Override
    public void addRule(org.jhotdraw.color.HarmonicRule newValue) {
        rules.add(newValue);
    }

    @java.lang.Override
    public void removeAllRules() {
        rules.clear();
    }

    @java.lang.Override
    public void addPropertyChangeListener(java.beans.PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(java.lang.String propertyName, java.beans.PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(propertyName, listener);
    }

    @java.lang.Override
    public void removePropertyChangeListener(java.beans.PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(java.lang.String propertyName, java.beans.PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(propertyName, listener);
    }

    protected void firePropertyChange(java.lang.String propertyName, boolean oldValue, boolean newValue) {
        propertySupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    protected void firePropertyChange(java.lang.String propertyName, int oldValue, int newValue) {
        propertySupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    protected void firePropertyChange(java.lang.String propertyName, java.lang.Object oldValue, java.lang.Object newValue) {
        propertySupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    @java.lang.Override
    public org.jhotdraw.color.DefaultHarmonicColorModel clone() {
        org.jhotdraw.color.DefaultHarmonicColorModel that;
        try {
            that = ((org.jhotdraw.color.DefaultHarmonicColorModel) (super.clone()));
        } catch (java.lang.CloneNotSupportedException ex) {
            java.lang.InternalError error = new java.lang.InternalError("Clone failed");
            error.initCause(ex);
            throw error;
        }
        that.propertySupport = new java.beans.PropertyChangeSupport(that);
        return that;
    }

    @java.lang.Override
    public void setColorSpace(java.awt.color.ColorSpace newValue) {
        java.awt.color.ColorSpace oldValue = sliderModel.getColorSpace();
        sliderModel.setColorSpace(newValue);
        firePropertyChange(org.jhotdraw.color.HarmonicColorModel.COLOR_SPACE_PROPERTY, oldValue, newValue);
        for (int i = 0; i < colors.size(); i++) {
            if (get(i) != null) {
                set(i, new java.awt.Color(newValue, org.jhotdraw.color.ColorUtil.fromColor(newValue, get(i)), 1.0F));
            }
        }
        fireContentsChanged(this, 0, size() - 1);
    }
}