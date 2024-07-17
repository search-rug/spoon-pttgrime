/* @(#)AbstractLocator.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.draw.locator;
/**
 * This abstract class can be extended to implement a {@link Locator}.
 */
public abstract class AbstractLocator implements org.jhotdraw.draw.locator.Locator , java.io.Serializable {
    private static final long serialVersionUID = 1L;

    public AbstractLocator() {
    }

    @java.lang.Override
    public org.jhotdraw.draw.locator.Locator.Position locate(org.jhotdraw.draw.figure.Figure owner, org.jhotdraw.draw.figure.Figure dependent, double scale) {
        return locate(owner, scale);
    }
}