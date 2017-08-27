package com.steel.model

class Tile {
    public static final int size = 16;
    int x, y

    public boolean blocks

    public Tile(int x, int y, boolean blocks = true) {
        this.x = x
        this.y = y
        this.blocks = blocks
    }

}
