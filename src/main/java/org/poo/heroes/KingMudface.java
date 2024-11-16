package org.poo.heroes;

import org.poo.gametable.Gametable;
import org.poo.minions.Minion;

import java.util.ArrayList;

public class KingMudface extends Hero {
    public KingMudface(final int mana, final String description, final ArrayList<String> colors,
                       final String name) {
        super(mana, description, colors, name);
    }

    /**
     *
     * @param g game table
     * @param row row on which the special ability is used
     */
    public void specialAbility(final Gametable g, final int row) {
        for (Minion m : g.getTable().get(row)) {
            m.setHealth(m.getHealth() + 1);
        }
    }
}
