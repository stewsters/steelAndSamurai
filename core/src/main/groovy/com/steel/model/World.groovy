package com.steel.model

import com.badlogic.gdx.math.Vector2
import groovy.transform.CompileStatic

@CompileStatic
class World {

    int worldSize = 80

    LinkedList<Soldier> units
    LinkedList<Formation> formations;

    TileType[][] tiles

    World() {
        tiles = new TileType[worldSize][worldSize]

        for(int x = 0 ; x< worldSize; x++){
            for(int y = 0 ; y< worldSize; y++){
                tiles[x][y] = TileType.GRASS
//                if (x > 10 && x < 50)
//                    tiles[x][y].blocks = false
            }
        }

        units = new LinkedList<>()
        formations = new LinkedList<>()
    }

    boolean isBlocked(Vector2 loc) {
        if (loc.x < 0 || loc.x > worldSize * Tile.size || loc.y < 0 || loc.y > worldSize * Tile.size) {
            return true
        } else {
            return (tiles[(loc.x / Tile.size) as int][(loc.y / Tile.size) as int].blocks)
        }
    }

    def update(float elapsedTime) {
        units.each {
            it.act(elapsedTime)
        }
    }

    def addUnit(Soldier unit) {
        unit.world = this
        units.add(unit)
    }


    def void moveForward(float dist) {
        for (Formation formation : selectedFormations) {
            formation.moveForward(dist)
            formation.sendOrders()
        }
    }

    def void moveRight(float dist) {
        for (Formation formation : selectedFormations) {
            formation.moveRight(dist)
            formation.sendOrders()
        }
    }

    def void turnRight(float dist) {
        for (Formation formation : selectedFormations) {
            formation.turnRight(dist)
            formation.sendOrders()
        }
    }

    public List<Formation> getSelectedFormations(){
        return formations.findAll{it.selected}
    }
}
