package heroes;

import gametable.Gametable;
import minions.Minion;

import java.util.ArrayList;

public class LordRoyce extends Hero {
    public LordRoyce(int mana, String description, ArrayList<String> colors, String name) {
        super(mana, description, colors, name);
    }
    public void specialAbility(Gametable g, int x) {
        for(Minion m : g.table.get(x)) {
            m.setStatusFrozen(true);
        }
    }
}
