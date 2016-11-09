package de.hackandstash.serializable;

public class Armor extends Treasure {
    private int protection;

    public Armor(String name, int protection, boolean metallic) {
        this.name = name;
        this.protection = protection;
        this.metallic = metallic;
    }

    public int getProtection() {
        return protection;
    }
}
