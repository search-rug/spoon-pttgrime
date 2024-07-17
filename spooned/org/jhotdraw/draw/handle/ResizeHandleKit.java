/* @(#)BoxHandleKit.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.handle;
/**
 * A set of utility methods to create handles which resize a Figure by using its <code>setBounds
 * </code> method, if the Figure is transformable.
 */
public class ResizeHandleKit {
    public ResizeHandleKit() {
    }

    /**
     * Creates handles for each corner of a figure and adds them to the provided collection.
     */
    public static void addCornerResizeHandles(org.jhotdraw.draw.figure.Figure f, java.util.Collection<org.jhotdraw.draw.handle.Handle> handles) {
        if (f.isTransformable()) {
            handles.add(org.jhotdraw.draw.handle.ResizeHandleKit.southEast(f));
            handles.add(org.jhotdraw.draw.handle.ResizeHandleKit.southWest(f));
            handles.add(org.jhotdraw.draw.handle.ResizeHandleKit.northEast(f));
            handles.add(org.jhotdraw.draw.handle.ResizeHandleKit.northWest(f));
        }
    }

    /**
     * Fills the given collection with handles at each the north, south, east, and west of the figure.
     */
    public static void addEdgeResizeHandles(org.jhotdraw.draw.figure.Figure f, java.util.Collection<org.jhotdraw.draw.handle.Handle> handles) {
        if (f.isTransformable()) {
            handles.add(org.jhotdraw.draw.handle.ResizeHandleKit.south(f));
            handles.add(org.jhotdraw.draw.handle.ResizeHandleKit.north(f));
            handles.add(org.jhotdraw.draw.handle.ResizeHandleKit.east(f));
            handles.add(org.jhotdraw.draw.handle.ResizeHandleKit.west(f));
        }
    }

    /**
     * Fills the given collection with handles at each the north, south, east, and west of the figure.
     */
    public static void addResizeHandles(org.jhotdraw.draw.figure.Figure f, java.util.Collection<org.jhotdraw.draw.handle.Handle> handles) {
        handles.add(new org.jhotdraw.draw.handle.BoundsOutlineHandle(f));
        if (f.isTransformable()) {
            org.jhotdraw.draw.handle.ResizeHandleKit.addCornerResizeHandles(f, handles);
            org.jhotdraw.draw.handle.ResizeHandleKit.addEdgeResizeHandles(f, handles);
        }
    }

    public static org.jhotdraw.draw.handle.Handle south(org.jhotdraw.draw.figure.Figure owner) {
        return new org.jhotdraw.draw.handle.ResizeHandleKit.SouthHandle(owner);
    }

    public static org.jhotdraw.draw.handle.Handle southEast(org.jhotdraw.draw.figure.Figure owner) {
        return new org.jhotdraw.draw.handle.ResizeHandleKit.SouthEastHandle(owner);
    }

    public static org.jhotdraw.draw.handle.Handle southWest(org.jhotdraw.draw.figure.Figure owner) {
        return new org.jhotdraw.draw.handle.ResizeHandleKit.SouthWestHandle(owner);
    }

    public static org.jhotdraw.draw.handle.Handle north(org.jhotdraw.draw.figure.Figure owner) {
        return new org.jhotdraw.draw.handle.ResizeHandleKit.NorthHandle(owner);
    }

    public static org.jhotdraw.draw.handle.Handle northEast(org.jhotdraw.draw.figure.Figure owner) {
        return new org.jhotdraw.draw.handle.ResizeHandleKit.NorthEastHandle(owner);
    }

    public static org.jhotdraw.draw.handle.Handle northWest(org.jhotdraw.draw.figure.Figure owner) {
        return new org.jhotdraw.draw.handle.ResizeHandleKit.NorthWestHandle(owner);
    }

    public static org.jhotdraw.draw.handle.Handle east(org.jhotdraw.draw.figure.Figure owner) {
        return new org.jhotdraw.draw.handle.ResizeHandleKit.EastHandle(owner);
    }

    public static org.jhotdraw.draw.handle.Handle west(org.jhotdraw.draw.figure.Figure owner) {
        return new org.jhotdraw.draw.handle.ResizeHandleKit.WestHandle(owner);
    }

    private static class ResizeHandle extends org.jhotdraw.draw.handle.LocatorHandle {
        private int sx;

        /**
         * Mouse coordinates on track start.
         */
        private int sy;

        /**
         * Geometry for undo.
         */
        private java.lang.Object geometry;

