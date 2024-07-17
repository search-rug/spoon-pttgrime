/* @(#)Arrangeable.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.api.gui;
/**
 * Arrangeable.
 */
public interface Arrangeable {
    enum Arrangement {

        VERTICAL,
        HORIZONTAL,
        CASCADE;
    }

    public void setArrangement(org.jhotdraw.api.gui.Arrangeable.Arrangement newValue);

    public org.jhotdraw.api.gui.Arrangeable.Arrangement getArrangement();

    public void addPropertyChangeListener(java.beans.PropertyChangeListener l);

    public void removePropertyChangeListener(java.beans.PropertyChangeListener l);
}