package com.steel.utils

import com.stewsters.util.math.MatUtils

public class SamuraiNameGen {

    ArrayList<String> givenName
    ArrayList<String> familyName

    void init(File file) {

        ArrayList<String> givenName = []
        ArrayList<String> familyName = []

        file.eachLine {
            def name = it.split('\\s')
            familyName.add name[0]
            givenName.add name[1]
        }

        this.givenName = givenName.unique()
        this.familyName = familyName.unique()
    }

    public String gen() {
        MatUtils.rand(familyName) + " " + MatUtils.rand(givenName)
    }

}