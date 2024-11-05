package gameplay;

import fileio.*;
import gametable.Gametable;
import heroes.Hero;
import lombok.Getter;
import lombok.Setter;
import minions.Minion;
import player.Player;

import java.util.ArrayList;

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
        String commmand;
        Gametable gametable = new Gametable();
        Player playerOne = this.setPlayerOne();
        Player playerTwo = this.setPlayerTwo();
        for (ActionsInput action : actions) {
            commmand = action.getCommand();
            this.checkCommand(commmand);
        }
    }

    public Player setPlayerOne() {
        CardInput card = this.startGame.getPlayerOneHero();
        Hero hero = new Hero(card.getMana(), card.getDescription(), card.getColors(), card.getName());
        Player player = new Player(0, hero);
        return player;
    }

    public Player setPlayerTwo() {
        CardInput card = this.startGame.getPlayerTwoHero();
        Hero hero = new Hero(card.getMana(), card.getDescription(), card.getColors(), card.getName());
        Player player = new Player(1, hero);
        return player;
    }

}
