/* @(#)SVGConstants.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.svg;
/**
 * SVGConstants.
 */
public class SVGConstants {
    public static final java.lang.String SVG_NAMESPACE = "http://www.w3.org/2000/svg";

    public static final java.lang.String SVG_MIMETYPE = "image/svg+xml";

    public static final java.util.Map<java.lang.String, java.awt.Color> SVG_COLORS;

    static {
        java.util.LinkedHashMap<java.lang.String, java.awt.Color> map = new java.util.LinkedHashMap<java.lang.String, java.awt.Color>();
        // SVG 1.2 Tiny colors
        map.put("black", new java.awt.Color(0, 0, 0));
        map.put("green", new java.awt.Color(0, 128, 0));
        map.put("silver", new java.awt.Color(192, 192, 192));
        map.put("lime", new java.awt.Color(0, 255, 0));
        map.put("gray", new java.awt.Color(128, 128, 128));
        map.put("olive", new java.awt.Color(128, 128, 0));
        map.put("white", new java.awt.Color(255, 255, 255));
        map.put("yellow", new java.awt.Color(255, 255, 0));
        map.put("maroon", new java.awt.Color(128, 0, 0));
        map.put("navy", new java.awt.Color(0, 0, 128));
        map.put("red", new java.awt.Color(255, 0, 0));
        map.put("blue", new java.awt.Color(0, 0, 255));
        map.put("purple", new java.awt.Color(128, 0, 128));
        map.put("teal", new java.awt.Color(0, 128, 128));
        map.put("fuchsia", new java.awt.Color(255, 0, 255));
        map.put("aqua", new java.awt.Color(0, 255, 255));
        // SVG 1.1 colors
        map.put("aliceblue", new java.awt.Color(240, 248, 255));
        map.put("antiquewhite", new java.awt.Color(250, 235, 215));
        map.put("aqua", new java.awt.Color(0, 255, 255));
        map.put("aquamarine", new java.awt.Color(127, 255, 212));
        map.put("azure", new java.awt.Color(240, 255, 255));
        map.put("beige", new java.awt.Color(245, 245, 220));
        map.put("bisque", new java.awt.Color(255, 228, 196));
        map.put("black", new java.awt.Color(0, 0, 0));
        map.put("blanchedalmond", new java.awt.Color(255, 235, 205));
        map.put("blue", new java.awt.Color(0, 0, 255));
        map.put("blueviolet", new java.awt.Color(138, 43, 226));
        map.put("brown", new java.awt.Color(165, 42, 42));
        map.put("burlywood", new java.awt.Color(222, 184, 135));
        map.put("cadetblue", new java.awt.Color(95, 158, 160));
        map.put("chartreuse", new java.awt.Color(127, 255, 0));
        map.put("chocolate", new java.awt.Color(210, 105, 30));
        map.put("coral", new java.awt.Color(255, 127, 80));
        map.put("cornflowerblue", new java.awt.Color(100, 149, 237));
        map.put("cornsilk", new java.awt.Color(255, 248, 220));
        map.put("crimson", new java.awt.Color(220, 20, 60));
        map.put("cyan", new java.awt.Color(0, 255, 255));
        map.put("darkblue", new java.awt.Color(0, 0, 139));
        map.put("darkcyan", new java.awt.Color(0, 139, 139));
        map.put("darkgoldenrod", new java.awt.Color(184, 134, 11));
        map.put("darkgray", new java.awt.Color(169, 169, 169));
        map.put("darkgreen", new java.awt.Color(0, 100, 0));
        map.put("darkgrey", new java.awt.Color(169, 169, 169));
        map.put("darkkhaki", new java.awt.Color(189, 183, 107));
        map.put("darkmagenta", new java.awt.Color(139, 0, 139));
        map.put("darkolivegreen", new java.awt.Color(85, 107, 47));
        map.put("darkorange", new java.awt.Color(255, 140, 0));
        map.put("darkorchid", new java.awt.Color(153, 50, 204));
        map.put("darkred", new java.awt.Color(139, 0, 0));
        map.put("darksalmon", new java.awt.Color(233, 150, 122));
        map.put("darkseagreen", new java.awt.Color(143, 188, 143));
        map.put("darkslateblue", new java.awt.Color(72, 61, 139));
        map.put("darkslategray", new java.awt.Color(47, 79, 79));
        map.put("darkslategrey", new java.awt.Color(47, 79, 79));
        map.put("darkturquoise", new java.awt.Color(0, 206, 209));
        map.put("darkviolet", new java.awt.Color(148, 0, 211));
        map.put("deeppink", new java.awt.Color(255, 20, 147));
        map.put("deepskyblue", new java.awt.Color(0, 191, 255));
        map.put("dimgray", new java.awt.Color(105, 105, 105));
        map.put("dimgrey", new java.awt.Color(105, 105, 105));
        map.put("dodgerblue", new java.awt.Color(30, 144, 255));
        map.put("firebrick", new java.awt.Color(178, 34, 34));
        map.put("floralwhite", new java.awt.Color(255, 250, 240));
        map.put("forestgreen", new java.awt.Color(34, 139, 34));
        map.put("fuchsia", new java.awt.Color(255, 0, 255));
        map.put("gainsboro", new java.awt.Color(220, 220, 220));
        map.put("ghostwhite", new java.awt.Color(248, 248, 255));
        map.put("gold", new java.awt.Color(255, 215, 0));
        map.put("goldenrod", new java.awt.Color(218, 165, 32));
        map.put("gray", new java.awt.Color(128, 128, 128));
        map.put("grey", new java.awt.Color(128, 128, 128));
        map.put("green", new java.awt.Color(0, 128, 0));
        map.put("greenyellow", new java.awt.Color(173, 255, 47));
        map.put("honeydew", new java.awt.Color(240, 255, 240));
        map.put("hotpink", new java.awt.Color(255, 105, 180));
        map.put("indianred", new java.awt.Color(205, 92, 92));
        map.put("indigo", new java.awt.Color(75, 0, 130));
        map.put("ivory", new java.awt.Color(255, 255, 240));
        map.put("khaki", new java.awt.Color(240, 230, 140));
        map.put("lavender", new java.awt.Color(230, 230, 250));
        map.put("lavenderblush", new java.awt.Color(255, 240, 245));
        map.put("lawngreen", new java.awt.Color(124, 252, 0));
        map.put("lemonchiffon", new java.awt.Color(255, 250, 205));
        map.put("lightblue", new java.awt.Color(173, 216, 230));
        map.put("lightcoral", new java.awt.Color(240, 128, 128));
        map.put("lightcyan", new java.awt.Color(224, 255, 255));
        map.put("lightgoldenrodyellow", new java.awt.Color(250, 250, 210));
        map.put("lightgray", new java.awt.Color(211, 211, 211));
        map.put("lightgreen", new java.awt.Color(144, 238, 144));
        map.put("lightgrey", new java.awt.Color(211, 211, 211));
        map.put("lightpink", new java.awt.Color(255, 182, 193));
        map.put("lightsalmon", new java.awt.Color(255, 160, 122));
        map.put("lightseagreen", new java.awt.Color(32, 178, 170));
        map.put("lightskyblue", new java.awt.Color(135, 206, 250));
        map.put("lightslategray", new java.awt.Color(119, 136, 153));
        map.put("lightslategrey", new java.awt.Color(119, 136, 153));
        map.put("lightsteelblue", new java.awt.Color(176, 196, 222));
        map.put("lightyellow", new java.awt.Color(255, 255, 224));
        map.put("lime", new java.awt.Color(0, 255, 0));
        map.put("limegreen", new java.awt.Color(50, 205, 50));
        map.put("linen", new java.awt.Color(250, 240, 230));
        map.put("magenta", new java.awt.Color(255, 0, 255));
        map.put("maroon", new java.awt.Color(128, 0, 0));
        map.put("mediumaquamarine", new java.awt.Color(102, 205, 170));
        map.put("mediumblue", new java.awt.Color(0, 0, 205));
        map.put("mediumorchid", new java.awt.Color(186, 85, 211));
        map.put("mediumpurple", new java.awt.Color(147, 112, 219));
        map.put("mediumseagreen", new java.awt.Color(60, 179, 113));
        map.put("mediumslateblue", new java.awt.Color(123, 104, 238));
        map.put("mediumspringgreen", new java.awt.Color(0, 250, 154));
        map.put("mediumturquoise", new java.awt.Color(72, 209, 204));
        map.put("mediumvioletred", new java.awt.Color(199, 21, 133));
        map.put("midnightblue", new java.awt.Color(25, 25, 112));
        map.put("mintcream", new java.awt.Color(245, 255, 250));
        map.put("mistyrose", new java.awt.Color(255, 228, 225));
        map.put("moccasin", new java.awt.Color(255, 228, 181));
        map.put("navajowhite", new java.awt.Color(255, 222, 173));
        map.put("navy", new java.awt.Color(0, 0, 128));
        map.put("oldlace", new java.awt.Color(253, 245, 230));
        map.put("olive", new java.awt.Color(128, 128, 0));
        map.put("olivedrab", new java.awt.Color(107, 142, 35));
        map.put("orange", new java.awt.Color(255, 165, 0));
        map.put("orangered", new java.awt.Color(255, 69, 0));
        map.put("orchid", new java.awt.Color(218, 112, 214));
        map.put("palegoldenrod", new java.awt.Color(238, 232, 170));
        map.put("palegreen", new java.awt.Color(152, 251, 152));
        map.put("paleturquoise", new java.awt.Color(175, 238, 238));
        map.put("palevioletred", new java.awt.Color(219, 112, 147));
        map.put("papayawhip", new java.awt.Color(255, 239, 213));
        map.put("peachpuff", new java.awt.Color(255, 218, 185));
        map.put("peru", new java.awt.Color(205, 133, 63));
        map.put("pink", new java.awt.Color(255, 192, 203));
        map.put("plum", new java.awt.Color(221, 160, 221));
        map.put("powderblue", new java.awt.Color(176, 224, 230));
        map.put("purple", new java.awt.Color(128, 0, 128));
        map.put("red", new java.awt.Color(255, 0, 0));
        map.put("rosybrown", new java.awt.Color(188, 143, 143));
        map.put("royalblue", new java.awt.Color(65, 105, 225));
        map.put("saddlebrown", new java.awt.Color(139, 69, 19));
        map.put("salmon", new java.awt.Color(250, 128, 114));
        map.put("sandybrown", new java.awt.Color(244, 164, 96));
        map.put("seagreen", new java.awt.Color(46, 139, 87));
        map.put("seashell", new java.awt.Color(255, 245, 238));
        map.put("sienna", new java.awt.Color(160, 82, 45));
        map.put("silver", new java.awt.Color(192, 192, 192));
        map.put("skyblue", new java.awt.Color(135, 206, 235));
        map.put("slateblue", new java.awt.Color(106, 90, 205));
        map.put("slategray", new java.awt.Color(112, 128, 144));
        map.put("slategrey", new java.awt.Color(112, 128, 144));
        map.put("snow", new java.awt.Color(255, 250, 250));
        map.put("springgreen", new java.awt.Color(0, 255, 127));
        map.put("steelblue", new java.awt.Color(70, 130, 180));
        map.put("tan", new java.awt.Color(210, 180, 140));
        map.put("teal", new java.awt.Color(0, 128, 128));
        map.put("thistle", new java.awt.Color(216, 191, 216));
        map.put("tomato", new java.awt.Color(255, 99, 71));
        map.put("turquoise", new java.awt.Color(64, 224, 208));
        map.put("violet", new java.awt.Color(238, 130, 238));
        map.put("wheat", new java.awt.Color(245, 222, 179));
        map.put("white", new java.awt.Color(255, 255, 255));
        map.put("whitesmoke", new java.awt.Color(245, 245, 245));
        map.put("yellow", new java.awt.Color(255, 255, 0));
        map.put("yellowgreen", new java.awt.Color(154, 205, 50));
        // SVG 1.2 Tiny system colors
        map.put("activeborder", javax.swing.UIManager.getColor("activeCaptionBorder"));
        map.put("activecaption", javax.swing.UIManager.getColor("activeCaption"));
        map.put("appworkspace", javax.swing.UIManager.getColor("window"));
        map.put("background", javax.swing.UIManager.getColor("desktop"));
        map.put("buttonface", javax.swing.UIManager.getColor("control"));
        map.put("buttonhighlight", javax.swing.UIManager.getColor("controlHighlight"));
        map.put("buttonshadow", javax.swing.UIManager.getColor("controlShadow"));
        map.put("buttontext", javax.swing.UIManager.getColor("controlText"));
        map.put("captiontext", javax.swing.UIManager.getColor("activeCaptionText"));
        map.put("graytext", javax.swing.UIManager.getColor("textInactiveText"));
        map.put("highlight", javax.swing.UIManager.getColor("textHighlight"));
        map.put("highlighttext", javax.swing.UIManager.getColor("textHighlightText"));
        map.put("inactiveborder", javax.swing.UIManager.getColor("inactiveCaptionBorder"));
        map.put("inactivecaption", javax.swing.UIManager.getColor("inactiveCaption"));
        map.put("inactivecaptiontext", javax.swing.UIManager.getColor("inactiveCaptionText"));
        map.put("infobackground", javax.swing.UIManager.getColor("info"));
        map.put("infotext", javax.swing.UIManager.getColor("infoText"));
        map.put("menu", javax.swing.UIManager.getColor("menu"));
        map.put("menutext", javax.swing.UIManager.getColor("menuText"));
        map.put("scrollbar", javax.swing.UIManager.getColor("scrollbar"));
        map.put("threeddarkshadow", javax.swing.UIManager.getColor("controlDkShadow"));
        map.put("threedface", javax.swing.UIManager.getColor("control"));
        map.put("threedhighlight", javax.swing.UIManager.getColor("controlHighlight"));
        map.put("threedlightshadow", javax.swing.UIManager.getColor("controlLtHighlight"));
        map.put("threedshadow", javax.swing.UIManager.getColor("controlShadow"));
        map.put("window", javax.swing.UIManager.getColor("window"));
        map.put("windowframe", javax.swing.UIManager.getColor("windowBorder"));
        map.put("windowtext", javax.swing.UIManager.getColor("windowText"));
        SVG_COLORS = java.util.Collections.unmodifiableMap(map);
    }

