package com.steel.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.steel.SteelSamuraiGame
import com.steel.model.Formation
import com.steel.model.Soldier
import com.steel.model.World
import groovy.transform.CompileStatic

@CompileStatic
class BattleScreen implements Screen, InputProcessor {

    public final static float SCALE = 32f
    public final static float INV_SCALE = 1f / SCALE as float
    // this is our "target" resolution, not that the window can be any size,
    // it is not bound to this one
    public final static float VP_WIDTH = 1280 * INV_SCALE
    public final static float VP_HEIGHT = 720 * INV_SCALE

    final SteelSamuraiGame game
    private OrthographicCamera camera
    private ExtendViewport viewport
    private ShapeRenderer shapeRenderer

    World world

    Vector2 lClickStart
    Vector2 rClickStart

    int mouseX = 0
    int mouseY = 0

    BattleScreen(SteelSamuraiGame game) {
        this.game = game

        //TODO: needs the 2 armies that need to fight it out, playerArmy and enemyArmy

        // Needs enough info to generate a local map

        world = new World()

        camera = new OrthographicCamera()

        viewport = new ExtendViewport(VP_WIDTH, VP_HEIGHT, camera)

        shapeRenderer = new ShapeRenderer()
        Gdx.input.setInputProcessor(this)
    }

    @Override
    void show() {

    }

    @Override
    void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.1f, 1)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        camera.update()
        game.batch.setProjectionMatrix(camera.combined)

        game.batch.enableBlending()
        game.batch.begin()

        game.batch.setColor(1, 1, 1, 1)
        for (int x = 0; x < world.worldSize; x++) {
            for (int y = 0; y < world.worldSize; y++) {
                game.batch.draw(world.tiles[x][y].texture, x * 16, y * 16, 17, 17)
            }
        }

        world.units.each {

            if (it.selected)
                game.batch.setColor(1, 0, 0, 1)
            else
                game.batch.setColor(0, 1, 0, 1)

            game.batch.draw(game.textureManager.daimyo, it.location.x * 16, it.location.y * 16, 17, 17)

        }

        game.batch.end()

        shapeRenderer.setProjectionMatrix(camera.combined)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)


        if (world.formations) {
            for (Formation formation : world.formations) {
                shapeRenderer.setColor(0.25f, 1f,0.25f,1f)
                shapeRenderer.line(formation.left.x, formation.left.y, formation.right.x, formation.right.y)


                shapeRenderer.line(
                        formation.left.x - (formation.unitForward.x * formation.rank) as float,
                        formation.left.y - (formation.unitForward.y * formation.rank) as float,
                        formation.right.x - (formation.unitForward.x * formation.rank) as float,
                        formation.right.y - (formation.unitForward.y * formation.rank) as float)

                shapeRenderer.setColor(0.25f, 0.25f,1f,1f)

                shapeRenderer.line(formation.center.x, formation.center.y,
                        (formation.center.x + formation.unitForward.x * 50) as float,
                        (formation.center.y + formation.unitForward.y * 50) as float)
            }
        }

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && lClickStart) {
            shapeRenderer.setColor(1, 1, 0.2f, 1)
            shapeRenderer.rect(lowLX, lowLY, highLX - lowLX, highLY - lowLY)

        } else if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            shapeRenderer.setColor(1, 1, 0.2f, 1)
            shapeRenderer.line(rClickStart.x, rClickStart.y, mouseX, mouseY)
        }

        shapeRenderer.end()


    }

    @Override
    void resize(int width, int height) {
        viewport.update(width, height, true)
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
        shapeRenderer.dispose()
    }


    @Override
    boolean keyDown(int key) {
        if (key == Input.Keys.W) {
            world.moveForward(10)
        } else if (key == Input.Keys.S) {
            world.moveForward(-10)
        } else if (key == Input.Keys.A) {
            world.moveRight(-10)

        } else if (key == Input.Keys.D) {
            world.moveRight(10)

        } else if (key == Input.Keys.Q) {
            world.turnRight(-0.5f)
        } else if (key == Input.Keys.E) {
            world.turnRight(0.5f)
        } else if (key == Input.Keys.F) {
            println("Forming")

            List<Soldier> selectedUnits = world.units.findAll { it.selected && it.formation == null }

            Formation formation = new Formation(world, new Vector2(10f, 10f), new Vector2(10f, 20f))

            selectedUnits.each { Soldier unit ->
                formation.join(unit)
            }

            world.formations.add(formation)
            formation.sendOrders()

        } else if (key == 'b') {
            println("Breaking formation")

            List<Formation> selectedFormations = world.formations.findAll { it.selected }

            selectedFormations.each { Formation formation ->
                formation.disband()
                world.formations.remove(formation)
            }
        }

        return false
    }

    //LEFT
    private int getHighLX() {
        Math.max(lClickStart.x, mouseX)
    }

    private int getLowLX() {
        Math.min(lClickStart.x, mouseX)
    }

    private int getHighLY() {
        Math.max(lClickStart.y, mouseY)
    }

    private int getLowLY() {
        Math.min(lClickStart.y, mouseY)
    }

