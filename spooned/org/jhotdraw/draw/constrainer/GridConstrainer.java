/* @(#)GridConstrainer.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.constrainer;
/**
 * Constrains a point such that it falls on a grid.
 */
public class GridConstrainer extends org.jhotdraw.draw.constrainer.AbstractConstrainer {
    private static final long serialVersionUID = 1L;

    /**
     * The width of a minor grid cell. The value 0 turns the constrainer off for the horizontal axis.
     */
    private double width;

    /**
     * The height of a minor grid cell. The value 0 turns the constrainer off for the vertical axis.
     */
    private double height;

    /**
     * The theta for constrained rotations on the grid. The value 0 turns the constrainer off for
     * rotations.
     */
    private double theta;

    /**
     * If this variable is true, the grid is drawn. Note: Grid cells are only drawn, if they are at
     * least two pixels apart on the view coordinate system.
     */
    private boolean isVisible;

    /**
     * The color for minor grid cells.
     */
    private static java.awt.Color minorColor = new java.awt.Color(0xebebeb);

    /**
     * The color for major grid cells.
     */
    private static java.awt.Color majorColor = new java.awt.Color(0xcacaca);

    /**
     * The spacing factor for a major grid cell.
     */
    private int majorGridSpacing = 5;

    /**
     * Creates a new instance with a grid of 1x1.
     */
    public GridConstrainer() {
        this(1.0, 1.0, 0.0, false);
    }

    /**
     * Creates a new instance with the specified grid size, and by 11.25° (in degrees) for rotations.
     * The grid is visible.
     *
     * @param width
     * 		The width of a grid cell.
     * @param height
     * 		The height of a grid cell.
     */
    public GridConstrainer(double width, double height) {
        this(width, height, java.lang.Math.PI / 8.0, true);
    }

    /**
     * Creates a new instance with the specified grid size. and by 11.25° (in degrees) for rotations.
     *
     * @param width
     * 		The width of a grid cell.
     * @param height
     * 		The height of a grid cell.
     * @param visible
     * 		Wether the grid is visible or not.
     */
    public GridConstrainer(double width, double height, boolean visible) {
        this(width, height, java.lang.Math.PI / 8.0, visible);
    }

