package minions;

import java.util.ArrayList;

public class Sentinel extends Minion {
    public Sentinel(final int mana, final int health, final int attackDamage,
                    final String description, final ArrayList<String> colors, final String name) {
        super(mana, health, attackDamage, description, colors, name);
        this.setPosition("back");
    }
}
