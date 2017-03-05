package com.steel

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.steel.screens.MainMenuScreen
import com.steel.utils.SamuraiNameGen
import groovy.transform.CompileStatic

@CompileStatic
public class SteelSamuraiGame extends Game {
    SpriteBatch batch
    BitmapFont font
    SamuraiNameGen nameGen

    @Override
    public void create() {
        batch = new SpriteBatch()
        font = new BitmapFont();
        nameGen = new SamuraiNameGen()
        nameGen.init(Gdx.files.internal('samuraiNames.txt').file())

        this.setScreen(new MainMenuScreen(this))

        // Testing samurai name generation

//        10.times {
//            Gdx.app.log this.class.name, nameGen.gen()
//        }

    }

    @Override
    public void render() {
        super.render()
    }

    @Override
    public void dispose() {
        batch.dispose()
        font.dispose()
    }
}
