package com.steel.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.steel.model.TileType
import com.steel.mapgen.map.overworld.BiomeType

class TextureManager {

    TextureAtlas atlas
     TextureRegion background
    TextureRegion daimyo

    public void init() {


        FileHandle fh = Gdx.files.internal("tile.atlas");
        atlas = new TextureAtlas(fh);

        TileType.values().each {
            it.texture = atlas.findRegion("battlemap/" + it.name().toLowerCase());
        }

        BiomeType.values().each {
            it.texture = atlas.findRegion("worldmap/" + it.name().toLowerCase());
        }

        background = atlas.findRegion("background")
        daimyo = atlas.findRegion("characters/daimyo")
    }

    public void destroy() {
        atlas.dispose()
    }

}
