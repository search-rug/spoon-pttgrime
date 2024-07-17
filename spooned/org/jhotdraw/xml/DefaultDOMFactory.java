/* @(#)DefaultDOMFactory.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.xml;
/**
 * {@code DefaultDOMFactory} can be used to serialize DOMStorable objects in a DOM with the use of a
 * mapping between Java class names and DOM element names.
 */
public class DefaultDOMFactory extends org.jhotdraw.xml.JavaPrimitivesDOMFactory {
    private org.jhotdraw.xml.record ClassRegistration;

    java.lang.String Class;

    java.lang.String tagName;

    org.jhotdraw.xml.DefaultDOMFactory.BiConsumerWithIOException<org.jhotdraw.xml.T, org.jhotdraw.xml.DOMInput> BiConsumerWithIOException;

    org.jhotdraw.xml.DefaultDOMFactory.BiConsumerWithIOException<org.jhotdraw.xml.T, org.jhotdraw.xml.DOMInput> read;

    {
    }

    private final java.util.HashMap<java.lang.String, org.jhotdraw.xml.ClassRegistration> REGISTRATION = new java.util.HashMap<>();

    private final java.util.HashMap<java.lang.Class<?>, java.lang.String> ENUM_TO_NAME = new java.util.HashMap<java.lang.Class<?>, java.lang.String>();

    private final java.util.HashMap<java.lang.String, java.lang.Class<?>> NAME_TO_ENUM = new java.util.HashMap<java.lang.String, java.lang.Class<?>>();

    @java.lang.SuppressWarnings("rawtypes")
    private static final java.util.HashMap<java.lang.Enum, java.lang.String> ENUM_TO_VALUE = new java.util.HashMap<java.lang.Enum, java.lang.String>();

    @java.lang.SuppressWarnings("rawtypes")
    private static final java.util.HashMap<java.lang.String, java.util.Set<java.lang.Enum>> VALUE_TO_ENUM = new java.util.HashMap<java.lang.String, java.util.Set<java.lang.Enum>>();

    public DefaultDOMFactory() {
    }

    /**
     * register a dom tag processor
     */
    public <T> void register(java.lang.String tagName, java.lang.Class<T> prototype, org.jhotdraw.xml.DefaultDOMFactory.BiConsumerWithIOException<T, org.jhotdraw.xml.DOMInput> read, org.jhotdraw.xml.DefaultDOMFactory.BiConsumerWithIOException<T, org.jhotdraw.xml.DOMOutput> write) {
        final org.jhotdraw.xml.ClassRegistration reg = new org.jhotdraw.xml.ClassRegistration(tagName, prototype, read, write);
        // to avoid double hashmaps we register each registration twice, one for the key tagName and one
        // for the key className.
        REGISTRATION.put(tagName, reg);
        REGISTRATION.put(reg.prototype.getName(), reg);
    }

    /**
     * register a dom tag processor assuming the the instance of this prototype is able to write /
     * read itself.
     */
    public <T extends org.jhotdraw.xml.DOMStorable> void register(java.lang.String tagName, java.lang.Class<T> prototype) {
        if (!prototype.isInstance(org.jhotdraw.xml.DOMStorable.class)) {
            throw new java.lang.IllegalArgumentException(prototype.getName() + " does not implement DOMStorable");
        }
        register(tagName, prototype, (t, domInput) -> ((org.jhotdraw.xml.DOMStorable) (t)).read(domInput), (t, domOutput) -> ((org.jhotdraw.xml.DOMStorable) (t)).write(domOutput));
    }

    // /** Adds a DOMStorable class to the DOMFactory. */
    // public void addStorableClass(String name, Class<?> c) {
    // NAME_TO_PROTOTYPE.put(name, c);
    // CLASS_TO_NAME.put(c, name);
    // }
    // 
    // /** Adds a DOMStorable prototype to the DOMFactory. */
    // public void addStorable(String name, DOMStorable prototype) {
    // NAME_TO_PROTOTYPE.put(name, prototype);
    // CLASS_TO_NAME.put(prototype.getClass(), name);
    // }
    /**
     * Adds an Enum class to the DOMFactory.
     */
    public void addEnumClass(java.lang.String name, java.lang.Class<?> c) {
        ENUM_TO_NAME.put(c, name);
        NAME_TO_ENUM.put(name, c);
    }

    /**
     * Adds an Enum value to the DOMFactory.
     */
    @java.lang.SuppressWarnings("rawtypes")
    public <T extends java.lang.Enum<T>> void addEnum(java.lang.String value, java.lang.Enum<T> e) {
        org.jhotdraw.xml.DefaultDOMFactory.ENUM_TO_VALUE.put(e, value);
        java.util.Set<java.lang.Enum> enums;
        if (org.jhotdraw.xml.DefaultDOMFactory.VALUE_TO_ENUM.containsKey(value)) {
            enums = org.jhotdraw.xml.DefaultDOMFactory.VALUE_TO_ENUM.get(value);
        } else {
            enums = new java.util.HashSet<java.lang.Enum>();
            org.jhotdraw.xml.DefaultDOMFactory.VALUE_TO_ENUM.put(value, enums);
        }
        enums.add(e);
    }

