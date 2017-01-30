package com.externalalarmclock.app.util;

/**
 * Stores a key value pair.
 */
public class Tuple {
    private String key;
    private String value;

    public Tuple(String key, String value) {
        this.key = key;
        this.value = value;
    }

    String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
