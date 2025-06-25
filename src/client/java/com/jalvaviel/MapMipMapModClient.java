package com.jalvaviel;

import com.jalvaviel.config.MmmmGameOptions;
import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MapMipMapModClient implements ClientModInitializer {
	public static final Logger LOG = LogManager.getLogger("MapMipMapMod");

	private static MmmmGameOptions CONFIG;
	public static boolean OUTDATED_DRIVER = false;
	public static int MAP_SIZE = 128;

	/**
	 * Loads the config file when the client initializes.
	 */
	@Override
	public void onInitializeClient() {
		CONFIG = loadConfig();
	}

	/**
	 * Getter for the options.
	 * @return the MapMipMapMod options running instance.
	 */
	public static MmmmGameOptions options() {
		if (CONFIG == null) {
			throw new IllegalStateException("Config not yet available.");
		} else {
			return CONFIG;
		}
	}

	/**
	 * Loads the MapMipMapMod config file.
	 * @return the MapMipMapMod options from the config file.
	 */
	private static MmmmGameOptions loadConfig() {
		try {
			return MmmmGameOptions.loadFromDisk();
		} catch (Exception e) {
			LOG.error("Failed to load configuration file", e);
			LOG.error("Using default configuration file in read-only mode");
			MmmmGameOptions config = MmmmGameOptions.defaults();
			config.setReadOnly();
			return config;
		}
	}
}