    public static final java.util.Map<java.lang.String, org.jhotdraw.draw.AttributeKeys.WindingRule> SVG_FILL_RULES;

    static {
        java.util.HashMap<java.lang.String, org.jhotdraw.draw.AttributeKeys.WindingRule> m = new java.util.HashMap<java.lang.String, org.jhotdraw.draw.AttributeKeys.WindingRule>();
        m.put("nonzero", org.jhotdraw.draw.AttributeKeys.WindingRule.NON_ZERO);
        m.put("evenodd", org.jhotdraw.draw.AttributeKeys.WindingRule.EVEN_ODD);
        SVG_FILL_RULES = java.util.Collections.unmodifiableMap(m);
    }

    public static final java.util.Map<java.lang.String, java.lang.Integer> SVG_STROKE_LINECAPS;

    static {
        java.util.HashMap<java.lang.String, java.lang.Integer> m = new java.util.HashMap<java.lang.String, java.lang.Integer>();
        m.put("butt", java.awt.BasicStroke.CAP_BUTT);
        m.put("round", java.awt.BasicStroke.CAP_ROUND);
        m.put("square", java.awt.BasicStroke.CAP_SQUARE);
        SVG_STROKE_LINECAPS = java.util.Collections.unmodifiableMap(m);
    }

    public static final java.util.Map<java.lang.String, java.lang.Integer> SVG_STROKE_LINEJOINS;

