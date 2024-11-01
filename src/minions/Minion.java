package minions;

import gametable.Gametable;
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

    public Minion(int mana, int health, int attackDamage, String description, ArrayList<String> colors, String name) {
        this.mana = mana;
        this.health = health;
        this.attackDamage = attackDamage;
        this.description = description;
        this.colors = colors;
        this.name = name;
        this.statusFrozen = false;
        this.position = "";
    }
}

class Sentinel extends Minion {
    public Sentinel(int mana, int health, int attackDamage, String description, ArrayList<String> colors, String name) {
        super(mana, health, attackDamage, description, colors, name);
        this.setPosition("back");
    }
}

class Berserker extends Minion {
    public Berserker(int mana, int health, int attackDamage, String description, ArrayList<String> colors, String name) {
        super(mana, health, attackDamage, description, colors, name);
        this.setPosition("back");
    }
}

class Goliath extends Minion {
    public Goliath(int mana, int health, int attackDamage, String description, ArrayList<String> colors, String name) {
        super(mana, health, attackDamage, description, colors, name);
        this.setPosition("front");
    }
}

class Warden extends Minion {
    public Warden(int mana, int health, int attackDamage, String description, ArrayList<String> colors, String name) {
        super(mana, health, attackDamage, description, colors, name);
        this.setPosition("front");
    }
}

class theRipper extends Minion {
    public theRipper(int mana, int health, int attackDamage, String description, ArrayList<String> colors, String name) {
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

class Miraj extends Minion {
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

class theCursedOne extends Minion {
    public theCursedOne(int mana, int health, int attackDamage, String description, ArrayList<String> colors, String name) {
        super(mana, health, attackDamage, description, colors, name);
        this.setAttackDamage(0);
    }

    public void specialAbility(Minion m) {
        int temp = m.getHealth();
        m.setHealth(this.getHealth());
        m.setAttackDamage(temp);
    }
}

class Disciple extends Minion {
    public Disciple(int mana, int health, int attackDamage, String description, ArrayList<String> colors, String name) {
        super(mana, health, attackDamage, description, colors, name);
        this.setAttackDamage(0);
    }

    public void specialAbility(Gametable g, int x) {
        for(Minion m : g.table[x]) {
            m.setHealth(m.getHealth() + 2);
        }
    }
}
