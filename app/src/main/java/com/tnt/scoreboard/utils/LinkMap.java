package com.tnt.scoreboard.utils;

import java.util.LinkedHashMap;
import java.util.Set;

public final class LinkMap<K, V> extends LinkedHashMap<K, V> {

    public K getKey(int i) {
        Entry<K, V> entry = getEntry(i);
        return entry == null ? null : entry.getKey();
    }

    public V getValue(int i) {
        Entry<K, V> entry = getEntry(i);
        return entry == null ? null : entry.getValue();
    }

    public Entry<K, V> getEntry(int position) {
        Set<Entry<K, V>> entries = entrySet();
        int i = 0;
        for (Entry<K, V> entry : entries) {
            if (i++ == position) return entry;
        }
        return null;
    }
}
