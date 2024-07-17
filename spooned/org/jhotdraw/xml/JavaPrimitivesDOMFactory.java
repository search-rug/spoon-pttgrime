/* @(#)JavaPrimitivesDOMFactory.java

Copyright (c) 2009-2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.xml;
/**
 * {@code JavaPrimitivesDOMFactory} can be used to serialize Java primitive objects and {@link DOMStorable} objects.
 *
 * <p>The following Java primitive types are supported. Object wrappers are automatically unwrapped
 * into their primitive types.
 *
 * <ul>
 *   <li>null
 *   <li>boolean
 *   <li>byte
 *   <li>short
 *   <li>char
 *   <li>int
 *   <li>long
 *   <li>float
 *   <li>double
 *   <li>string
 *   <li>enum
 *   <li>color (will be removed in a future revision of this class!)
 *   <li>font (will be removed in a future revision of this class!)
 * </ul>
 *
 * Arrays of primitive types are supported, by appending the word "Array" to a primitive type name.
 *
 * <p>You can add support for additional primitive types by overriding the methods {@code read} and
 * {@code write}.
 *
 * <p>In addition to the primitive types, this factory can store and read {@link DOMStorable}
 * objects. No mapping for {@link DOMStorable} class names is performed. For example, if a {@link DOMStorable} object has the class name {@code com.example.MyClass}, then the DOM element has the
 * same name, that is: {@code &lt;com.example.MyClass&gt;}.
 *
 * <p>Since no mapping between DOM element names and {@link DOMStorable} class names is performed,
 * DOM's generated with JavaPrimitivesDOMFactory are not suited for long-term storage of objects.
 * This is because a DOM element can not be read back into an object, if the class name of the
 * object has changed.
 *
 * <p>You can implement a mapping by overriding the methods {@code getName}, {@code create}, {@code getEnumName} and {@code getEnumValue}.
 */
public class JavaPrimitivesDOMFactory implements org.jhotdraw.xml.DOMFactory {
    private java.lang.String escape(java.lang.String name) {
        // Escape dollar characters by two full-stop characters
        name = name.replaceAll("\\$", "..");
        return name;
    }

    private java.lang.String unescape(java.lang.String name) {
        // Unescape dollar characters from two full-stop characters
        name = name.replaceAll("\\.\\.", java.util.regex.Matcher.quoteReplacement("$"));
        return name;
    }

    @java.lang.Override
    public java.lang.String getName(java.lang.Object o) {
        if (o == null) {
            return "null";
        } else if (o instanceof java.lang.Boolean) {
            return "boolean";
        } else if (o instanceof java.lang.Byte) {
            return "byte";
        } else if (o instanceof java.lang.Character) {
            return "char";
        } else if (o instanceof java.lang.Short) {
            return "short";
        } else if (o instanceof java.lang.Integer) {
            return "int";
        } else if (o instanceof java.lang.Long) {
            return "long";
        } else if (o instanceof java.lang.Float) {
            return "float";
        } else if (o instanceof java.lang.Double) {
            return "double";
        } else if (o instanceof java.awt.Color) {
            return "color";
        } else if (o instanceof java.awt.Font) {
            return "font";
        } else if (o instanceof byte[]) {
            return "byteArray";
        } else if (o instanceof char[]) {
            return "charArray";
        } else if (o instanceof short[]) {
            return "shortArray";
        } else if (o instanceof int[]) {
            return "intArray";
        } else if (o instanceof long[]) {
            return "longArray";
        } else if (o instanceof float[]) {
            return "floatArray";
        } else if (o instanceof double[]) {
            return "doubleArray";
        } else if (o instanceof java.lang.String) {
            return "string";
        } else if (o instanceof java.lang.Enum) {
            return "enum";
        } else if (o instanceof java.awt.Color) {
            return "color";
        } else if (o instanceof java.awt.Font) {
            return "font";
        }
        return escape(o.getClass().getName());
    }