        /**
         * Figure bounds on track start.
         */
        protected java.awt.geom.Rectangle2D.Double sb;

        /**
         * Aspect ratio on track start.
         */
        double aspectRatio;

        /**
         * Caches the value returned by getOwner().isTransformable():
         */
        private boolean isTransformableCache;

        ResizeHandle(org.jhotdraw.draw.figure.Figure owner, org.jhotdraw.draw.locator.Locator loc) {
            super(owner, loc);
        }

        @java.lang.Override
        public java.lang.String getToolTipText(java.awt.Point p) {
            org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
            return labels.getString("handle.resize.toolTipText");
        }

        /**
         * Draws this handle.
         *
         * <p>If the figure is transformable, the handle is drawn as a filled rectangle. If the figure
         * is not transformable, the handle is drawn as an unfilled rectangle.
         */
        @java.lang.Override
        public void draw(java.awt.Graphics2D g) {
            if (getEditor().getTool().supportsHandleInteraction()) {
                if (getOwner().isTransformable()) {
                    drawRectangle(g, getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.RESIZE_HANDLE_FILL_COLOR), getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.RESIZE_HANDLE_STROKE_COLOR));
                } else {
                    drawRectangle(g, getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.NULL_HANDLE_FILL_COLOR), getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.NULL_HANDLE_STROKE_COLOR));
                }
            } else {
                drawRectangle(g, getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.HANDLE_FILL_COLOR_DISABLED), getEditor().getHandleAttribute(org.jhotdraw.draw.handle.HandleAttributeKeys.HANDLE_STROKE_COLOR_DISABLED));
            }
        }

        @java.lang.Override
        public void trackStart(java.awt.Point anchor, int modifiersEx) {
            isTransformableCache = getOwner().isTransformable();
            if (!isTransformableCache) {
                return;
            }
            geometry = getOwner().getTransformRestoreData();
            java.awt.Point location = getScreenLocation();
            sx = (-anchor.x) + location.x;
            sy = (-anchor.y) + location.y;
            sb = getOwner().getBounds();
            aspectRatio = sb.height / sb.width;
        }

        @java.lang.Override
        public void trackStep(java.awt.Point anchor, java.awt.Point lead, int modifiersEx) {
            if (!isTransformableCache) {
                return;
            }
            java.awt.geom.Point2D.Double p = view.viewToDrawing(new java.awt.Point(lead.x + sx, lead.y + sy));
            if (view.getConstrainer() != null) {
                p = view.getConstrainer().constrainPoint(p);
            }
            if (getOwner().attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM) != null) {
                try {
                    getOwner().attr().get(org.jhotdraw.draw.AttributeKeys.TRANSFORM).inverseTransform(p, p);
                } catch (java.awt.geom.NoninvertibleTransformException ex) {
                    org.jhotdraw.draw.handle.ResizeHandleKit.ResizeHandle.LOG.throwing(org.jhotdraw.draw.handle.ResizeHandleKit.class.getName(), "trackStep", ex);
                }
            }
            trackStepNormalized(p, (modifiersEx & ((java.awt.event.InputEvent.ALT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK) | java.awt.event.InputEvent.SHIFT_DOWN_MASK)) != 0);
        }

        private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(org.jhotdraw.draw.handle.ResizeHandleKit.ResizeHandle.class.getName());

        @java.lang.Override
        public void trackEnd(java.awt.Point anchor, java.awt.Point lead, int modifiersEx) {
            if (!isTransformableCache) {
                return;
            }
            fireUndoableEditHappened(new org.jhotdraw.draw.event.TransformRestoreEdit(getOwner(), geometry, getOwner().getTransformRestoreData()));
        }

        protected void trackStepNormalized(java.awt.geom.Point2D.Double p, boolean keepAspect) {
        }

        protected void setBounds(java.awt.geom.Point2D.Double anchor, java.awt.geom.Point2D.Double lead) {
            org.jhotdraw.draw.figure.Figure f = getOwner();
            f.willChange();
            f.setBounds(anchor, lead);
            f.changed();
        }
    }

    private static class NorthEastHandle extends org.jhotdraw.draw.handle.ResizeHandleKit.ResizeHandle {
        NorthEastHandle(org.jhotdraw.draw.figure.Figure owner) {
            super(owner, org.jhotdraw.draw.locator.RelativeLocator.northEast(true));
        }

        @java.lang.Override
        protected void trackStepNormalized(java.awt.geom.Point2D.Double p, boolean keepAspect) {
            double nx = java.lang.Math.max(sb.x + 1, p.x);
            double ny = java.lang.Math.min((sb.y + sb.height) - 1, p.y);
            if (keepAspect) {
                double nxx = ((sb.x + sb.width) - 1) + java.lang.Math.max(1, (sb.y - p.y) / aspectRatio);
                if (nxx >= p.x) {
                    nx = nxx;
                } else {
                    ny = (sb.y + sb.height) - java.lang.Math.max(1, (p.x - sb.x) * aspectRatio);
                }
            }
            setBounds(new java.awt.geom.Point2D.Double(sb.x, ny), new java.awt.geom.Point2D.Double(nx, sb.y + sb.height));
        }

        @java.lang.Override
        public void keyPressed(java.awt.event.KeyEvent evt) {
            if (!getOwner().isTransformable()) {
                evt.consume();
                return;
            }
            java.awt.geom.Rectangle2D.Double r = getOwner().getBounds();
            switch (evt.getKeyCode()) {
                case java.awt.event.KeyEvent.VK_UP :
                    setBounds(new java.awt.geom.Point2D.Double(r.x, r.y - 1), new java.awt.geom.Point2D.Double(r.x + r.width, r.y + r.height));
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_DOWN :
                    if (r.height > 1) {
                        setBounds(new java.awt.geom.Point2D.Double(r.x, r.y + 1), new java.awt.geom.Point2D.Double(r.x + r.width, r.y + r.height));
                    }
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_LEFT :
                    if (r.width > 1) {
                        setBounds(new java.awt.geom.Point2D.Double(r.x, r.y), new java.awt.geom.Point2D.Double((r.x + r.width) - 1, r.y + r.height));
                    }
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_RIGHT :
                    setBounds(new java.awt.geom.Point2D.Double(r.x, r.y), new java.awt.geom.Point2D.Double((r.x + r.width) + 1, r.y + r.height));
                    evt.consume();
                    break;
            }
        }

        @java.lang.Override
        public java.awt.Cursor getCursor() {
            return java.awt.Cursor.getPredefinedCursor(getOwner().isTransformable() ? java.awt.Cursor.NE_RESIZE_CURSOR : java.awt.Cursor.DEFAULT_CURSOR);
        }
    }

    private static class EastHandle extends org.jhotdraw.draw.handle.ResizeHandleKit.ResizeHandle {
        EastHandle(org.jhotdraw.draw.figure.Figure owner) {
            super(owner, org.jhotdraw.draw.locator.RelativeLocator.east(true));
        }

        @java.lang.Override
        protected void trackStepNormalized(java.awt.geom.Point2D.Double p, boolean keepAspect) {
            setBounds(new java.awt.geom.Point2D.Double(sb.x, sb.y), new java.awt.geom.Point2D.Double(java.lang.Math.max(sb.x + 1, p.x), sb.y + sb.height));
        }

        @java.lang.Override
        public void keyPressed(java.awt.event.KeyEvent evt) {
            if (!getOwner().isTransformable()) {
                evt.consume();
                return;
            }
            java.awt.geom.Rectangle2D.Double r = getOwner().getBounds();
            switch (evt.getKeyCode()) {
                case java.awt.event.KeyEvent.VK_UP :
                case java.awt.event.KeyEvent.VK_DOWN :
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_LEFT :
                    if (r.width > 1) {
                        setBounds(new java.awt.geom.Point2D.Double(r.x, r.y), new java.awt.geom.Point2D.Double((r.x + r.width) - 1, r.y + r.height));
                    }
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_RIGHT :
                    setBounds(new java.awt.geom.Point2D.Double(r.x, r.y), new java.awt.geom.Point2D.Double((r.x + r.width) + 1, r.y + r.height));
                    evt.consume();
                    break;
            }
        }

        @java.lang.Override
        public java.awt.Cursor getCursor() {
            return java.awt.Cursor.getPredefinedCursor(getOwner().isTransformable() ? java.awt.Cursor.E_RESIZE_CURSOR : java.awt.Cursor.DEFAULT_CURSOR);
        }
    }

    private static class NorthHandle extends org.jhotdraw.draw.handle.ResizeHandleKit.ResizeHandle {
        NorthHandle(org.jhotdraw.draw.figure.Figure owner) {
            super(owner, org.jhotdraw.draw.locator.RelativeLocator.north(true));
        }

        @java.lang.Override
        protected void trackStepNormalized(java.awt.geom.Point2D.Double p, boolean keepAspect) {
            setBounds(new java.awt.geom.Point2D.Double(sb.x, java.lang.Math.min((sb.y + sb.height) - 1, p.y)), new java.awt.geom.Point2D.Double(sb.x + sb.width, sb.y + sb.height));
        }

        @java.lang.Override
        public void keyPressed(java.awt.event.KeyEvent evt) {
            if (!getOwner().isTransformable()) {
                evt.consume();
                return;
            }
            java.awt.geom.Rectangle2D.Double r = getOwner().getBounds();
            switch (evt.getKeyCode()) {
                case java.awt.event.KeyEvent.VK_UP :
                    setBounds(new java.awt.geom.Point2D.Double(r.x, r.y - 1), new java.awt.geom.Point2D.Double(r.x + r.width, r.y + r.height));
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_DOWN :
                    if (r.height > 1) {
                        setBounds(new java.awt.geom.Point2D.Double(r.x, r.y + 1), new java.awt.geom.Point2D.Double(r.x + r.width, r.y + r.height));
                    }
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_LEFT :
                case java.awt.event.KeyEvent.VK_RIGHT :
                    evt.consume();
                    break;
            }
        }

        @java.lang.Override
        public java.awt.Cursor getCursor() {
            return java.awt.Cursor.getPredefinedCursor(getOwner().isTransformable() ? java.awt.Cursor.N_RESIZE_CURSOR : java.awt.Cursor.DEFAULT_CURSOR);
        }
    }

    private static class NorthWestHandle extends org.jhotdraw.draw.handle.ResizeHandleKit.ResizeHandle {
        NorthWestHandle(org.jhotdraw.draw.figure.Figure owner) {
            super(owner, org.jhotdraw.draw.locator.RelativeLocator.northWest(true));
        }

        @java.lang.Override
        protected void trackStepNormalized(java.awt.geom.Point2D.Double p, boolean keepAspect) {
            double nx = java.lang.Math.min((sb.x + sb.width) - 1, p.x);
            double ny = java.lang.Math.min((sb.y + sb.height) - 1, p.y);
            if (keepAspect) {
                double nxx = sb.x - java.lang.Math.max(1, (sb.y - p.y) / aspectRatio);
                if (nxx <= p.x) {
                    nx = nxx;
                } else {
                    ny = sb.y - java.lang.Math.max(1, (sb.x - p.x) * aspectRatio);
                }
            }
            setBounds(new java.awt.geom.Point2D.Double(nx, ny), new java.awt.geom.Point2D.Double(sb.x + sb.width, sb.y + sb.height));
        }

        @java.lang.Override
        public void keyPressed(java.awt.event.KeyEvent evt) {
            if (!getOwner().isTransformable()) {
                evt.consume();
                return;
            }
            java.awt.geom.Rectangle2D.Double r = getOwner().getBounds();
            switch (evt.getKeyCode()) {
                case java.awt.event.KeyEvent.VK_UP :
                    setBounds(new java.awt.geom.Point2D.Double(r.x, r.y - 1), new java.awt.geom.Point2D.Double(r.x + r.width, r.y + r.height));
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_DOWN :
                    if (r.height > 1) {
                        setBounds(new java.awt.geom.Point2D.Double(r.x, r.y + 1), new java.awt.geom.Point2D.Double(r.x + r.width, r.y + r.height));
                    }
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_LEFT :
                    setBounds(new java.awt.geom.Point2D.Double(r.x - 1, r.y), new java.awt.geom.Point2D.Double(r.x + r.width, r.y + r.height));
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_RIGHT :
                    if (r.width > 1) {
                        setBounds(new java.awt.geom.Point2D.Double(r.x + 1, r.y), new java.awt.geom.Point2D.Double(r.x + r.width, r.y + r.height));
                    }
                    evt.consume();
                    break;
            }
        }

        @java.lang.Override
        public java.awt.Cursor getCursor() {
            return java.awt.Cursor.getPredefinedCursor(getOwner().isTransformable() ? java.awt.Cursor.NW_RESIZE_CURSOR : java.awt.Cursor.DEFAULT_CURSOR);
        }
    }

    private static class SouthEastHandle extends org.jhotdraw.draw.handle.ResizeHandleKit.ResizeHandle {
        SouthEastHandle(org.jhotdraw.draw.figure.Figure owner) {
            super(owner, org.jhotdraw.draw.locator.RelativeLocator.southEast(true));
        }

        @java.lang.Override
        protected void trackStepNormalized(java.awt.geom.Point2D.Double p, boolean keepAspect) {
            double nx = java.lang.Math.max(sb.x + 1, p.x);
            double ny = java.lang.Math.max(sb.y + 1, p.y);
            if (keepAspect) {
                double nxx = sb.x + java.lang.Math.max(1, (p.y - sb.y) / aspectRatio);
                if (nxx >= p.x) {
                    nx = nxx;
                } else {
                    ny = sb.y + java.lang.Math.max(1, (p.x - sb.x) * aspectRatio);
                }
            }
            setBounds(new java.awt.geom.Point2D.Double(sb.x, sb.y), new java.awt.geom.Point2D.Double(nx, ny));
        }

        @java.lang.Override
        public void keyPressed(java.awt.event.KeyEvent evt) {
            if (!getOwner().isTransformable()) {
                evt.consume();
                return;
            }
            java.awt.geom.Rectangle2D.Double r = getOwner().getBounds();
            switch (evt.getKeyCode()) {
                case java.awt.event.KeyEvent.VK_UP :
                    if (r.height > 1) {
                        setBounds(new java.awt.geom.Point2D.Double(r.x, r.y), new java.awt.geom.Point2D.Double(r.x + r.width, (r.y + r.height) - 1));
                    }
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_DOWN :
                    setBounds(new java.awt.geom.Point2D.Double(r.x, r.y), new java.awt.geom.Point2D.Double(r.x + r.width, (r.y + r.height) + 1));
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_LEFT :
                    if (r.width > 1) {
                        setBounds(new java.awt.geom.Point2D.Double(r.x, r.y), new java.awt.geom.Point2D.Double((r.x + r.width) - 1, r.y + r.height));
                    }
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_RIGHT :
                    setBounds(new java.awt.geom.Point2D.Double(r.x, r.y), new java.awt.geom.Point2D.Double((r.x + r.width) + 1, r.y + r.height));
                    evt.consume();
                    break;
            }
        }

        @java.lang.Override
        public java.awt.Cursor getCursor() {
            return java.awt.Cursor.getPredefinedCursor(getOwner().isTransformable() ? java.awt.Cursor.SE_RESIZE_CURSOR : java.awt.Cursor.DEFAULT_CURSOR);
        }
    }

    private static class SouthHandle extends org.jhotdraw.draw.handle.ResizeHandleKit.ResizeHandle {
        SouthHandle(org.jhotdraw.draw.figure.Figure owner) {
            super(owner, org.jhotdraw.draw.locator.RelativeLocator.south(true));
        }

        @java.lang.Override
        protected void trackStepNormalized(java.awt.geom.Point2D.Double p, boolean keepAspect) {
            setBounds(new java.awt.geom.Point2D.Double(sb.x, sb.y), new java.awt.geom.Point2D.Double(sb.x + sb.width, java.lang.Math.max(sb.y + 1, p.y)));
        }

        @java.lang.Override
        public void keyPressed(java.awt.event.KeyEvent evt) {
            if (!getOwner().isTransformable()) {
                evt.consume();
                return;
            }
            java.awt.geom.Rectangle2D.Double r = getOwner().getBounds();
            switch (evt.getKeyCode()) {
                case java.awt.event.KeyEvent.VK_UP :
                    if (r.height > 1) {
                        setBounds(new java.awt.geom.Point2D.Double(r.x, r.y), new java.awt.geom.Point2D.Double(r.x + r.width, (r.y + r.height) - 1));
                    }
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_DOWN :
                    setBounds(new java.awt.geom.Point2D.Double(r.x, r.y), new java.awt.geom.Point2D.Double(r.x + r.width, (r.y + r.height) + 1));
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_LEFT :
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_RIGHT :
                    evt.consume();
                    break;
            }
        }

        @java.lang.Override
        public java.awt.Cursor getCursor() {
            return java.awt.Cursor.getPredefinedCursor(getOwner().isTransformable() ? java.awt.Cursor.S_RESIZE_CURSOR : java.awt.Cursor.DEFAULT_CURSOR);
        }
    }

    private static class SouthWestHandle extends org.jhotdraw.draw.handle.ResizeHandleKit.ResizeHandle {
        SouthWestHandle(org.jhotdraw.draw.figure.Figure owner) {
            super(owner, org.jhotdraw.draw.locator.RelativeLocator.southWest(true));
        }

        @java.lang.Override
        protected void trackStepNormalized(java.awt.geom.Point2D.Double p, boolean keepAspect) {
            double nx = java.lang.Math.min((sb.x + sb.width) - 1, p.x);
            double ny = java.lang.Math.max(sb.y + 1, p.y);
            if (keepAspect) {
                double nxx = (sb.x + sb.width) - java.lang.Math.max(1, (p.y - sb.y) / aspectRatio);
                if (nxx <= p.x) {
                    nx = nxx;
                } else {
                    ny = sb.y + java.lang.Math.max(1, (((sb.x + sb.width) - 1) - p.x) * aspectRatio);
                }
            }
            setBounds(new java.awt.geom.Point2D.Double(nx, sb.y), new java.awt.geom.Point2D.Double(sb.x + sb.width, ny));
        }

        @java.lang.Override
        public void keyPressed(java.awt.event.KeyEvent evt) {
            if (!getOwner().isTransformable()) {
                evt.consume();
                return;
            }
            java.awt.geom.Rectangle2D.Double r = getOwner().getBounds();
            switch (evt.getKeyCode()) {
                case java.awt.event.KeyEvent.VK_UP :
                    if (r.height > 1) {
                        setBounds(new java.awt.geom.Point2D.Double(r.x, r.y), new java.awt.geom.Point2D.Double(r.x + r.width, (r.y + r.height) - 1));
                    }
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_DOWN :
                    setBounds(new java.awt.geom.Point2D.Double(r.x, r.y), new java.awt.geom.Point2D.Double(r.x + r.width, (r.y + r.height) + 1));
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_LEFT :
                    setBounds(new java.awt.geom.Point2D.Double(r.x - 1, r.y), new java.awt.geom.Point2D.Double(r.x + r.width, r.y + r.height));
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_RIGHT :
                    if (r.width > 1) {
                        setBounds(new java.awt.geom.Point2D.Double(r.x + 1, r.y), new java.awt.geom.Point2D.Double(r.x + r.width, r.y + r.height));
                    }
                    evt.consume();
                    break;
            }
        }

        @java.lang.Override
        public java.awt.Cursor getCursor() {
            return java.awt.Cursor.getPredefinedCursor(getOwner().isTransformable() ? java.awt.Cursor.SW_RESIZE_CURSOR : java.awt.Cursor.DEFAULT_CURSOR);
        }
    }

    private static class WestHandle extends org.jhotdraw.draw.handle.ResizeHandleKit.ResizeHandle {
        WestHandle(org.jhotdraw.draw.figure.Figure owner) {
            super(owner, org.jhotdraw.draw.locator.RelativeLocator.west(true));
        }

        @java.lang.Override
        protected void trackStepNormalized(java.awt.geom.Point2D.Double p, boolean keepAspect) {
            setBounds(new java.awt.geom.Point2D.Double(java.lang.Math.min((sb.x + sb.width) - 1, p.x), sb.y), new java.awt.geom.Point2D.Double(sb.x + sb.width, sb.y + sb.height));
        }

        @java.lang.Override
        public void keyPressed(java.awt.event.KeyEvent evt) {
            if (!getOwner().isTransformable()) {
                evt.consume();
                return;
            }
            java.awt.geom.Rectangle2D.Double r = getOwner().getBounds();
            switch (evt.getKeyCode()) {
                case java.awt.event.KeyEvent.VK_UP :
                case java.awt.event.KeyEvent.VK_DOWN :
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_LEFT :
                    setBounds(new java.awt.geom.Point2D.Double(r.x - 1, r.y), new java.awt.geom.Point2D.Double(r.x + r.width, r.y + r.height));
                    evt.consume();
                    break;
                case java.awt.event.KeyEvent.VK_RIGHT :
                    if (r.width > 1) {
                        setBounds(new java.awt.geom.Point2D.Double(r.x + 1, r.y), new java.awt.geom.Point2D.Double(r.x + r.width, r.y + r.height));
                    }
                    evt.consume();
                    break;
            }
        }

        @java.lang.Override
        public java.awt.Cursor getCursor() {
            return java.awt.Cursor.getPredefinedCursor(getOwner().isTransformable() ? java.awt.Cursor.W_RESIZE_CURSOR : java.awt.Cursor.DEFAULT_CURSOR);
        }
    }
}