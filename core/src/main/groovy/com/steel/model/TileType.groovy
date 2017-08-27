package com.steel.model

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureRegion
import groovy.transform.CompileStatic

@CompileStatic
public enum TileType {
    GRASS("Grass", false, Color.GREEN),
    TALL_GRASS("Tall Grass", false, Color.GREEN),
    MUD("Mud", false, Color.BROWN),
    SAND("Sand", false, Color.TAN),
    RICE("Rice Paddy", false, Color.GREEN, Color.BLUE),

    BAMBOO("Bamboo", true, Color.GREEN, Color.DARK_GRAY),
    OAK_TREE("Oak Tree", true, Color.FOREST),

    WOOD_WALL("Wood Wall", true, Color.FIREBRICK),
    STONE_WALL("Stone Wall", true, Color.LIGHT_GRAY),

    WOOD_FLOOR("Wooden Floor", false, Color.BROWN),
    STONE_FLOOR("Stone Floor", false, Color.GRAY),

    WATER("Water", false, Color.BLUE)

    final String cleanName
    final Color color
    final Color background
    TextureRegion texture
    final boolean blocks

    public TileType(String name, boolean blocks, Color color, Color background = null) {
        cleanName = name
        this.blocks = blocks
        this.color = color
        this.background = background ?: new Color((float) (color.r * 0.9f), (float) (color.g * 0.9f), (float) (color.b * 0.9f), 1f);
    }
}
