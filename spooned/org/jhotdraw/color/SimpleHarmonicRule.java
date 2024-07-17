/**
 *
 * @(#)SimpleHarmonicRule.java <p>Copyright (c) 2008 The authors and contributors of JHotDraw. You may not use, copy or modify
this file, except in compliance with the accompanying license terms.
 */
package org.jhotdraw.color;
/**
 * SimpleHarmonicRule.
 */
public class SimpleHarmonicRule extends org.jhotdraw.color.AbstractHarmonicRule {
    private float difference;

    private int componentIndex;

    public SimpleHarmonicRule(int componentIndex, float difference, int baseIndex, int... derivedIndices) {
        this.componentIndex = componentIndex;
        this.difference = difference;
        this.baseIndex = baseIndex;
        this.derivedIndices = derivedIndices;
    }

    public void setConstraint(float constraint) {
        this.difference = constraint;
    }

    public float getConstraint(float constraint) {
        return constraint;
    }

    public void setComponentIndex(int newValue) {
        this.componentIndex = newValue;
    }

    public int getComponentIndex() {
        return componentIndex;
    }

    @java.lang.Override
    public void apply(org.jhotdraw.color.HarmonicColorModel model) {
        if (derivedIndices != null) {
            java.awt.Color baseColor = model.get(getBaseIndex());
            if (baseColor != null) {
                float[] derivedComponents = null;
                for (int i = 0; i < derivedIndices.length; i++) {
                    derivedComponents = baseColor.getComponents(derivedComponents);
                    derivedComponents[componentIndex] += difference * (i + 1);
                    model.set(derivedIndices[i], new java.awt.Color(model.getColorSpace(), derivedComponents, 1.0F));
                }
            }
        }
    }

    @java.lang.Override
    public void colorChanged(org.jhotdraw.color.HarmonicColorModel model, int index, java.awt.Color oldValue, java.awt.Color newValue) {
    }
}