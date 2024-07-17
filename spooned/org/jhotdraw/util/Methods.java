/* @(#)Methods.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.util;
/**
 * Methods contains convenience methods for method invocations using java.lang.reflect.
 */
@java.lang.SuppressWarnings("unchecked")
public class Methods {
    /**
     * Prevent instance creation.
     */
    private Methods() {
    }

    /**
     * Invokes the specified accessible parameterless method if it exists.
     *
     * @param obj
     * 		The object on which to invoke the method.
     * @param methodName
     * 		The name of the method.
     * @return The return value of the method.
     * @return NoSuchMethodException if the method does not exist or is not accessible.
     */
    public static java.lang.Object invoke(java.lang.Object obj, java.lang.String methodName) throws java.lang.NoSuchMethodException {
        try {
            java.lang.reflect.Method method = obj.getClass().getMethod(methodName, new java.lang.Class<?>[0]);
            java.lang.Object result = method.invoke(obj, new java.lang.Object[0]);
            return result;
        } catch (java.lang.IllegalAccessException e) {
            throw new java.lang.NoSuchMethodException(methodName + " is not accessible");
        } catch (java.lang.reflect.InvocationTargetException e) {
            // The method is not supposed to throw exceptions
            throw new java.lang.InternalError(e.getMessage());
        }
    }

    /**
     * Invokes the specified accessible method with a string parameter if it exists.
     *
     * @param obj
     * 		The object on which to invoke the method.
     * @param methodName
     * 		The name of the method.
     * @param stringParameter
     * 		The String parameter
     * @return The return value of the method or METHOD_NOT_FOUND.
     * @return NoSuchMethodException if the method does not exist or is not accessible.
     */
    public static java.lang.Object invoke(java.lang.Object obj, java.lang.String methodName, java.lang.String stringParameter) throws java.lang.NoSuchMethodException {
        try {
            java.lang.reflect.Method method = obj.getClass().getMethod(methodName, new java.lang.Class<?>[]{ java.lang.String.class });
            java.lang.Object result = method.invoke(obj, new java.lang.Object[]{ stringParameter });
            return result;
        } catch (java.lang.IllegalAccessException e) {
            throw new java.lang.NoSuchMethodException(methodName + " is not accessible");
        } catch (java.lang.reflect.InvocationTargetException e) {
            // The method is not supposed to throw exceptions
            throw new java.lang.InternalError(e.getMessage());
        }
    }

    /**
     * Invokes the specified accessible parameterless method if it exists.
     *
     * @param clazz
     * 		The class on which to invoke the method.
     * @param methodName
     * 		The name of the method.
     * @return The return value of the method or METHOD_NOT_FOUND.
     * @return NoSuchMethodException if the method does not exist or is not accessible.
     */
    public static java.lang.Object invokeStatic(java.lang.Class<?> clazz, java.lang.String methodName) throws java.lang.NoSuchMethodException {
        try {
            java.lang.reflect.Method method = clazz.getMethod(methodName, new java.lang.Class<?>[0]);
            java.lang.Object result = method.invoke(null, new java.lang.Object[0]);
            return result;
        } catch (java.lang.IllegalAccessException e) {
            throw new java.lang.NoSuchMethodException(methodName + " is not accessible");
        } catch (java.lang.reflect.InvocationTargetException e) {
            // The method is not supposed to throw exceptions
            throw new java.lang.InternalError(e.getMessage());
        }
    }

    /**
     * Invokes the specified accessible parameterless method if it exists.
     *
     * @param clazz
     * 		The class on which to invoke the method.
     * @param methodName
     * 		The name of the method.
     * @return The return value of the method.
     * @return NoSuchMethodException if the method does not exist or is not accessible.
     */
    public static java.lang.Object invokeStatic(java.lang.String clazz, java.lang.String methodName) throws java.lang.NoSuchMethodException {
        try {
            return org.jhotdraw.util.Methods.invokeStatic(java.lang.Class.forName(clazz), methodName);
        } catch (java.lang.ClassNotFoundException e) {
            throw new java.lang.NoSuchMethodException(("class " + clazz) + " not found");
        }
    }

    /**
     * Invokes the specified parameterless method if it exists.
     *
     * @param clazz
     * 		The class on which to invoke the method.
     * @param methodName
     * 		The name of the method.
     * @param type
     * 		The parameter type.
     * @param value
     * 		The parameter value.
     * @return The return value of the method.
     * @return NoSuchMethodException if the method does not exist or is not accessible.
     */
    public static java.lang.Object invokeStatic(java.lang.Class<?> clazz, java.lang.String methodName, java.lang.Class<?> type, java.lang.Object value) throws java.lang.NoSuchMethodException {
        return org.jhotdraw.util.Methods.invokeStatic(clazz, methodName, new java.lang.Class<?>[]{ type }, new java.lang.Object[]{ value });
    }

