package heroes;

import gametable.Gametable;


import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Hero {
    private int mana;
    private int health;
    private String description;
    private ArrayList<String> colors;
    private String name;
    private boolean hasAttacked;
    private static final int MAX_HEALTH = 30;

    public Hero(final int mana, final String description, final ArrayList<String> colors,
                final String name) {
        this.mana = mana;
        this.health = MAX_HEALTH;
        this.description = description;
        this.colors = colors;
        this.name = name;
        this.hasAttacked = false;
    }

    /**
     *
     * @param g game table
     * @param row row on which the special ability is used
     */
    public void specialAbility(final Gametable g, final int row) {
        // This method will be overridden by the subclasses
    }
}
