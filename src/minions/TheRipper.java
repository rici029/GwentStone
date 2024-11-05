package minions;

import java.util.ArrayList;

public class TheRipper extends Minion {
    public TheRipper(int mana, int health, int attackDamage, String description, ArrayList<String> colors, String name) {
        super(mana, health, attackDamage, description, colors, name);
        this.setPosition("front");
    }

    public void specialAbility(Minion m) {
        if(m.getAttackDamage() < 2)
            m.setAttackDamage(0);
        else
            m.setAttackDamage(m.getAttackDamage() - 2);
    }
}
