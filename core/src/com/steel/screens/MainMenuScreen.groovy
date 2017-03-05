package com.steel.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.steel.SteelSamuraiGame
import groovy.transform.CompileStatic

import static com.badlogic.gdx.Gdx.input

@CompileStatic
class MainMenuScreen implements Screen {
    final SteelSamuraiGame game
    OrthographicCamera camera;

    Texture img



    MainMenuScreen(SteelSamuraiGame game) {
        this.game = game

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

    @Override
    void show() {
        img = new Texture("badlogic.jpg")
    }

    @Override
    void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(img, 20, 30)
        game.largeFont.draw(game.batch, "Steel and Samurai", 100, 150);
        game.largeFont.draw(game.batch, "N to start a new game", 100, 100);
        game.batch.end();

        if (input.isButtonPressed(Input.Keys.N)) {
            game.setScreen(new GenerationScreen(game))
            dispose()
        } else if (input.isButtonPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit()
        }

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
