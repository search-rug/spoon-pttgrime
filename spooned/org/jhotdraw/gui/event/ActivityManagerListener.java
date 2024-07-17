/* @(#)ActivityManagerListener.java

Copyright (c) 2011 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
license agreement you entered into with the copyright holders. For details
see accompanying license terms.
 */
package org.jhotdraw.gui.event;
/**
 * Interface for listeners on the {@code ActivityManager}.
 *
 * @author Werner Randelshofer
 * @version 1.0 2011-09-08 Created.
 */
public interface ActivityManagerListener extends java.util.EventListener {
    public void activityModelAdded(org.jhotdraw.gui.event.ActivityManagerEvent evt);

    public void activityModelRemoved(org.jhotdraw.gui.event.ActivityManagerEvent evt);
}