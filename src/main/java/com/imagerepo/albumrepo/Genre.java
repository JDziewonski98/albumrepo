package com.imagerepo.albumrepo;

public enum Genre {
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
