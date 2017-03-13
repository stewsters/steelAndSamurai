package com.steel.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.steel.SteelSamuraiGame
import com.steel.mapgen.game.Daimyo
import com.steel.mapgen.map.overworld.BiomeType
import groovy.transform.CompileStatic

@CompileStatic
class OverworldScreen implements Screen {
    final SteelSamuraiGame game
    OrthographicCamera camera

    int xCenter = 500;
    int yCenter = 500;

    OverworldScreen(SteelSamuraiGame game) {
        this.game = game
        camera = new OrthographicCamera()
        camera.setToOrtho(false, 800, 480)
    }

    @Override
    void show() {

    }

    @Override
    void render(float delta) {
        // Render the game world
        Gdx.gl.glClearColor(0, 0, 0.1f, 1)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        camera.update()
        game.batch.setProjectionMatrix(camera.combined)

        game.batch.enableBlending();
        game.batch.begin()

        for (int x = 0; x < 40; x++) {
            for (int y = 0; y < 30; y++) {
                int xCur = xCenter + x
                int yCur = yCenter + y

                float elevation = 1 - Math.abs(game.overWorld.getElevation(xCur, yCur));


                BiomeType biome = game.overWorld.getTileType(xCur, yCur)
                game.batch.setColor(biome.background)
                game.batch.draw(game.textureManager.background, x * 16, y * 16, 17, 17)

                if (biome.texture) {
                    game.batch.setColor(
                            biome.color.r * elevation,
                            biome.color.g * elevation,
                            biome.color.b * elevation,
                            1
                    )
                    game.batch.draw(biome.texture, x * 16, y * 16, 17, 17)
                } else {
                    Gdx.app.log("Missing", biome.name().toLowerCase())
                }
            }
        }
        for (Daimyo d : Daimyo.daimyos) {
            int xCur = d.pos.x - xCenter
            int yCur = d.pos.y - yCenter
            game.batch.setColor(d.color)
            game.batch.draw(game.textureManager.daimyo, xCur * 16, yCur * 16, 17, 17)

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
