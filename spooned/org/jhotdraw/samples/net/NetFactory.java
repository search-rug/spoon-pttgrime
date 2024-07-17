/* @(#)PertFactory.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.net;
/**
 * NetFactory.
 */
public class NetFactory extends org.jhotdraw.xml.DefaultDOMFactory {
    private static final java.lang.Object[][] CLASS_TAGS = new java.lang.Object[][]{ new java.lang.Object[]{ org.jhotdraw.draw.DefaultDrawing.class, "Net" }, new java.lang.Object[]{ org.jhotdraw.samples.net.figures.NodeFigure.class, "node" }, new java.lang.Object[]{ org.jhotdraw.draw.figure.LineConnectionFigure.class, "link" }, new java.lang.Object[]{ org.jhotdraw.draw.figure.GroupFigure.class, "g" }, new java.lang.Object[]{ org.jhotdraw.draw.figure.GroupFigure.class, "g" }, new java.lang.Object[]{ org.jhotdraw.draw.figure.TextAreaFigure.class, "ta" }, new java.lang.Object[]{ org.jhotdraw.draw.connector.LocatorConnector.class, "locConnect" }, new java.lang.Object[]{ org.jhotdraw.draw.connector.ChopRectangleConnector.class, "rectConnect" }, new java.lang.Object[]{ org.jhotdraw.draw.decoration.ArrowTip.class, "arrowTip" }, new java.lang.Object[]{ org.jhotdraw.geom.Insets2D.Double.class, "insets" }, new java.lang.Object[]{ org.jhotdraw.draw.locator.RelativeLocator.class, "relativeLoc" } };

    private static final java.lang.Object[][] ENUM_TAGS = new java.lang.Object[][]{ new java.lang.Object[]{ org.jhotdraw.draw.AttributeKeys.StrokeType.class, "strokeType" } };

    public NetFactory() {
        register("Net", org.jhotdraw.draw.DefaultDrawing.class, null, null);// do not allow processing

        register("node", org.jhotdraw.samples.net.figures.NodeFigure.class, org.jhotdraw.samples.net.NetFactory::readNode, org.jhotdraw.samples.net.NetFactory::writeNode);
        register("link", org.jhotdraw.draw.figure.LineConnectionFigure.class, org.jhotdraw.io.DOMDefaultDrawFigureFactory::readLineConnection, org.jhotdraw.io.DOMDefaultDrawFigureFactory::writeLineConnection);
        register("g", org.jhotdraw.draw.figure.GroupFigure.class, org.jhotdraw.io.DOMDefaultDrawFigureFactory::readGroup, org.jhotdraw.io.DOMDefaultDrawFigureFactory::writeGroup);
        register("ta", org.jhotdraw.draw.figure.TextAreaFigure.class, org.jhotdraw.io.DOMDefaultDrawFigureFactory::readBaseData, org.jhotdraw.io.DOMDefaultDrawFigureFactory::writeBaseData);
        register("locConnect", org.jhotdraw.draw.connector.LocatorConnector.class, org.jhotdraw.io.DOMDefaultDrawFigureFactory::readLocatorConnector, org.jhotdraw.io.DOMDefaultDrawFigureFactory::writeLocatorConnector);
        register("rectConnect", org.jhotdraw.draw.connector.ChopRectangleConnector.class, org.jhotdraw.io.DOMDefaultDrawFigureFactory::readConnector, org.jhotdraw.io.DOMDefaultDrawFigureFactory::writeConnector);
        register("arrowTip", org.jhotdraw.draw.decoration.ArrowTip.class, org.jhotdraw.io.DOMDefaultDrawFigureFactory::readArrowTip, org.jhotdraw.io.DOMDefaultDrawFigureFactory::writeArrowTip);
        register("insets", org.jhotdraw.geom.Insets2D.Double.class, (f, i) -> {
        }, (f, o) -> {
        });// do nothing;

        register("relativeLoc", org.jhotdraw.draw.locator.RelativeLocator.class, (f, i) -> {
        }, (f, o) -> {
        });// do nothing;

        for (java.lang.Object[] o : org.jhotdraw.samples.net.NetFactory.ENUM_TAGS) {
            addEnumClass(((java.lang.String) (o[1])), ((java.lang.Class) (o[0])));
        }
    }

    public static void readNode(org.jhotdraw.samples.net.figures.NodeFigure figure, org.jhotdraw.xml.DOMInput domInput) throws java.io.IOException {
        figure.setBounds(new java.awt.geom.Point2D.Double(domInput.getAttribute("x", 0.0), domInput.getAttribute("y", 0.0)), new java.awt.geom.Point2D.Double(0, 0));
        org.jhotdraw.io.DOMDefaultDrawFigureFactory.readAttributes(figure, domInput);
    }

    public static void writeNode(org.jhotdraw.samples.net.figures.NodeFigure figure, org.jhotdraw.xml.DOMOutput domOutput) throws java.io.IOException {
        java.awt.geom.Rectangle2D.Double b = figure.getBounds();
        domOutput.addAttribute("x", b.x);
        domOutput.addAttribute("y", b.y);
        org.jhotdraw.io.DOMDefaultDrawFigureFactory.writeAttributes(figure, domOutput);
    }
}