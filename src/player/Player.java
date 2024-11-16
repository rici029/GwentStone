package player;

import heroes.Hero;
import lombok.Getter;
import lombok.Setter;
import minions.Minion;

import java.util.ArrayList;

@Getter
@Setter
public class Player {
    private int manaToUse;
    private Hero hero;
    private ArrayList<Minion> hand;

    public Player(final int manaToUse, final Hero hero) {
        this.manaToUse = manaToUse;
        this.hero = hero;
        this.hand = new ArrayList<>();
    }

}
