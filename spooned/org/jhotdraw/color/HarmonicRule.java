/**
 *
 * @(#)HarmonicRule.java <p>Copyright (c) 2008 The authors and contributors of JHotDraw. You may not use, copy or modify
this file, except in compliance with the accompanying license terms.
 */
package org.jhotdraw.color;
/**
 * HarmonicRule.
 */
public interface HarmonicRule {
    public void setBaseIndex();

    public int getBaseIndex();

    public void setDerivedIndices(int... indices);

    public int[] getDerivedIndices();

    public void apply(org.jhotdraw.color.HarmonicColorModel model);

    public void colorChanged(org.jhotdraw.color.HarmonicColorModel model, int index, java.awt.Color oldValue, java.awt.Color newValue);
}