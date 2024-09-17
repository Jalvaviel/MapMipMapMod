# MapMipMapMod
When maps in-game are rendered far away from the player, they cause artifacts. These artifacts happen because mipmaps for dynamic textures are disabled by default in Minecraft. This mod aims to solve this niche problem.
Image on the left is with level 0 mipmaps (default in-game) and the image on the right is with level 4 mipmaps (with the mod).
![imagen](https://github.com/user-attachments/assets/d76ed9df-9245-4fb9-8a61-b77c2d4d38a3)

The image has been rendered 10 blocks away from the player, using a palette of only flat carpet colors. The image has been preprocessed with Floyd-Steinberg dithering.
