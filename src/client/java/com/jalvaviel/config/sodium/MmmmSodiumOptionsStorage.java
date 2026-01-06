package com.jalvaviel.config.sodium;

import com.jalvaviel.MapMipMapModClient;
import com.jalvaviel.config.MmmmGameOptions;

import java.io.IOException;

public class MmmmSodiumOptionsStorage {
    private final MmmmGameOptions options = MapMipMapModClient.options();

    public MmmmSodiumOptionsStorage() {
    }

    /**
     * Getter for the running config.
     * @return the MmmmGameOptions singleton reference.
     */
    public MmmmGameOptions getData() {
        return this.options;
    }

    public void save() {
        try {
            MmmmGameOptions.writeToDisk(this.options);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't save configuration changes", e);
        }
    }
}
