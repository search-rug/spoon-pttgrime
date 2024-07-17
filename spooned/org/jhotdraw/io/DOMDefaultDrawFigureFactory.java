/* @(#)DrawFigureFactory.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.io;
/**
 * DOM based figure factory
 */
public class DOMDefaultDrawFigureFactory extends org.jhotdraw.xml.DefaultDOMFactory {
    private static final java.lang.Object[][] ENUM_TAGS = new java.lang.Object[][]{ new java.lang.Object[]{ org.jhotdraw.draw.AttributeKeys.StrokePlacement.class, "strokePlacement" }, new java.lang.Object[]{ org.jhotdraw.draw.AttributeKeys.StrokeType.class, "strokeType" }, new java.lang.Object[]{ org.jhotdraw.draw.AttributeKeys.Underfill.class, "underfill" }, new java.lang.Object[]{ org.jhotdraw.draw.AttributeKeys.Orientation.class, "orientation" } };

    public DOMDefaultDrawFigureFactory() {
        register("drawing", org.jhotdraw.draw.DefaultDrawing.class, null, null);// do not allow processing

        register("drawing", org.jhotdraw.draw.QuadTreeDrawing.class, null, null);// do not allow processing

        register("diamond", org.jhotdraw.draw.figure.DiamondFigure.class, org.jhotdraw.io.DOMDefaultDrawFigureFactory::readBaseData, org.jhotdraw.io.DOMDefaultDrawFigureFactory::writeBaseData);
        register("triangle", org.jhotdraw.draw.figure.TriangleFigure.class, org.jhotdraw.io.DOMDefaultDrawFigureFactory::readBaseData, org.jhotdraw.io.DOMDefaultDrawFigureFactory::writeBaseData);
        register("bezier", org.jhotdraw.draw.figure.BezierFigure.class, org.jhotdraw.io.DOMDefaultDrawFigureFactory::readBezier, org.jhotdraw.io.DOMDefaultDrawFigureFactory::writeBezier);
        register("r", org.jhotdraw.draw.figure.RectangleFigure.class, org.jhotdraw.io.DOMDefaultDrawFigureFactory::readBaseData, org.jhotdraw.io.DOMDefaultDrawFigureFactory::writeBaseData);
        register("rr", org.jhotdraw.draw.figure.RoundRectangleFigure.class, org.jhotdraw.io.DOMDefaultDrawFigureFactory::readRoundRectangle, org.jhotdraw.io.DOMDefaultDrawFigureFactory::writeRoundRectangle);
        register("l", org.jhotdraw.draw.figure.LineFigure.class, org.jhotdraw.io.DOMDefaultDrawFigureFactory::readBezier, org.jhotdraw.io.DOMDefaultDrawFigureFactory::writeBezier);
        register("b", org.jhotdraw.draw.figure.BezierFigure.class, org.jhotdraw.io.DOMDefaultDrawFigureFactory::readBezier, org.jhotdraw.io.DOMDefaultDrawFigureFactory::writeBezier);
        register("lnk", org.jhotdraw.draw.figure.LineConnectionFigure.class, org.jhotdraw.io.DOMDefaultDrawFigureFactory::readLineConnection, org.jhotdraw.io.DOMDefaultDrawFigureFactory::writeLineConnection);
        register("e", org.jhotdraw.draw.figure.EllipseFigure.class, org.jhotdraw.io.DOMDefaultDrawFigureFactory::readBaseData, org.jhotdraw.io.DOMDefaultDrawFigureFactory::writeBaseData);
        register("t", org.jhotdraw.draw.figure.TextFigure.class, org.jhotdraw.io.DOMDefaultDrawFigureFactory::readText, org.jhotdraw.io.DOMDefaultDrawFigureFactory::writeText);
        register("ta", org.jhotdraw.draw.figure.TextAreaFigure.class, org.jhotdraw.io.DOMDefaultDrawFigureFactory::readBaseData, org.jhotdraw.io.DOMDefaultDrawFigureFactory::writeBaseData);
        register("image", org.jhotdraw.draw.figure.ImageFigure.class, org.jhotdraw.io.DOMDefaultDrawFigureFactory::readImage, org.jhotdraw.io.DOMDefaultDrawFigureFactory::writeImage);
        register("g", org.jhotdraw.draw.figure.GroupFigure.class, org.jhotdraw.io.DOMDefaultDrawFigureFactory::readGroup, org.jhotdraw.io.DOMDefaultDrawFigureFactory::writeGroup);
        register("arrowTip", org.jhotdraw.draw.decoration.ArrowTip.class, org.jhotdraw.io.DOMDefaultDrawFigureFactory::readArrowTip, org.jhotdraw.io.DOMDefaultDrawFigureFactory::writeArrowTip);
        register("rConnector", org.jhotdraw.draw.connector.ChopRectangleConnector.class, org.jhotdraw.io.DOMDefaultDrawFigureFactory::readConnector, org.jhotdraw.io.DOMDefaultDrawFigureFactory::writeConnector);
        register("ellipseConnector", org.jhotdraw.draw.connector.ChopEllipseConnector.class, org.jhotdraw.io.DOMDefaultDrawFigureFactory::readConnector, org.jhotdraw.io.DOMDefaultDrawFigureFactory::writeConnector);
        register("rrConnector", org.jhotdraw.draw.connector.ChopRoundRectangleConnector.class, org.jhotdraw.io.DOMDefaultDrawFigureFactory::readConnector, org.jhotdraw.io.DOMDefaultDrawFigureFactory::writeConnector);
        register("triangleConnector", org.jhotdraw.draw.connector.ChopTriangleConnector.class, org.jhotdraw.io.DOMDefaultDrawFigureFactory::readConnector, org.jhotdraw.io.DOMDefaultDrawFigureFactory::writeConnector);
        register("diamondConnector", org.jhotdraw.draw.connector.ChopDiamondConnector.class, org.jhotdraw.io.DOMDefaultDrawFigureFactory::readConnector, org.jhotdraw.io.DOMDefaultDrawFigureFactory::writeConnector);
        register("bezierConnector", org.jhotdraw.draw.connector.ChopBezierConnector.class, org.jhotdraw.io.DOMDefaultDrawFigureFactory::readConnector, org.jhotdraw.io.DOMDefaultDrawFigureFactory::writeConnector);
        register("locatorConnector", org.jhotdraw.draw.connector.LocatorConnector.class, org.jhotdraw.io.DOMDefaultDrawFigureFactory::readLocatorConnector, org.jhotdraw.io.DOMDefaultDrawFigureFactory::writeLocatorConnector);
        register("relativeLoc", org.jhotdraw.draw.locator.RelativeLocator.class, (f, i) -> {
        }, (f, o) -> {
        });// do nothing;

        register("elbowLiner", org.jhotdraw.draw.liner.ElbowLiner.class, (f, i) -> {
        }, (f, o) -> {
        });// do nothing

        register("curvedLiner", org.jhotdraw.draw.liner.CurvedLiner.class, (f, i) -> {
        }, (f, o) -> {
        });// do nothing

        register("bezierLabelLoc", org.jhotdraw.draw.locator.BezierLabelLocator.class, (f, i) -> {
        }, (f, o) -> {
        });// do nothing;

        for (java.lang.Object[] o : org.jhotdraw.io.DOMDefaultDrawFigureFactory.ENUM_TAGS) {
            addEnumClass(((java.lang.String) (o[1])), ((java.lang.Class) (o[0])));
        }
    }