    static {
        java.util.HashMap<java.lang.String, java.lang.Integer> m = new java.util.HashMap<java.lang.String, java.lang.Integer>();
        m.put("miter", java.awt.BasicStroke.JOIN_MITER);
        m.put("round", java.awt.BasicStroke.JOIN_ROUND);
        m.put("bevel", java.awt.BasicStroke.JOIN_BEVEL);
        SVG_STROKE_LINEJOINS = java.util.Collections.unmodifiableMap(m);
    }

    public static final java.util.Map<java.lang.String, java.lang.Double> SVG_ABSOLUTE_FONT_SIZES;

    static {
        java.util.HashMap<java.lang.String, java.lang.Double> m = new java.util.HashMap<java.lang.String, java.lang.Double>();
        m.put("xx-small", 6.944444);
        m.put("x-small", 8.3333333);
        m.put("small", 10.0);
        m.put("medium", 12.0);
        m.put("large", 14.4);
        m.put("x-large", 17.28);
        m.put("xx-large", 20.736);
        SVG_ABSOLUTE_FONT_SIZES = java.util.Collections.unmodifiableMap(m);
    }

    public static final java.util.Map<java.lang.String, java.lang.Double> SVG_RELATIVE_FONT_SIZES;

    static {
        java.util.HashMap<java.lang.String, java.lang.Double> m = new java.util.HashMap<java.lang.String, java.lang.Double>();
        m.put("larger", 1.2);
        m.put("smaller", 0.83333333);
        SVG_RELATIVE_FONT_SIZES = java.util.Collections.unmodifiableMap(m);
    }