    /**
     * Invokes the specified parameterless method if it exists.
     *
     * @param clazz
     * 		The class on which to invoke the method.
     * @param methodName
     * 		The name of the method.
     * @param types
     * 		The parameter types.
     * @param values
     * 		The parameter values.
     * @return The return value of the method.
     * @return NoSuchMethodException if the method does not exist or is not accessible.
     */
    public static java.lang.Object invokeStatic(java.lang.Class<?> clazz, java.lang.String methodName, java.lang.Class<?>[] types, java.lang.Object[] values) throws java.lang.NoSuchMethodException {
        try {
            java.lang.reflect.Method method = clazz.getMethod(methodName, types);
            java.lang.Object result = method.invoke(null, values);
            return result;
        } catch (java.lang.IllegalAccessException e) {
            throw new java.lang.NoSuchMethodException(methodName + " is not accessible");
        } catch (java.lang.reflect.InvocationTargetException e) {
            // The method is not supposed to throw exceptions
            throw new java.lang.InternalError(e.getMessage());
        }
    }

    /**
     * Invokes the specified parameterless method if it exists.
     *
     * @param clazz
     * 		The class on which to invoke the method.
     * @param methodName
     * 		The name of the method.
     * @param types
     * 		The parameter types.
     * @param values
     * 		The parameter values.
     * @return The return value of the method.
     * @return NoSuchMethodException if the method does not exist or is not accessible.
     */
    public static java.lang.Object invokeStatic(java.lang.String clazz, java.lang.String methodName, java.lang.Class<?>[] types, java.lang.Object[] values) throws java.lang.NoSuchMethodException {
        try {
            return org.jhotdraw.util.Methods.invokeStatic(java.lang.Class.forName(clazz), methodName, types, values);
        } catch (java.lang.ClassNotFoundException e) {
            throw new java.lang.NoSuchMethodException(("class " + clazz) + " not found");
        }
    }

    /**
     * Invokes the specified parameterless method if it exists.
     *
     * @param clazz
     * 		The class on which to invoke the method.
     * @param methodName
     * 		The name of the method.
     * @param types
     * 		The parameter types.
     * @param values
     * 		The parameter values.
     * @param defaultValue
     * 		The default value.
     * @return The return value of the method or the default value if the method does not exist or is
    not accessible.
     */
    public static java.lang.Object invokeStatic(java.lang.String clazz, java.lang.String methodName, java.lang.Class<?>[] types, java.lang.Object[] values, java.lang.Object defaultValue) {
        try {
            return org.jhotdraw.util.Methods.invokeStatic(java.lang.Class.forName(clazz), methodName, types, values);
        } catch (java.lang.ClassNotFoundException | java.lang.NoSuchMethodException e) {
            return defaultValue;
        }
    }

    /**
     * Invokes the specified getter method if it exists.
     *
     * @param obj
     * 		The object on which to invoke the method.
     * @param methodName
     * 		The name of the method.
     * @param defaultValue
     * 		This value is returned, if the method does not exist.
     * @return The value returned by the getter method or the default value.
     */
    public static int invokeGetter(java.lang.Object obj, java.lang.String methodName, int defaultValue) {
        try {
            java.lang.reflect.Method method = obj.getClass().getMethod(methodName, new java.lang.Class<?>[0]);
            java.lang.Object result = method.invoke(obj, new java.lang.Object[0]);
            return ((java.lang.Integer) (result));
        } catch (java.lang.NoSuchMethodException | java.lang.IllegalAccessException | java.lang.reflect.InvocationTargetException e) {
            return defaultValue;
        }
    }

    /**
     * Invokes the specified getter method if it exists.
     *
     * @param obj
     * 		The object on which to invoke the method.
     * @param methodName
     * 		The name of the method.
     * @param defaultValue
     * 		This value is returned, if the method does not exist.
     * @return The value returned by the getter method or the default value.
     */
    public static long invokeGetter(java.lang.Object obj, java.lang.String methodName, long defaultValue) {
        try {
            java.lang.reflect.Method method = obj.getClass().getMethod(methodName, new java.lang.Class<?>[0]);
            java.lang.Object result = method.invoke(obj, new java.lang.Object[0]);
            return ((java.lang.Long) (result));
        } catch (java.lang.NoSuchMethodException | java.lang.IllegalAccessException | java.lang.reflect.InvocationTargetException e) {
            return defaultValue;
        }
    }

