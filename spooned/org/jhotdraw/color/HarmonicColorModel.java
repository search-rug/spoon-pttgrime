/**
 *
 * @(#)HarmonicColorModel.java <p>Copyright (c) 2008 The authors and contributors of JHotDraw. You may not use, copy or modify
this file, except in compliance with the accompanying license terms.
 */
package org.jhotdraw.color;
/**
 * HarmonicColorModel.
 */
public interface HarmonicColorModel extends javax.swing.ListModel {
    public static final java.lang.String COLOR_SPACE_PROPERTY = "colorSpace";

    public void setBase(int newValue);

    public int getBase();

    public void addRule(org.jhotdraw.color.HarmonicRule rule);

    public void removeAllRules();

    public void applyRules();

    public java.awt.color.ColorSpace getColorSpace();

    public void setColorSpace(java.awt.color.ColorSpace newValue);

    public void setSize(int newValue);

    public int size();

    public boolean isAdjusting();

    public boolean add(java.awt.Color c);

    public void set(int index, java.awt.Color color);

    public java.awt.Color get(int index);

    public float[] RGBtoComponent(int rgb, float[] hsb);

    public int componentToRGB(float h, float s, float b);

    public void addPropertyChangeListener(java.beans.PropertyChangeListener listener);

    public void removePropertyChangeListener(java.beans.PropertyChangeListener listener);
}