package minions;

import java.util.ArrayList;

public class Sentinel extends Minion {
    public Sentinel(int mana, int health, int attackDamage, String description, ArrayList<String> colors, String name) {
        super(mana, health, attackDamage, description, colors, name);
        this.setPosition("back");
    }
}
