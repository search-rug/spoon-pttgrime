/* @(#)PertFactory.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.pert;
/**
 * PertFactory.
 */
public class PertFactory extends org.jhotdraw.xml.DefaultDOMFactory {
    private static final java.lang.Object[][] CLASS_TAGS = new java.lang.Object[][]{ new java.lang.Object[]{ org.jhotdraw.draw.DefaultDrawing.class, "PertDiagram" }, new java.lang.Object[]{ org.jhotdraw.samples.pert.figures.TaskFigure.class, "task" }, new java.lang.Object[]{ org.jhotdraw.samples.pert.figures.DependencyFigure.class, "dep" }, new java.lang.Object[]{ org.jhotdraw.draw.figure.ListFigure.class, "list" }, new java.lang.Object[]{ org.jhotdraw.draw.figure.TextFigure.class, "text" }, new java.lang.Object[]{ org.jhotdraw.draw.figure.GroupFigure.class, "g" }, new java.lang.Object[]{ org.jhotdraw.draw.figure.TextAreaFigure.class, "ta" }, new java.lang.Object[]{ org.jhotdraw.samples.pert.figures.SeparatorLineFigure.class, "separator" }, new java.lang.Object[]{ org.jhotdraw.draw.connector.ChopRectangleConnector.class, "rectConnector" }, new java.lang.Object[]{ org.jhotdraw.draw.connector.LocatorConnector.class, "locConnector" }, new java.lang.Object[]{ org.jhotdraw.draw.locator.RelativeLocator.class, "relativeLocator" }, new java.lang.Object[]{ org.jhotdraw.draw.decoration.ArrowTip.class, "arrowTip" } };

    public PertFactory() {
        register("Net", org.jhotdraw.draw.DefaultDrawing.class, null, null);// do not allow processing

        register("t", org.jhotdraw.draw.figure.TextFigure.class, org.jhotdraw.io.DOMDefaultDrawFigureFactory::readText, org.jhotdraw.io.DOMDefaultDrawFigureFactory::writeText);
        register("g", org.jhotdraw.draw.figure.GroupFigure.class, org.jhotdraw.io.DOMDefaultDrawFigureFactory::readGroup, org.jhotdraw.io.DOMDefaultDrawFigureFactory::writeGroup);
        register("ta", org.jhotdraw.draw.figure.TextAreaFigure.class, org.jhotdraw.io.DOMDefaultDrawFigureFactory::readBaseData, org.jhotdraw.io.DOMDefaultDrawFigureFactory::writeBaseData);
        register("rectConnector", org.jhotdraw.draw.connector.ChopRectangleConnector.class, org.jhotdraw.io.DOMDefaultDrawFigureFactory::readConnector, org.jhotdraw.io.DOMDefaultDrawFigureFactory::writeConnector);
        register("locConnector", org.jhotdraw.draw.connector.LocatorConnector.class, org.jhotdraw.io.DOMDefaultDrawFigureFactory::readLocatorConnector, org.jhotdraw.io.DOMDefaultDrawFigureFactory::writeLocatorConnector);
        register("arrowTip", org.jhotdraw.draw.decoration.ArrowTip.class, org.jhotdraw.io.DOMDefaultDrawFigureFactory::readArrowTip, org.jhotdraw.io.DOMDefaultDrawFigureFactory::writeArrowTip);
        register("relativeLoc", org.jhotdraw.draw.locator.RelativeLocator.class, (f, i) -> {
        }, (f, o) -> {
        });// do nothing;

        // for (Object[] o : CLASS_TAGS) {
        // register((String) o[1], (Class) o[0], null, null);
        // }
    }
}