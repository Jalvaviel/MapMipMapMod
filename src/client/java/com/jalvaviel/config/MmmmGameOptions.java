package com.jalvaviel.config;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static com.jalvaviel.MapMipMapModClient.*;

/**
 * <h1>MmmmGameOptions class</h1>
 * It has all the File IO to save and load from a json file, as well as a GeneralOptions object to better organise all the config options in case they get expanded.
 * Only a single instance should be active at any moment, and should be accessible via the mod's entrypoint.
 */
public class MmmmGameOptions {
    private static final String DEFAULT_FILE_NAME = "mapmipmapmod-options.json";

    public final GeneralOptions generalOptions = new GeneralOptions();

    private boolean readOnly;
    private static final Gson GSON;

    private MmmmGameOptions() {
    }

    /**
     * Default config getter
     * @return A new object instance with the default options.
     */
    @Contract(" -> new")
    public static @NotNull MmmmGameOptions defaults() {
        return new MmmmGameOptions();
    }

    /**
     * Loads the config file into memory using GSON. It updates the config file afterwards.
     * It gets called in the initialization process to save it as a singleton instance in MapMipMapModClient entrypoint.
     * @return The config as a MmmmGameOptions class.
     */
    public static MmmmGameOptions loadFromDisk() {
        Path path = getConfigPath();
        MmmmGameOptions config;
        if (Files.exists(path)) {
            try (FileReader reader = new FileReader(path.toFile())) {
                config = GSON.fromJson(reader, MmmmGameOptions.class);
            } catch (IOException e) {
                throw new RuntimeException("Could not parse config", e);
            }
        } else {
            config = new MmmmGameOptions();
        }

        try {
            writeToDisk(config);
            return config;
        } catch (IOException e) {
            throw new RuntimeException("Couldn't update config file", e);
        }
    }

    /**
     * Resolves the Path for the config file.
     * @return the config file path.
     */
    private static @NotNull Path getConfigPath() {
        return FabricLoader.getInstance().getConfigDir().resolve(DEFAULT_FILE_NAME);
    }

    /**
     * Writes the config file from memory using GSON. It gets called using the save() method in OptionStorage.
     * @param config The MmmmGameOptions instance to be saved.
     * @throws IOException If the config is read-only.
     * @see OptionStorage
     */
    public static void writeToDisk(@NotNull MmmmGameOptions config) throws IOException {
        if (config.isReadOnly()) {
            throw new IllegalStateException("Config file is read-only");
        } else {
            Path path = getConfigPath();
            Path dir = path.getParent();
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            } else if (!Files.isDirectory(dir)) {
                throw new IOException("Not a directory: " + dir);
            }

            writeTextRobustly(GSON.toJson(config), path);
        }
    }

    /**
     * Just a copy-paste from Sodium's SodiumGameOptions to change the temporary file to a definitive one.
     * @param text The JSON string provided by GSON.
     * @param path The path to save it to.
     * @throws IOException If the config is read-only.
     * @see net.caffeinemc.mods.sodium.client.gui.SodiumGameOptions
     */
    private static void writeTextRobustly(String text, @NotNull Path path) throws IOException {
        Path tempPath = path.resolveSibling(path.getFileName() + ".tmp");
        Files.writeString(tempPath, text);
        Files.move(tempPath, path, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
    }

    public boolean isReadOnly() {
        return this.readOnly;
    }

    public void setReadOnly() {
        this.readOnly = true;
    }

    static {
        GSON = (new GsonBuilder()).setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();
    }

    /**
     * <h2>GeneralOptions class</h1>
     * This is an internal class that wraps the actual options for MapMipMapMod configuration.
     * In case the config needs to be expanded, it can use other option subclasses to be serialized separately by MmmmGameOptions.
     */
    public static class GeneralOptions {
        private int mapmipmapLevels = -1;
        private int atlasSize = 0;
        private boolean lockedMapUpdates = true;
        public GeneralOptions() {}

        // Mipmap levels with defaults
        public int getMapmipmapLevels() {
            return OUTDATED_DRIVER ? 0 : (this.mapmipmapLevels <= -1 ? MinecraftClient.getInstance().options.getMipmapLevels().getValue() : this.mapmipmapLevels);
        }

        // Mipmap levels for "auto" display on sodium MmmmGameOptionsPages
        public int getLiteralMapmipmapLevels() {
            return this.mapmipmapLevels;
        }

        // Atlas size in pixels
        public int getAtlasSize() {
            return this.atlasSize <= 0 ? 2048-getMapmipmapLevels()*128 : this.atlasSize * MAP_SIZE;
        }

        // Atlas size in maps
        public int getLiteralAtlasSize() {
            return this.atlasSize;
        }

        public boolean isLockedMapUpdates() {
            return this.lockedMapUpdates;
        }

        public void setMapmipmapLevels(int mapmipmapLevels) {
            this.mapmipmapLevels = mapmipmapLevels;
        }

        public void setAtlasSize(int atlasSize) {
            this.atlasSize = atlasSize;
        }

        public void setLockedMapUpdates(boolean lockedMapUpdates) {
            this.lockedMapUpdates = lockedMapUpdates;
        }
    }
}
