package com.steel.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.steel.SteelSamuraiGame
import com.steel.mapgen.map.overworld.BiomeType
import groovy.transform.CompileStatic

@CompileStatic
class OverworldScreen implements Screen {
    final SteelSamuraiGame game
    OrthographicCamera camera

    OverworldScreen(SteelSamuraiGame game) {
        this.game = game
        camera = new OrthographicCamera()
        camera.setToOrtho(false, 800, 480)
    }

    @Override
    void show() {

    }
    int xCenter = 500;
    int yCenter = 500;

    @Override
    void render(float delta) {
        // Render the game world
        Gdx.gl.glClearColor(0, 0, 0.1f, 1)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        camera.update()
        game.batch.setProjectionMatrix(camera.combined)

        game.batch.begin()

        for (int x = 0; x < 40; x++) {
            for (int y = 0; y < 30; y++) {
                int xCur = xCenter+x
                int yCur = yCenter+y

                BiomeType biome = game.overWorld.getTileType(xCur, yCur)
                if (biome.texture) {
                    game.batch.setColor(biome.color)
                    game.batch.draw(biome.texture, x * 16, y * 16, 16, 16)
                } else {
                    Gdx.app.log("Missing", biome.name().toLowerCase())
                }
            }
        }

        game.batch.end()

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            //move left
            xCenter--
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            //move right
            xCenter++
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            //move up
            yCenter++
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            //move down
            yCenter--
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

    }
}