    public static final java.util.Map<java.lang.String, org.jhotdraw.samples.svg.SVGAttributeKeys.TextAnchor> SVG_TEXT_ANCHORS;

    static {
        java.util.HashMap<java.lang.String, org.jhotdraw.samples.svg.SVGAttributeKeys.TextAnchor> m = new java.util.HashMap<java.lang.String, org.jhotdraw.samples.svg.SVGAttributeKeys.TextAnchor>();
        m.put("start", org.jhotdraw.samples.svg.SVGAttributeKeys.TextAnchor.START);
        m.put("middle", org.jhotdraw.samples.svg.SVGAttributeKeys.TextAnchor.MIDDLE);
        m.put("end", org.jhotdraw.samples.svg.SVGAttributeKeys.TextAnchor.END);
        SVG_TEXT_ANCHORS = java.util.Collections.unmodifiableMap(m);
    }

    public static final java.util.Map<java.lang.String, org.jhotdraw.samples.svg.SVGAttributeKeys.TextAlign> SVG_TEXT_ALIGNS;

    static {
        java.util.HashMap<java.lang.String, org.jhotdraw.samples.svg.SVGAttributeKeys.TextAlign> m = new java.util.HashMap<java.lang.String, org.jhotdraw.samples.svg.SVGAttributeKeys.TextAlign>();
        m.put("start", org.jhotdraw.samples.svg.SVGAttributeKeys.TextAlign.START);
        m.put("center", org.jhotdraw.samples.svg.SVGAttributeKeys.TextAlign.CENTER);
        m.put("end", org.jhotdraw.samples.svg.SVGAttributeKeys.TextAlign.END);
        SVG_TEXT_ALIGNS = java.util.Collections.unmodifiableMap(m);
    }

    /**
     * Prevents instance creation.
     */
    private SVGConstants() {
    }
}