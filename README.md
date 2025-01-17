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
