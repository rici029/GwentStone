package minions;

import java.util.ArrayList;

public class Goliath extends Minion {
    public Goliath(int mana, int health, int attackDamage, String description, ArrayList<String> colors, String name) {
        super(mana, health, attackDamage, description, colors, name);
        this.setPosition("front");
    }
}