//    //RIGHT
//    private int getHighRX() {
//        Math.max(rClickStart.x, mouseX)
//    }
//
//    private int getLowRX() {
//        Math.min(rClickStart.x, mouseX)
//    }
//
//    private int getHighRY() {
//        Math.max(rClickStart.y, mouseY)
//    }
//
//    private int getLowRY() {
//        Math.min(rClickStart.y, mouseY)
//    }

    private int maxClickDrag = 4
    private int smallDist = 4

    @Override
    boolean keyUp(int keycode) {
        return false
    }

    @Override
    boolean keyTyped(char character) {
        return false
    }

    @Override
    boolean touchDown(int screenX, int screenY, int pointer, int button) {

        if (button == Input.Buttons.LEFT) {
            world.units*.selected = false
            world.formations*.selected = false
            lClickStart = new Vector2(screenX, screenY)
        } else if (button == Input.Buttons.RIGHT) {
            rClickStart = new Vector2(screenX, screenY)
        }

        return false
    }

    @Override
    boolean touchUp(int screenX, int screenY, int pointer, int button) {

        if (button == Input.Buttons.LEFT) {
            int _highX = highLX
            int _lowX = lowLX
            int _highY = highLY
            int _lowY = lowLY

            boolean still = Math.abs(lClickStart.x - screenX) < maxClickDrag || Math.abs(lClickStart.y - screenY) < maxClickDrag
            //on mouseup, see if we have traveled far enough

            if (still) {

                Soldier selected = world.units.findAll { Soldier unit ->
                    (Math.abs(unit.location.x - lClickStart.x) < smallDist) && (Math.abs(unit.location.y - lClickStart.y) < smallDist)
                }?.min { Soldier unit ->
                    Math.pow(unit.location.x - lClickStart.x, 2) + Math.pow(unit.location.y - lClickStart.y, 2)

                }

                selected?.select()
            } else {
                List<Soldier> selectedUnits = world.units.findAll { Soldier unit ->
                    unit.location.x > _lowX && unit.location.x < _highX && unit.location.y > _lowY && unit.location.y < _highY
                }

                selectedUnits.each { Soldier unit ->
                    unit.select()
                }
            }
            lClickStart = null


        } else if (button == Input.Buttons.RIGHT) {

            if (false) {
                //TODO: send formation to a location

            } else {

                //if a formation is selected, then we need to move the formation to the line
                List<Formation> formations = world.selectedFormations
                if (formations.size() == 1) {
                    Formation formation = formations.first()

                    formation.setLine(rClickStart, new Vector2(screenX, screenY))
                    formation.sendOrders()
                } else if (formations) {


                    Vector2 unitLine = rClickStart.cpy()
                    unitLine.sub(new Vector2(screenX, screenY))

                    float magnitude = unitLine.len()
                    unitLine.nor()

                    float distancePerLine = (magnitude / formations.size()) as int

                    formations.eachWithIndex { Formation formation, int i ->
                        Vector2 left = unitLine.cpy()
                        left.scl((distancePerLine * -i) as float)
                        left.add(rClickStart)

                        Vector2 right = unitLine.cpy()
                        right.scl((distancePerLine * -(i + 1)) as float)
                        right.add(rClickStart)

                        formation.setLine(left, right)
                        formation.sendOrders()
                    }

                    //unitLine this is the line that needs to be filled by our formations

                    //if multiple formations are selected, then we need to move them all into the line
                    //figure out width of each of the formations,

                } else {
                    println("no formation")
                }

            }
        }

        return false
    }

    @Override
    boolean touchDragged(int screenX, int screenY, int pointer) {
        return false
    }

    @Override
    boolean mouseMoved(int screenX, int screenY) {
        this.mouseX = screenX
        this.mouseY = screenY
        return false
    }

    @Override
    boolean scrolled(int amount) {
        return false
    }
}
