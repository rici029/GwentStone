package gameplay;

import fileio.*;
import gametable.Gametable;
import heroes.Hero;
import lombok.Getter;
import lombok.Setter;
import minions.Minion;
import minions.Sentinel;
import minions.Berserker;
import minions.Goliath;
import minions.Warden;
import minions.Disciple;
import minions.Miraj;
import minions.TheCursedOne;
import minions.TheRipper;
import player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

@Getter
@Setter
public class Gameplay {
    private StartGameInput startGame;
    private ArrayList<ActionsInput> actions;
    private DecksInput playerOneDecks;
    private DecksInput playerTwoDecks;

    public Gameplay(final GameInput game,final DecksInput playerOneDecks,final DecksInput playerTwoDecks) {
        this.startGame = game.getStartGame();
        this.actions = game.getActions();
        this.playerOneDecks = playerOneDecks;
        this.playerTwoDecks = playerTwoDecks;
    }

    public void startGame() {
        Gametable gametable = new Gametable();
        Player playerOne = this.setPlayerOne();
        Player playerTwo = this.setPlayerTwo();
        ArrayList<Minion> deckOne = this.getChosenDeck(playerOneDecks,
                this.startGame.getPlayerOneDeckIdx(), this.startGame.getShuffleSeed());
        ArrayList<Minion> deckTwo = this.getChosenDeck(playerTwoDecks,
                this.startGame.getPlayerTwoDeckIdx(), this.startGame.getShuffleSeed());
        for (ActionsInput action : actions) {
            String command = action.getCommand();
            this.checkCommand(command);
        }
    }

    public Player setPlayerOne() {
        CardInput card = this.startGame.getPlayerOneHero();
        Hero hero = new Hero(card.getMana(), card.getDescription(), card.getColors(), card.getName());
        return new Player(0, hero);
    }

    public Player setPlayerTwo() {
        CardInput card = this.startGame.getPlayerTwoHero();
        Hero hero = new Hero(card.getMana(), card.getDescription(), card.getColors(), card.getName());
        return new Player(0, hero);
    }

    public ArrayList<Minion> getChosenDeck(DecksInput decks, int idx, int shuffleSeed) {
        ArrayList<ArrayList<CardInput>> decksList= decks.getDecks();
        ArrayList<CardInput> deck = decksList.get(idx);
        ArrayList<Minion> minions = new ArrayList<>();
        for (CardInput card : deck) {
            Minion minion = this.createMinion(card);
        }
        Collections.shuffle(minions, new Random(shuffleSeed));
        return minions;
    }

    public Minion createMinion(CardInput card) {
        switch (card.getName()) {
            case "Sentinel":
                return new Sentinel(card.getMana(), card.getHealth(), card.getAttackDamage(), card.getDescription(), card.getColors(), card.getName());
            case "Berserker":
                return new Berserker(card.getMana(), card.getHealth(), card.getAttackDamage(), card.getDescription(), card.getColors(), card.getName());
            case "Goliath":
                return new Goliath(card.getMana(), card.getHealth(), card.getAttackDamage(), card.getDescription(), card.getColors(), card.getName());
            case "Warden":
                return new Warden(card.getMana(), card.getHealth(), card.getAttackDamage(), card.getDescription(), card.getColors(), card.getName());
            case "Disciple":
                return new Disciple(card.getMana(), card.getHealth(), card.getAttackDamage(), card.getDescription(), card.getColors(), card.getName());
            case "Miraj":
                return new Miraj(card.getMana(), card.getHealth(), card.getAttackDamage(), card.getDescription(), card.getColors(), card.getName());
            case "TheCursedOne":
                return new TheCursedOne(card.getMana(), card.getHealth(), card.getAttackDamage(), card.getDescription(), card.getColors(), card.getName());
            case "TheRipper":
                return new TheRipper(card.getMana(), card.getHealth(), card.getAttackDamage(), card.getDescription(), card.getColors(), card.getName());
            default:
                return null;
        }
    }
}