    public static void readImage(org.jhotdraw.draw.figure.ImageFigure figure, org.jhotdraw.xml.DOMInput domInput) throws java.io.IOException {
        org.jhotdraw.io.DOMDefaultDrawFigureFactory.readBaseData(figure, domInput);
        org.jhotdraw.io.DOMDefaultDrawFigureFactory.readDecorator(figure, domInput);
        if (domInput.getElementCount("imageData") > 0) {
            domInput.openElement("imageData");
            java.lang.String base64Data = domInput.getText();
            if (base64Data != null) {
                figure.setImageData(org.jhotdraw.io.Base64.decode(base64Data));
            }
            domInput.closeElement();
        }
    }

    public static void writeImage(org.jhotdraw.draw.figure.ImageFigure figure, org.jhotdraw.xml.DOMOutput domOutput) throws java.io.IOException {
        org.jhotdraw.io.DOMDefaultDrawFigureFactory.writeBaseData(figure, domOutput);
        org.jhotdraw.io.DOMDefaultDrawFigureFactory.writeDecorator(figure, domOutput);
        if (figure.getImageData() != null) {
            domOutput.openElement("imageData");
            domOutput.addText(org.jhotdraw.io.Base64.encodeBytes(figure.getImageData()));
            domOutput.closeElement();
        }
    }

