/* @(#)Matcher.java

Copyright (c) 2007 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.samples.teddy.regex;
/**
 * Searches for an occurence of a case (in)sensitive text on a document. This is a rather slow
 * implementation that does not use advanced techniques such as Boyer-Moore.
 */
public class Matcher {
    /**
     * The document to be examined.
     */
    private javax.swing.text.Document document;

    /**
     * The string to be matched.
     */
    private java.lang.String findString;

    /**
     * The start index for the next findNext operation.
     */
    private int startIndex;

    /**
     * The array of lower case matching chars.
     */
    private char[] matchLowerCase;

    /**
     * The array of upper case matching chars.
     */
    private char[] matchUpperCase;

    /**
     * The match type.
     */
    private org.jhotdraw.samples.teddy.regex.MatchType matchType;

    /**
     * Creates a new instance of Matcher which performs a case sensitive search.
     *
     * @param document
     * 		The document to be examined
     * @param findString
     * 		The string to be searched.
     */
    public Matcher(javax.swing.text.Document document, java.lang.String findString) {
        this(document, findString, true, org.jhotdraw.samples.teddy.regex.MatchType.CONTAINS);
    }

    /**
     * Creates a new instance of Matcher
     *
     * @param document
     * 		The document to be examined
     * @param findString
     * 		The string to be searched.
     * @param matchCase
     * 		Set to true for case sensitive search.
     * @param matchType
     * 		Sets the match type.
     */
    public Matcher(javax.swing.text.Document document, java.lang.String findString, boolean matchCase, org.jhotdraw.samples.teddy.regex.MatchType matchType) {
        this.document = document;
        this.findString = findString;
        startIndex = 0;
        // Convert to chars for efficiency
        if (matchCase) {
            matchLowerCase = matchUpperCase = findString.toCharArray();
        } else {
            matchUpperCase = findString.toUpperCase().toCharArray();
            matchLowerCase = findString.toLowerCase().toCharArray();
        }
        this.matchType = matchType;
    }

    public java.lang.String getFindString() {
        return findString;
    }

    public boolean isMatchCase() {
        return matchLowerCase == matchUpperCase;
    }

    public org.jhotdraw.samples.teddy.regex.MatchType getMatchType() {
        return matchType;
    }

    /**
     * Sets the start index for the findNext(), findPrevious() methods.
     */
    public void setStartIndex(int newValue) {
        startIndex = newValue;
    }

    /**
     * Resets this matcher and then attempts to find the next subsequence of the input sequence that
     * matches the pattern, starting at the specified index.
     *
     * @param startIndex
     * 		the index from which to start the search.
     * @return the index of the first occurrence of the search string, starting at the specified
    offset, or -1 if no occurrence was found.
     */
    public int findNext(int startIndex) {
        this.startIndex = startIndex;
        return findNext();
    }

    /**
     * Attempts to find the next subsequence of the input sequence that matches the pattern.
     *
     * <p>This method starts at the beginning of the input sequence or, if a previous invocation of
     * the method was successful and the matcher has not since been reset, at the first character not
     * matched by the previous match.
     *
     * @return the index of the first occurrence of the search string, starting at the specified
    offset, or -1 if no occurrence was found.
     */
    public int findNext() {
        // Don't match empty strings and don't match if we are at the end of the document.
        if ((findString.length() == 0) || ((document.getLength() - findString.length()) < startIndex)) {
            return -1;
        }
        try {
            int nextMatch = 0;// index of next matching character

            // Iterate through all segments of the document starting from offset
            javax.swing.text.Segment text = new javax.swing.text.Segment();
            text.setPartialReturn(true);
            int offset = startIndex;
            int nleft = document.getLength() - startIndex;
            while (nleft > 0) {
                document.getText(offset, nleft, text);
                // Iterate through the characters in the current segment
                char next = text.first();
                for (text.first(); next != javax.swing.text.Segment.DONE; next = text.next()) {
                    // Check if the current character matches with the next
                    // search character.
                    char current = text.current();
                    if ((current == matchUpperCase[nextMatch]) || (current == matchLowerCase[nextMatch])) {
                        nextMatch++;
                        // Did we match all search characters?
                        if (nextMatch == matchLowerCase.length) {
                            int foundIndex = (((text.getIndex() - text.getBeginIndex()) + offset) - matchLowerCase.length) + 1;
                            switch (matchType) {
                                case CONTAINS :
                                    return foundIndex;
                                    // break; <- never reached
                                case STARTS_WITH :
                                    if (!isWordChar(foundIndex - 1)) {
                                        return foundIndex;
                                    }
                                    break;
                                case FULL_WORD :
                                    if ((!isWordChar(foundIndex - 1)) && (!isWordChar(foundIndex + matchLowerCase.length))) {
                                        return foundIndex;
                                    }
                                    break;
                            }
                            nextMatch = 0;
                        }
                    } else {
                        nextMatch = 0;
                    }
                }
                // Move forward to the next segment
                nleft -= text.count;
                offset += text.count;
            } 
            return -1;
        } catch (javax.swing.text.BadLocationException e) {
            throw new java.lang.IndexOutOfBoundsException();
        }
    }

