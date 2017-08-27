package com.steel.model

import com.badlogic.gdx.math.Vector2
import groovy.transform.CompileStatic

@CompileStatic
public class Soldier {

    World world
    Vector2 location
    Vector2 destination

    Weapon weapon
    int hp
    int armor
    int morale
    int exp

    int width = 5;
    float speed = 15f;
    float varianceSq = 5f;
    //cohesion - treat units like

    Formation formation
    boolean selected = false

    public Soldier(int x = 0, int y = 0) {
        location = new Vector2(x, y)
    }

    def act(float elapsedTime) {
        if (destination) {
            Vector2 distance = destination.cpy()
            distance.sub(location)

            float magSq = distance.len2()
            if (varianceSq > magSq) {
                destination = null
            } else {
                distance.setLength((speed * elapsedTime) as float)

                distance.add(location)
                if (!world.isBlocked(distance)) {
                    location = distance
                }

            }

        }
    }

    def sendOrder(Vector2 dest) {
        destination = dest
    }

    def sendOrder(float x, float y) {
        destination = new Vector2(x, y)
    }

    def select() {
        selected = true

        formation?.select()

    }
}