    public static void readGroup(org.jhotdraw.draw.figure.GroupFigure figure, org.jhotdraw.xml.DOMInput domInput) throws java.io.IOException {
        domInput.openElement("children");
        for (int i = 0; i < domInput.getElementCount(); i++) {
            figure.basicAdd(((org.jhotdraw.draw.figure.Figure) (domInput.readObject(i))));
        }
        domInput.closeElement();
    }

    public static void writeGroup(org.jhotdraw.draw.figure.GroupFigure figure, org.jhotdraw.xml.DOMOutput domOutput) throws java.io.IOException {
        domOutput.openElement("children");
        for (org.jhotdraw.draw.figure.Figure child : figure.getChildren()) {
            domOutput.writeObject(child);
        }
        domOutput.closeElement();
    }

    public static void readArrowTip(org.jhotdraw.draw.decoration.ArrowTip figure, org.jhotdraw.xml.DOMInput domInput) throws java.io.IOException {
        figure.setAngle(domInput.getAttribute("angle", 0.35F));
        figure.setInnerRadius(domInput.getAttribute("innerRadius", 12.0F));
        figure.setOuterRadius(domInput.getAttribute("outerRadius", 12.0F));
        figure.setFilled(domInput.getAttribute("isFilled", false));
        figure.setStroked(domInput.getAttribute("isStroked", false));
        figure.setSolid(domInput.getAttribute("isSolid", false));
    }

    public static void writeArrowTip(org.jhotdraw.draw.decoration.ArrowTip figure, org.jhotdraw.xml.DOMOutput domOutput) throws java.io.IOException {
        domOutput.addAttribute("angle", figure.getAngle());
        domOutput.addAttribute("innerRadius", figure.getInnerRadius());
        domOutput.addAttribute("outerRadius", figure.getOuterRadius());
        domOutput.addAttribute("isFilled", figure.isFilled());
        domOutput.addAttribute("isStroked", figure.isStroked());
        domOutput.addAttribute("isSolid", figure.isSolid());
    }

    public static void readLineConnection(org.jhotdraw.draw.figure.LineConnectionFigure figure, org.jhotdraw.xml.DOMInput domInput) throws java.io.IOException {
        figure.removeNode(0);
        org.jhotdraw.io.DOMDefaultDrawFigureFactory.readAttributes(figure, domInput);
        org.jhotdraw.io.DOMDefaultDrawFigureFactory.readLiner(figure, domInput);
        org.jhotdraw.io.DOMDefaultDrawFigureFactory.readPoints(figure, domInput);
        org.jhotdraw.io.DOMDefaultDrawFigureFactory.readPointsForLineConnection(figure, domInput);
    }

    public static void readLiner(org.jhotdraw.draw.figure.LineConnectionFigure figure, org.jhotdraw.xml.DOMInput domInput) throws java.io.IOException {
        if (domInput.getElementCount("liner") > 0) {
            domInput.openElement("liner");
            figure.setLiner(((org.jhotdraw.draw.liner.Liner) (domInput.readObject())));
            domInput.closeElement();
        } else {
            figure.setLiner(null);
        }
    }

    public static void readPointsForLineConnection(org.jhotdraw.draw.figure.LineConnectionFigure figure, org.jhotdraw.xml.DOMInput domInput) throws java.io.IOException {
        domInput.openElement("startConnector");
        figure.setStartConnector(((org.jhotdraw.draw.connector.Connector) (domInput.readObject())));
        domInput.closeElement();
        domInput.openElement("endConnector");
        figure.setEndConnector(((org.jhotdraw.draw.connector.Connector) (domInput.readObject())));
        domInput.closeElement();
    }

    public static void writeLineConnection(org.jhotdraw.draw.figure.LineConnectionFigure figure, org.jhotdraw.xml.DOMOutput domOutput) throws java.io.IOException {
        org.jhotdraw.io.DOMDefaultDrawFigureFactory.writePoints(figure, domOutput);
        org.jhotdraw.io.DOMDefaultDrawFigureFactory.writePointsForLineConnection(figure, domOutput);
        org.jhotdraw.io.DOMDefaultDrawFigureFactory.writeAttributes(figure, domOutput);
        org.jhotdraw.io.DOMDefaultDrawFigureFactory.writeLiner(figure, domOutput);
    }

    public static void writeLiner(org.jhotdraw.draw.figure.LineConnectionFigure figure, org.jhotdraw.xml.DOMOutput domOutput) throws java.io.IOException {
        if (figure.getLiner() != null) {
            domOutput.openElement("liner");
            domOutput.writeObject(figure.getLiner());
            domOutput.closeElement();
        }
    }

