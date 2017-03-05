package com.steel.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.steel.SteelSamuraiGame
import groovy.transform.CompileStatic

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

@CompileStatic
class GenerationScreen implements Screen {
    final SteelSamuraiGame game
    OrthographicCamera camera;
    String lastStep
    ExecutorService executor
    Future future

    GenerationScreen(SteelSamuraiGame game) {
        this.game = game
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        lastStep = "Starting"

        executor = Executors.newFixedThreadPool(1);

        future = executor.submit({
            lastStep = "Started"

            return ;
        });

    }

    @Override
    void show() {

    }

    @Override
    void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.largeFont.draw(game.batch, "Generating", 100, 150);
        game.largeFont.draw(game.batch, lastStep, 100, 100);
        game.batch.end();

        if(future.isDone()){

            game.setScreen(new OverworldScreen(game))
            dispose()
        }

//        if (input.isButtonPressed(Input.Keys.N)) {
//            game.setScreen(new GenerationScreen(game))
//            dispose()
//        } else if (input.isButtonPressed(Input.Keys.ESCAPE)) {
//            Gdx.app.exit()
//        }
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

    }
}
