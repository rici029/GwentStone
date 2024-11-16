package gametable;

import lombok.Getter;
import lombok.Setter;
import minions.Minion;

import java.util.ArrayList;

@Getter
@Setter
public class Gametable {
    private ArrayList<ArrayList<Minion>> table;
    private static final int ROWS = 4;
    private static final int BACK_ROW_PLAYER_ONE = 3;

    public Gametable() {
        this.table = new ArrayList<>();
        for (int i = 0; i < ROWS; i++) {
            this.table.add(new ArrayList<>());
        }
    }

    /**
     *
     * @param minion minion to be placed on the table
     */
    public void placeCardPlayerOne(final Minion minion) {
        switch (minion.getPosition()) {
            case "back":
                this.table.get(BACK_ROW_PLAYER_ONE).add(minion);
                break;
            case "front":
                this.table.get(2).add(minion);
                break;
            default:
                break;
        }
    }

    /**
     *
     * @param minion minion to be placed on the table
     */
    public void placeCardPlayerTwo(final Minion minion) {
        switch (minion.getPosition()) {
            case "back":
                this.table.get(0).add(minion);
                break;
            case "front":
                this.table.get(1).add(minion);
                break;
            default:
                break;
        }
    }
}