    public static void writePointsForLineConnection(org.jhotdraw.draw.figure.LineConnectionFigure figure, org.jhotdraw.xml.DOMOutput domOutput) throws java.io.IOException {
        domOutput.openElement("startConnector");
        domOutput.writeObject(figure.getStartConnector());
        domOutput.closeElement();
        domOutput.openElement("endConnector");
        domOutput.writeObject(figure.getEndConnector());
        domOutput.closeElement();
    }

    public static void readBezierLabelLocator(org.jhotdraw.draw.locator.BezierLabelLocator locator, org.jhotdraw.xml.DOMInput domInput) throws java.io.IOException {
        locator.setRelativePosition(domInput.getAttribute("relativePosition", 0.5));
        locator.setAngle(domInput.getAttribute("angle", 0.0));
        locator.setDistance(domInput.getAttribute("distance", 0.0));
    }

    public static void writeBezierLabelLocator(org.jhotdraw.draw.locator.BezierLabelLocator locator, org.jhotdraw.xml.DOMOutput domOutput) throws java.io.IOException {
        domOutput.addAttribute("relativePosition", locator.getRelativePosition());
        domOutput.addAttribute("angle", locator.getAngle());
        domOutput.addAttribute("distance", locator.getDistance());
    }

    public static void readLocatorConnector(org.jhotdraw.draw.connector.LocatorConnector connector, org.jhotdraw.xml.DOMInput domInput) throws java.io.IOException {
        org.jhotdraw.io.DOMDefaultDrawFigureFactory.readConnector(connector, domInput);
        domInput.openElement("locator");
        connector.setLocator(((org.jhotdraw.draw.locator.Locator) (domInput.readObject(0))));
        domInput.closeElement();
    }

    public static void writeLocatorConnector(org.jhotdraw.draw.connector.LocatorConnector connector, org.jhotdraw.xml.DOMOutput domOutput) throws java.io.IOException {
        org.jhotdraw.io.DOMDefaultDrawFigureFactory.writeConnector(connector, domOutput);
        domOutput.openElement("locator");
        domOutput.writeObject(connector.getLocator());
        domOutput.closeElement();
    }

    public static void readConnector(org.jhotdraw.draw.connector.AbstractConnector connector, org.jhotdraw.xml.DOMInput domInput) throws java.io.IOException {
        // statePersistent is never set
        // if (connector.isStatePersistent) {
        // isConnectToDecorator = in.getAttribute("connectToDecorator", false);
        // }
        domInput.openElement("Owner");
        connector.setOwner(((org.jhotdraw.draw.figure.Figure) (domInput.readObject(0))));
        domInput.closeElement();
    }

    public static void writeConnector(org.jhotdraw.draw.connector.Connector connector, org.jhotdraw.xml.DOMOutput domOutput) throws java.io.IOException {
        // statePersistent is never set
        // if (isStatePersistent) {
        // if (isConnectToDecorator) {
        // out.addAttribute("connectToDecorator", true);
        // }
        // }
        domOutput.openElement("Owner");
        domOutput.writeObject(connector.getOwner());
        domOutput.closeElement();
    }

    public static void readBezier(org.jhotdraw.draw.figure.BezierFigure figure, org.jhotdraw.xml.DOMInput domInput) throws java.io.IOException {
        org.jhotdraw.io.DOMDefaultDrawFigureFactory.readPoints(figure, domInput);
        org.jhotdraw.io.DOMDefaultDrawFigureFactory.readAttributes(figure, domInput);
    }

    public static void readPoints(org.jhotdraw.draw.figure.BezierFigure figure, org.jhotdraw.xml.DOMInput domInput) throws java.io.IOException {
        while (figure.getNodeCount() > 0) {
            figure.removeNode(0);
        } 
        domInput.openElement("points");
        figure.setClosed(domInput.getAttribute("closed", false));
        java.util.List<org.jhotdraw.geom.path.BezierPath.Node> nodes = new java.util.ArrayList<>();
        for (int i = 0, n = domInput.getElementCount("p"); i < n; i++) {
            domInput.openElement("p", i);
            org.jhotdraw.geom.path.BezierPath.Node node = new org.jhotdraw.geom.path.BezierPath.Node(domInput.getAttribute("mask", 0), domInput.getAttribute("x", 0.0), domInput.getAttribute("y", 0.0), domInput.getAttribute("c1x", domInput.getAttribute("x", 0.0)), domInput.getAttribute("c1y", domInput.getAttribute("y", 0.0)), domInput.getAttribute("c2x", domInput.getAttribute("x", 0.0)), domInput.getAttribute("c2y", domInput.getAttribute("y", 0.0)));
            node.keepColinear = domInput.getAttribute("colinear", true);
            figure.addNode(node);
            domInput.closeElement();
        }
        domInput.closeElement();
    }

