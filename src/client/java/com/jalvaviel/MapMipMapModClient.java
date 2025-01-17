package com.jalvaviel;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.opengl.GL11;

import java.util.Map;

public class MapMipMapModClient implements ClientModInitializer {
	public static boolean OUTDATED_DRIVER = false;
	public static int MAP_SIZE = 128;
	private static final Map<Integer,Integer> MIPMAP_TO_ATLAS = Map.of(0,4096,1,2048,2,2048,3,1024,4,1024,5,1024);
	public static int ATLAS_SIZE;
	public static int MAPS_PER_ATLAS;
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
	}

	/**
	 * Checks if the atlas size can and needs to be updated.
	 */
	public static void updateAtlasSize() {
		int mipmapLevel = OUTDATED_DRIVER ? 0 : MinecraftClient.getInstance().options.getMipmapLevels().getValue();
		ATLAS_SIZE = MIPMAP_TO_ATLAS.getOrDefault(mipmapLevel, 1024);
		MAPS_PER_ATLAS = (ATLAS_SIZE / MAP_SIZE) * (ATLAS_SIZE / MAP_SIZE);
		MinecraftClient.getInstance().gameRenderer.getMapRenderer().clearStateTextures(); //getMapTextureManager().clear();
	}
}