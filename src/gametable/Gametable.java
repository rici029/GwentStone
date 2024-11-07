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
        for (int i = 0; i < 4; i++) {
            this.table.add(new ArrayList<>());
        }
    }

    public void placeCardPlayerOne(final Minion minion){
        switch (minion.getPosition()){
            case "back":
                this.table.get(3).add(minion);
                break;
            case "front":
                this.table.get(2).add(minion);
                break;
        }
    }

    public void placeCardPlayerTwo(final Minion minion){
        switch (minion.getPosition()){
            case "back":
                this.table.get(0).add(minion);
                break;
            case "front":
                this.table.get(1).add(minion);
                break;
        }
    }
}
