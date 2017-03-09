package com.steel.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.steel.SteelSamuraiGame
import com.steel.mapgen.game.Daimyo
import com.steel.mapgen.game.Settlement
import com.steel.mapgen.map.overworld.OverWorld
import com.steel.mapgen.procGen.WorldGenerator
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

    WorldGenerator worldGenerator

    GenerationScreen(SteelSamuraiGame game) {
        this.game = game
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        lastStep = "Starting"

        worldGenerator = new WorldGenerator()
        executor = Executors.newFixedThreadPool(1);

        future = executor.submit({
            int xSize = 32;
            int ySize = 16;
            OverWorld overWorld = new OverWorld(xSize, ySize);

            lastStep += "\nStarting Elevation... "

            worldGenerator.generateElevation(overWorld);

            lastStep += "Elevation Finished.\nDropping Edges... "
            worldGenerator.dropEdges(overWorld);
            lastStep += "Edges Dropped.\nGenerating Continents... "

            worldGenerator.generateContinents(overWorld);
            lastStep += "Continents Genereated.\nPopulating Settlements... "

            worldGenerator.populateSettlements(overWorld);
            lastStep += "Settlements Populated.\nExpanding Realms... "

            //We can do this, but it takes forever
            // worldGenerator.createRoadNetwork(overWorld);
            // lastStep = "Generating Roads"

            worldGenerator.expandRealms(overWorld);
            lastStep += "Realms Expanded.\nAdding Armies... "

            game.overWorld = overWorld;

            Settlement.settlements.each {
                it.ruler = Daimyo.build(it.pos.x, it.pos.y, game.nameGen.gen())
            }

            lastStep += "Added.\nDone."
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
        game.largeFont.draw(game.batch, "Generating", 100, 330);

        lastStep.split("\n").eachWithIndex { String line, int i ->
            game.largeFont.draw(game.batch, line, 100, 300 - (i * 16));
        }

        game.batch.end();

        if (future.isDone()) {
            game.setScreen(new OverworldScreen(game))
            dispose()
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
