/* @(#)TransformHandleKit.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.handle;
/**
 * A set of utility methods to create Handles which transform a Figure by using its <code>transform
 * </code> method.
 */
public class TransformHandleKit {
    public TransformHandleKit() {
    }

    /**
     * Creates handles for each corner of a figure and adds them to the provided collection.
     */
    public static void addCornerTransformHandles(org.jhotdraw.draw.figure.Figure f, java.util.Collection<org.jhotdraw.draw.handle.Handle> handles) {
        if (f.isTransformable()) {
            handles.add(org.jhotdraw.draw.handle.TransformHandleKit.southEast(f));
            handles.add(org.jhotdraw.draw.handle.TransformHandleKit.southWest(f));
            handles.add(org.jhotdraw.draw.handle.TransformHandleKit.northEast(f));
            handles.add(org.jhotdraw.draw.handle.TransformHandleKit.northWest(f));
        }
    }

    /**
     * Fills the given collection with handles at each the north, south, east, and west of the figure.
     */
    public static void addEdgeTransformHandles(org.jhotdraw.draw.figure.Figure f, java.util.Collection<org.jhotdraw.draw.handle.Handle> handles) {
        if (f.isTransformable()) {
            handles.add(org.jhotdraw.draw.handle.TransformHandleKit.south(f));
            handles.add(org.jhotdraw.draw.handle.TransformHandleKit.north(f));
            handles.add(org.jhotdraw.draw.handle.TransformHandleKit.east(f));
            handles.add(org.jhotdraw.draw.handle.TransformHandleKit.west(f));
        }
    }

    /**
     * Adds handles for scaling and moving a Figure.
     */
    public static void addScaleMoveTransformHandles(org.jhotdraw.draw.figure.Figure f, java.util.Collection<org.jhotdraw.draw.handle.Handle> handles) {
        if (f.isTransformable()) {
            org.jhotdraw.draw.handle.TransformHandleKit.addCornerTransformHandles(f, handles);
            org.jhotdraw.draw.handle.TransformHandleKit.addEdgeTransformHandles(f, handles);
        }
    }

    /**
     * Adds handles for scaling, moving, rotating and shearing a Figure.
     */
    public static void addTransformHandles(org.jhotdraw.draw.figure.Figure f, java.util.Collection<org.jhotdraw.draw.handle.Handle> handles) {
        handles.add(new org.jhotdraw.draw.handle.BoundsOutlineHandle(f, true, false));
        if (f.isTransformable()) {
            org.jhotdraw.draw.handle.TransformHandleKit.addCornerTransformHandles(f, handles);
            org.jhotdraw.draw.handle.TransformHandleKit.addEdgeTransformHandles(f, handles);
            handles.add(new org.jhotdraw.draw.handle.RotateHandle(f));
        }
    }

    /**
     * Adds handles for scaling, moving, rotating and shearing a Figure.
     */
    public static void addGroupTransformHandles(org.jhotdraw.draw.figure.Figure f, java.util.Collection<org.jhotdraw.draw.handle.Handle> handles) {
        handles.add(new org.jhotdraw.draw.handle.BoundsOutlineHandle(f, org.jhotdraw.draw.handle.HandleAttributeKeys.GROUP_BOUNDS_STROKE_1, org.jhotdraw.draw.handle.HandleAttributeKeys.GROUP_BOUNDS_COLOR_1, org.jhotdraw.draw.handle.HandleAttributeKeys.GROUP_BOUNDS_STROKE_2, org.jhotdraw.draw.handle.HandleAttributeKeys.GROUP_BOUNDS_COLOR_2, org.jhotdraw.draw.handle.HandleAttributeKeys.GROUP_BOUNDS_STROKE_1_DISABLED, org.jhotdraw.draw.handle.HandleAttributeKeys.GROUP_BOUNDS_COLOR_1_DISABLED, org.jhotdraw.draw.handle.HandleAttributeKeys.GROUP_BOUNDS_STROKE_2_DISABLED, org.jhotdraw.draw.handle.HandleAttributeKeys.GROUP_BOUNDS_COLOR_2_DISABLED));
        org.jhotdraw.draw.handle.TransformHandleKit.addCornerTransformHandles(f, handles);
        org.jhotdraw.draw.handle.TransformHandleKit.addEdgeTransformHandles(f, handles);
        handles.add(new org.jhotdraw.draw.handle.RotateHandle(f));
    }

