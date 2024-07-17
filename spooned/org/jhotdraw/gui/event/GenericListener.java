/* @(#)GenericListener.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.gui.event;
/**
 * The GenericListener creates anonymous listener classes at runtime.
 *
 * <p>Usage:
 *
 * <pre>
 * public class Demo {
 *   JPanel root = new JPanel(new BorderLayout());
 *   JLabel label = new JLabel(" ");
 *
 *   public void myButtonAction(ActionEvent e) {
 *     label.setText("buttonAction");
 *   }
 *
 *   public void myMouseEntered(MouseEvent e) {
 *     label.setText("mouseEntered: "+e.toString());
 *   }
 *
 *   Demo() {
 *     JButton button = new JButton("Button with Dynamic Listener");
 *
 *     //This listener will be generated at run-time, for example at run-time
 *     // an ActionListener class will be code-generated and then
 *     // class-loaded.  Only one of these is actually created, even
 *     // if many calls to GenericListener.create(ActionListener.class ...)
 *     // are made.
 *     ActionListener actionListener = (ActionListener)(GenericListener.create(
 *       ActionListener.class,
 *       "actionPerformed",
 *       this,
 *       "myButtonAction")
 *     );
 *     button.addActionListener(actionListener);
 *
 *     // Here's another dynamically generated listener.  This one is
 *     // a little different because the listenerMethod argument actually
 *     // specifies one of many listener methods.  In the previous example
 *     // "actionPerformed" named the one and only ActionListener method.
 *     MouseListener mouseListener = (MouseListener)(GenericListener.create(
 *       MouseListener.class,
 *       "mouseEntered",
 *       this,
 *       "myMouseEntered")
 *     );
 *     button.addMouseListener(mouseListener);
 * </pre>
 */
public abstract class GenericListener {
    /**
     * A convenient version of <code>create(listenerMethod, targetObject, targetMethod)</code>. This
     * version looks up the listener and target Methods, so you don't have to.
     */
    public static java.lang.Object create(java.lang.Class<?> listenerInterface, java.lang.String listenerMethodName, java.lang.Object target, java.lang.String targetMethodName) {
        java.lang.reflect.Method listenerMethod = org.jhotdraw.gui.event.GenericListener.getListenerMethod(listenerInterface, listenerMethodName);
        // Search a target method with the same parameter types as the listener method.
        java.lang.reflect.Method targetMethod = org.jhotdraw.gui.event.GenericListener.getTargetMethod(target, targetMethodName, listenerMethod.getParameterTypes());
        // Nothing found? Search a target method with no parameters
        if (targetMethod == null) {
            targetMethod = org.jhotdraw.gui.event.GenericListener.getTargetMethod(target, targetMethodName, new java.lang.Class<?>[0]);
        }
        // Still nothing found? We give up.
        if (targetMethod == null) {
            throw new java.lang.RuntimeException((("no such method " + targetMethodName) + " in ") + target.getClass());
        }
        return org.jhotdraw.gui.event.GenericListener.create(listenerMethod, target, targetMethod);
    }

    /**
     * Return an instance of a class that implements the interface that contains the declaration for
     * <code>listenerMethod</code>. In this new class, <code>listenerMethod</code> will apply <code>
     * target.targetMethod</code> to the incoming Event.
     */
    public static java.lang.Object create(final java.lang.reflect.Method listenerMethod, final java.lang.Object target, final java.lang.reflect.Method targetMethod) {
        /**
         * The implementation of the create method uses the Dynamic Proxy API introduced in JDK 1.3.
         *
         * <p>Create an instance of the DefaultInvoker and override the invoke method to handle the
         * invoking the targetMethod on the target.
         */
        java.lang.reflect.InvocationHandler handler = new org.jhotdraw.gui.event.GenericListener.DefaultInvoker() {
            @java.lang.Override
            public java.lang.Object invoke(java.lang.Object proxy, java.lang.reflect.Method method, java.lang.Object[] args) throws java.lang.Throwable {
                // Send all methods except for the targetMethod to
                // the superclass for handling.
                if (listenerMethod.equals(method)) {
                    if (targetMethod.getParameterTypes().length == 0) {
                        // Special treatment for parameterless target methods:
                        return targetMethod.invoke(target, new java.lang.Object[0]);
                    } else {
                        // Regular treatment for target methods having the same
                        // argument list as the listener method.
                        return targetMethod.invoke(target, args);
                    }
                } else {
                    return super.invoke(proxy, method, args);
                }
            }
        };
        java.lang.Class<?> cls = listenerMethod.getDeclaringClass();
        java.lang.ClassLoader cl = cls.getClassLoader();
        return java.lang.reflect.Proxy.newProxyInstance(cl, new java.lang.Class<?>[]{ cls }, handler);
    }

    /**
     * Implementation of the InvocationHandler which handles the basic object methods.
     */
    private static class DefaultInvoker implements java.lang.reflect.InvocationHandler {
        @java.lang.Override
        public java.lang.Object invoke(java.lang.Object proxy, java.lang.reflect.Method method, java.lang.Object[] args) throws java.lang.Throwable {
            if (method.getDeclaringClass() == java.lang.Object.class) {
                java.lang.String methodName = method.getName();
                if ("hashCode".equals(methodName)) {
                    return proxyHashCode(proxy);
                } else if ("equals".equals(methodName)) {
                    return proxyEquals(proxy, args[0]);
                } else if ("toString".equals(methodName)) {
                    return proxyToString(proxy);
                }
            }
            // Although listener methods are supposed to be void, we
            // allow for any return type here and produce null/0/false
            // as appropriate.
            return org.jhotdraw.gui.event.GenericListener.DefaultInvoker.nullValueOf(method.getReturnType());
        }

