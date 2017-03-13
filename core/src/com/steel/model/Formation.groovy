package com.steel.model

import com.badlogic.gdx.math.Vector2
import groovy.transform.CompileStatic

@CompileStatic
public class Formation {
    World world

    Vector2 left
    Vector2 right

    Vector2 center
    Vector2 unitForward

    public LinkedList<Soldier> units

    public boolean selected = false

    float rank
    float file

    //TODO: about face then turning around rather then shuffle through the center
    //TODO: separation
    //TODO: move as a formation
    //TODO: formation speed slower than units, so they can catch up


    public Formation(World world, Vector2 left, Vector2 right) {
        this.world = world;
        this.left = left;
        this.right = right;

        units = []

        calculateCenter()

    }

    def setLine(Vector2 left, Vector2 right) {
        this.left = left;
        this.right = right;
        calculateCenter()
    }


    private void calculateCenter() {
        center = new Vector2(((left.x + right.x) / 2f) as float, ((left.y + right.y) / 2f) as float)
    }



    public void sendOrders() {

        Vector2 unitLine = new Vector2((left.x - right.x) as float, (left.y - right.y) as float)
        float dist = unitLine.len()
        unitLine.nor()

        unitForward = new Vector2(-unitLine.y, unitLine.x)

        file = 0f;
        rank = 0f;

        units.eachWithIndex { Soldier unit, int i ->

            float x = (left.x - (file * unitLine.x) - (rank * unitForward.x)) as float
            float y = (left.y - (file * unitLine.y) - (rank * unitForward.y)) as float

            //This probably should use slope anymore, use the unitline
            unit.sendOrder(x, y)

            file += unit.width * 2

            if (file > dist) {
                file = 0
                rank += unit.width * 2
            }

        }

    }


    //Movement prototype

    public void moveForward(float distance) {
        Vector2 movement = unitForward.cpy()
        movement.setLength(distance)

        left.add(movement)
        right.add(movement)
        calculateCenter()

    }

    public void moveRight(float distance) {
        //move formation to the right
        Vector2 movement = right.cpy()
        movement.sub(left)
        movement.setLength(distance)

        left.add(movement)
        right.add(movement)
        calculateCenter()
    }


    public void turnRight(float angle) {
        //turn formation to the right
        left = rotatePoint(left, center, angle)
        right = rotatePoint(right, center, angle)
        calculateCenter()
    }


    public void aboutFace() {
        //reorders formation to cause least shuffling when turning around
    }

    private Vector2 rotatePoint(Vector2 point, Vector2 pivot, float angle) {
        float s = Math.sin(angle) as float;
        float c = Math.cos(angle) as float;

        // translate point back to origin:
        Vector2 result = point.cpy()
        result.sub(pivot)

        // rotate point
        float newX = result.x * c - result.y * s;
        float newY = result.x * s + result.y * c;

        result.x = newX
        result.y = newY
        // translate point back:
        result.add(pivot);

        return result;

    }

    def join(Soldier unit) {
        unit.formation = this
        units.add unit

    }

    def disband() {
        selected = false
        units.each {
            it.selected = false
            it.formation = null
        }
        units.clear()
    }


    public String println() {
        return units.size()
    }

    public void select() {
        selected = true
        units.each {
            it.selected = true
        }
    }
}
