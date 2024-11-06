package gameplay;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
    private int playerTurn;

    public Gameplay(final GameInput game,final DecksInput playerOneDecks,final DecksInput playerTwoDecks) {
        this.startGame = game.getStartGame();
        this.actions = game.getActions();
        this.playerOneDecks = playerOneDecks;
        this.playerTwoDecks = playerTwoDecks;
        this.playerTurn = this.startGame.getStartingPlayer();
    }

    public void startGame(ArrayNode output) {
        Gametable gametable = new Gametable();
        Player playerOne = this.setPlayerOne();
        Player playerTwo = this.setPlayerTwo();
        ArrayList<Minion> deckOne = this.getChosenDeck(this.playerOneDecks,
                this.startGame.getPlayerOneDeckIdx(), this.startGame.getShuffleSeed());
        ArrayList<Minion> deckTwo = this.getChosenDeck(this.playerTwoDecks,
                this.startGame.getPlayerTwoDeckIdx(), this.startGame.getShuffleSeed());
        this.putCardsInHand(playerOne, deckOne);
        this.putCardsInHand(playerTwo, deckTwo);
        for (ActionsInput action : actions) {
            String command = action.getCommand();
            this.checkCommand(command, gametable, playerOne, playerTwo, deckOne, deckTwo, action, output);
        }
    }
    //********MODIFY THIS TO CONSTRUCT WITH DIFFERNET TYPES OF HEROES*******
    public Player setPlayerOne() {
        CardInput card = this.startGame.getPlayerOneHero();
        Hero hero = new Hero(card.getMana(), card.getDescription(), card.getColors(), card.getName());
        return new Player(0, hero);
    }
    //********MODIFY THIS TO CONSTRUCT WITH DIFFERNET TYPES OF HEROES**************
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
            minions.add(minion);
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
            case "The Cursed One":
                return new TheCursedOne(card.getMana(), card.getHealth(), card.getAttackDamage(), card.getDescription(), card.getColors(), card.getName());
            case "The Ripper":
                return new TheRipper(card.getMana(), card.getHealth(), card.getAttackDamage(), card.getDescription(), card.getColors(), card.getName());
            default:
                return null;
        }
    }

    public void putCardsInHand(final Player player, final ArrayList<Minion> deck) {
        ArrayList<Minion> hand = player.getHand();
        if(!deck.isEmpty()) {
            hand.add(deck.get(0));
            deck.remove(0);
        }
    }

    public void checkCommand(final String command, final Gametable gametable, final Player playerOne,
                             final Player playerTwo, final ArrayList<Minion> deckOne, final ArrayList<Minion> deckTwo,
                             final ActionsInput action, final ArrayNode output) {
        switch (command){
            case "getPlayerDeck":
                if(action.getPlayerIdx() == 1) {
                    this.displayDeck(deckOne, output, command, action.getPlayerIdx());
                } else {
                    this.displayDeck(deckTwo, output, command, action.getPlayerIdx());
                }
                break;
            case "getPlayerHero":
                if(action.getPlayerIdx() == 1) {
                    this.displayHero(playerOne, output, action.getPlayerIdx(), command);
                } else {
                    this.displayHero(playerTwo, output, action.getPlayerIdx(), command);
                }
                break;
            case "getPlayerTurn":
                this.displayTurn(this.playerTurn, output, command);
                break;
            case "endPlayerTurn":
                if(this.playerTurn == 1) {
                    this.playerTurn = 2;
                    this.putCardsInHand(playerTwo, deckTwo);
                    this.increaseMana(playerTwo);
                } else {
                    this.playerTurn = 1;
                    this.putCardsInHand(playerOne, deckOne);
                    this.increaseMana(playerOne);
                }
                break;
            case "placeCard":
                break;
            case "getCardsInHand":
                if(action.getPlayerIdx() == 1) {
                    this.displayHand(playerOne.getHand(), output);
                } else {
                    this.displayHand(playerTwo.getHand(), output);
                }
                break;
            case "getCardsOnTable":
                this.displayTable(gametable, output);
                break;
            default:
                break;
        }
    }

    public void displayDeck(final ArrayList<Minion> deck, final ArrayNode output, final String command, final int playerIdx) {
        //display deck in json using output
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode preObjectNode = objectMapper.createObjectNode();
        preObjectNode.put("command", command);
        preObjectNode.put("playerIdx", playerIdx);
        ArrayNode arrayNode = objectMapper.createArrayNode();
        for (Minion card : deck) {
            ObjectNode cardNode = objectMapper.createObjectNode();
            ArrayNode colors = objectMapper.createArrayNode();
            cardNode.put("mana", card.getMana());
            cardNode.put("attackDamage", card.getAttackDamage());
            cardNode.put("health", card.getHealth());
            cardNode.put("description", card.getDescription());
            for (String color : card.getColors()) {
                colors.add(color);
            }
            cardNode.put("colors", colors);
            cardNode.put("name", card.getName());
            arrayNode.add(cardNode);
        }
        preObjectNode.put("output", arrayNode);
        output.add(preObjectNode);
    }

    public void displayHero(final Player player, final ArrayNode output, final int playerIdx, final String command) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", command);
        objectNode.put("playerIdx", playerIdx);
        ObjectNode heroNode = objectMapper.createObjectNode();
        heroNode.put("mana", player.getHero().getMana());
        heroNode.put("description", player.getHero().getDescription());
        ArrayNode colors = objectMapper.createArrayNode();
        for (String color : player.getHero().getColors()) {
            colors.add(color);
        }
        heroNode.put("colors", colors);
        heroNode.put("name", player.getHero().getName());
        heroNode.put("health", player.getHero().getHealth());
        objectNode.put("output", heroNode);
        output.add(objectNode);

    }

    public void displayTurn(final int playerTurn, final ArrayNode output, final String command) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", command);
        objectNode.put("output", playerTurn);
        output.add(objectNode);
    }

    public void displayHand(final ArrayList<Minion> hand, final ArrayNode output) {
        //display hand
    }

    public void displayTable(final Gametable gametable, final ArrayNode output) {
        //display table
    }
    //*************************TO MODIFY********************************
    public void increaseMana(final Player player) {
        player.setManaToUse(player.getManaToUse() + 1);
    }
}

