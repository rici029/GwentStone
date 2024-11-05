package minions;

import java.util.ArrayList;

public class Miraj extends Minion {
    public Miraj(int mana, int health, int attackDamage, String description, ArrayList<String> colors, String name) {
        super(mana, health, attackDamage, description, colors, name);
        this.setPosition("front");
    }

    public void specialAbility(Minion m) {
        int temp = m.getHealth();
        this.setHealth(m.getHealth());
        m.setHealth(temp);
    }
}
