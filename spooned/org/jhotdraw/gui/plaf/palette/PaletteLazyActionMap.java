/**
 *
 * @(#)QuaquaLazyActionMap.java <p>Copyright (c) 2008 The authors and contributors of JHotDraw. You may not use, copy or modify
this file, except in compliance with the accompanying license terms.
 */
package org.jhotdraw.gui.plaf.palette;
/**
 * QuaquaLazyActionMap.
 */
public class PaletteLazyActionMap extends javax.swing.plaf.ActionMapUIResource {
    private static final long serialVersionUID = 1L;

    /**
     * Object to invoke <code>loadActionMap</code> on. This may be a Class object.
     */
    private transient java.lang.Object loader;

    /**
     * Installs an ActionMap that will be populated by invoking the <code>loadActionMap</code> method
     * on the specified Class when necessary.
     *
     * <p>This should be used if the ActionMap can be shared.
     *
     * @param c
     * 		JComponent to install the ActionMap on.
     * @param loaderClass
     * 		Class object that gets loadActionMap invoked on.
     * @param defaultsKey
     * 		Key to use to defaults table to check for existing map and what resulting
     * 		Map will be registered on.
     */
    static void installLazyActionMap(javax.swing.JComponent c, java.lang.Class<?> loaderClass, java.lang.String defaultsKey) {
        javax.swing.ActionMap map = ((javax.swing.ActionMap) (javax.swing.UIManager.get(defaultsKey)));
        if (map == null) {
            map = new org.jhotdraw.gui.plaf.palette.PaletteLazyActionMap(loaderClass);
            javax.swing.UIManager.getLookAndFeelDefaults().put(defaultsKey, map);
        }
        javax.swing.SwingUtilities.replaceUIActionMap(c, map);
    }

    /**
     * Returns an ActionMap that will be populated by invoking the <code>loadActionMap</code> method
     * on the specified Class when necessary.
     *
     * <p>This should be used if the ActionMap can be shared.
     *
     * @param loaderClass
     * 		Class object that gets loadActionMap invoked on.
     * @param defaultsKey
     * 		Key to use to defaults table to check for existing map and what resulting
     * 		Map will be registered on.
     */
    static javax.swing.ActionMap getActionMap(java.lang.Class<?> loaderClass, java.lang.String defaultsKey) {
        javax.swing.ActionMap map = ((javax.swing.ActionMap) (javax.swing.UIManager.get(defaultsKey)));
        if (map == null) {
            map = new org.jhotdraw.gui.plaf.palette.PaletteLazyActionMap(loaderClass);
            javax.swing.UIManager.getLookAndFeelDefaults().put(defaultsKey, map);
        }
        return map;
    }

    private PaletteLazyActionMap(java.lang.Class<?> loader) {
        this.loader = loader;
    }

    public void put(javax.swing.Action action) {
        put(action.getValue(javax.swing.Action.NAME), action);
    }

    @java.lang.Override
    public void put(java.lang.Object key, javax.swing.Action action) {
        loadIfNecessary();
        super.put(key, action);
    }

    @java.lang.Override
    public javax.swing.Action get(java.lang.Object key) {
        loadIfNecessary();
        return super.get(key);
    }

    @java.lang.Override
    public void remove(java.lang.Object key) {
        loadIfNecessary();
        super.remove(key);
    }

    @java.lang.Override
    public void clear() {
        loadIfNecessary();
        super.clear();
    }

    @java.lang.Override
    public java.lang.Object[] keys() {
        loadIfNecessary();
        return super.keys();
    }

    @java.lang.Override
    public int size() {
        loadIfNecessary();
        return super.size();
    }

    @java.lang.Override
    public java.lang.Object[] allKeys() {
        loadIfNecessary();
        return super.allKeys();
    }

    @java.lang.Override
    public void setParent(javax.swing.ActionMap map) {
        loadIfNecessary();
        super.setParent(map);
    }

    @java.lang.SuppressWarnings("unchecked")
    private void loadIfNecessary() {
        if (loader != null) {
            java.lang.Object ldr = loader;
            loader = null;
            java.lang.Class<?> klass = ((java.lang.Class) (ldr));
            try {
                java.lang.reflect.Method method = klass.getDeclaredMethod("loadActionMap", new java.lang.Class<?>[]{ org.jhotdraw.gui.plaf.palette.PaletteLazyActionMap.class });
                method.invoke(klass, new java.lang.Object[]{ this });
            } catch (java.lang.NoSuchMethodException nsme) {
                assert false : "LazyActionMap unable to load actions " + klass;
            } catch (java.lang.IllegalAccessException | java.lang.reflect.InvocationTargetException | java.lang.IllegalArgumentException iae) {
                assert false : "LazyActionMap unable to load actions " + iae;
            }
        }
    }
}