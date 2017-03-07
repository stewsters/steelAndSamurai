package com.steel.mapgen.map.overworld;

import com.badlogic.gdx.graphics.Color
import groovy.transform.CompileStatic;

@CompileStatic
public enum BiomeType {

    VILLIAGE(false, false, Color.BROWN),
    FIELD(false, false, Color.GOLDENROD),

    TEMPLE(false, false, Color.FIREBRICK),
    CITY(false, false, Color.GOLD),
    CASTLE(false, false, Color.WHITE),

    MOUNTAIN(true, false, Color.BLUE),
    GRASSLAND(false, false, Color.GREEN),

    FOREST(false, false, Color.FOREST),
    DEEP_FOREST(true, false, Color.FOREST, Color.BLACK),
    BAMBOO(false, false, Color.GREEN),

    OCEAN(true, true, Color.BLUE);


    public final boolean blocks;
    public final boolean water;

    public final Color color;
    public final Color background;

    BiomeType(boolean blocks, boolean water, Color color, Color background = null) {
        this.blocks = blocks;
        this.water = water;
        this.color = color;

        this.background = background ?: new Color((float)(color.r * 0.9f), (float)(color.g * 0.9f), (float)(color.b * 0.9f), 1f);
    }

    // Tree Line - highest survivable trees
    // 4000 near the equator, 2000 near the poles
    // timberline - Highest canopy - forest
    //   Simplified biome chart: http://imgur.com/kM8b5Zq
    public static BiomeType biome(double e, double t, double s) {

        if (e < 0.0) return OCEAN;

        if (e > 0.8 || s > 0.5) {
            return MOUNTAIN;
        }

        if (t < 0) return SNOW;
        if (e > 0.7) { // Above Treeline
            return BARE;
        }

        if (e < 0.01) {
            return BEACH;
        }

        if (t < 0) {
            if (p < 0.1) return SCORCHED;
            if (p < 0.2) return BARE;
            if (p < 0.5) return TUNDRA;
            return SNOW;
        }

        if (t < 0.40) {
            if (p < 0.2) return TEMPERATE_DESERT;
            if (p < 0.66) return SHRUBLAND;
            return TAIGA;
        }

        if (t < 0.6) {
            if (p < 0.16) return TEMPERATE_DESERT;
            if (p < 0.50) return GRASSLAND;
            if (p < 0.83) return TEMPERATE_DECIDUOUS_FOREST;
            return TEMPERATE_RAIN_FOREST;
        }

        if (p < 0.10) return SUBTROPICAL_DESERT;
        if (p < 0.33) return GRASSLAND;
        if (p < 0.66) return TROPICAL_SEASONAL_FOREST;
        return TROPICAL_RAIN_FOREST;
    }

    public byte id() {
        return (byte) ordinal();
    }
}
