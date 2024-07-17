/* @(#)TaskFigure.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.pert.figures;
/**
 * TaskFigure.
 *
 * @author Werner Randelshofer.
 * @version $Id$
 */
public class TaskFigure extends org.jhotdraw.draw.figure.GraphicalCompositeFigure {
    private static final long serialVersionUID = 1L;

    private java.util.HashSet<org.jhotdraw.samples.pert.figures.DependencyFigure> dependencies;

    /**
     * This adapter is used, to connect a TextFigure with the name of the TaskFigure model.
     */
    private static class NameAdapter extends org.jhotdraw.draw.event.FigureListenerAdapter {
        private org.jhotdraw.samples.pert.figures.TaskFigure target;

        public NameAdapter(org.jhotdraw.samples.pert.figures.TaskFigure target) {
            this.target = target;
        }

        @java.lang.Override
        public void attributeChanged(org.jhotdraw.draw.event.FigureEvent e) {
            // We could fire a property change event here, in case
            // some other object would like to observe us.
            // target.firePropertyChange("name", e.getOldValue(), e.getNewValue());
        }
    }

    private static class DurationAdapter extends org.jhotdraw.draw.event.FigureListenerAdapter {
        private org.jhotdraw.samples.pert.figures.TaskFigure target;

        public DurationAdapter(org.jhotdraw.samples.pert.figures.TaskFigure target) {
            this.target = target;
        }

        @java.lang.Override
        public void attributeChanged(org.jhotdraw.draw.event.FigureEvent evt) {
            // We could fire a property change event here, in case
            // some other object would like to observe us.
            // target.firePropertyChange("duration", e.getOldValue(), e.getNewValue());
            for (org.jhotdraw.samples.pert.figures.TaskFigure succ : target.getSuccessors()) {
                succ.updateStartTime();
            }
        }
    }

    public TaskFigure() {
        super(new org.jhotdraw.draw.figure.RectangleFigure());
        setLayouter(new org.jhotdraw.draw.layouter.VerticalLayouter());
        org.jhotdraw.draw.figure.RectangleFigure nameCompartmentPF = new org.jhotdraw.draw.figure.RectangleFigure();
        nameCompartmentPF.attr().set(org.jhotdraw.draw.AttributeKeys.STROKE_COLOR, null);
        nameCompartmentPF.attr().setAttributeEnabled(org.jhotdraw.draw.AttributeKeys.STROKE_COLOR, false);
        nameCompartmentPF.attr().set(org.jhotdraw.draw.AttributeKeys.FILL_COLOR, null);
        nameCompartmentPF.attr().setAttributeEnabled(org.jhotdraw.draw.AttributeKeys.FILL_COLOR, false);
        org.jhotdraw.draw.figure.ListFigure nameCompartment = new org.jhotdraw.draw.figure.ListFigure(nameCompartmentPF);
        org.jhotdraw.draw.figure.ListFigure attributeCompartment = new org.jhotdraw.draw.figure.ListFigure();
        org.jhotdraw.samples.pert.figures.SeparatorLineFigure separator1 = new org.jhotdraw.samples.pert.figures.SeparatorLineFigure();
        add(nameCompartment);
        add(separator1);
        add(attributeCompartment);
        org.jhotdraw.geom.Insets2D.Double insets = new org.jhotdraw.geom.Insets2D.Double(4, 8, 4, 8);
        nameCompartment.attr().set(org.jhotdraw.draw.figure.CompositeFigure.LAYOUT_INSETS, insets);
        attributeCompartment.attr().set(org.jhotdraw.draw.figure.CompositeFigure.LAYOUT_INSETS, insets);
        org.jhotdraw.draw.figure.TextFigure nameFigure;
        nameCompartment.add(nameFigure = new org.jhotdraw.draw.figure.TextFigure());
        nameFigure.attr().set(org.jhotdraw.draw.AttributeKeys.FONT_BOLD, true);
        nameFigure.attr().setAttributeEnabled(org.jhotdraw.draw.AttributeKeys.FONT_BOLD, false);
        org.jhotdraw.draw.figure.TextFigure durationFigure;
        attributeCompartment.add(durationFigure = new org.jhotdraw.draw.figure.TextFigure());
        durationFigure.attr().set(org.jhotdraw.draw.AttributeKeys.FONT_BOLD, true);
        durationFigure.setText("0");
        durationFigure.attr().setAttributeEnabled(org.jhotdraw.draw.AttributeKeys.FONT_BOLD, false);
        org.jhotdraw.draw.figure.TextFigure startTimeFigure;
        attributeCompartment.add(startTimeFigure = new org.jhotdraw.draw.figure.TextFigure());
        startTimeFigure.setEditable(false);
        startTimeFigure.setText("0");
        startTimeFigure.attr().setAttributeEnabled(org.jhotdraw.draw.AttributeKeys.FONT_BOLD, false);
        attr().setAttributeEnabled(org.jhotdraw.draw.AttributeKeys.STROKE_DASHES, false);
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.samples.pert.Labels");
        setName(labels.getString("pert.task.defaultName"));
        setDuration(0);
        dependencies = new java.util.HashSet<org.jhotdraw.samples.pert.figures.DependencyFigure>();
        nameFigure.addFigureListener(new org.jhotdraw.samples.pert.figures.TaskFigure.NameAdapter(this));
        durationFigure.addFigureListener(new org.jhotdraw.samples.pert.figures.TaskFigure.DurationAdapter(this));
    }

