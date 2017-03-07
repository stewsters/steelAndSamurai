package com.steel.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.steel.mapgen.map.battle.TileType
import com.steel.mapgen.map.overworld.BiomeType

class TextureManager {

    TextureAtlas atlas

    public void init() {


        FileHandle fh = Gdx.files.internal("tile.atlas");
        atlas = new TextureAtlas(fh);

        TileType.values().each {
            it.texture = atlas.findRegion("battlemap/" + it.name());
        }

        BiomeType.values().each {
            it.texture = atlas.findRegion("worldmap/" + it.name());
        }

    }

    public void destroy() {
        atlas.dispose()
    }

}
