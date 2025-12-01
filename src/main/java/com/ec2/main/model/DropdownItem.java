package com.ec2.main.model;

public class DropdownItem {
    private String id;
    private String label;

    public DropdownItem(String id, String label) {
        this.id = id;
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    // 👇 Add this to keep backward compatibility
    public String getName() {
        return label;
    }
}
