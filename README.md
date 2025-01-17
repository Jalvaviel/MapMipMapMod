# MapMipMapMod
When maps in-game are rendered far away from the player, they cause artifacts. These artifacts happen because mipmaps for dynamic textures are disabled by default in Minecraft. This mod aims to solve this niche problem.

Image on the left is with level 0 mipmaps (default in-game) and the image on the right is with level 4 mipmaps (with the mod).
![imagen](https://github.com/user-attachments/assets/d76ed9df-9245-4fb9-8a61-b77c2d4d38a3)
The image has been rendered 10 blocks away from the player, using a palette of only flat carpet colors. The image has been preprocessed with Floyd-Steinberg dithering.

Thanks to **EarilGarion** for his infinite patience and his OpenGL explanations.

---

### New version of MapMipMapMod with big performance improvements.

- Uses map texture atlasing to store multiple textures in a single one and boost performance.
- Generates mipmaps on the GPU instead of the CPU (vanilla). Doesn't compromise on the performance.
- Doesn't update textures on locked maps, removing lag spikes when rendering many maps.

### Benchmark
- GPU: RTX 3060 (laptop)
- CPU: i7-11800H
- RAM: 32GB
- Maps: 1260
- Empty world.

| Mipmaps | MapMipMapMod + ImmediatelyFast + Sodium | MapMipMapMod + ImmediatelyFast | Vanilla |
|---------|-----------------------------------------|--------------------------------|---------|
| 0-4     | 200 fps                                 | 130 fps                        | 50 fps  |
