package com.disha.restapi;

import java.time.*;

class InventoryItem {

    private String name = null;
    private String description = null;
    private int id = -1;
    private boolean active = false;
    private LocalDateTime added = null;
    private LocalDateTime removed = null;

    private static int IdSeed = 0;

    public InventoryItem(String name, String description) {
        this.id = IdSeed++;
        this.name = name;
        this.description = description;
        this.added = LocalDateTime.now();
        this.active = true;
    }

    public void markAsRemoved() {
        this.active = false;
        this.removed = LocalDateTime.now();
    }

    public int getId() {
        return this.id;
    }

    public boolean isActive() {
        return this.active;
    }

    public LocalDateTime removedWhen() {
        return this.removed;
    }

    public LocalDateTime addedWhen() {
        return this.added;
    }

    public String toString() {
        StringBuilder json = new StringBuilder();
        json.append("{ \"name\": \"").append(this.name).append("\", ");
        json.append("\"description\": \"").append(this.description).append("\", ");
        json.append("\"added_when\": \"").append(this.added.toString()).append("\"");
        json.append(" }");
        return json.toString();
    }
}