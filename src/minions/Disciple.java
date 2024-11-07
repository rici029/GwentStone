package minions;

import gametable.Gametable;

import java.util.ArrayList;

public class Disciple extends Minion {
    public Disciple(int mana, int health, int attackDamage, String description, ArrayList<String> colors, String name) {
        super(mana, health, attackDamage, description, colors, name);
        this.setPosition("back");
    }

    public void specialAbility(Gametable g, int x) {
        for(Minion m : g.table.get(x)) {
            m.setHealth(m.getHealth() + 2);
        }
    }
}
