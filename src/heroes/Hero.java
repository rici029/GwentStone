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

    public void specialAbility(Gametable g , Minion m) {
        // This method will be overridden by the subclasses
    }
}
