package com.steel.model

import groovy.transform.CompileStatic

@CompileStatic
enum Weapon {

    KATANA("katana", "sword"),
    YARI("yari", "spear"),
    KANABO("kanabo", "club"),
    YUMI("yumi", "bow"),
    TANEGASHIMA("tanegashima", "matchlock")

    String japanese
    String english

    Weapon(String japanese, String english) {
        this.japanese = japanese
        this.english = english
    }
}