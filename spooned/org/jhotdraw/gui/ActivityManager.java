/* @(#)ActivityManager.java

Copyright (c) 2011 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
license agreement you entered into with the copyright holders. For details
see accompanying license terms.
 */
package org.jhotdraw.gui;
import org.jhotdraw.api.gui.ActivityModel;
/**
 * The activity manager keeps track of all active {@code ActivityModel} objects.
 *
 * <p><hr> <b>Design Patterns</b>
 *
 * <p><em>Framework</em><br>
 * The interfaces and classes listed below define a framework for progress management.<br>
 * Contract: {@link ActivityManager}, {@link ActivityModel}, {@link JActivityWindow}, {@link JActivityIndicator}.
 *
 * @author Werner Randelshofer
 * @version 1.0 2011-09-07 Created.
 */
public class ActivityManager {
    private static org.jhotdraw.gui.ActivityManager instance;

    public static synchronized org.jhotdraw.gui.ActivityManager getInstance() {
        if (org.jhotdraw.gui.ActivityManager.instance == null) {
            org.jhotdraw.gui.ActivityManager.instance = new org.jhotdraw.gui.ActivityManager();
        }
        return org.jhotdraw.gui.ActivityManager.instance;
    }

    private java.util.ArrayList<org.jhotdraw.api.gui.ActivityModel> models = new java.util.ArrayList<>();

    private java.util.ArrayList<org.jhotdraw.gui.event.ActivityManagerListener> listeners = new java.util.ArrayList<>();

    /**
     * Adds a listener to the progress manager.
     */
    public synchronized void addActivityManagerListener(org.jhotdraw.gui.event.ActivityManagerListener l) {
        listeners.add(l);
    }

    /**
     * Removes a listener from the progress manager.
     */
    public synchronized void removeActivityManagerListener(org.jhotdraw.gui.event.ActivityManagerListener l) {
        listeners.remove(l);
    }

    /**
     * Adds a progress model to the manager. This method is thread safe.
     */
    public void add(final org.jhotdraw.api.gui.ActivityModel pm) {
        org.jhotdraw.gui.ActivityManager.invokeAndWait(new java.lang.Runnable() {
            @java.lang.Override
            @java.lang.SuppressWarnings("unchecked")
            public void run() {
                if (models.add(pm)) {
                    java.util.ArrayList<org.jhotdraw.gui.event.ActivityManagerListener> ls;
                    synchronized(ActivityManager.this) {
                        ls = ((java.util.ArrayList<org.jhotdraw.gui.event.ActivityManagerListener>) (listeners.clone()));
                    }
                    org.jhotdraw.gui.event.ActivityManagerEvent evt = new org.jhotdraw.gui.event.ActivityManagerEvent(ActivityManager.this, pm);
                    for (org.jhotdraw.gui.event.ActivityManagerListener l : ls) {
                        l.activityModelAdded(evt);
                    }
                }
            }
        });
    }

    /**
     * Removes a progress model from the manager. This method is thread safe.
     */
    public void remove(final org.jhotdraw.api.gui.ActivityModel pm) {
        org.jhotdraw.gui.ActivityManager.invokeAndWait(new java.lang.Runnable() {
            @java.lang.Override
            @java.lang.SuppressWarnings("unchecked")
            public void run() {
                if (models.remove(pm)) {
                    java.util.ArrayList<org.jhotdraw.gui.event.ActivityManagerListener> ls;
                    synchronized(ActivityManager.this) {
                        ls = ((java.util.ArrayList<org.jhotdraw.gui.event.ActivityManagerListener>) (listeners.clone()));
                    }
                    org.jhotdraw.gui.event.ActivityManagerEvent evt = new org.jhotdraw.gui.event.ActivityManagerEvent(ActivityManager.this, pm);
                    for (org.jhotdraw.gui.event.ActivityManagerListener l : ls) {
                        l.activityModelRemoved(evt);
                    }
                }
            }
        });
    }

    @java.lang.SuppressWarnings("unchecked")
    public java.util.ArrayList<org.jhotdraw.api.gui.ActivityModel> getActivityModels() {
        return ((java.util.ArrayList<org.jhotdraw.api.gui.ActivityModel>) (models.clone()));
    }

    /**
     * Invokes a runnable on the EDT and wait until it is finished. FIXME - Maybe move this into a
     * utility class?
     */
    protected static void invokeAndWait(java.lang.Runnable r) {
        if (javax.swing.SwingUtilities.isEventDispatchThread()) {
            r.run();
        } else {
            try {
                javax.swing.SwingUtilities.invokeAndWait(r);
            } catch (java.lang.InterruptedException | java.lang.reflect.InvocationTargetException ex) {
                ex.printStackTrace();
            }
        }
    }
}