package heroes;

import gametable.Gametable;
import minions.Minion;

import java.util.ArrayList;

public class KingMudface extends Hero {
    public KingMudface(int mana, String description, ArrayList<String> colors, String name) {
        super(mana, description, colors, name);
    }

    public void specialAbility(Gametable g, int x) {
        for(Minion m : g.table.get(x)) {
            m.setHealth(m.getHealth() + 1);
        }
    }
}
