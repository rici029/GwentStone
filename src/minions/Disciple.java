package minions;

import gametable.Gametable;

import java.util.ArrayList;

public class Disciple extends Minion {
    public Disciple(int mana, int health, int attackDamage, String description, ArrayList<String> colors, String name) {
        super(mana, health, attackDamage, description, colors, name);
        this.setPosition("back");
    }
}
