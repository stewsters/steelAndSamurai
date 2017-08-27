package com.steel.mapgen.map.overworld;

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureRegion
import groovy.transform.CompileStatic;

@CompileStatic
public enum BiomeType {

    VILLIAGE(false, false, Color.BROWN),
    FIELD(false, false, Color.GOLDENROD),

    TEMPLE(false, false, Color.FIREBRICK),
    CITY(false, false, Color.GOLD),
    CASTLE(false, false, Color.WHITE),

    MOUNTAIN(true, false, Color.ORANGE),
    GRASSLAND(false, false, Color.GREEN),

    FOREST(false, false, Color.FOREST),
    DEEP_FOREST(true, false, Color.FOREST, Color.BLACK),
    BAMBOO(false, false, Color.GREEN),

    OCEAN(true, true, Color.BLUE)


    public final boolean blocks
    public final boolean water

    public final Color color
    public final Color background
    public TextureRegion texture

    BiomeType(boolean blocks, boolean water, Color color, Color background = null) {
        this.blocks = blocks
        this.water = water
        this.color = color

        this.background = background ?: new Color((float) (color.r * 0.9f), (float) (color.g * 0.9f), (float) (color.b * 0.9f), 1f)
    }

    // Tree Line - highest survivable trees
    // 4000 near the equator, 2000 near the poles
    // timberline - Highest canopy - forest
    //   Simplified biome chart: http://imgur.com/kM8b5Zq
    public static BiomeType biome(double elev, double temp, double prec) {

        if (elev < 0.0) return OCEAN

        if (elev < 0.3) {
            return GRASSLAND
        }
        if (elev < 0.4) {
            return FOREST
        }
        if (elev < 0.5) {
            return DEEP_FOREST
        }
        return MOUNTAIN

    }

    public byte id() {
        return (byte) ordinal()
    }
}
