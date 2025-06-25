package com.jalvaviel.config;

import com.jalvaviel.MapMipMapModClient;

import java.io.IOException;

/**
 * <h1>MmmmOptionsStorage class</h1>
 * A lightweight class that provides an interface to communicate with the MmmmGameOptions instance to save it to disk and get its options.
 */
public class MmmmOptionsStorage implements OptionStorage<MmmmGameOptions> {
    private final MmmmGameOptions options = MapMipMapModClient.options();

    public MmmmOptionsStorage() {
    }

    /**
     * Getter for the running config.
     * @return the MmmmGameOptions singleton reference.
     */
    public MmmmGameOptions getData() {
        return this.options;
    }

    /**
     * Wrapper for the writeToDisk method that saves the running config to the config file.
     */
    public void save() {
        try {
            MmmmGameOptions.writeToDisk(this.options);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't save configuration changes", e);
        }
    }
}
