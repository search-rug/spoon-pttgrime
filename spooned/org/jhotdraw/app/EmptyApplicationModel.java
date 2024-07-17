/* @(#)EmptyApplicationModel.java

Copyright (c) 2009-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.app;
/**
 * An {@link ApplicationModel} which neither creates {@code Action}s, nor overrides the menu bars,
 * nor creates tool bars.
 *
 * <p>The {@code createActionMap} method of this model returns an empty ActionMap.
 *
 * @author Werner Randelshofer.
 * @version $Id$
 */
public class EmptyApplicationModel extends org.jhotdraw.app.AbstractApplicationModel {
    private static final long serialVersionUID = 1L;

    /**
     * Returns an empty ActionMap.
     */
    @java.lang.Override
    public javax.swing.ActionMap createActionMap(org.jhotdraw.api.app.Application a, org.jhotdraw.api.app.View v) {
        return new javax.swing.ActionMap();
    }

    /**
     * Returns an empty unmodifiable list.
     */
    @java.lang.Override
    public java.util.List<javax.swing.JToolBar> createToolBars(org.jhotdraw.api.app.Application app, org.jhotdraw.api.app.View v) {
        return java.util.Collections.emptyList();
    }

    @java.lang.Override
    public org.jhotdraw.api.app.MenuBuilder getMenuBuilder() {
        return new org.jhotdraw.app.EmptyMenuBuilder();
    }
}