    /**
     * Creates a new instance with the specified grid size.
     *
     * @param width
     * 		The width of a grid cell.
     * @param height
     * 		The height of a grid cell.
     * @param theta
     * 		The theta for rotations in radians.
     * @param visible
     * 		Wether the grid is visible or not.
     */
    public GridConstrainer(double width, double height, double theta, boolean visible) {
        if ((width <= 0) || (height <= 0)) {
            throw new java.lang.IllegalArgumentException("Width or height is <= 0");
        }
        this.width = width;
        this.height = height;
        this.theta = theta;
        this.isVisible = visible;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getTheta() {
        return theta;
    }

    public void setWidth(double newValue) {
        double oldValue = width;
        width = newValue;
        fireStateChanged();
    }

    public void setHeight(double newValue) {
        double oldValue = height;
        height = newValue;
        fireStateChanged();
    }

    public void setTheta(double newValue) {
        double oldValue = theta;
        theta = newValue;
        fireStateChanged();
    }

    /**
     * Constrains a point to the closest grid point in any direction.
     */
    @java.lang.Override
    public java.awt.geom.Point2D.Double constrainPoint(java.awt.geom.Point2D.Double p, org.jhotdraw.draw.figure.Figure... figure) {
        p.x = java.lang.Math.round(p.x / width) * width;
        p.y = java.lang.Math.round(p.y / height) * height;
        return p;
    }

    /**
     * Constrains the placement of a point towards a direction.
     *
     * <p>This method changes the point which is passed as a parameter.
     *
     * @param p
     * 		A point on the drawing.
     * @param dir
     * 		A direction.
     * @return Returns the constrained point.
     */
    protected java.awt.geom.Point2D.Double constrainPoint(java.awt.geom.Point2D.Double p, org.jhotdraw.draw.constrainer.TranslationDirection dir, org.jhotdraw.draw.figure.Figure... figure) {
        java.awt.geom.Point2D.Double p0 = constrainPoint(((java.awt.geom.Point2D.Double) (p.clone())), figure);
        switch (dir) {
            case NORTH :
            case NORTH_WEST :
            case NORTH_EAST :
                if (p0.y < p.y) {
                    p.y = p0.y;
                } else if (p0.y > p.y) {
                    p.y = p0.y - height;
                }
                break;
            case SOUTH :
            case SOUTH_WEST :
            case SOUTH_EAST :
                if (p0.y < p.y) {
                    p.y = p0.y + height;
                } else if (p0.y > p.y) {
                    p.y = p0.y;
                }
                break;
        }
        switch (dir) {
            case WEST :
            case NORTH_WEST :
            case SOUTH_WEST :
                if (p0.x < p.x) {
                    p.x = p0.x;
                } else if (p0.x > p.x) {
                    p.x = p0.x - width;
                }
                break;
            case EAST :
            case NORTH_EAST :
            case SOUTH_EAST :
                if (p0.x < p.x) {
                    p.x = p0.x + width;
                } else if (p0.x > p.x) {
                    p.x = p0.x;
                }
                break;
        }
        return p;
    }

    /**
     * Moves a point to the closest grid point in a direction.
     */
    @java.lang.Override
    public java.awt.geom.Point2D.Double translatePoint(java.awt.geom.Point2D.Double p, org.jhotdraw.draw.constrainer.TranslationDirection dir, org.jhotdraw.draw.figure.Figure... figure) {
        java.awt.geom.Point2D.Double p0 = constrainPoint(((java.awt.geom.Point2D.Double) (p.clone())), figure);
        switch (dir) {
            case NORTH :
            case NORTH_WEST :
            case NORTH_EAST :
                p.y = p0.y - height;
                break;
            case SOUTH :
            case SOUTH_WEST :
            case SOUTH_EAST :
                p.y = p0.y + height;
                break;
        }
        switch (dir) {
            case WEST :
            case NORTH_WEST :
            case SOUTH_WEST :
                p.x = p0.x - width;
                break;
            case EAST :
            case NORTH_EAST :
            case SOUTH_EAST :
                p.x = p0.x + width;
                break;
        }
        return p;
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double constrainRectangle(java.awt.geom.Rectangle2D.Double r, org.jhotdraw.draw.figure.Figure... figure) {
        java.awt.geom.Point2D.Double p0 = constrainPoint(new java.awt.geom.Point2D.Double(r.x, r.y), figure);
        java.awt.geom.Point2D.Double p1 = constrainPoint(new java.awt.geom.Point2D.Double(r.x + r.width, r.y + r.height), figure);
        if (java.lang.Math.abs(p0.x - r.x) < java.lang.Math.abs((p1.x - r.x) - r.width)) {
            r.x = p0.x;
        } else {
            r.x = p1.x - r.width;
        }
        if (java.lang.Math.abs(p0.y - r.y) < java.lang.Math.abs((p1.y - r.y) - r.height)) {
            r.y = p0.y;
        } else {
            r.y = p1.y - r.height;
        }
        return r;
    }

    /**
     * Constrains the placement of a rectangle towards a direction.
     *
     * <p>This method changes the location of the rectangle which is passed as a parameter. This
     * method does not change the size of the rectangle.
     *
     * @param r
     * 		A rectangle on the drawing.
     * @param dir
     * 		A direction.
     * @return Returns the constrained rectangle.
     */
    protected java.awt.geom.Rectangle2D.Double constrainRectangle(java.awt.geom.Rectangle2D.Double r, org.jhotdraw.draw.constrainer.TranslationDirection dir, org.jhotdraw.draw.figure.Figure... figure) {
        java.awt.geom.Point2D.Double p0 = new java.awt.geom.Point2D.Double(r.x, r.y);
        switch (dir) {
            case NORTH :
            case NORTH_WEST :
            case WEST :
                constrainPoint(p0, dir, figure);
                break;
            case EAST :
            case NORTH_EAST :
                p0.x += r.width;
                constrainPoint(p0, dir, figure);
                p0.x -= r.width;
                break;
            case SOUTH :
            case SOUTH_WEST :
                p0.y += r.height;
                constrainPoint(p0, dir, figure);
                p0.y -= r.height;
                break;
            case SOUTH_EAST :
                p0.y += r.height;
                p0.x += r.width;
                constrainPoint(p0, dir, figure);
                p0.y -= r.height;
                p0.x -= r.width;
                break;
        }
        r.x = p0.x;
        r.y = p0.y;
        return r;
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D.Double translateRectangle(java.awt.geom.Rectangle2D.Double r, org.jhotdraw.draw.constrainer.TranslationDirection dir, org.jhotdraw.draw.figure.Figure... figure) {
        double x = r.x;
        double y = r.y;
        constrainRectangle(r, dir, figure);
        switch (dir) {
            case NORTH :
            case NORTH_WEST :
            case NORTH_EAST :
                if (y == r.y) {
                    r.y -= height;
                }
                break;
            case SOUTH :
            case SOUTH_WEST :
            case SOUTH_EAST :
                if (y == r.y) {
                    r.y += height;
                }
                break;
        }
        switch (dir) {
            case WEST :
            case NORTH_WEST :
            case SOUTH_WEST :
                if (x == r.x) {
                    r.x -= width;
                }
                break;
            case EAST :
            case NORTH_EAST :
            case SOUTH_EAST :
                if (x == r.x) {
                    r.x += width;
                }
                break;
        }
        return r;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((super.toString() + "[") + width) + ",") + height) + "]";
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean newValue) {
        boolean oldValue = isVisible;
        isVisible = newValue;
        fireStateChanged();
    }

    /**
     * Spacing between major grid lines.
     */
    public int getMajorGridSpacing() {
        return majorGridSpacing;
    }

    /**
     * Spacing between major grid lines.
     */
    public void setMajorGridSpacing(int newValue) {
        int oldValue = majorGridSpacing;
        majorGridSpacing = newValue;
        fireStateChanged();
    }

    @java.lang.Override
    public void draw(java.awt.Graphics2D g, org.jhotdraw.draw.DrawingView view) {
        if (isVisible) {
            java.awt.geom.AffineTransform t = view.getDrawingToViewTransform();
            java.awt.Rectangle viewBounds = g.getClipBounds();
            java.awt.geom.Rectangle2D.Double bounds = view.viewToDrawing(viewBounds);
            java.awt.geom.Point2D.Double origin = constrainPoint(new java.awt.geom.Point2D.Double(bounds.x, bounds.y));
            java.awt.geom.Point2D.Double point = new java.awt.geom.Point2D.Double();
            java.awt.geom.Point2D.Double viewPoint = new java.awt.geom.Point2D.Double();
            // vertical grid lines are only drawn, if they are at least two
            // pixels apart on the view coordinate system.
            if ((width * view.getScaleFactor()) > 2) {
                g.setColor(org.jhotdraw.draw.constrainer.GridConstrainer.minorColor);
                for (int i = ((int) (origin.x / width)), m = ((int) ((origin.x + bounds.width) / width)) + 1; i <= m; i++) {
                    g.setColor((i % majorGridSpacing) == 0 ? org.jhotdraw.draw.constrainer.GridConstrainer.majorColor : org.jhotdraw.draw.constrainer.GridConstrainer.minorColor);
                    point.x = width * i;
                    t.transform(point, viewPoint);
                    g.drawLine(((int) (viewPoint.x)), viewBounds.y, ((int) (viewPoint.x)), viewBounds.y + viewBounds.height);
                }
            } else if (((width * majorGridSpacing) * view.getScaleFactor()) > 2) {
                g.setColor(org.jhotdraw.draw.constrainer.GridConstrainer.majorColor);
                for (int i = ((int) (origin.x / width)), m = ((int) ((origin.x + bounds.width) / width)) + 1; i <= m; i++) {
                    if ((i % majorGridSpacing) == 0) {
                        point.x = width * i;
                        t.transform(point, viewPoint);
                        g.drawLine(((int) (viewPoint.x)), viewBounds.y, ((int) (viewPoint.x)), viewBounds.y + viewBounds.height);
                    }
                }
            }
            // horizontal grid lines are only drawn, if they are at least two
            // pixels apart on the view coordinate system.
            if ((height * view.getScaleFactor()) > 2) {
                g.setColor(org.jhotdraw.draw.constrainer.GridConstrainer.minorColor);
                for (int i = ((int) (origin.y / height)), m = ((int) ((origin.y + bounds.height) / height)) + 1; i <= m; i++) {
                    g.setColor((i % majorGridSpacing) == 0 ? org.jhotdraw.draw.constrainer.GridConstrainer.majorColor : org.jhotdraw.draw.constrainer.GridConstrainer.minorColor);
                    point.y = height * i;
                    t.transform(point, viewPoint);
                    g.drawLine(viewBounds.x, ((int) (viewPoint.y)), viewBounds.x + viewBounds.width, ((int) (viewPoint.y)));
                }
            } else if (((height * majorGridSpacing) * view.getScaleFactor()) > 2) {
                g.setColor(org.jhotdraw.draw.constrainer.GridConstrainer.majorColor);
                for (int i = ((int) (origin.y / height)), m = ((int) ((origin.y + bounds.height) / height)) + 1; i <= m; i++) {
                    if ((i % majorGridSpacing) == 0) {
                        point.y = height * i;
                        t.transform(point, viewPoint);
                        g.drawLine(viewBounds.x, ((int) (viewPoint.y)), viewBounds.x + viewBounds.width, ((int) (viewPoint.y)));
                    }
                }
            }
        }
    }

    @java.lang.Override
    public double constrainAngle(double angle, org.jhotdraw.draw.figure.Figure... figure) {
        // No step specified then no constraining
        if (theta == 0) {
            return angle;
        }
        double factor = java.lang.Math.round(angle / theta);
        return theta * factor;
    }

    @java.lang.Override
    public double rotateAngle(double angle, org.jhotdraw.draw.constrainer.RotationDirection dir, org.jhotdraw.draw.figure.Figure... figure) {
        // Check parameters
        if (dir == null) {
            throw new java.lang.IllegalArgumentException("dir must not be null");
        }
        // Rotate into the specified direction by theta
        angle = constrainAngle(angle, figure);
        switch (dir) {
            case CLOCKWISE :
                angle += theta;
                break;
            case COUNTER_CLOCKWISE :
            default :
                angle -= theta;
                break;
        }
        return angle;
    }
}