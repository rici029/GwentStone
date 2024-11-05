package player;

import heroes.Hero;
import lombok.Getter;
import lombok.Setter;
import minions.Minion;

import java.util.ArrayList;

@Getter
@Setter
public class Player {
    int manaToUse;
    Hero hero;
    ArrayList<Minion> hand;

    public Player(final int manaToUse, final Hero hero) {
        this.manaToUse = manaToUse;
        this.hero = hero;
        this.hand = new ArrayList<>();
    }

}
