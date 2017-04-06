package de.framey.lab.evil.eviltentaclesofdeath.util;

import java.util.Map;
import java.util.function.Supplier;

public interface DefaultableMap<K, V> extends Map<K, V> {

    default V get(K key, V defaultValue) {
        return get(key, () -> defaultValue);
    }

    default V get(K key, Supplier<V> supplier) {
        V value = get(key);
        if (value == null) {
            value = supplier.get();
            put(key, value);
        }
        return value;
    }
}