    @java.lang.Override
    public java.lang.Object createPrototype(java.lang.String tagName) {
        java.lang.String name = unescape(tagName);
        try {
            return java.lang.Class.forName(name).getConstructor().newInstance();
        } catch (java.lang.Exception ex) {
            throw new java.lang.IllegalArgumentException("unable to instatiate instance from class " + name, ex);
        }
    }

    protected java.lang.String getEnumName(java.lang.Enum<?> o) {
        return escape(o.getClass().getName());
    }

    protected java.lang.String getEnumValue(java.lang.Enum<?> o) {
        return o.name();
    }

    @java.lang.SuppressWarnings("unchecked")
    protected <E extends java.lang.Enum<E>> java.lang.Enum<E> createEnum(java.lang.String name, java.lang.String value) {
        name = unescape(name);
        java.lang.Class<E> enumClass;
        try {
            enumClass = ((java.lang.Class<E>) (java.lang.Class.forName(name)));
        } catch (java.lang.ClassNotFoundException ex) {
            throw new java.lang.IllegalArgumentException("Class not found for Enum with name:" + name);
        }
        if (enumClass == null) {
            throw new java.lang.IllegalArgumentException("Enum name not known to factory:" + name);
        }
        return java.lang.Enum.valueOf(enumClass, value);
    }

    @java.lang.Override
    public void write(org.jhotdraw.xml.DOMOutput out, java.lang.Object o) throws java.io.IOException {
        if (o == null) {
            // nothing to do
        } else if (o instanceof org.jhotdraw.xml.DOMStorable) {
            // ((DOMStorable) o).write(out);
            org.jhotdraw.xml.JavaPrimitivesDOMFactory.LOG.warning("direct call of DOMOutput from JavaPrimitivesDOMFactory removed");
        } else if (o instanceof java.lang.String) {
            out.addText(((java.lang.String) (o)));
        } else if (o instanceof java.lang.Integer) {
            out.addText(o.toString());
        } else if (o instanceof java.lang.Long) {
            out.addText(o.toString());
        } else if (o instanceof java.lang.Double) {
            out.addText(o.toString());
        } else if (o instanceof java.lang.Float) {
            out.addText(o.toString());
        } else if (o instanceof java.lang.Boolean) {
            out.addText(o.toString());
        } else if (o instanceof java.awt.Color) {
            java.awt.Color c = ((java.awt.Color) (o));
            out.addAttribute("rgba", "#" + java.lang.Integer.toHexString(c.getRGB()));
        } else if (o instanceof byte[]) {
            byte[] a = ((byte[]) (o));
            for (int i = 0; i < a.length; i++) {
                out.openElement("byte");
                write(out, a[i]);
                out.closeElement();
            }
        } else if (o instanceof boolean[]) {
            boolean[] a = ((boolean[]) (o));
            for (int i = 0; i < a.length; i++) {
                out.openElement("boolean");
                write(out, a[i]);
                out.closeElement();
            }
        } else if (o instanceof char[]) {
            char[] a = ((char[]) (o));
            for (int i = 0; i < a.length; i++) {
                out.openElement("char");
                write(out, a[i]);
                out.closeElement();
            }
        } else if (o instanceof short[]) {
            short[] a = ((short[]) (o));
            for (int i = 0; i < a.length; i++) {
                out.openElement("short");
                write(out, a[i]);
                out.closeElement();
            }
        } else if (o instanceof int[]) {
            int[] a = ((int[]) (o));
            for (int i = 0; i < a.length; i++) {
                out.openElement("int");
                write(out, a[i]);
                out.closeElement();
            }
        } else if (o instanceof long[]) {
            long[] a = ((long[]) (o));
            for (int i = 0; i < a.length; i++) {
                out.openElement("long");
                write(out, a[i]);
                out.closeElement();
            }
        } else if (o instanceof float[]) {
            float[] a = ((float[]) (o));
            for (int i = 0; i < a.length; i++) {
                out.openElement("float");
                write(out, a[i]);
                out.closeElement();
            }
        } else if (o instanceof double[]) {
            double[] a = ((double[]) (o));
            for (int i = 0; i < a.length; i++) {
                out.openElement("double");
                write(out, a[i]);
                out.closeElement();
            }
        } else if (o instanceof java.awt.Font) {
            java.awt.Font f = ((java.awt.Font) (o));
            out.addAttribute("name", f.getName());
            out.addAttribute("style", f.getStyle());
            out.addAttribute("size", f.getSize());
        } else if (o instanceof java.lang.Enum) {
            java.lang.Enum<?> e = ((java.lang.Enum<?>) (o));
            out.addAttribute("type", getEnumName(e));
            out.addText(getEnumValue(e));
        } else {
            throw new java.lang.IllegalArgumentException("Unsupported object type:" + o);
        }
    }

    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(org.jhotdraw.xml.JavaPrimitivesDOMFactory.class.getName());