    public static void writeBezier(org.jhotdraw.draw.figure.BezierFigure figure, org.jhotdraw.xml.DOMOutput domOutput) throws java.io.IOException {
        org.jhotdraw.io.DOMDefaultDrawFigureFactory.writePoints(figure, domOutput);
        org.jhotdraw.io.DOMDefaultDrawFigureFactory.writeAttributes(figure, domOutput);
    }

    public static void writePoints(org.jhotdraw.draw.figure.BezierFigure figure, org.jhotdraw.xml.DOMOutput domOutput) throws java.io.IOException {
        domOutput.openElement("points");
        if (figure.isClosed()) {
            domOutput.addAttribute("closed", true);
        }
        for (int i = 0, n = figure.getNodeCount(); i < n; i++) {
            org.jhotdraw.geom.path.BezierPath.Node node = figure.getNode(i);
            domOutput.openElement("p");
            domOutput.addAttribute("mask", node.mask, 0);
            domOutput.addAttribute("colinear", true);
            domOutput.addAttribute("x", node.x[0]);
            domOutput.addAttribute("y", node.y[0]);
            domOutput.addAttribute("c1x", node.x[1], node.x[0]);
            domOutput.addAttribute("c1y", node.y[1], node.y[0]);
            domOutput.addAttribute("c2x", node.x[2], node.x[0]);
            domOutput.addAttribute("c2y", node.y[2], node.y[0]);
            domOutput.closeElement();
        }
        domOutput.closeElement();
    }

    public static void readText(org.jhotdraw.draw.figure.TextFigure figure, org.jhotdraw.xml.DOMInput domInput) throws java.io.IOException {
        figure.setOrigin(new java.awt.geom.Point2D.Double(domInput.getAttribute("x", 0.0), domInput.getAttribute("y", 0.0)));
        // figure.setBounds(
        // new Point2D.Double(domInput.getAttribute("x", 0d), domInput.getAttribute("y", 0d)),
        // new Point2D.Double(0, 0));
        org.jhotdraw.io.DOMDefaultDrawFigureFactory.readAttributes(figure, domInput);
        org.jhotdraw.io.DOMDefaultDrawFigureFactory.readDecorator(figure, domInput);
    }

    public static void readDecorator(org.jhotdraw.draw.figure.DecoratedFigure figure, org.jhotdraw.xml.DOMInput domInput) throws java.io.IOException {
        if (domInput.getElementCount("decorator") > 0) {
            domInput.openElement("decorator");
            figure.setDecorator(((org.jhotdraw.draw.figure.Figure) (domInput.readObject())));
            domInput.closeElement();
        } else {
            figure.setDecorator(null);
        }
    }

    public static void writeText(org.jhotdraw.draw.figure.TextFigure figure, org.jhotdraw.xml.DOMOutput domOutput) throws java.io.IOException {
        // Rectangle2D.Double b = figure.getBounds();
        java.awt.geom.Point2D.Double b = figure.getOrigin();
        domOutput.addAttribute("x", b.x);
        domOutput.addAttribute("y", b.y);
        org.jhotdraw.io.DOMDefaultDrawFigureFactory.writeAttributes(figure, domOutput);
        org.jhotdraw.io.DOMDefaultDrawFigureFactory.writeDecorator(figure, domOutput);
    }

    public static void writeDecorator(org.jhotdraw.draw.figure.DecoratedFigure figure, org.jhotdraw.xml.DOMOutput domOutput) throws java.io.IOException {
        if (figure.getDecorator() != null) {
            domOutput.openElement("decorator");
            domOutput.writeObject(figure.getDecorator());
            domOutput.closeElement();
        }
    }

