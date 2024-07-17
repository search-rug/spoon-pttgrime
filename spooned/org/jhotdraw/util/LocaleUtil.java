/* @(#)LocaleUtil.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.util;
/**
 * LocaleUtil provides a setDefault()/getDefault() wrapper to java.util.Locale in order to overcome
 * the security restriction preventing Applets from using their own locale.
 */
public class LocaleUtil {
    private static java.util.Locale defaultLocale;

    public LocaleUtil() {
    }

    public static void setDefault(java.util.Locale newValue) {
        org.jhotdraw.util.LocaleUtil.defaultLocale = newValue;
    }

    public static java.util.Locale getDefault() {
        return org.jhotdraw.util.LocaleUtil.defaultLocale == null ? java.util.Locale.getDefault() : org.jhotdraw.util.LocaleUtil.defaultLocale;
    }
}