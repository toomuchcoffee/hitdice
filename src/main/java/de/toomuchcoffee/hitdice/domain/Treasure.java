package de.toomuchcoffee.hitdice.domain;

public interface Treasure extends Event {
    String getName();
    boolean isMetallic();
    String getDisplayName();
}
