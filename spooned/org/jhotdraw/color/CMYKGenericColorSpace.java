/* @(#)CMYKGenericColorSpace.java

Copyright (c) 2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.color;
/**
 * A {@code ColorSpace} for CMYK color components using a generic CMYK profile.
 */
public class CMYKGenericColorSpace extends java.awt.color.ICC_ColorSpace {
    private static final long serialVersionUID = 1L;

    private static org.jhotdraw.color.CMYKGenericColorSpace instance;

    public static org.jhotdraw.color.CMYKGenericColorSpace getInstance() {
        if (org.jhotdraw.color.CMYKGenericColorSpace.instance == null) {
            try {
                org.jhotdraw.color.CMYKGenericColorSpace.instance = new org.jhotdraw.color.CMYKGenericColorSpace();
            } catch (java.io.IOException ex) {
                java.lang.InternalError error = new java.lang.InternalError("Can't instanciate CMYKColorSpace");
                error.initCause(ex);
                throw error;
            }
        }
        return org.jhotdraw.color.CMYKGenericColorSpace.instance;
    }

    public CMYKGenericColorSpace() throws java.io.IOException {
        super(java.awt.color.ICC_Profile.getInstance(org.jhotdraw.color.CMYKGenericColorSpace.class.getResourceAsStream("Generic CMYK Profile.icc")));
    }
}