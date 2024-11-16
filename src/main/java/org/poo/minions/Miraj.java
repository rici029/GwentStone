package org.poo.minions;

import java.util.ArrayList;

public class Miraj extends Minion {
    public Miraj(final int mana, final int health, final int attackDamage,
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
        int temp = this.getHealth();
        this.setHealth(m.getHealth());
        m.setHealth(temp);
    }
}
