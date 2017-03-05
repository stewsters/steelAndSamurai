package com.steel

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.steel.mapgen.map.overworld.OverWorld
import com.steel.screens.MainMenuScreen
import com.steel.utils.SamuraiNameGen
import groovy.transform.CompileStatic

@CompileStatic
public class SteelSamuraiGame extends Game {

    // The game can be accessed by any of the screens, so anything that you
    // may need in multiple locations can go here
    SpriteBatch batch
    BitmapFont font
    BitmapFont largeFont
    SamuraiNameGen nameGen
    OverWorld overWorld = null

    @Override
    public void create() {
        batch = new SpriteBatch()
        font = new BitmapFont();
        largeFont = new BitmapFont(); // TODO: we need a large title font

        nameGen = new SamuraiNameGen()
        nameGen.init(Gdx.files.internal('samuraiNames.txt').file())

        this.setScreen(new MainMenuScreen(this))

    }

    @Override
    public void render() {
        super.render()
    }

    @Override
    public void dispose() {
        batch.dispose()
        font.dispose()
        largeFont.dispose()
    }
}
