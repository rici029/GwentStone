package player;

import heroes.Hero;

public class Player {
    int manaToUse;
    Hero hero;

    public Player(int manaToUse, Hero hero) {
        this.manaToUse = manaToUse;
        this.hero = hero;
    }
}
