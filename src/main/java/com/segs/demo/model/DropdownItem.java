package com.segs.demo.model;

public class DropdownItem {
    private String id;
    private String name;

    public DropdownItem(String id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}