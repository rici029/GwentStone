package org.poo.heroes;

import org.poo.gametable.Gametable;
import org.poo.minions.Minion;

import java.util.ArrayList;

public class EmpressThorina extends Hero {
    public EmpressThorina(final int mana, final String description,
                          final ArrayList<String> colors, final String name) {
        super(mana, description, colors, name);
    }

    /**
     *
     * @param g game table
     * @param row row on which the special ability is used
     */
    public void specialAbility(final Gametable g, final int row) {
        Minion max = g.getTable().get(row).get(0);
        int maxIndex = 0;
        int i = 0;
        for (Minion m : g.getTable().get(row)) {
            if (m.getHealth() > max.getHealth()) {
                max = m;
                maxIndex = i;
            }
            i++;
        }
        g.getTable().get(row).remove(maxIndex);
    }
}