    /**
     * Adds handles for scaling, moving, rotating and shearing a Figure.
     */
    public static void addGroupHoverHandles(org.jhotdraw.draw.figure.Figure f, java.util.Collection<org.jhotdraw.draw.handle.Handle> handles) {
        handles.add(new org.jhotdraw.draw.handle.BoundsOutlineHandle(f, org.jhotdraw.draw.handle.HandleAttributeKeys.GROUP_BOUNDS_STROKE_1_HOVER, org.jhotdraw.draw.handle.HandleAttributeKeys.GROUP_BOUNDS_COLOR_1_HOVER, org.jhotdraw.draw.handle.HandleAttributeKeys.GROUP_BOUNDS_STROKE_2_HOVER, org.jhotdraw.draw.handle.HandleAttributeKeys.GROUP_BOUNDS_COLOR_2_HOVER, org.jhotdraw.draw.handle.HandleAttributeKeys.GROUP_BOUNDS_STROKE_1_DISABLED, org.jhotdraw.draw.handle.HandleAttributeKeys.GROUP_BOUNDS_COLOR_1_DISABLED, org.jhotdraw.draw.handle.HandleAttributeKeys.GROUP_BOUNDS_STROKE_2_DISABLED, org.jhotdraw.draw.handle.HandleAttributeKeys.GROUP_BOUNDS_COLOR_2_DISABLED));
    }

    public static org.jhotdraw.draw.handle.Handle south(org.jhotdraw.draw.figure.Figure owner) {
        return new org.jhotdraw.draw.handle.TransformHandleKit.SouthHandle(owner);
    }

    public static org.jhotdraw.draw.handle.Handle southEast(org.jhotdraw.draw.figure.Figure owner) {
        return new org.jhotdraw.draw.handle.TransformHandleKit.SouthEastHandle(owner);
    }

    public static org.jhotdraw.draw.handle.Handle southWest(org.jhotdraw.draw.figure.Figure owner) {
        return new org.jhotdraw.draw.handle.TransformHandleKit.SouthWestHandle(owner);
    }

    public static org.jhotdraw.draw.handle.Handle north(org.jhotdraw.draw.figure.Figure owner) {
        return new org.jhotdraw.draw.handle.TransformHandleKit.NorthHandle(owner);
    }

    public static org.jhotdraw.draw.handle.Handle northEast(org.jhotdraw.draw.figure.Figure owner) {
        return new org.jhotdraw.draw.handle.TransformHandleKit.NorthEastHandle(owner);
    }

    public static org.jhotdraw.draw.handle.Handle northWest(org.jhotdraw.draw.figure.Figure owner) {
        return new org.jhotdraw.draw.handle.TransformHandleKit.NorthWestHandle(owner);
    }

    public static org.jhotdraw.draw.handle.Handle east(org.jhotdraw.draw.figure.Figure owner) {
        return new org.jhotdraw.draw.handle.TransformHandleKit.EastHandle(owner);
    }

    public static org.jhotdraw.draw.handle.Handle west(org.jhotdraw.draw.figure.Figure owner) {
        return new org.jhotdraw.draw.handle.TransformHandleKit.WestHandle(owner);
    }

    private static class TransformHandle extends org.jhotdraw.draw.handle.LocatorHandle {
        private int dx;

        private int dy;

        private java.lang.Object geometry;

        /**
         * Caches the value returned by getOwner().isTransformable():
         */
        private boolean isTransformableCache;

        TransformHandle(org.jhotdraw.draw.figure.Figure owner, org.jhotdraw.draw.locator.Locator loc) {
            super(owner, loc);
        }

        @java.lang.Override
        public java.lang.String getToolTipText(java.awt.Point p) {
            org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
            return labels.getString("handle.transform.toolTipText");
        }