    @java.lang.Override
    public java.util.Collection<org.jhotdraw.draw.handle.Handle> createHandles(int detailLevel) {
        java.util.List<org.jhotdraw.draw.handle.Handle> handles = new java.util.LinkedList<org.jhotdraw.draw.handle.Handle>();
        switch (detailLevel) {
            case -1 :
                handles.add(new org.jhotdraw.draw.handle.BoundsOutlineHandle(getPresentationFigure(), false, true));
                break;
            case 0 :
                handles.add(new org.jhotdraw.draw.handle.MoveHandle(this, org.jhotdraw.draw.locator.RelativeLocator.northWest()));
                handles.add(new org.jhotdraw.draw.handle.MoveHandle(this, org.jhotdraw.draw.locator.RelativeLocator.northEast()));
                handles.add(new org.jhotdraw.draw.handle.MoveHandle(this, org.jhotdraw.draw.locator.RelativeLocator.southWest()));
                handles.add(new org.jhotdraw.draw.handle.MoveHandle(this, org.jhotdraw.draw.locator.RelativeLocator.southEast()));
                org.jhotdraw.draw.handle.ConnectorHandle ch;
                handles.add(ch = new org.jhotdraw.draw.handle.ConnectorHandle(new org.jhotdraw.draw.connector.LocatorConnector(this, org.jhotdraw.draw.locator.RelativeLocator.east()), new org.jhotdraw.samples.pert.figures.DependencyFigure()));
                ch.setToolTipText("Drag the connector to a dependent task.");
                break;
        }
        return handles;
    }

    public void setName(java.lang.String newValue) {
        getNameFigure().setText(newValue);
    }

    public java.lang.String getName() {
        return getNameFigure().getText();
    }

    public void setDuration(int newValue) {
        int oldValue = getDuration();
        getDurationFigure().setText(java.lang.Integer.toString(newValue));
        if (oldValue != newValue) {
            for (org.jhotdraw.samples.pert.figures.TaskFigure succ : getSuccessors()) {
                succ.updateStartTime();
            }
        }
    }

    public int getDuration() {
        try {
            return java.lang.Integer.valueOf(getDurationFigure().getText());
        } catch (java.lang.NumberFormatException e) {
            return 0;
        }
    }

    public void updateStartTime() {
        willChange();
        int oldValue = getStartTime();
        int newValue = 0;
        for (org.jhotdraw.samples.pert.figures.TaskFigure pre : getPredecessors()) {
            newValue = java.lang.Math.max(newValue, pre.getStartTime() + pre.getDuration());
        }
        getStartTimeFigure().setText(java.lang.Integer.toString(newValue));
        if (newValue != oldValue) {
            for (org.jhotdraw.samples.pert.figures.TaskFigure succ : getSuccessors()) {
                // The if-statement here guards against
                // cyclic task dependencies.
                if (!this.isDependentOf(succ)) {
                    succ.updateStartTime();
                }
            }
        }
        changed();
    }

    public int getStartTime() {
        try {
            return java.lang.Integer.valueOf(getStartTimeFigure().getText());
        } catch (java.lang.NumberFormatException e) {
            return 0;
        }
    }

    private org.jhotdraw.draw.figure.TextFigure getNameFigure() {
        return ((org.jhotdraw.draw.figure.TextFigure) (((org.jhotdraw.draw.figure.ListFigure) (getChild(0))).getChild(0)));
    }

    private org.jhotdraw.draw.figure.TextFigure getDurationFigure() {
        return ((org.jhotdraw.draw.figure.TextFigure) (((org.jhotdraw.draw.figure.ListFigure) (getChild(2))).getChild(0)));
    }