    public static void readRoundRectangle(org.jhotdraw.draw.figure.RoundRectangleFigure figure, org.jhotdraw.xml.DOMInput domInput) throws java.io.IOException {
        org.jhotdraw.io.DOMDefaultDrawFigureFactory.readBaseData(figure, domInput);
        figure.setArc(domInput.getAttribute("arcWidth", org.jhotdraw.draw.figure.RoundRectangleFigure.DEFAULT_ARC), domInput.getAttribute("arcHeight", org.jhotdraw.draw.figure.RoundRectangleFigure.DEFAULT_ARC));
    }

    public static void writeRoundRectangle(org.jhotdraw.draw.figure.RoundRectangleFigure figure, org.jhotdraw.xml.DOMOutput domOutput) throws java.io.IOException {
        org.jhotdraw.io.DOMDefaultDrawFigureFactory.writeBaseData(figure, domOutput);
        domOutput.addAttribute("arcWidth", figure.getArcWidth());
        domOutput.addAttribute("arcHeight", figure.getArcHeight());
    }

    public static void readBaseData(org.jhotdraw.draw.figure.Figure figure, org.jhotdraw.xml.DOMInput domInput) throws java.io.IOException {
        org.jhotdraw.io.DOMDefaultDrawFigureFactory.readBounds(figure, domInput);
        org.jhotdraw.io.DOMDefaultDrawFigureFactory.readAttributes(figure, domInput);
    }

    public static void readBounds(org.jhotdraw.draw.figure.Figure figure, org.jhotdraw.xml.DOMInput domInput) {
        double x = domInput.getAttribute("x", 0.0);
        double y = domInput.getAttribute("y", 0.0);
        double w = domInput.getAttribute("w", 0.0);
        double h = domInput.getAttribute("h", 0.0);
        figure.setBounds(new java.awt.geom.Point2D.Double(x, y), new java.awt.geom.Point2D.Double(x + w, y + h));
    }

    public static void readAttributes(org.jhotdraw.draw.figure.Figure figure, org.jhotdraw.xml.DOMInput domInput) throws java.io.IOException {
        if (domInput.getElementCount("a") > 0) {
            domInput.openElement("a");
            for (int i = 0, n = domInput.getElementCount(); i < n; i++) {
                domInput.openElement(i);
                java.lang.Object value = domInput.readObject();
                org.jhotdraw.draw.AttributeKey<?> key = org.jhotdraw.draw.AttributeKeys.SUPPORTED_ATTRIBUTES_MAP.get(domInput.getTagName());
                if ((key != null) && key.isAssignable(value)) {
                    figure.attr().set(((org.jhotdraw.draw.AttributeKey<java.lang.Object>) (key)), value);
                }
                domInput.closeElement();
            }
            domInput.closeElement();
        }
    }

    public static void writeBaseData(org.jhotdraw.draw.figure.Figure figure, org.jhotdraw.xml.DOMOutput domOutput) throws java.io.IOException {
        org.jhotdraw.io.DOMDefaultDrawFigureFactory.writeBounds(figure, domOutput);
        org.jhotdraw.io.DOMDefaultDrawFigureFactory.writeAttributes(figure, domOutput);
    }

    public static void writeAttributes(org.jhotdraw.draw.figure.Figure figure, org.jhotdraw.xml.DOMOutput domOutput) throws java.io.IOException {
        org.jhotdraw.draw.figure.Figure prototype = ((org.jhotdraw.draw.figure.Figure) (domOutput.getPrototype()));
        boolean isElementOpen = false;
        for (java.util.Map.Entry<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> entry : figure.attr().getAttributes().entrySet()) {
            org.jhotdraw.draw.AttributeKey<?> key = entry.getKey();
            if (figure.attr().isAttributeEnabled(key)) {
                java.lang.Object prototypeValue = prototype.attr().get(key);
                java.lang.Object attributeValue = figure.attr().get(key);
                if (!java.util.Objects.equals(prototypeValue, attributeValue)) {
                    if (!isElementOpen) {
                        domOutput.openElement("a");
                        isElementOpen = true;
                    }
                    domOutput.openElement(key.getKey());
                    domOutput.writeObject(entry.getValue());
                    domOutput.closeElement();
                }
            }
        }
        if (isElementOpen) {
            domOutput.closeElement();
        }
    }

    public static void writeBounds(org.jhotdraw.draw.figure.Figure figure, org.jhotdraw.xml.DOMOutput domOutput) {
        java.awt.geom.Rectangle2D.Double r = figure.getBounds();
        domOutput.addAttribute("x", r.x);
        domOutput.addAttribute("y", r.y);
        domOutput.addAttribute("w", r.width);
        domOutput.addAttribute("h", r.height);
    }
}