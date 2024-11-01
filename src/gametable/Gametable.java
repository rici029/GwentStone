package gametable;

import lombok.Getter;
import lombok.Setter;
import minions.Minion;

@Getter
@Setter
public class Gametable {
    public Minion[][] table;

    public Gametable() {
        table = new Minion[4][5];
    }

    public void addMinion(Minion m, int x, int y) {
        table[x][y] = m;
    }

    public void removeMinion(int x, int y) {
        table[x][y] = null;
    }

    public void shiftLeft(int y) {
        for (int i = 0; i < 4; i++) {
            if (table[i][y] != null) {
                table[i][y - 1] = table[i][y];
                table[i][y] = null;
            }
        }
    }
}
