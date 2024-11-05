package minions;

import java.util.ArrayList;

public class Warden extends Minion {
    public Warden(int mana, int health, int attackDamage, String description, ArrayList<String> colors, String name) {
        super(mana, health, attackDamage, description, colors, name);
        this.setPosition("front");
    }
}
