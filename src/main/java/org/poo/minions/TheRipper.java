package org.poo.minions;

import java.util.ArrayList;

public class TheRipper extends Minion {
    public TheRipper(final int mana, final int health, final int attackDamage,
                     final String description, final ArrayList<String> colors,
                     final String name) {
        super(mana, health, attackDamage, description, colors, name);
        this.setPosition("front");
    }

    /**
     *
     * @param m minion on which the special ability is used
     */
    public void specialAbility(final Minion m) {
        if (m.getAttackDamage() < 2) {
            m.setAttackDamage(0);
        } else {
            m.setAttackDamage(m.getAttackDamage() - 2);
        }
    }
}