        /**
         * Draws this handle.
         */
        @java.lang.Override
        public void draw(java.awt.Graphics2D g) {
            if (getEditor().getTool().supportsHandleInteraction()) {
                // drawArc(g);
                drawDiamond(g, getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.TRANSFORM_HANDLE_FILL_COLOR), getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.TRANSFORM_HANDLE_STROKE_COLOR));
            } else {
                drawDiamond(g, getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.TRANSFORM_HANDLE_FILL_COLOR_DISABLED), getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.TRANSFORM_HANDLE_STROKE_COLOR_DISABLED));
            }
        }

        protected void drawArc(java.awt.Graphics2D g) {
            java.awt.Point p = getScreenLocation();
            g.drawArc(p.x, p.y, 6, 6, 0, 180);
        }

        protected java.awt.geom.Rectangle2D.Double getTransformedBounds() {
            org.jhotdraw.draw.figure.Figure owner = getOwner();
            java.awt.geom.Rectangle2D.Double bounds = owner.getBounds();
            if (owner.attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) {
                java.awt.geom.Rectangle2D r = owner.attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM).createTransformedShape(bounds).getBounds2D();
                bounds.x = r.getX();
                bounds.y = r.getY();
                bounds.width = r.getWidth();
                bounds.height = r.getHeight();
            }
            return bounds;
        }

        @java.lang.Override
        public void trackStart(java.awt.Point anchor, int modifiersEx) {
            isTransformableCache = getOwner().isTransformable();
            if (!isTransformableCache) {
                return;
            }
            geometry = getOwner().getTransformRestoreData();
            java.awt.Point location = getScreenLocation();
            dx = (-anchor.x) + location.x;
            dy = (-anchor.y) + location.y;
        }

        @java.lang.Override
        public void trackStep(java.awt.Point anchor, java.awt.Point lead, int modifiersEx) {
            if (!isTransformableCache) {
                return;
            }
            java.awt.geom.Point2D.Double p = view.viewToDrawing(new java.awt.Point(lead.x + dx, lead.y + dy));
            if (view.getConstrainer() != null) {
                p = view.getConstrainer().constrainPoint(p);
            }
            trackStepNormalized(p);
        }

        @java.lang.Override
        public void trackEnd(java.awt.Point anchor, java.awt.Point lead, int modifiersEx) {
            if (!isTransformableCache) {
                return;
            }
            fireUndoableEditHappened(new org.jhotdraw.draw.event.TransformRestoreEdit(getOwner(), geometry, getOwner().getTransformRestoreData()));
        }

        protected void trackStepNormalized(java.awt.geom.Point2D.Double p) {
        }

        protected void transform(java.awt.geom.Point2D.Double anchor, java.awt.geom.Point2D.Double lead) {
            org.jhotdraw.draw.figure.Figure f = getOwner();
            f.willChange();
            java.awt.geom.Rectangle2D.Double oldBounds = getTransformedBounds();
            java.awt.geom.Rectangle2D.Double newBounds = new java.awt.geom.Rectangle2D.Double(java.lang.Math.min(anchor.x, lead.x), java.lang.Math.min(anchor.y, lead.y), java.lang.Math.abs(anchor.x - lead.x), java.lang.Math.abs(anchor.y - lead.y));
            double sx = newBounds.width / oldBounds.width;
            double sy = newBounds.height / oldBounds.height;
            java.awt.geom.AffineTransform tx = new java.awt.geom.AffineTransform();
            tx.translate(-oldBounds.x, -oldBounds.y);
            if (((((!java.lang.Double.isNaN(sx)) && (!java.lang.Double.isNaN(sy))) && ((sx != 1.0) || (sy != 1.0))) && (!(sx < 1.0E-4))) && (!(sy < 1.0E-4))) {
                f.transform(tx);
                tx.setToIdentity();
                tx.scale(sx, sy);
                f.transform(tx);
                tx.setToIdentity();
            }
            tx.translate(newBounds.x, newBounds.y);
            f.transform(tx);
            f.changed();
        }
    }

    private static class NorthEastHandle extends org.jhotdraw.draw.handle.TransformHandleKit.TransformHandle {
        NorthEastHandle(org.jhotdraw.draw.figure.Figure owner) {
            super(owner, org.jhotdraw.draw.locator.RelativeLocator.northEast());
        }

        @java.lang.Override
        protected void trackStepNormalized(java.awt.geom.Point2D.Double p) {
            java.awt.geom.Rectangle2D.Double r = getTransformedBounds();
            transform(new java.awt.geom.Point2D.Double(r.x, java.lang.Math.min((r.y + r.height) - 1, p.y)), new java.awt.geom.Point2D.Double(java.lang.Math.max(r.x, p.x), r.y + r.height));
        }

        @java.lang.Override
        public void keyPressed(java.awt.event.KeyEvent evt) {
            if (!getOwner().isTransformable()) {
                evt.consume();
                return;
            }
            java.lang.Object geom = getOwner().getTransformRestoreData();
            java.awt.geom.Rectangle2D.Double r = getTransformedBounds();
            switch (evt.getKeyCode()) {
                case java.awt.event.KeyEvent.VK_UP :
                    transform(new java.awt.geom.Point2D.Double(r.x, r.y - 1), new java.awt.geom.Point2D.Double(r.x + r.width, r.y + r.height));
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_DOWN :
                    if (r.height > 1) {
                        transform(new java.awt.geom.Point2D.Double(r.x, r.y + 1), new java.awt.geom.Point2D.Double(r.x + r.width, r.y + r.height));
                    }
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_LEFT :
                    if (r.width > 1) {
                        transform(new java.awt.geom.Point2D.Double(r.x, r.y), new java.awt.geom.Point2D.Double((r.x + r.width) - 1, r.y + r.height));
                    }
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_RIGHT :
                    transform(new java.awt.geom.Point2D.Double(r.x, r.y), new java.awt.geom.Point2D.Double((r.x + r.width) + 1, r.y + r.height));
                    evt.consume();
                    break;
            }
            fireUndoableEditHappened(new org.jhotdraw.draw.event.TransformRestoreEdit(getOwner(), geom, getOwner().getTransformRestoreData()));
        }

        @java.lang.Override
        public java.awt.Cursor getCursor() {
            return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.NE_RESIZE_CURSOR);
        }
    }

    private static class EastHandle extends org.jhotdraw.draw.handle.TransformHandleKit.TransformHandle {
        EastHandle(org.jhotdraw.draw.figure.Figure owner) {
            super(owner, org.jhotdraw.draw.locator.RelativeLocator.east());
        }

        @java.lang.Override
        protected void trackStepNormalized(java.awt.geom.Point2D.Double p) {
            java.awt.geom.Rectangle2D.Double r = getTransformedBounds();
            transform(new java.awt.geom.Point2D.Double(r.x, r.y), new java.awt.geom.Point2D.Double(java.lang.Math.max(r.x + 1, p.x), r.y + r.height));
        }

        @java.lang.Override
        public void keyPressed(java.awt.event.KeyEvent evt) {
            if (!getOwner().isTransformable()) {
                evt.consume();
                return;
            }
            java.lang.Object geom = getOwner().getTransformRestoreData();
            java.awt.geom.Rectangle2D.Double r = getTransformedBounds();
            switch (evt.getKeyCode()) {
                case java.awt.event.KeyEvent.VK_UP :
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_DOWN :
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_LEFT :
                    if (r.width > 1) {
                        transform(new java.awt.geom.Point2D.Double(r.x, r.y), new java.awt.geom.Point2D.Double((r.x + r.width) - 1, r.y + r.height));
                    }
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_RIGHT :
                    transform(new java.awt.geom.Point2D.Double(r.x, r.y), new java.awt.geom.Point2D.Double((r.x + r.width) + 1, r.y + r.height));
                    evt.consume();
                    break;
            }
            fireUndoableEditHappened(new org.jhotdraw.draw.event.TransformRestoreEdit(getOwner(), geom, getOwner().getTransformRestoreData()));
        }

        @java.lang.Override
        public java.awt.Cursor getCursor() {
            return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.E_RESIZE_CURSOR);
        }
    }

    private static class NorthHandle extends org.jhotdraw.draw.handle.TransformHandleKit.TransformHandle {
        NorthHandle(org.jhotdraw.draw.figure.Figure owner) {
            super(owner, org.jhotdraw.draw.locator.RelativeLocator.north());
        }

        @java.lang.Override
        protected void trackStepNormalized(java.awt.geom.Point2D.Double p) {
            java.awt.geom.Rectangle2D.Double r = getTransformedBounds();
            transform(new java.awt.geom.Point2D.Double(r.x, java.lang.Math.min((r.y + r.height) - 1, p.y)), new java.awt.geom.Point2D.Double(r.x + r.width, r.y + r.height));
        }

        @java.lang.Override
        public void keyPressed(java.awt.event.KeyEvent evt) {
            if (!getOwner().isTransformable()) {
                evt.consume();
                return;
            }
            java.lang.Object geom = getOwner().getTransformRestoreData();
            java.awt.geom.Rectangle2D.Double r = getTransformedBounds();
            switch (evt.getKeyCode()) {
                case java.awt.event.KeyEvent.VK_UP :
                    transform(new java.awt.geom.Point2D.Double(r.x, r.y - 1), new java.awt.geom.Point2D.Double(r.x + r.width, r.y + r.height));
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_DOWN :
                    if (r.height > 1) {
                        transform(new java.awt.geom.Point2D.Double(r.x, r.y + 1), new java.awt.geom.Point2D.Double(r.x + r.width, r.y + r.height));
                    }
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_LEFT :
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_RIGHT :
                    evt.consume();
                    break;
            }
            fireUndoableEditHappened(new org.jhotdraw.draw.event.TransformRestoreEdit(getOwner(), geom, getOwner().getTransformRestoreData()));
        }

        @java.lang.Override
        public java.awt.Cursor getCursor() {
            return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.N_RESIZE_CURSOR);
        }
    }

    private static class NorthWestHandle extends org.jhotdraw.draw.handle.TransformHandleKit.TransformHandle {
        NorthWestHandle(org.jhotdraw.draw.figure.Figure owner) {
            super(owner, org.jhotdraw.draw.locator.RelativeLocator.northWest());
        }

        @java.lang.Override
        protected void trackStepNormalized(java.awt.geom.Point2D.Double p) {
            java.awt.geom.Rectangle2D.Double r = getTransformedBounds();
            transform(new java.awt.geom.Point2D.Double(java.lang.Math.min((r.x + r.width) - 1, p.x), java.lang.Math.min((r.y + r.height) - 1, p.y)), new java.awt.geom.Point2D.Double(r.x + r.width, r.y + r.height));
        }

        @java.lang.Override
        public void keyPressed(java.awt.event.KeyEvent evt) {
            if (!getOwner().isTransformable()) {
                evt.consume();
                return;
            }
            java.lang.Object geom = getOwner().getTransformRestoreData();
            java.awt.geom.Rectangle2D.Double r = getTransformedBounds();
            switch (evt.getKeyCode()) {
                case java.awt.event.KeyEvent.VK_UP :
                    transform(new java.awt.geom.Point2D.Double(r.x, r.y - 1), new java.awt.geom.Point2D.Double(r.x + r.width, r.y + r.height));
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_DOWN :
                    if (r.height > 1) {
                        transform(new java.awt.geom.Point2D.Double(r.x, r.y + 1), new java.awt.geom.Point2D.Double(r.x + r.width, r.y + r.height));
                    }
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_LEFT :
                    transform(new java.awt.geom.Point2D.Double(r.x - 1, r.y), new java.awt.geom.Point2D.Double(r.x + r.width, r.y + r.height));
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_RIGHT :
                    if (r.width > 1) {
                        transform(new java.awt.geom.Point2D.Double(r.x + 1, r.y), new java.awt.geom.Point2D.Double(r.x + r.width, r.y + r.height));
                    }
                    evt.consume();
                    break;
            }
            fireUndoableEditHappened(new org.jhotdraw.draw.event.TransformRestoreEdit(getOwner(), geom, getOwner().getTransformRestoreData()));
        }

        @java.lang.Override
        public java.awt.Cursor getCursor() {
            return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.NW_RESIZE_CURSOR);
        }
    }

    private static class SouthEastHandle extends org.jhotdraw.draw.handle.TransformHandleKit.TransformHandle {
        SouthEastHandle(org.jhotdraw.draw.figure.Figure owner) {
            super(owner, org.jhotdraw.draw.locator.RelativeLocator.southEast());
        }

        @java.lang.Override
        protected void trackStepNormalized(java.awt.geom.Point2D.Double p) {
            java.awt.geom.Rectangle2D.Double r = getTransformedBounds();
            transform(new java.awt.geom.Point2D.Double(r.x, r.y), new java.awt.geom.Point2D.Double(java.lang.Math.max(r.x + 1, p.x), java.lang.Math.max(r.y + 1, p.y)));
        }

        @java.lang.Override
        public void keyPressed(java.awt.event.KeyEvent evt) {
            if (!getOwner().isTransformable()) {
                evt.consume();
                return;
            }
            java.lang.Object geom = getOwner().getTransformRestoreData();
            java.awt.geom.Rectangle2D.Double r = getTransformedBounds();
            switch (evt.getKeyCode()) {
                case java.awt.event.KeyEvent.VK_UP :
                    if (r.height > 1) {
                        transform(new java.awt.geom.Point2D.Double(r.x, r.y), new java.awt.geom.Point2D.Double(r.x + r.width, (r.y + r.height) - 1));
                    }
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_DOWN :
                    transform(new java.awt.geom.Point2D.Double(r.x, r.y), new java.awt.geom.Point2D.Double(r.x + r.width, (r.y + r.height) + 1));
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_LEFT :
                    if (r.width > 1) {
                        transform(new java.awt.geom.Point2D.Double(r.x, r.y), new java.awt.geom.Point2D.Double((r.x + r.width) - 1, r.y + r.height));
                    }
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_RIGHT :
                    transform(new java.awt.geom.Point2D.Double(r.x, r.y), new java.awt.geom.Point2D.Double((r.x + r.width) + 1, r.y + r.height));
                    evt.consume();
                    break;
            }
            fireUndoableEditHappened(new org.jhotdraw.draw.event.TransformRestoreEdit(getOwner(), geom, getOwner().getTransformRestoreData()));
        }

        @java.lang.Override
        public java.awt.Cursor getCursor() {
            return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.SE_RESIZE_CURSOR);
        }
    }

    private static class SouthHandle extends org.jhotdraw.draw.handle.TransformHandleKit.TransformHandle {
        SouthHandle(org.jhotdraw.draw.figure.Figure owner) {
            super(owner, org.jhotdraw.draw.locator.RelativeLocator.south());
        }

        @java.lang.Override
        protected void trackStepNormalized(java.awt.geom.Point2D.Double p) {
            java.awt.geom.Rectangle2D.Double r = getTransformedBounds();
            transform(new java.awt.geom.Point2D.Double(r.x, r.y), new java.awt.geom.Point2D.Double(r.x + r.width, java.lang.Math.max(r.y + 1, p.y)));
        }

        @java.lang.Override
        public void keyPressed(java.awt.event.KeyEvent evt) {
            if (!getOwner().isTransformable()) {
                evt.consume();
                return;
            }
            java.lang.Object geom = getOwner().getTransformRestoreData();
            java.awt.geom.Rectangle2D.Double r = getTransformedBounds();
            switch (evt.getKeyCode()) {
                case java.awt.event.KeyEvent.VK_UP :
                    if (r.height > 1) {
                        transform(new java.awt.geom.Point2D.Double(r.x, r.y), new java.awt.geom.Point2D.Double(r.x + r.width, (r.y + r.height) - 1));
                    }
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_DOWN :
                    transform(new java.awt.geom.Point2D.Double(r.x, r.y), new java.awt.geom.Point2D.Double(r.x + r.width, (r.y + r.height) + 1));
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_LEFT :
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_RIGHT :
                    evt.consume();
                    break;
            }
            fireUndoableEditHappened(new org.jhotdraw.draw.event.TransformRestoreEdit(getOwner(), geom, getOwner().getTransformRestoreData()));
        }

        @java.lang.Override
        public java.awt.Cursor getCursor() {
            return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.S_RESIZE_CURSOR);
        }
    }

    private static class SouthWestHandle extends org.jhotdraw.draw.handle.TransformHandleKit.TransformHandle {
        SouthWestHandle(org.jhotdraw.draw.figure.Figure owner) {
            super(owner, org.jhotdraw.draw.locator.RelativeLocator.southWest());
        }

        @java.lang.Override
        protected void trackStepNormalized(java.awt.geom.Point2D.Double p) {
            java.awt.geom.Rectangle2D.Double r = getTransformedBounds();
            transform(new java.awt.geom.Point2D.Double(java.lang.Math.min((r.x + r.width) - 1, p.x), r.y), new java.awt.geom.Point2D.Double(r.x + r.width, java.lang.Math.max(r.y + 1, p.y)));
        }

        @java.lang.Override
        public void keyPressed(java.awt.event.KeyEvent evt) {
            if (!getOwner().isTransformable()) {
                evt.consume();
                return;
            }
            java.lang.Object geom = getOwner().getTransformRestoreData();
            java.awt.geom.Rectangle2D.Double r = getTransformedBounds();
            switch (evt.getKeyCode()) {
                case java.awt.event.KeyEvent.VK_UP :
                    if (r.height > 1) {
                        transform(new java.awt.geom.Point2D.Double(r.x, r.y), new java.awt.geom.Point2D.Double(r.x + r.width, (r.y + r.height) - 1));
                    }
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_DOWN :
                    transform(new java.awt.geom.Point2D.Double(r.x, r.y), new java.awt.geom.Point2D.Double(r.x + r.width, (r.y + r.height) + 1));
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_LEFT :
                    transform(new java.awt.geom.Point2D.Double(r.x - 1, r.y), new java.awt.geom.Point2D.Double(r.x + r.width, r.y + r.height));
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_RIGHT :
                    if (r.width > 1) {
                        transform(new java.awt.geom.Point2D.Double(r.x + 1, r.y), new java.awt.geom.Point2D.Double(r.x + r.width, r.y + r.height));
                    }
                    evt.consume();
                    break;
            }
            fireUndoableEditHappened(new org.jhotdraw.draw.event.TransformRestoreEdit(getOwner(), geom, getOwner().getTransformRestoreData()));
        }

        @java.lang.Override
        public java.awt.Cursor getCursor() {
            return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.SW_RESIZE_CURSOR);
        }
    }

    private static class WestHandle extends org.jhotdraw.draw.handle.TransformHandleKit.TransformHandle {
        WestHandle(org.jhotdraw.draw.figure.Figure owner) {
            super(owner, org.jhotdraw.draw.locator.RelativeLocator.west());
        }

        @java.lang.Override
        protected void trackStepNormalized(java.awt.geom.Point2D.Double p) {
            java.awt.geom.Rectangle2D.Double r = getTransformedBounds();
            transform(new java.awt.geom.Point2D.Double(java.lang.Math.min((r.x + r.width) - 1, p.x), r.y), new java.awt.geom.Point2D.Double(r.x + r.width, r.y + r.height));
        }

        @java.lang.Override
        public void keyPressed(java.awt.event.KeyEvent evt) {
            if (!getOwner().isTransformable()) {
                evt.consume();
                return;
            }
            java.lang.Object geom = getOwner().getTransformRestoreData();
            java.awt.geom.Rectangle2D.Double r = getTransformedBounds();
            switch (evt.getKeyCode()) {
                case java.awt.event.KeyEvent.VK_UP :
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_DOWN :
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_LEFT :
                    transform(new java.awt.geom.Point2D.Double(r.x - 1, r.y), new java.awt.geom.Point2D.Double(r.x + r.width, r.y + r.height));
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_RIGHT :
                    if (r.width > 1) {
                        transform(new java.awt.geom.Point2D.Double(r.x + 1, r.y), new java.awt.geom.Point2D.Double(r.x + r.width, r.y + r.height));
                    }
                    evt.consume();
                    break;
            }
            fireUndoableEditHappened(new org.jhotdraw.draw.event.TransformRestoreEdit(getOwner(), geom, getOwner().getTransformRestoreData()));
        }

        @java.lang.Override
        public java.awt.Cursor getCursor() {
            return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.W_RESIZE_CURSOR);
        }
    }
}