    /**
     * Invokes the specified getter method if it exists.
     *
     * @param obj
     * 		The object on which to invoke the method.
     * @param methodName
     * 		The name of the method.
     * @param defaultValue
     * 		This value is returned, if the method does not exist.
     * @return The value returned by the getter method or the default value.
     */
    public static boolean invokeGetter(java.lang.Object obj, java.lang.String methodName, boolean defaultValue) {
        try {
            java.lang.reflect.Method method = obj.getClass().getMethod(methodName, new java.lang.Class<?>[0]);
            java.lang.Object result = method.invoke(obj, new java.lang.Object[0]);
            return ((java.lang.Boolean) (result));
        } catch (java.lang.NoSuchMethodException | java.lang.IllegalAccessException | java.lang.reflect.InvocationTargetException e) {
            return defaultValue;
        }
    }

    /**
     * Invokes the specified getter method if it exists.
     *
     * @param obj
     * 		The object on which to invoke the method.
     * @param methodName
     * 		The name of the method.
     * @param defaultValue
     * 		This value is returned, if the method does not exist.
     * @return The value returned by the getter method or the default value.
     */
    public static java.lang.Object invokeGetter(java.lang.Object obj, java.lang.String methodName, java.lang.Object defaultValue) {
        try {
            java.lang.reflect.Method method = obj.getClass().getMethod(methodName, new java.lang.Class<?>[0]);
            java.lang.Object result = method.invoke(obj, new java.lang.Object[0]);
            return result;
        } catch (java.lang.NoSuchMethodException | java.lang.IllegalAccessException | java.lang.reflect.InvocationTargetException e) {
            return defaultValue;
        }
    }

    /**
     * Invokes the specified getter method if it exists.
     *
     * @param clazz
     * 		The object on which to invoke the method.
     * @param methodName
     * 		The name of the method.
     * @param defaultValue
     * 		This value is returned, if the method does not exist.
     * @return The value returned by the getter method or the default value.
     */
    public static boolean invokeStaticGetter(java.lang.Class<?> clazz, java.lang.String methodName, boolean defaultValue) {
        try {
            java.lang.reflect.Method method = clazz.getMethod(methodName, new java.lang.Class<?>[0]);
            java.lang.Object result = method.invoke(null, new java.lang.Object[0]);
            return ((java.lang.Boolean) (result));
        } catch (java.lang.NoSuchMethodException | java.lang.IllegalAccessException | java.lang.reflect.InvocationTargetException e) {
            return defaultValue;
        }
    }

    /**
     * Invokes the specified setter method if it exists.
     *
     * @param obj
     * 		The object on which to invoke the method.
     * @param methodName
     * 		The name of the method.
     */
    public static java.lang.Object invoke(java.lang.Object obj, java.lang.String methodName, boolean newValue) throws java.lang.NoSuchMethodException {
        try {
            java.lang.reflect.Method method = obj.getClass().getMethod(methodName, new java.lang.Class<?>[]{ java.lang.Boolean.TYPE });
            return method.invoke(obj, new java.lang.Object[]{ newValue });
        } catch (java.lang.IllegalAccessException e) {
            throw new java.lang.NoSuchMethodException(methodName + " is not accessible");
        } catch (java.lang.reflect.InvocationTargetException e) {
            // The method is not supposed to throw exceptions
            throw new java.lang.InternalError(e.getMessage());
        }
    }

    /**
     * Invokes the specified method if it exists.
     *
     * @param obj
     * 		The object on which to invoke the method.
     * @param methodName
     * 		The name of the method.
     */
    public static java.lang.Object invoke(java.lang.Object obj, java.lang.String methodName, int newValue) throws java.lang.NoSuchMethodException {
        try {
            java.lang.reflect.Method method = obj.getClass().getMethod(methodName, new java.lang.Class<?>[]{ java.lang.Integer.TYPE });
            return method.invoke(obj, new java.lang.Object[]{ newValue });
        } catch (java.lang.IllegalAccessException e) {
            throw new java.lang.NoSuchMethodException(methodName + " is not accessible");
        } catch (java.lang.reflect.InvocationTargetException e) {
            // The method is not supposed to throw exceptions
            throw new java.lang.InternalError(e.getMessage());
        }
    }

    /**
     * Invokes the specified setter method if it exists.
     *
     * @param obj
     * 		The object on which to invoke the method.
     * @param methodName
     * 		The name of the method.
     */
    public static java.lang.Object invoke(java.lang.Object obj, java.lang.String methodName, float newValue) throws java.lang.NoSuchMethodException {
        try {
            java.lang.reflect.Method method = obj.getClass().getMethod(methodName, new java.lang.Class<?>[]{ java.lang.Float.TYPE });
            return method.invoke(obj, new java.lang.Object[]{ newValue });
        } catch (java.lang.IllegalAccessException e) {
            throw new java.lang.NoSuchMethodException(methodName + " is not accessible");
        } catch (java.lang.reflect.InvocationTargetException e) {
            // The method is not supposed to throw exceptions
            throw new java.lang.InternalError(e.getMessage());
        }
    }

