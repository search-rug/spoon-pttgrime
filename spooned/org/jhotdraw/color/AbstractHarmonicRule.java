/**
 *
 * @(#)AbstractHarmonicRule.java <p>Copyright (c) 2008 The authors and contributors of JHotDraw. You may not use, copy or modify
this file, except in compliance with the accompanying license terms.
 */
package org.jhotdraw.color;
/**
 * AbstractHarmonicRule.
 */
public abstract class AbstractHarmonicRule implements org.jhotdraw.color.HarmonicRule {
    protected int baseIndex;

    protected int[] derivedIndices;

    @java.lang.Override
    public void setBaseIndex() {
        // this.baseIndex = baseIndex;
    }

    @java.lang.Override
    public int getBaseIndex() {
        return baseIndex;
    }

    @java.lang.Override
    public void setDerivedIndices(int... indices) {
        this.derivedIndices = indices;
    }

    @java.lang.Override
    public int[] getDerivedIndices() {
        return derivedIndices;
    }
}