    /**
     * Resets this matcher and then attempts to find the previous subsequence of the input sequence
     * that matches the pattern, starting at the specified index.
     *
     * @param startIndex
     * 		the index from which to start the search.
     * @return the index of the first occurrence of the search string, starting at the specified
    offset, or -1 if no occurrence was found.
     */
    public int findPrevious(int startIndex) {
        this.startIndex = startIndex;
        return findPrevious();
    }

    /**
     * Attempts to find the previous subsequence of the input sequence that matches the pattern.
     *
     * <p>This method starts at the beginning of the input sequence or, if a previous invocation of
     * the method was successful and the matcher has not since been reset, at the first character not
     * matched by the previous match.
     *
     * @return the index of the first occurrence of the search string, starting at the specified
    offset, or -1 if no occurrence was found.
     */
    public int findPrevious() {
        // Don't match empty strings and don't match if we are at the beginning of the document.
        if ((findString.length() == 0) || (startIndex < (findString.length() - 1))) {
            // System.out.println("too close to start");
            return -1;
        }
        try {
            int nextMatch = matchLowerCase.length - 1;// index of next matching character

            // For simplicity, we request all text of the document in a single
            // segment.
            javax.swing.text.Segment text = new javax.swing.text.Segment();
            text.setPartialReturn(false);
            document.getText(0, startIndex + 1, text);
            // Iterate through the characters in the current segment
            char previous = text.last();
            // System.out.println("previus isch "+previous);
            for (text.last(); previous != javax.swing.text.Segment.DONE; previous = text.previous()) {
                // Check if the current character matches with the next
                // search character.
                char current = text.current();
                if ((current == matchUpperCase[nextMatch]) || (current == matchLowerCase[nextMatch])) {
                    nextMatch--;
                    // System.out.println("matched "+nextMatch);
                    // Did we match all search characters?
                    if (nextMatch == (-1)) {
                        int foundIndex = text.getIndex() - text.getBeginIndex();
                        // System.out.println("found index:"+foundIndex);
                        if (matchType == org.jhotdraw.samples.teddy.regex.MatchType.CONTAINS) {
                            return foundIndex;
                        } else if (matchType == org.jhotdraw.samples.teddy.regex.MatchType.STARTS_WITH) {
                            if (!isWordChar(foundIndex - 1)) {
                                return foundIndex;
                            }
                        } else if (matchType == org.jhotdraw.samples.teddy.regex.MatchType.FULL_WORD) {
                            if ((!isWordChar(foundIndex - 1)) && (!isWordChar(foundIndex + matchLowerCase.length))) {
                                return foundIndex;
                            }
                        }
                        nextMatch = matchLowerCase.length - 1;
                    }
                } else {
                    nextMatch = matchLowerCase.length - 1;
                }
            }
            return -1;
        } catch (javax.swing.text.BadLocationException e) {
            throw new java.lang.IndexOutOfBoundsException();
        }
    }

    /**
     * Resets the startIndex of the matcher to 0.
     */
    public void reset() {
        startIndex = 0;
    }

    private boolean isWordChar(int index) {
        try {
            char ch = document.getText(index, 1).charAt(0);
            return java.lang.Character.isLetterOrDigit(ch);
        } catch (javax.swing.text.BadLocationException e) {
            return false;
        }
    }
}