package org.poo.minions;

import java.util.ArrayList;

public class Warden extends Minion {
    public Warden(final int mana, final int health, final int attackDamage,
                  final String description, final ArrayList<String> colors, final String name) {
        super(mana, health, attackDamage, description, colors, name);
        this.setPosition("front");
    }
}
