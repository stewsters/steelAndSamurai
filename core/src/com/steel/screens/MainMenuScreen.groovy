package com.steel.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.steel.SteelSamuraiGame

class MainMenuScreen implements Screen {
    final SteelSamuraiGame game
    Texture img

    MainMenuScreen(SteelSamuraiGame game) {
        this.game = game
    }

    @Override
    void show() {
        img = new Texture("badlogic.jpg")
    }

    @Override
    void render(float delta) {

        Gdx.gl.glClearColor(1, 0, 0, 1)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        game.batch.begin()
        game.batch.draw(img, 20, 30)
        game.batch.end()
    }

    @Override
    void resize(int width, int height) {

    }

    @Override
    void pause() {

    }

    @Override
    void resume() {

    }

    @Override
    void hide() {

    }

    @Override
    void dispose() {
        img.dispose()
    }
}