    /**
     * Creates a DOMStorable object and reads it in.
     */
    @java.lang.Override
    public java.lang.Object createPrototype(java.lang.String name) {
        // Object o = NAME_TO_PROTOTYPE.get(name);
        org.jhotdraw.xml.ClassRegistration reg = REGISTRATION.get(name);
        if (reg == null) {
            throw new java.lang.IllegalArgumentException("Storable name not known to factory: " + name);
        }
        try {
            return reg.prototype().getConstructor().newInstance();
        } catch (java.lang.Exception e) {
            java.lang.IllegalArgumentException error = new java.lang.IllegalArgumentException("Storable class not instantiable by factory: " + name);
            error.initCause(e);
            throw error;
        }
    }

    @java.lang.Override
    public void write(org.jhotdraw.xml.DOMOutput out, java.lang.Object o) throws java.io.IOException {
        if (o == null) {
            super.write(out, o);
            return;
        }
        org.jhotdraw.xml.ClassRegistration reg = REGISTRATION.get(o.getClass().getName());
        if (reg != null) {
            reg.write().accept(o, out);
        } else {
            super.write(out, o);
        }
    }

    @java.lang.Override
    public java.lang.Object read(org.jhotdraw.xml.DOMInput in) throws java.io.IOException {
        java.lang.String tagName = in.getTagName();
        org.jhotdraw.xml.ClassRegistration reg = REGISTRATION.get(tagName);
        if (reg != null) {
            java.lang.Object instance;
            try {
                instance = reg.prototype().getConstructor().newInstance();
            } catch (java.lang.Exception ex) {
                throw new java.lang.IllegalArgumentException("could not create class", ex);
            }
            reg.read().accept(instance, in);
            return instance;
        }
        return super.read(in);
    }

    @java.lang.Override
    public java.lang.String getName(java.lang.Object o) {
        if (o == null) {
            return super.getName(o);
        }
        return java.util.Optional.ofNullable(REGISTRATION.get(o.getClass().getName())).map(reg -> reg.tagName()).or(() -> java.util.Optional.ofNullable(super.getName(o))).orElseThrow(() -> new java.lang.IllegalArgumentException((("Storable class not known to factory. Storable class:" + o.getClass()) + " Factory:") + this.getClass()));
    }

    @java.lang.SuppressWarnings("rawtypes")
    @java.lang.Override
    protected java.lang.String getEnumName(java.lang.Enum e) {
        java.lang.String name = ENUM_TO_NAME.get(e.getClass());
        if (name == null) {
            throw new java.lang.IllegalArgumentException("Enum class not known to factory:" + e.getClass());
        }
        return name;
    }

    @java.lang.SuppressWarnings("rawtypes")
    @java.lang.Override
    protected java.lang.String getEnumValue(java.lang.Enum e) {
        return org.jhotdraw.xml.DefaultDOMFactory.ENUM_TO_VALUE.containsKey(e) ? org.jhotdraw.xml.DefaultDOMFactory.ENUM_TO_VALUE.get(e) : e.toString();
    }

    @java.lang.SuppressWarnings({ "unchecked", "rawtypes" })
    @java.lang.Override
    protected <T extends java.lang.Enum<T>> java.lang.Enum<T> createEnum(java.lang.String name, java.lang.String value) {
        java.lang.Class<T> enumClass = ((java.lang.Class<T>) (NAME_TO_ENUM.get(name)));
        if (enumClass == null) {
            throw new java.lang.IllegalArgumentException("Enum name not known to factory:" + name);
        }
        java.util.Set<java.lang.Enum> enums = org.jhotdraw.xml.DefaultDOMFactory.VALUE_TO_ENUM.get(value);
        if (enums == null) {
            return java.lang.Enum.valueOf(enumClass, value);
        }
        for (java.lang.Enum e : enums) {
            if (e.getClass() == enumClass) {
                return e;
            }
        }
        throw new java.lang.IllegalArgumentException("Enum value not known to factory:" + value);
    }

    @java.lang.FunctionalInterface
    public static interface BiConsumerWithIOException<T, U> {
        /**
         * Performs this operation on the given arguments.
         *
         * @param t
         * 		the first input argument
         * @param u
         * 		the second input argument
         */
        void accept(T t, U u) throws java.io.IOException;

        /**
         * Returns a composed {@code BiConsumer} that performs, in sequence, this operation followed by
         * the {@code after} operation. If performing either operation throws an exception, it is
         * relayed to the caller of the composed operation. If performing this operation throws an
         * exception, the {@code after} operation will not be performed.
         *
         * @param after
         * 		the operation to perform after this operation
         * @return a composed {@code BiConsumer} that performs in sequence this operation followed by
        the {@code after} operation
         * @throws NullPointerException
         * 		if {@code after} is null
         */
        default org.jhotdraw.xml.DefaultDOMFactory.BiConsumerWithIOException<T, U> andThen(org.jhotdraw.xml.DefaultDOMFactory.BiConsumerWithIOException<? super T, ? super U> after) throws java.io.IOException {
            java.util.Objects.requireNonNull(after);
            return (l, r) -> {
                accept(l, r);
                after.accept(l, r);
            };
        }
    }
}