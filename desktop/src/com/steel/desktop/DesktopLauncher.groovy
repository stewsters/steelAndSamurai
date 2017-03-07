package com.steel.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.steel.SteelSamuraiGame
import groovy.transform.CompileStatic

@CompileStatic
public class DesktopLauncher {
    public static void main(String[] arg) {
        ImagePacker.run();

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration()
        new LwjglApplication(new SteelSamuraiGame(), config)
    }
}
