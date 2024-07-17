/* @(#)DrawFigureFactory.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.draw;
/**
 * DrawFigureFactory.
 */
@java.lang.Deprecated
public class DrawFigureFactory extends org.jhotdraw.xml.DefaultDOMFactory {
    private static final java.lang.Object[][] CLASS_TAGS = new java.lang.Object[][]{ new java.lang.Object[]{ org.jhotdraw.draw.DefaultDrawing.class, "drawing" }, new java.lang.Object[]{ org.jhotdraw.draw.QuadTreeDrawing.class, "drawing" }, new java.lang.Object[]{ org.jhotdraw.draw.figure.DiamondFigure.class, "diamond" }, new java.lang.Object[]{ org.jhotdraw.draw.figure.TriangleFigure.class, "triangle" }, new java.lang.Object[]{ org.jhotdraw.draw.figure.BezierFigure.class, "bezier" }, new java.lang.Object[]{ org.jhotdraw.draw.figure.RectangleFigure.class, "r" }, new java.lang.Object[]{ org.jhotdraw.draw.figure.RoundRectangleFigure.class, "rr" }, new java.lang.Object[]{ org.jhotdraw.draw.figure.LineFigure.class, "l" }, new java.lang.Object[]{ org.jhotdraw.draw.figure.BezierFigure.class, "b" }, new java.lang.Object[]{ org.jhotdraw.draw.figure.LineConnectionFigure.class, "lnk" }, new java.lang.Object[]{ org.jhotdraw.draw.figure.EllipseFigure.class, "e" }, new java.lang.Object[]{ org.jhotdraw.draw.figure.TextFigure.class, "t" }, new java.lang.Object[]{ org.jhotdraw.draw.figure.TextAreaFigure.class, "ta" }, new java.lang.Object[]{ org.jhotdraw.draw.figure.ImageFigure.class, "image" }, new java.lang.Object[]{ org.jhotdraw.draw.figure.GroupFigure.class, "g" }, new java.lang.Object[]{ org.jhotdraw.draw.decoration.ArrowTip.class, "arrowTip" }, new java.lang.Object[]{ org.jhotdraw.draw.connector.ChopRectangleConnector.class, "rConnector" }, new java.lang.Object[]{ org.jhotdraw.draw.connector.ChopEllipseConnector.class, "ellipseConnector" }, new java.lang.Object[]{ org.jhotdraw.draw.connector.ChopRoundRectangleConnector.class, "rrConnector" }, new java.lang.Object[]{ org.jhotdraw.draw.connector.ChopTriangleConnector.class, "triangleConnector" }, new java.lang.Object[]{ org.jhotdraw.draw.connector.ChopDiamondConnector.class, "diamondConnector" }, new java.lang.Object[]{ org.jhotdraw.draw.connector.ChopBezierConnector.class, "bezierConnector" }, new java.lang.Object[]{ org.jhotdraw.draw.liner.ElbowLiner.class, "elbowLiner" }, new java.lang.Object[]{ org.jhotdraw.draw.liner.CurvedLiner.class, "curvedLiner" } };

    private static final java.lang.Object[][] ENUM_TAGS = new java.lang.Object[][]{ new java.lang.Object[]{ org.jhotdraw.draw.AttributeKeys.StrokePlacement.class, "strokePlacement" }, new java.lang.Object[]{ org.jhotdraw.draw.AttributeKeys.StrokeType.class, "strokeType" }, new java.lang.Object[]{ org.jhotdraw.draw.AttributeKeys.Underfill.class, "underfill" }, new java.lang.Object[]{ org.jhotdraw.draw.AttributeKeys.Orientation.class, "orientation" } };

    public DrawFigureFactory() {
        for (java.lang.Object[] o : org.jhotdraw.samples.draw.DrawFigureFactory.CLASS_TAGS) {
            register(((java.lang.String) (o[1])), ((java.lang.Class) (o[0])));
        }
        for (java.lang.Object[] o : org.jhotdraw.samples.draw.DrawFigureFactory.ENUM_TAGS) {
            addEnumClass(((java.lang.String) (o[1])), ((java.lang.Class) (o[0])));
        }
    }
}