/* @(#)ODGConstants.java

Copyright (c) 2007 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.odg;
/**
 * ODGConstants.
 */
public class ODGConstants {
    public static final java.lang.String OFFICE_NAMESPACE = "urn:oasis:names:tc:opendocument:xmlns:office:1.0";

    public static final java.lang.String DRAWING_NAMESPACE = "urn:oasis:names:tc:opendocument:xmlns:drawing:1.0";

    public static final java.lang.String SVG_NAMESPACE = "urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0";

    public static final java.lang.String STYLE_NAMESPACE = "urn:oasis:names:tc:opendocument:xmlns:style:1.0";

    public static final java.lang.String TEXT_NAMESPACE = "urn:oasis:names:tc:opendocument:xmlns:text:1.0";

    public static enum StrokeStyle {

        NONE,
        DASH,
        SOLID;
    }

    public static final java.util.Map<java.lang.String, org.jhotdraw.samples.odg.ODGConstants.StrokeStyle> STROKE_STYLES;

    static {
        java.util.HashMap<java.lang.String, org.jhotdraw.samples.odg.ODGConstants.StrokeStyle> m = new java.util.HashMap<java.lang.String, org.jhotdraw.samples.odg.ODGConstants.StrokeStyle>();
        m.put("none", org.jhotdraw.samples.odg.ODGConstants.StrokeStyle.NONE);
        m.put("dash", org.jhotdraw.samples.odg.ODGConstants.StrokeStyle.DASH);
        m.put("solid", org.jhotdraw.samples.odg.ODGConstants.StrokeStyle.SOLID);
        STROKE_STYLES = java.util.Collections.unmodifiableMap(m);
    }

    public static enum FillStyle {

        NONE,
        SOLID,
        BITMAP,
        GRADIENT,
        HATCH;
    }

    public static final java.util.Map<java.lang.String, org.jhotdraw.samples.odg.ODGConstants.FillStyle> FILL_STYLES;

    static {
        java.util.HashMap<java.lang.String, org.jhotdraw.samples.odg.ODGConstants.FillStyle> m = new java.util.HashMap<java.lang.String, org.jhotdraw.samples.odg.ODGConstants.FillStyle>();
        m.put("none", org.jhotdraw.samples.odg.ODGConstants.FillStyle.NONE);
        m.put("solid", org.jhotdraw.samples.odg.ODGConstants.FillStyle.SOLID);
        m.put("bitmap", org.jhotdraw.samples.odg.ODGConstants.FillStyle.BITMAP);
        m.put("gradient", org.jhotdraw.samples.odg.ODGConstants.FillStyle.GRADIENT);
        m.put("hatch", org.jhotdraw.samples.odg.ODGConstants.FillStyle.HATCH);
        FILL_STYLES = java.util.Collections.unmodifiableMap(m);
    }

    /**
     * Prevent instance creation.
     */
    private ODGConstants() {
    }
}