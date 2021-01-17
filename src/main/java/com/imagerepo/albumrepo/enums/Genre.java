package com.imagerepo.albumrepo.enums;

public enum Genre {
    //can add whatever genres more I desire
    Rock("Rock"),
    Pop("Pop"),
    Metal("Metal"),
    Jazz("Jazz"),
    Classical("Classical");
    private String name;
    public String getName() {
        return this.name;
    }
    Genre(String name) {
        this.name = name;
    }
}
