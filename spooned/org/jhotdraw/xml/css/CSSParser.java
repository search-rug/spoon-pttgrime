/* @(#)CSSLoader.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.

Original code taken from article "Swing and CSS" by Joshua Marinacci 10/14/2003
http://today.java.net/pub/a/today/2003/10/14/swingcss.html
 */
package org.jhotdraw.xml.css;
/**
 * Parsers a Cascading Style Sheet (CSS).
 *
 * <pre>
 * IDENT  {ident}
 * ATKEYWORD  @{ident}
 * STRING  {string}
 * INVALID  {invalid}
 * HASH  #{name}
 * NUMBER  {num}
 * PERCENTAGE  {num}%
 * DIMENSION  {num}{ident}
 * URI  url\({w}{string}{w}\)
 * |url\({w}([!#$%&amp;*-~]|{nonascii}|{escape})*{w}\)
 * UNICODE-RANGE  U\+[0-9A-F?]{1,6}(-[0-9A-F]{1,6})?
 * CDO  <!--
 * CDC  -->
 * ;  ;
 * {  \{
 * }  \}
 * (  \(
 * )  \)
 * [  \[
 * ]  \]
 * S  [ \t\r\n\f]+
 * COMMENT  \/\*[^*]*\*+([^/*][^*]*\*+)*\/
 * FUNCTION  {ident}\(
 * INCLUDES  ~=
 * DASHMATCH  |=
 * DELIM  any other character not matched by the above rules, and neither a single nor a double quote
 *
 *
 * stylesheet  : [ CDO | CDC | S | statement ]*;
 * statement   : ruleset | at-rule;
 * at-rule     : ATKEYWORD S* any* [ block | ';' S* ];
 * block       : '{' S* [ any | block | ATKEYWORD S* | ';' S* ]* '}' S*;
 * ruleset     : selector? '{' S* declaration? [ ';' S* declaration? ]* '}' S*;
 * selector    : any+;
 * declaration : DELIM? property S* ':' S* value;
 * property    : IDENT;
 * value       : [ any | block | ATKEYWORD S* ]+;
 * any         : [ IDENT | NUMBER | PERCENTAGE | DIMENSION | STRING
 * | DELIM | URI | HASH | UNICODE-RANGE | INCLUDES
 * | DASHMATCH | FUNCTION S* any* ')'
 * | '(' S* any* ')' | '[' S* any* ']' ] S*;
 * </pre>
 */
public class CSSParser {
    public void parse(java.lang.String css, org.jhotdraw.xml.css.StyleManager rm) throws java.io.IOException {
        parse(new java.io.StringReader(css), rm);
    }

    public void parse(java.io.Reader css, org.jhotdraw.xml.css.StyleManager rm) throws java.io.IOException {
        java.io.StreamTokenizer tt = new java.io.StreamTokenizer(css);
        tt.resetSyntax();
        tt.wordChars('a', 'z');
        tt.wordChars('A', 'Z');
        tt.wordChars('0', '9');
        tt.wordChars(128 + 32, 255);
        tt.whitespaceChars(0, ' ');
        tt.commentChar('/');
        tt.slashStarComments(true);
        parseStylesheet(tt, rm);
    }

    private void parseStylesheet(java.io.StreamTokenizer tt, org.jhotdraw.xml.css.StyleManager rm) throws java.io.IOException {
        while (tt.nextToken() != java.io.StreamTokenizer.TT_EOF) {
            tt.pushBack();
            parseRuleset(tt, rm);
        } 
    }

    private void parseRuleset(java.io.StreamTokenizer tt, org.jhotdraw.xml.css.StyleManager rm) throws java.io.IOException {
        // parse selector list
        java.util.List<java.lang.String> selectors = parseSelectorList(tt);
        if (tt.nextToken() != '{') {
            throw new java.io.IOException("Ruleset '{' missing for " + selectors);
        }
        java.util.Map<java.lang.String, java.lang.String> declarations = parseDeclarationMap(tt);
        if (tt.nextToken() != '}') {
            throw new java.io.IOException("Ruleset '}' missing for " + selectors);
        }
        for (java.lang.String selector : selectors) {
            rm.add(new org.jhotdraw.xml.css.CSSRule(selector, declarations));
            // System.out.println("CSSParser.add("+selector+","+declarations);
            /* for (Map.Entry<String,String> entry : declarations.entrySet()) {
            rm.add(new CSSRule(selector, entry.getKey(), entry.getValue()));
            }
             */
        }
    }

    private java.util.List<java.lang.String> parseSelectorList(java.io.StreamTokenizer tt) throws java.io.IOException {
        java.util.List<java.lang.String> list = new java.util.ArrayList<java.lang.String>();
        java.lang.StringBuilder selector = new java.lang.StringBuilder();
        boolean needsWhitespace = false;
        while ((tt.nextToken() != java.io.StreamTokenizer.TT_EOF) && (tt.ttype != '{')) {
            switch (tt.ttype) {
                case java.io.StreamTokenizer.TT_WORD :
                    if (needsWhitespace) {
                        selector.append(' ');
                    }
                    selector.append(tt.sval);
                    needsWhitespace = true;
                    break;
                case ',' :
                    list.add(selector.toString());
                    selector.setLength(0);
                    needsWhitespace = false;
                    break;
                default :
                    if (needsWhitespace) {
                        selector.append(' ');
                    }
                    selector.append(((char) (tt.ttype)));
                    needsWhitespace = false;
                    break;
            }
        } 
        if (selector.length() != 0) {
            list.add(selector.toString());
        }
        tt.pushBack();
        // System.out.println("selectors:"+list);
        return list;
    }

    private java.util.Map<java.lang.String, java.lang.String> parseDeclarationMap(java.io.StreamTokenizer tt) throws java.io.IOException {
        java.util.HashMap<java.lang.String, java.lang.String> map = new java.util.HashMap<java.lang.String, java.lang.String>();
        do {
            // Parse key
            java.lang.StringBuilder key = new java.lang.StringBuilder();
            while ((((tt.nextToken() != java.io.StreamTokenizer.TT_EOF) && (tt.ttype != '}')) && (tt.ttype != ':')) && (tt.ttype != ';')) {
                switch (tt.ttype) {
                    case java.io.StreamTokenizer.TT_WORD :
                        key.append(tt.sval);
                        break;
                    default :
                        key.append(((char) (tt.ttype)));
                        break;
                }
            } 
            if ((tt.ttype == '}') && (key.length() == 0)) {
                break;
            }
            if (tt.ttype != ':') {
                throw new java.io.IOException("Declaration ':' missing for " + key);
            }
            // Parse value
            java.lang.StringBuilder value = new java.lang.StringBuilder();
            boolean needsWhitespace = false;
            while (((tt.nextToken() != java.io.StreamTokenizer.TT_EOF) && (tt.ttype != ';')) && (tt.ttype != '}')) {
                switch (tt.ttype) {
                    case java.io.StreamTokenizer.TT_WORD :
                        if (needsWhitespace) {
                            value.append(' ');
                        }
                        value.append(tt.sval);
                        needsWhitespace = true;
                        break;
                    default :
                        value.append(((char) (tt.ttype)));
                        needsWhitespace = false;
                        break;
                }
            } 
            map.put(key.toString(), value.toString());
            // System.out.println("  declaration: "+key+":"+value);
        } while ((tt.ttype != '}') && (tt.ttype != java.io.StreamTokenizer.TT_EOF) );
        tt.pushBack();
        return map;
    }
}