package com.steel.mapgen.map;

public enum BiomeType {

    RIVER(false, true),

    OCEAN_ABYSSAL(false, true),
    OCEAN_DEEP(false, true),
    OCEAN_SHALLOW(false, true),

    SEA_ICE(false, false),

    BEACH(false, false),
    SCORCHED(false, false),
    BARE(false, false),

    TUNDRA(false, false),
    SNOW(false, false),

    SHRUBLAND(false, false),
    TAIGA(false, false),

    TEMPERATE_DESERT(false, false),
    TEMPERATE_DECIDUOUS_FOREST(false, false),
    TEMPERATE_RAIN_FOREST(false, false),


    SUBTROPICAL_DESERT(false, false),
    GRASSLAND(false, false),
    TROPICAL_SEASONAL_FOREST(false, false),
    TROPICAL_RAIN_FOREST(false, false);


    public final boolean blocks;
    public final boolean water;

    BiomeType(boolean blocks, boolean water) {
        this.blocks = blocks;
        this.water = water;
    }

    // Tree Line - highest survivable trees
    // 4000 near the equator, 2000 near the poles
    // timberline - Highest canopy - forest
    //   Simplified biome chart: http://imgur.com/kM8b5Zq
    public static BiomeType biome(double e, double t, double p) {

        if (e < 0.0 && t < 0.0) return SEA_ICE;

        if (e < -0.75) return OCEAN_ABYSSAL;
        if (e < -0.05) return OCEAN_DEEP;
        if (e < 0.0) return OCEAN_SHALLOW;

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
