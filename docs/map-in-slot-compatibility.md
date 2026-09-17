# Map-in-slot compatibility (Minecraft 1.21.11)

MapMipMapMod changes ImmediatelyFast's map atlas dimensions. Its existing
`MapRendererMixinSquared` adjusts world-map UVs, but GUI maps are drawn by
ImmediatelyFast's separate `MixinGuiGraphics.modifyTextureCoordinates` handler.
That handler still divides all four UV bounds by the original `ATLAS_SIZE`
(4096), sampling the wrong area when MapMipMapMod uses a different atlas size.

`DrawContextMixinSquared` applies the same configured atlas size to all four
GUI UV bounds. It leaves slot positions, scaling, map decorations and the
non-atlas rendering path unchanged. No changes to map-in-slot are required.
The injection requires four matches so an incompatible ImmediatelyFast handler
fails visibly instead of silently leaving some UV bounds incorrect.

The build also removes the standalone Sodium API 0.8.0 dependency. Sodium 0.8.2
already bundles the matching API; the older duplicate shadows its config
interfaces and prevents this upstream branch from compiling on a fresh setup.

## Manual regression check

Use Minecraft 1.21.11, Fabric, ImmediatelyFast 1.14.1, a compatible map-in-slot
release, and this MapMipMapMod build. Replace the original MapMipMapMod jar.

1. Use several visibly different filled maps in adjacent inventory and hotbar
   slots. Compare each preview with the map held in hand and in an item frame.
2. Check maps allocated at the start of an atlas, in later columns, and in later
   rows (load enough distinct maps to pass one atlas row).
3. Repeat with the automatic atlas size and two explicit atlas sizes, restarting
   the client after each change. Include 4096 pixels as a control.
4. Check GUI scales, map decorations, stack counts, and inventory tooltips.
5. Disable ImmediatelyFast's map atlas generation and restart: standalone map
   previews should remain correct. Also check world maps without map-in-slot.

Build with Java 21 using `./gradlew build`. Compilation alone does not verify
MixinSquared runtime injection or the in-game visual checks above.

## Validation performed

- Java 21 / Gradle 9.2.0: `build` passed, including access-widener validation
  and remapping the distributable jar. The project has no unit-test sources.
- Fabric Loader 0.18.4 pre-launch smoke check on Minecraft 1.21.11, loading
  ImmediatelyFast 1.14.1, map-in-slot 3.4.1 and Sodium 0.8.2 together: passed.
- The exported, transformed `DrawContext` bytecode contains all four calls to
  the new atlas-size handler and retains map-in-slot's `drawMap` handler.
- The packaged jar identifies itself as `1.3.0+map-in-slot-fix.1`.

The smoke check exits before opening a game window. In-world visual regression
checks listed above have not been performed.
