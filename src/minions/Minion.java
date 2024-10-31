package minions;

import gametable.Gametable;

public class Minion {
    int mana;
    int health;
    int attackDamage;
    String description;
    String colors;
    String name;
    boolean statusFrozen;
    String position;

    public Minion(int mana, int health, int attackDamage, String description, String colors, String name) {
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
    public Sentinel(int mana, int health, int attackDamage, String description, String colors, String name) {
        super(mana, health, attackDamage, description, colors, name);
        this.position = "back";
    }
}

class Berserker extends Minion {
    public Berserker(int mana, int health, int attackDamage, String description, String colors, String name) {
        super(mana, health, attackDamage, description, colors, name);
        this.position = "back";
    }
}

class Goliath extends Minion {
    public Goliath(int mana, int health, int attackDamage, String description, String colors, String name) {
        super(mana, health, attackDamage, description, colors, name);
        this.position = "front";
    }
}

class Warden extends Minion {
    public Warden(int mana, int health, int attackDamage, String description, String colors, String name) {
        super(mana, health, attackDamage, description, colors, name);
        this.position = "front";
    }
}

class theRipper extends Minion {
    public theRipper(int mana, int health, int attackDamage, String description, String colors, String name) {
        super(mana, health, attackDamage, description, colors, name);
        this.position = "front";
    }

    public void specialAbility(Minion m) {
        if(m.attackDamage < 2)
            m.attackDamage = 0;
        else
            m.attackDamage -= 2;
    }
}

class Miraj extends Minion {
    public Miraj(int mana, int health, int attackDamage, String description, String colors, String name) {
        super(mana, health, attackDamage, description, colors, name);
        this.position = "front";
    }

    public void specialAbility(Minion m) {
        int temp = m.health;
        m.health = this.health;
        this.health = temp;
    }
}

class theCursedOne extends Minion {
    public theCursedOne(int mana, int health, int attackDamage, String description, String colors, String name) {
        super(mana, health, attackDamage, description, colors, name);
        this.attackDamage = 0;
    }

    public void specialAbility(Minion m) {
        int temp = m.health;
        m.health = m.attackDamage;
        m.attackDamage = temp;
    }
}

class Disciple extends Minion {
    public Disciple(int mana, int health, int attackDamage, String description, String colors, String name) {
        super(mana, health, attackDamage, description, colors, name);
        this.attackDamage = 0;
    }

    public void specialAbility(Gametable g, int x) {
        for(Minion m : g.table[x]) {
            m.health += 2;
        }
    }
}