        protected java.lang.Integer proxyHashCode(java.lang.Object proxy) {
            return java.lang.System.identityHashCode(proxy);
        }

        protected java.lang.Boolean proxyEquals(java.lang.Object proxy, java.lang.Object other) {
            return proxy == other ? java.lang.Boolean.TRUE : java.lang.Boolean.FALSE;
        }

        protected java.lang.String proxyToString(java.lang.Object proxy) {
            return (proxy.getClass().getName() + '@') + java.lang.Integer.toHexString(proxy.hashCode());
        }

        private static final java.lang.Character CHAR_0 = ((char) (0));

        private static final java.lang.Byte BYTE_0 = ((byte) (0));

        private static final java.lang.Object nullValueOf(java.lang.Class<?> rt) {
            if (!rt.isPrimitive()) {
                return null;
            } else if (rt == void.class) {
                return null;
            } else if (rt == boolean.class) {
                return java.lang.Boolean.FALSE;
            } else if (rt == char.class) {
                return org.jhotdraw.gui.event.GenericListener.DefaultInvoker.CHAR_0;
            } else {
                // this will convert to any other kind of number
                return org.jhotdraw.gui.event.GenericListener.DefaultInvoker.BYTE_0;
            }
        }
    }

    /* Helper methods for "EZ" version of create(): */
    private static java.lang.reflect.Method getListenerMethod(java.lang.Class<?> listenerInterface, java.lang.String listenerMethodName) {
        // given the arguments to create(), find out which listener is desired:
        java.lang.reflect.Method[] m = listenerInterface.getMethods();
        java.lang.reflect.Method result = null;
        for (java.lang.reflect.Method m1 : m) {
            if (listenerMethodName.equals(m1.getName())) {
                if (result != null) {
                    throw new java.lang.RuntimeException((("ambiguous method: " + m1) + " vs. ") + result);
                }
                result = m1;
            }
        }
        if (result == null) {
            throw new java.lang.RuntimeException((("no such method " + listenerMethodName) + " in ") + listenerInterface);
        }
        return result;
    }

    @java.lang.SuppressWarnings("unchecked")
    private static java.lang.reflect.Method getTargetMethod(java.lang.Object target, java.lang.String targetMethodName, java.lang.Class<?>[] parameterTypes) {
        java.lang.reflect.Method[] m = target.getClass().getMethods();
        java.lang.reflect.Method result = null;
        eachMethod : for (java.lang.reflect.Method m1 : m) {
            if (!targetMethodName.equals(m1.getName())) {
                continue eachMethod;
            }
            java.lang.Class<?>[] p = m1.getParameterTypes();
            if (p.length != parameterTypes.length) {
                continue eachMethod;
            }
            for (int j = 0; j < p.length; j++) {
                if (!p[j].isAssignableFrom(parameterTypes[j])) {
                    continue eachMethod;
                }
            }
            if (result != null) {
                throw new java.lang.RuntimeException((("ambiguous method: " + m1) + " vs. ") + result);
            }
            result = m1;
        }
        /* if (result == null) {
        throw new RuntimeException("no such method "+targetMethodName+" in "+target.getClass());
        }
         */
        if (result == null) {
            return null;
        }
        java.lang.reflect.Method publicResult = org.jhotdraw.gui.event.GenericListener.raiseToPublicClass(result);
        if (publicResult != null) {
            result = publicResult;
        }
        return result;
    }

    private static java.lang.reflect.Method raiseToPublicClass(java.lang.reflect.Method m) {
        java.lang.Class<?> c = m.getDeclaringClass();
        if (java.lang.reflect.Modifier.isPublic(m.getModifiers()) && java.lang.reflect.Modifier.isPublic(c.getModifiers())) {
            return m;// yes!

        }// search for a public version which m overrides

        java.lang.Class<?> sc = c.getSuperclass();
        if (sc != null) {
            java.lang.reflect.Method sm = org.jhotdraw.gui.event.GenericListener.raiseToPublicClass(m, sc);
            if (sm != null) {
                return sm;
            }
        }
        java.lang.Class<?>[] ints = c.getInterfaces();
        for (java.lang.Class<?> int1 : ints) {
            java.lang.reflect.Method im = org.jhotdraw.gui.event.GenericListener.raiseToPublicClass(m, int1);
            if (im != null) {
                return im;
            }
        }
        // no public version of m here
        return null;
    }

    @java.lang.SuppressWarnings("unchecked")
    private static java.lang.reflect.Method raiseToPublicClass(java.lang.reflect.Method m, java.lang.Class<?> c) {
        try {
            java.lang.reflect.Method sm = c.getMethod(m.getName(), m.getParameterTypes());
            return org.jhotdraw.gui.event.GenericListener.raiseToPublicClass(sm);
        } catch (java.lang.NoSuchMethodException ee) {
            return null;
        }
    }

    private GenericListener() {
    }
}