    @java.lang.Override
    public java.lang.Object read(org.jhotdraw.xml.DOMInput in) throws java.io.IOException {
        java.lang.Object o;
        java.lang.String tagName = in.getTagName();
        if ("null".equals(tagName)) {
            o = null;
        } else if ("boolean".equals(tagName)) {
            o = java.lang.Boolean.valueOf(in.getText());
        } else if ("byte".equals(tagName)) {
            o = java.lang.Byte.decode(in.getText());
        } else if ("short".equals(tagName)) {
            o = java.lang.Short.decode(in.getText());
        } else if ("int".equals(tagName)) {
            o = java.lang.Integer.decode(in.getText());
        } else if ("long".equals(tagName)) {
            o = java.lang.Long.decode(in.getText());
        } else if ("float".equals(tagName)) {
            o = java.lang.Float.parseFloat(in.getText());
        } else if ("double".equals(tagName)) {
            o = java.lang.Double.parseDouble(in.getText());
        } else if ("string".equals(tagName)) {
            o = in.getText();
        } else if ("enum".equals(tagName)) {
            o = createEnum(in.getAttribute("type", ((java.lang.String) (null))), in.getText());
        } else if ("color".equals(tagName)) {
            o = new java.awt.Color(in.getAttribute("rgba", 0xff));
        } else if ("font".equals(tagName)) {
            o = new java.awt.Font(in.getAttribute("name", "Dialog"), in.getAttribute("style", 0), in.getAttribute("size", 0));
        } else if ("byteArray".equals(tagName)) {
            byte[] a = new byte[in.getElementCount()];
            for (int i = 0; i < a.length; i++) {
                a[i] = ((java.lang.Byte) (in.readObject(i))).byteValue();
            }
            o = a;
        } else if ("shortArray".equals(tagName)) {
            short[] a = new short[in.getElementCount()];
            for (int i = 0; i < a.length; i++) {
                a[i] = ((java.lang.Short) (in.readObject(i))).shortValue();
            }
            o = a;
        } else if ("intArray".equals(tagName)) {
            int[] a = new int[in.getElementCount()];
            for (int i = 0; i < a.length; i++) {
                a[i] = ((java.lang.Integer) (in.readObject(i))).intValue();
            }
            o = a;
        } else if ("longArray".equals(tagName)) {
            long[] a = new long[in.getElementCount()];
            for (int i = 0; i < a.length; i++) {
                a[i] = ((java.lang.Long) (in.readObject(i))).longValue();
            }
            o = a;
        } else if ("floatArray".equals(tagName)) {
            float[] a = new float[in.getElementCount()];
            for (int i = 0; i < a.length; i++) {
                a[i] = ((java.lang.Float) (in.readObject(i))).floatValue();
            }
            o = a;
        } else if ("doubleArray".equals(tagName)) {
            double[] a = new double[in.getElementCount()];
            for (int i = 0; i < a.length; i++) {
                a[i] = ((java.lang.Double) (in.readObject(i))).doubleValue();
            }
            o = a;
        } else {
            throw new java.lang.IllegalArgumentException(("unkown tagname " + tagName) + " --> is not registered");
        }
        return o;
    }
}