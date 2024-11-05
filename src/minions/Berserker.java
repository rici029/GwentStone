package minions;

import java.util.ArrayList;

public class Berserker extends Minion {
    public Berserker(int mana, int health, int attackDamage, String description, ArrayList<String> colors, String name) {
        super(mana, health, attackDamage, description, colors, name);
        this.setPosition("back");
    }
}
