package minions;

import java.util.ArrayList;

public class TheCursedOne extends Minion {
    public TheCursedOne(final int mana, final int health, final int attackDamage,
                        final String description, final ArrayList<String> colors,
                        final String name) {
        super(mana, health, attackDamage, description, colors, name);
        this.setPosition("back");
    }

    /**
     *
     * @param m minion on which the special ability is used
     */
    public void specialAbility(final Minion m) {
        int temp = m.getHealth();
        m.setHealth(m.getAttackDamage());
        m.setAttackDamage(temp);
    }
}
