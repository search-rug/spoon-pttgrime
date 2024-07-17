/* @(#)PathTool.java

Copyright (c) 2007 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.odg;
/**
 * Tool to scribble a ODGPath
 */
public class PathTool extends org.jhotdraw.draw.tool.BezierTool {
    private static final long serialVersionUID = 1L;

    /**
     * The path prototype for new figures.
     */
    private org.jhotdraw.samples.svg.figures.SVGPathFigure pathPrototype;

    public PathTool(org.jhotdraw.samples.svg.figures.SVGPathFigure pathPrototype, org.jhotdraw.samples.svg.figures.SVGBezierFigure bezierPrototype) {
        this(pathPrototype, bezierPrototype, null);
    }

    public PathTool(org.jhotdraw.samples.svg.figures.SVGPathFigure pathPrototype, org.jhotdraw.samples.svg.figures.SVGBezierFigure bezierPrototype, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes) {
        super(bezierPrototype, attributes);
        this.pathPrototype = pathPrototype;
    }

    @java.lang.SuppressWarnings("unchecked")
    protected org.jhotdraw.samples.svg.figures.SVGPathFigure createPath() {
        org.jhotdraw.samples.svg.figures.SVGPathFigure f = pathPrototype.clone();
        getEditor().applyDefaultAttributesTo(f);
        if (attributes != null) {
            for (java.util.Map.Entry<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> entry : attributes.entrySet()) {
                f.attr().set(((org.jhotdraw.draw.AttributeKey<java.lang.Object>) (entry.getKey())), entry.getValue());
            }
        }
        return f;
    }

    @java.lang.Override
    protected void finishCreation(org.jhotdraw.draw.figure.BezierFigure createdFigure, org.jhotdraw.draw.DrawingView creationView) {
        creationView.getDrawing().remove(createdFigure);
        org.jhotdraw.samples.svg.figures.SVGPathFigure createdPath = createPath();
        createdPath.removeAllChildren();
        createdPath.add(createdFigure);
        creationView.getDrawing().add(createdPath);
        creationView.addToSelection(createdPath);
        fireUndoEvent(createdPath, creationView);
    }
}