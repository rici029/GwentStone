package heroes;

import gametable.Gametable;
import minions.Minion;

import java.util.ArrayList;

public class KingMudface extends Hero {
    public KingMudface(int mana, String description, ArrayList<String> colors, String name) {
        super(mana, description, colors, name);
    }

    public void specialAbility(Gametable g, int row) {
        for(Minion m : g.table.get(row)) {
            m.setHealth(m.getHealth() + 1);
        }
    }
}
