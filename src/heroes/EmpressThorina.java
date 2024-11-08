package heroes;

import gametable.Gametable;
import minions.Minion;

import java.util.ArrayList;

public class EmpressThorina extends Hero {
    public EmpressThorina(int mana, String description, ArrayList<String> colors, String name) {
        super(mana, description, colors, name);
    }

    public void specialAbility(Gametable g, int row) {
        Minion max = g.table.get(row).get(0);
        int maxIndex = 0;
        int i = 0;
        for(Minion m : g.table.get(row)) {
            if(m.getHealth() > max.getHealth()) {
                max = m;
                maxIndex = i;
            }
            i++;
        }
        g.table.get(row).remove(maxIndex);
    }
}
