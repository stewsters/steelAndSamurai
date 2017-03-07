package com.steel.mapgen.map.battle

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureRegion
import groovy.transform.CompileStatic

@CompileStatic
public enum TileType {
    GRASS("Grass", Color.GREEN),
    TALL_GRASS("Tall Grass", Color.GREEN),
    MUD("Mud", Color.BROWN),
    SAND("Sand", Color.TAN),
    RICE("Rice Paddy", Color.GREEN, Color.BLUE),

    BAMBOO("Bamboo", Color.GREEN, Color.DARK_GRAY),
    OAK_TREE("Oak Tree", Color.FOREST),

    WOOD_WALL("Wood Wall", Color.FIREBRICK),
    STONE_WALL("Stone Wall", Color.LIGHT_GRAY),

    WOOD_FLOOR("Wooden Floor", Color.BROWN),
    STONE_FLOOR("Stone Floor", Color.GRAY),

    WATER("Water", Color.BLUE)

    String cleanName
    Color color
    Color background
    TextureRegion texture

    public TileType(String name, Color color, Color background = null) {
        cleanName = name
        this.color = color
        this.background = background ?: new Color((float) (color.r * 0.9f), (float) (color.g * 0.9f), (float) (color.b * 0.9f), 1f);
    }
}
