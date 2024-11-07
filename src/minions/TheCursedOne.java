package minions;

import java.util.ArrayList;

public class TheCursedOne extends Minion {
    public TheCursedOne(int mana, int health, int attackDamage, String description, ArrayList<String> colors, String name) {
        super(mana, health, attackDamage, description, colors, name);
        this.setPosition("back");
    }

    public void specialAbility(Minion m) {
        int temp = m.getHealth();
        m.setHealth(m.getAttackDamage());
        m.setAttackDamage(temp);
    }
}
