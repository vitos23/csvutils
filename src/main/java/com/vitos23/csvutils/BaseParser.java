package com.vitos23.csvutils;

import java.util.NoSuchElementException;

abstract class BaseParser {
    private final String source;
    private int pos;

    private final char[] lineSeparators = {'\n', '\r', '\u000b', '\u000c', '\u0085', '\u2028', '\u2029'};

    public BaseParser(String source) {
        this.source = source;
        this.pos = 0;
    }

    protected boolean hasNext() {
        return pos < source.length();
    }

    protected boolean test(char c) {
        return hasNext() && c == source.charAt(pos);
    }

    protected boolean test(String s) {
        if (source.length() - pos < s.length()) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            if (source.charAt(pos + i) != s.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    protected char next() {
        if (!hasNext()) {
            throw new NoSuchElementException("Source is exhausted!");
        }
        return source.charAt(pos++);
    }

    protected boolean take(char c) {
        if (test(c)) {
            pos++;
            return true;
        }
        return false;
    }

    protected boolean take(String s) {
        if (test(s)) {
            pos += s.length();
            return true;
        }
        return false;
    }

    protected boolean takeEndOfLine() {
        if (!take("\r\n")) {
            for (char c : lineSeparators) {
                if (take(c)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    protected boolean testEndOfLine() {
        int cachedPos = pos;
        if (takeEndOfLine()) {
            pos = cachedPos;
            return true;
        }
        return !hasNext();
    }

    protected String getCurrentChar() {
        if (pos < source.length()) {
            return String.valueOf(source.charAt(pos));
        }
        return "end of string";
    }
}
