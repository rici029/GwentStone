package gametable;

import lombok.Getter;
import lombok.Setter;
import minions.Minion;

import java.util.ArrayList;

@Getter
@Setter
public class Gametable {
    public ArrayList<ArrayList<Minion>> table;

    public Gametable() {
        this.table = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            this.table.add(new ArrayList<>());
        }
    }
}
