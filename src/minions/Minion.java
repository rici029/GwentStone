package minions;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Minion {
    private int mana;
    private int health;
    private int attackDamage;
    private String description;
    private ArrayList<String> colors;
    private String name;
    private boolean statusFrozen;
    private String position;
    private boolean hasAttacked;

    public Minion(final int mana, final int health, final int attackDamage,
                  final String description, final ArrayList<String> colors, final String name) {
        this.mana = mana;
        this.health = health;
        this.attackDamage = attackDamage;
        this.description = description;
        this.colors = colors;
        this.name = name;
        this.statusFrozen = false;
        this.position = "";
        this.hasAttacked = false;
    }

    /**
     *
     * @param m minion on which the special ability is used
     */
    public void specialAbility(final Minion m) {
        // This method will be overridden by the subclasses
    }
}
