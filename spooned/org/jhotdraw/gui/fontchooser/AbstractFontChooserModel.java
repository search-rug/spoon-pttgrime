/**
 *
 * @(#)AbstractFontChooserModel.java <p>Copyright (c) 2008 The authors and contributors of JHotDraw. You may not use, copy or modify
this file, except in compliance with the accompanying license terms.
 */
package org.jhotdraw.gui.fontchooser;
/**
 * AbstractFontChooserModel.
 */
public abstract class AbstractFontChooserModel implements org.jhotdraw.gui.fontchooser.FontChooserModel {
    /**
     * Listeners.
     */
    protected javax.swing.event.EventListenerList listenerList = new javax.swing.event.EventListenerList();

    // Events
    /**
     * Adds a listener for the TreeModelEvent posted after the tree changes.
     *
     * @see #removeTreeModelListener
     * @param l
     * 		the listener to add
     */
    @java.lang.Override
    public void addTreeModelListener(javax.swing.event.TreeModelListener l) {
        listenerList.add(javax.swing.event.TreeModelListener.class, l);
    }

    /**
     * Removes a listener previously added with <B>addTreeModelListener()</B>.
     *
     * @see #addTreeModelListener
     * @param l
     * 		the listener to remove
     */
    @java.lang.Override
    public void removeTreeModelListener(javax.swing.event.TreeModelListener l) {
        listenerList.remove(javax.swing.event.TreeModelListener.class, l);
    }

    /**
     * Returns an array of all the tree model listeners registered on this model.
     *
     * @return all of this model's <code>TreeModelListener</code>s or an empty array if no tree model
    listeners are currently registered
     * @see #addTreeModelListener
     * @see #removeTreeModelListener
     * @since 1.4
     */
    public javax.swing.event.TreeModelListener[] getTreeModelListeners() {
        return listenerList.getListeners(javax.swing.event.TreeModelListener.class);
    }

    /**
     * Notifies all listeners that have registered interest for notification on this event type. The
     * event instance is lazily created using the parameters passed into the fire method.
     *
     * @param source
     * 		the node being changed
     * @param path
     * 		the path to the root node
     * @param childIndices
     * 		the indices of the changed elements
     * @param children
     * 		the changed elements
     * @see EventListenerList
     */
    protected void fireTreeNodesChanged(java.lang.Object source, java.lang.Object[] path, int[] childIndices, java.lang.Object[] children) {
        // Guaranteed to return a non-null array
        java.lang.Object[] listeners = listenerList.getListenerList();
        javax.swing.event.TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == javax.swing.event.TreeModelListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e = new javax.swing.event.TreeModelEvent(source, path, childIndices, children);
                }
                ((javax.swing.event.TreeModelListener) (listeners[i + 1])).treeNodesChanged(e);
            }
        }
    }

    /**
     * Notifies all listeners that have registered interest for notification on this event type. The
     * event instance is lazily created using the parameters passed into the fire method.
     *
     * @param source
     * 		the node where new elements are being inserted
     * @param path
     * 		the path to the root node
     * @param childIndices
     * 		the indices of the new elements
     * @param children
     * 		the new elements
     * @see EventListenerList
     */
    protected void fireTreeNodesInserted(java.lang.Object source, java.lang.Object[] path, int[] childIndices, java.lang.Object[] children) {
        // Guaranteed to return a non-null array
        java.lang.Object[] listeners = listenerList.getListenerList();
        javax.swing.event.TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == javax.swing.event.TreeModelListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e = new javax.swing.event.TreeModelEvent(source, path, childIndices, children);
                }
                ((javax.swing.event.TreeModelListener) (listeners[i + 1])).treeNodesInserted(e);
            }
        }
    }

    /**
     * Notifies all listeners that have registered interest for notification on this event type. The
     * event instance is lazily created using the parameters passed into the fire method.
     *
     * @param source
     * 		the node where elements are being removed
     * @param path
     * 		the path to the root node
     * @param childIndices
     * 		the indices of the removed elements
     * @param children
     * 		the removed elements
     * @see EventListenerList
     */
    protected void fireTreeNodesRemoved(java.lang.Object source, java.lang.Object[] path, int[] childIndices, java.lang.Object[] children) {
        // Guaranteed to return a non-null array
        java.lang.Object[] listeners = listenerList.getListenerList();
        javax.swing.event.TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == javax.swing.event.TreeModelListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e = new javax.swing.event.TreeModelEvent(source, path, childIndices, children);
                }
                ((javax.swing.event.TreeModelListener) (listeners[i + 1])).treeNodesRemoved(e);
            }
        }
    }

    /**
     * Notifies all listeners that have registered interest for notification on this event type. The
     * event instance is lazily created using the parameters passed into the fire method.
     *
     * @param source
     * 		the node where the tree model has changed
     * @param path
     * 		the path to the root node
     * @param childIndices
     * 		the indices of the affected elements
     * @param children
     * 		the affected elements
     * @see EventListenerList
     */
    protected void fireTreeStructureChanged(java.lang.Object source, java.lang.Object[] path, int[] childIndices, java.lang.Object[] children) {
        // Guaranteed to return a non-null array
        java.lang.Object[] listeners = listenerList.getListenerList();
        javax.swing.event.TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == javax.swing.event.TreeModelListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e = new javax.swing.event.TreeModelEvent(source, path, childIndices, children);
                }
                ((javax.swing.event.TreeModelListener) (listeners[i + 1])).treeStructureChanged(e);
            }
        }
    }

    /* Notifies all listeners that have registered interest for
    notification on this event type.  The event instance
    is lazily created using the parameters passed into
    the fire method.

    @param source the node where the tree model has changed
    @param path the path to the root node
    @see EventListenerList
     */
    protected void fireTreeStructureChanged(java.lang.Object source, javax.swing.tree.TreePath path) {
        // Guaranteed to return a non-null array
        java.lang.Object[] listeners = listenerList.getListenerList();
        javax.swing.event.TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == javax.swing.event.TreeModelListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e = new javax.swing.event.TreeModelEvent(source, path);
                }
                ((javax.swing.event.TreeModelListener) (listeners[i + 1])).treeStructureChanged(e);
            }
        }
    }

    /**
     * Returns an array of all the objects currently registered as <code><em>Foo</em>Listener</code>s
     * upon this model. <code><em>Foo</em>Listener</code>s are registered using the <code>
     * add<em>Foo</em>Listener</code> method.
     *
     * <p>You can specify the <code>listenerType</code> argument with a class literal, such as <code>
     * <em>Foo</em>Listener.class</code>. For example, you can query a <code>DefaultTreeModel</code>
     * <code>m</code> for its tree model listeners with the following code:
     *
     * <pre>TreeModelListener[] tmls = (TreeModelListener[])(m.getListeners(TreeModelListener.class));
     * </pre>
     *
     * If no such listeners exist, this method returns an empty array.
     *
     * @param listenerType
     * 		the type of listeners requested; this parameter should specify an interface
     * 		that descends from <code>java.util.EventListener</code>
     * @return an array of all objects registered as <code><em>Foo</em>Listener</code>s on this
    component, or an empty array if no such listeners have been added
     * @exception ClassCastException
     * 		if <code>listenerType</code> doesn't specify a class or interface
     * 		that implements <code>java.util.EventListener</code>
     * @see #getTreeModelListeners
     * @since 1.3
     */
    public <T extends java.util.EventListener> T[] getListeners(java.lang.Class<T> listenerType) {
        return listenerList.getListeners(listenerType);
    }
}