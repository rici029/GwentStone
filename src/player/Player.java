package player;

import heroes.Hero;
import minions.Minion;

import java.util.ArrayList;

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
