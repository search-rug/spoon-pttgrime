/* @(#)QuickAndDirtyDOMStorableSample.java

Copyright (c) 2009-2010 The authors and contributors of JHotDraw.

You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.mini;
/**
 * {@code QuickAndDirtyDOMStorableSample} serializes a DOMStorable MyObject into a String using the
 * DefaultDOMFactory and then deserializes it from the String.
 */
public class DefaultDOMStorableSample {
    public static class MyObject implements org.jhotdraw.xml.DOMStorable {
        private java.lang.String name;

        /**
         * DOM Storable objects must have a non-argument constructor.
         */
        public MyObject() {
        }

        public MyObject(java.lang.String name) {
            this.name = name;
        }

        public java.lang.String getName() {
            return name;
        }

        public void setName(java.lang.String name) {
            this.name = name;
        }

        @java.lang.Override
        public void write(org.jhotdraw.xml.DOMOutput out) throws java.io.IOException {
            out.addAttribute("name", name);
        }

        @java.lang.Override
        public void read(org.jhotdraw.xml.DOMInput in) throws java.io.IOException {
            name = in.getAttribute("name", null);
        }
    }

    public static void main(java.lang.String[] args) {
        try {
            // Set up the DefaultDOMFactory
            org.jhotdraw.xml.DefaultDOMFactory factory = new org.jhotdraw.xml.DefaultDOMFactory();
            factory.register("MyElementName", org.jhotdraw.samples.mini.DefaultDOMStorableSample.MyObject.class);
            // Create a DOMStorable object
            org.jhotdraw.samples.mini.DefaultDOMStorableSample.MyObject obj = new org.jhotdraw.samples.mini.DefaultDOMStorableSample.MyObject("Hello World");
            java.lang.System.out.println("The name of the original object is:" + obj.getName());
            // Write the object into a DOM, and then serialize the DOM into a String
            org.jhotdraw.xml.JavaxDOMOutput out = new org.jhotdraw.xml.JavaxDOMOutput(factory);
            out.writeObject(obj);
            java.io.StringWriter writer = new java.io.StringWriter();
            out.save(writer);
            java.lang.String serializedString = writer.toString();
            java.lang.System.out.println("\nThe serialized representation of the object is:\n" + serializedString);
            // Deserialize a DOM from a String, and then read the object from the DOM
            java.io.StringReader reader = new java.io.StringReader(serializedString);
            org.jhotdraw.xml.JavaxDOMInput in = new org.jhotdraw.xml.JavaxDOMInput(factory, reader);
            org.jhotdraw.samples.mini.DefaultDOMStorableSample.MyObject obj2 = ((org.jhotdraw.samples.mini.DefaultDOMStorableSample.MyObject) (in.readObject()));
            java.lang.System.out.println("\nThe name of the restored object is:" + obj2.getName());
        } catch (java.io.IOException ex) {
            java.util.logging.Logger.getLogger(org.jhotdraw.samples.mini.DefaultDOMStorableSample.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
}