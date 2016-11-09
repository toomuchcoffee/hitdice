package de.hackandstash.serializable;

import java.io.Serializable;

public class Treasure implements Serializable {
    private static final long serialVersionUID = 8609046467486911921L;

    protected String name;
    protected boolean metallic;

    public String getName() {
        return name;
    }

    public boolean isMetallic() {
        return metallic;
    }
}
