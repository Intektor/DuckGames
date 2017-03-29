package de.intektor.duckgames.common;

/**
 * @author Intektor
 */
public interface BiFunction<T, K, V> {

    T apply(K k, V v);
}