    private org.jhotdraw.draw.figure.TextFigure getStartTimeFigure() {
        return ((org.jhotdraw.draw.figure.TextFigure) (((org.jhotdraw.draw.figure.ListFigure) (getChild(2))).getChild(1)));
    }

    @java.lang.Override
    public org.jhotdraw.samples.pert.figures.TaskFigure clone() {
        org.jhotdraw.samples.pert.figures.TaskFigure that = ((org.jhotdraw.samples.pert.figures.TaskFigure) (super.clone()));
        that.dependencies = new java.util.HashSet<org.jhotdraw.samples.pert.figures.DependencyFigure>();
        that.getNameFigure().addFigureListener(new org.jhotdraw.samples.pert.figures.TaskFigure.NameAdapter(that));
        that.getDurationFigure().addFigureListener(new org.jhotdraw.samples.pert.figures.TaskFigure.DurationAdapter(that));
        that.updateStartTime();
        return that;
    }

    // @Override
    // public void read(DOMInput in) throws IOException {
    // double x = in.getAttribute("x", 0d);
    // double y = in.getAttribute("y", 0d);
    // double w = in.getAttribute("w", 0d);
    // double h = in.getAttribute("h", 0d);
    // setBounds(new Point2D.Double(x, y), new Point2D.Double(x + w, y + h));
    // readAttributes(in);
    // in.openElement("model");
    // in.openElement("name");
    // setName((String) in.readObject());
    // in.closeElement();
    // in.openElement("duration");
    // setDuration((Integer) in.readObject());
    // in.closeElement();
    // in.closeElement();
    // }
    // 
    // @Override
    // public void write(DOMOutput out) throws IOException {
    // Rectangle2D.Double r = getBounds();
    // out.addAttribute("x", r.x);
    // out.addAttribute("y", r.y);
    // writeAttributes(out);
    // out.openElement("model");
    // out.openElement("name");
    // out.writeObject(getName());
    // out.closeElement();
    // out.openElement("duration");
    // out.writeObject(getDuration());
    // out.closeElement();
    // out.closeElement();
    // }
    @java.lang.Override
    public int getLayer() {
        return 0;
    }

    public java.util.Set<org.jhotdraw.samples.pert.figures.DependencyFigure> getDependencies() {
        return java.util.Collections.unmodifiableSet(dependencies);
    }

    public void addDependency(org.jhotdraw.samples.pert.figures.DependencyFigure f) {
        dependencies.add(f);
        updateStartTime();
    }

    public void removeDependency(org.jhotdraw.samples.pert.figures.DependencyFigure f) {
        dependencies.remove(f);
        updateStartTime();
    }

    /**
     * Returns dependent PertTasks which are directly connected via a PertDependency to this
     * TaskFigure.
     */
    public java.util.List<org.jhotdraw.samples.pert.figures.TaskFigure> getSuccessors() {
        java.util.LinkedList<org.jhotdraw.samples.pert.figures.TaskFigure> list = new java.util.LinkedList<org.jhotdraw.samples.pert.figures.TaskFigure>();
        for (org.jhotdraw.samples.pert.figures.DependencyFigure c : getDependencies()) {
            if (c.getStartFigure() == this) {
                list.add(((org.jhotdraw.samples.pert.figures.TaskFigure) (c.getEndFigure())));
            }
        }
        return list;
    }

    /**
     * Returns predecessor PertTasks which are directly connected via a PertDependency to this
     * TaskFigure.
     */
    public java.util.List<org.jhotdraw.samples.pert.figures.TaskFigure> getPredecessors() {
        java.util.LinkedList<org.jhotdraw.samples.pert.figures.TaskFigure> list = new java.util.LinkedList<org.jhotdraw.samples.pert.figures.TaskFigure>();
        for (org.jhotdraw.samples.pert.figures.DependencyFigure c : getDependencies()) {
            if (c.getEndFigure() == this) {
                list.add(((org.jhotdraw.samples.pert.figures.TaskFigure) (c.getStartFigure())));
            }
        }
        return list;
    }

    /**
     * Returns true, if the current task is a direct or indirect dependent of the specified task. If
     * the dependency is cyclic, then this method returns true if <code>this</code> is passed as a
     * parameter and for every other task in the cycle.
     */
    public boolean isDependentOf(org.jhotdraw.samples.pert.figures.TaskFigure t) {
        if (this == t) {
            return true;
        }
        for (org.jhotdraw.samples.pert.figures.TaskFigure pre : getPredecessors()) {
            if (pre.isDependentOf(t)) {
                return true;
            }
        }
        return false;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((((("TaskFigure#" + hashCode()) + " ") + getName()) + " ") + getDuration()) + " ") + getStartTime();
    }
}