package com.jalvaviel.config;

/**
 * This interface is just a copy-paste from the sodium interface of the same name for consistency between screen builders.
 * @param <T> Generic for the class of the config.
 */
public interface OptionStorage<T> {
    T getData();
    void save();
}
