/* Copyright (C) 2023 JHotDraw.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
MA 02110-1301  USA
 */
package org.jhotdraw.draw.figure;
/**
 * implementation of Attribute storage and processing.
 */
public final class Attributes {
    private java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes = new java.util.HashMap<>();

    /**
     * Forbidden attributes can't be put by the put() operation. They can only be changed by put().
     */
    private java.util.HashSet<org.jhotdraw.draw.AttributeKey<?>> forbiddenAttributes;

    private org.jhotdraw.draw.figure.Attributes.AttributeListener listener;

    private java.util.function.Supplier<java.util.List<org.jhotdraw.draw.figure.Attributes>> DEPENDENT;

    public Attributes() {
        this(null, null);
    }

    public Attributes(org.jhotdraw.draw.figure.Attributes.AttributeListener listener) {
        this(listener, null);
    }

    public Attributes(org.jhotdraw.draw.figure.Attributes.AttributeListener listener, java.util.function.Supplier<java.util.List<org.jhotdraw.draw.figure.Attributes>> dependent) {
        this.listener = listener;
        this.DEPENDENT = (dependent == null) ? () -> java.util.Collections.emptyList() : dependent;
    }

    public void dependents(java.util.function.Supplier<java.util.List<org.jhotdraw.draw.figure.Attributes>> dependent) {
        this.DEPENDENT = (dependent == null) ? () -> java.util.Collections.emptyList() : dependent;
    }

    public void setAttributeEnabled(org.jhotdraw.draw.AttributeKey<?> key, boolean b) {
        if (forbiddenAttributes == null) {
            forbiddenAttributes = new java.util.HashSet<>();
        }
        if (b) {
            forbiddenAttributes.remove(key);
        } else {
            forbiddenAttributes.add(key);
        }
    }

    /**
     * Is this attribute enabled for this figure to be processed.
     *
     * @param key
     * @return  */
    public boolean isAttributeEnabled(org.jhotdraw.draw.AttributeKey<?> key) {
        return (forbiddenAttributes == null) || (!forbiddenAttributes.contains(key));
    }

    public void setAttributes(java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> map) {
        for (java.util.Map.Entry<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> entry : map.entrySet()) {
            set(((org.jhotdraw.draw.AttributeKey<java.lang.Object>) (entry.getKey())), entry.getValue());
        }
    }

    public java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> getAttributes() {
        return java.util.Collections.unmodifiableMap(attributes);
    }

    /**
     * Gets data which can be used to restore the attributes of the figure after a set has been
     * applied to it.
     */
    public java.lang.Object getAttributesRestoreData() {
        java.util.List<org.jhotdraw.draw.figure.Attributes> dependent = DEPENDENT.get();
        if (dependent.isEmpty()) {
            return getAttributes();
        } else {
            java.util.List<java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>> list = new java.util.ArrayList<>();
            list.add(getAttributes());
            for (org.jhotdraw.draw.figure.Attributes attr : dependent) {
                list.add(attr.getAttributes());
            }
            return list;
        }
    }

    /**
     * Restores the attributes of the figure to a previously stored state.
     */
    public void restoreAttributesTo(java.lang.Object restoreData) {
        if (restoreData instanceof java.util.List) {
            java.util.List<java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>> list = ((java.util.List<java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>>) (restoreData));
            restoreAttributesTo(list.get(0));
            int idx = 1;
            for (org.jhotdraw.draw.figure.Attributes attr : DEPENDENT.get()) {
                attr.restoreAttributesTo(list.get(idx));
                idx++;
            }
        } else {
            attributes.clear();
            java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> restoreDataHashMap = ((java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object>) (restoreData));
            setAttributes(restoreDataHashMap);
        }
    }

    /**
     * Sets an attribute on the figure and calls {@code attributeChanged} on all registered {@code FigureListener}s if the attribute value has changed.
     *
     * <p>For efficiency reasons, the drawing is not automatically repainted. If you want the drawing
     * to be repainted when the attribute is changed, you can either use {@code key.set(figure,
     * value);} or
     *
     * <pre>
     * figure.willChange();
     * figure.set(...);
     * figure.changed();
     * </pre>
     *
     * @see AttributeKey#set
     */
    public <T> org.jhotdraw.draw.figure.Attributes set(final org.jhotdraw.draw.AttributeKey<T> key, final T newValue) {
        if ((forbiddenAttributes == null) || (!forbiddenAttributes.contains(key))) {
            T oldValue = key.put(attributes, newValue);
            fireAttributeChanged(key, oldValue, newValue);
        }
        DEPENDENT.get().forEach(a -> java.util.Optional.ofNullable(a).ifPresent(at -> at.set(key, newValue)));
        return this;
    }

    /**
     * Gets an attribute from the Figure.
     *
     * @see AttributeKey#get
     * @return Returns the attribute value. If the Figure does not have an attribute with the
    specified key, returns key.getDefaultValue().
     */
    public <T> T get(org.jhotdraw.draw.AttributeKey<T> key) {
        return key.get(attributes);
    }

    public static org.jhotdraw.draw.AttributeKey<?> getAttributeKey(java.lang.String name) {
        return org.jhotdraw.draw.AttributeKeys.SUPPORTED_ATTRIBUTES_MAP.get(name);
    }

    public <T> void removeAttribute(org.jhotdraw.draw.AttributeKey<T> key) {
        if (hasAttribute(key)) {
            T oldValue = get(key);
            attributes.remove(key);
            fireAttributeChanged(key, oldValue, key.getDefaultValue());
        }
    }

    /**
     * Is this attribute set within this container.
     *
     * @param key
     * @return  */
    public boolean hasAttribute(org.jhotdraw.draw.AttributeKey<?> key) {
        return attributes.containsKey(key);
    }

    private <T> void fireAttributeChanged(org.jhotdraw.draw.AttributeKey<T> attribute, T oldValue, T newValue) {
        if (listener != null) {
            listener.attributeChanged(attribute, oldValue, newValue);
        }
    }

    @java.lang.FunctionalInterface
    public static interface AttributeListener {
        <T> void attributeChanged(org.jhotdraw.draw.AttributeKey<T> attribute, T oldValue, T newValue);
    }

    public static org.jhotdraw.draw.figure.Attributes from(org.jhotdraw.draw.figure.Attributes source) {
        return org.jhotdraw.draw.figure.Attributes.from(source, null, null);
    }

    public static org.jhotdraw.draw.figure.Attributes from(org.jhotdraw.draw.figure.Attributes source, org.jhotdraw.draw.figure.Attributes.AttributeListener listener) {
        return org.jhotdraw.draw.figure.Attributes.from(source, listener, null);
    }

    public static org.jhotdraw.draw.figure.Attributes from(org.jhotdraw.draw.figure.Attributes source, org.jhotdraw.draw.figure.Attributes.AttributeListener listener, java.util.function.Supplier<java.util.List<org.jhotdraw.draw.figure.Attributes>> dependent) {
        org.jhotdraw.draw.figure.Attributes attr = new org.jhotdraw.draw.figure.Attributes(listener, dependent);
        attr.attributes.putAll(source.attributes);
        if (source.forbiddenAttributes != null) {
            attr.forbiddenAttributes = new java.util.HashSet<>(source.forbiddenAttributes);
        }
        return attr;
    }

    public static java.util.function.Supplier<java.util.List<org.jhotdraw.draw.figure.Attributes>> attrSupplier(java.util.function.Supplier<java.util.List<org.jhotdraw.draw.figure.Figure>> dependent) {
        return () -> dependent.get().stream().filter(f -> f != null).map(f -> f.attr()).collect(java.util.stream.Collectors.toList());
    }
}