    /**
     * Invokes the specified setter method if it exists.
     *
     * @param obj
     * 		The object on which to invoke the method.
     * @param methodName
     * 		The name of the method.
     */
    public static java.lang.Object invoke(java.lang.Object obj, java.lang.String methodName, java.lang.Class<?> clazz, java.lang.Object newValue) throws java.lang.NoSuchMethodException {
        try {
            java.lang.reflect.Method method = obj.getClass().getMethod(methodName, new java.lang.Class<?>[]{ clazz });
            return method.invoke(obj, new java.lang.Object[]{ newValue });
        } catch (java.lang.IllegalAccessException e) {
            throw new java.lang.NoSuchMethodException(methodName + " is not accessible");
        } catch (java.lang.reflect.InvocationTargetException e) {
            // The method is not supposed to throw exceptions
            throw new java.lang.InternalError(e.getMessage());
        }
    }

    /**
     * Invokes the specified setter method if it exists.
     *
     * @param obj
     * 		The object on which to invoke the method.
     * @param methodName
     * 		The name of the method.
     */
    public static java.lang.Object invoke(java.lang.Object obj, java.lang.String methodName, java.lang.Class<?>[] clazz, java.lang.Object... newValue) throws java.lang.NoSuchMethodException {
        try {
            java.lang.reflect.Method method = obj.getClass().getMethod(methodName, clazz);
            return method.invoke(obj, newValue);
        } catch (java.lang.IllegalAccessException e) {
            throw new java.lang.NoSuchMethodException(methodName + " is not accessible");
        } catch (java.lang.reflect.InvocationTargetException e) {
            // The method is not supposed to throw exceptions
            java.lang.InternalError error = new java.lang.InternalError(e.getMessage());
            error.initCause(e.getCause() != null ? e.getCause() : e);
            throw error;
        }
    }

    /**
     * Invokes the specified setter method if it exists.
     *
     * @param obj
     * 		The object on which to invoke the method.
     * @param methodName
     * 		The name of the method.
     */
    public static void invokeIfExists(java.lang.Object obj, java.lang.String methodName) {
        try {
            org.jhotdraw.util.Methods.invoke(obj, methodName);
        } catch (java.lang.NoSuchMethodException e) {
            // ignore
        }
    }

    /**
     * Invokes the specified setter method if it exists.
     *
     * @param obj
     * 		The object on which to invoke the method.
     * @param methodName
     * 		The name of the method.
     */
    public static void invokeIfExists(java.lang.Object obj, java.lang.String methodName, float newValue) {
        try {
            org.jhotdraw.util.Methods.invoke(obj, methodName, newValue);
        } catch (java.lang.NoSuchMethodException e) {
            // ignore
        }
    }

    /**
     * Invokes the specified method if it exists.
     *
     * @param obj
     * 		The object on which to invoke the method.
     * @param methodName
     * 		The name of the method.
     */
    public static void invokeIfExists(java.lang.Object obj, java.lang.String methodName, boolean newValue) {
        try {
            org.jhotdraw.util.Methods.invoke(obj, methodName, newValue);
        } catch (java.lang.NoSuchMethodException e) {
            // ignore
        }
    }

    /**
     * Invokes the specified setter method if it exists.
     *
     * @param obj
     * 		The object on which to invoke the method.
     * @param methodName
     * 		The name of the method.
     */
    public static void invokeIfExists(java.lang.Object obj, java.lang.String methodName, java.lang.Class<?> clazz, java.lang.Object newValue) {
        try {
            org.jhotdraw.util.Methods.invoke(obj, methodName, clazz, newValue);
        } catch (java.lang.NoSuchMethodException e) {
            // ignore
        }
    }

    /**
     * Invokes the specified setter method if it exists.
     *
     * @param obj
     * 		The object on which to invoke the method.
     * @param methodName
     * 		The name of the method.
     */
    public static void invokeIfExistsWithEnum(java.lang.Object obj, java.lang.String methodName, java.lang.String enumClassName, java.lang.String enumValueName) {
        try {
            java.lang.Class<?> enumClass = java.lang.Class.forName(enumClassName);
            java.lang.Object enumValue = org.jhotdraw.util.Methods.invokeStatic("java.lang.Enum", "valueOf", new java.lang.Class<?>[]{ java.lang.Class.class, java.lang.String.class }, new java.lang.Object[]{ enumClass, enumValueName });
            org.jhotdraw.util.Methods.invoke(obj, methodName, enumClass, enumValue);
        } catch (java.lang.ClassNotFoundException | java.lang.NoSuchMethodException e) {
            // ignore
            e.printStackTrace();
        }
    }
}