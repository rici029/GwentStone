package gamestats;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Gamestats {
    private int nrOfGames;
    private int nrPlayerOneWins;
    private int nrPlayerTwoWins;

    public Gamestats() {
        this.nrOfGames = 0;
        this.nrPlayerOneWins = 0;
        this.nrPlayerTwoWins = 0;
    }

}
