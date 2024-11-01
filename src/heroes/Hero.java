package heroes;

import gametable.Gametable;

import minions.Minion;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Hero {
    private int mana;
    private int health;
    private String description;
    private ArrayList<String> colors;
    private String name;

    public Hero(int mana, String description, ArrayList<String> colors, String name) {
        this.mana = mana;
        this.health = 30;
        this.description = description;
        this.colors = colors;
        this.name = name;
    }
}

class LordRoyce extends Hero {
    public LordRoyce(int mana, String description, ArrayList<String> colors, String name) {
        super(mana, description, colors, name);
    }
    public void specialAbility(Gametable g, int x) {
        for(Minion m : g.table[x]) {
             m.setStatusFrozen(true);
        }
    }
}

class EmpressThorina extends Hero {
    public EmpressThorina(int mana, String description, ArrayList<String> colors, String name) {
        super(mana, description, colors, name);
    }

    public void specialAbility(Gametable g, int x) {
        Minion max = g.table[x][0];
        int maxIndex = 0;
        int i = 0;
        for(Minion m : g.table[x]) {
            if(m.getHealth() > max.getHealth()) {
                max = m;
                maxIndex = i;
            }
            i++;
        }
        g.removeMinion(x, maxIndex);
    }
}

class KingMudface extends Hero {
    public KingMudface(int mana, String description, ArrayList<String> colors, String name) {
        super(mana, description, colors, name);
    }

    public void specialAbility(Gametable g, int x) {
        for(Minion m : g.table[x]) {
            m.setHealth(m.getHealth() + 1);
        }
    }
}

class GeneralKocioraw extends Hero {
    public GeneralKocioraw(int mana, String description, ArrayList<String> colors, String name) {
        super(mana, description, colors, name);
    }
    public void specialAbility(Gametable g, int x) {
        for(Minion m : g.table[x]) {
            m.setAttackDamage(m.getAttackDamage() + 1);
        }
    }
}
