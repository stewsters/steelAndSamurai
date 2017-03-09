package com.steel.mapgen.game;


import com.badlogic.gdx.graphics.Color;
import com.stewsters.util.math.MatUtils;
import com.stewsters.util.math.Point2i;

import java.util.ArrayList;

public class Daimyo {

    private static int topId = 0;
    private static ArrayList<Daimyo> daimyos = new ArrayList<Daimyo>();

    public int id;
    public String name;
    public Color color;
    public Point2i pos;

    private Daimyo(int id, Point2i point2i, String name) {
        this.id = id;
        this.pos = point2i;
        this.name = name;
        this.color = new Color(
                MatUtils.getFloatInRange(0.1f,1),
                MatUtils.getFloatInRange(0.1f,1),
                MatUtils.getFloatInRange(0.1f,1),
                1);
    }

    public static Daimyo build(int x, int y, String name) {

        Daimyo daimyo = new Daimyo(topId, new Point2i(x, y), name);

        daimyos.add(topId++, daimyo);
        return daimyo;
    }

    public static Daimyo get(int id) {
        return daimyos.get(id);
    }

}
