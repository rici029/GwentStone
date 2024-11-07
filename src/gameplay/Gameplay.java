package gameplay;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.*;
import gametable.Gametable;
import heroes.*;
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
    private int round;

    public Gameplay(final GameInput game, final DecksInput playerOneDecks,
                    final DecksInput playerTwoDecks) {
        this.startGame = game.getStartGame();
        this.actions = game.getActions();
        this.playerOneDecks = playerOneDecks;
        this.playerTwoDecks = playerTwoDecks;
        this.playerTurn = this.startGame.getStartingPlayer();
        this.round = 1;
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
            this.checkCommand(command, gametable, playerOne, playerTwo,
                    deckOne, deckTwo, action, output);
        }
    }

    public Player setPlayerOne() {
        CardInput card = this.startGame.getPlayerOneHero();
        Hero hero = this.createHero(card);
        return new Player(1, hero);
    }

    public Player setPlayerTwo() {
        CardInput card = this.startGame.getPlayerTwoHero();
        Hero hero = this.createHero(card);
        return new Player(1, hero);
    }

    public Hero createHero(final CardInput card) {
        switch (card.getName()) {
            case "Empress Thorina":
                return new EmpressThorina(card.getMana(), card.getDescription(), card.getColors(), card.getName());
            case "General Kocioraw":
                return new GeneralKocioraw(card.getMana(), card.getDescription(), card.getColors(), card.getName());
            case "King Mudface":
                return new KingMudface(card.getMana(), card.getDescription(), card.getColors(), card.getName());
            case "Lord Royce":
                return new LordRoyce(card.getMana(), card.getDescription(), card.getColors(), card.getName());
            default:
                return null;
        }
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
                return new Sentinel(card.getMana(), card.getHealth(), card.getAttackDamage(),
                        card.getDescription(), card.getColors(), card.getName());
            case "Berserker":
                return new Berserker(card.getMana(), card.getHealth(), card.getAttackDamage(),
                        card.getDescription(), card.getColors(), card.getName());
            case "Goliath":
                return new Goliath(card.getMana(), card.getHealth(), card.getAttackDamage(),
                        card.getDescription(), card.getColors(), card.getName());
            case "Warden":
                return new Warden(card.getMana(), card.getHealth(), card.getAttackDamage(),
                        card.getDescription(), card.getColors(), card.getName());
            case "Disciple":
                return new Disciple(card.getMana(), card.getHealth(), card.getAttackDamage(),
                        card.getDescription(), card.getColors(), card.getName());
            case "Miraj":
                return new Miraj(card.getMana(), card.getHealth(), card.getAttackDamage(),
                        card.getDescription(), card.getColors(), card.getName());
            case "The Cursed One":
                return new TheCursedOne(card.getMana(), card.getHealth(), card.getAttackDamage(),
                        card.getDescription(), card.getColors(), card.getName());
            case "The Ripper":
                return new TheRipper(card.getMana(), card.getHealth(), card.getAttackDamage(),
                        card.getDescription(), card.getColors(), card.getName());
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

    public void checkCommand(final String command, final Gametable gametable,
                             final Player playerOne, final Player playerTwo,
                             final ArrayList<Minion> deckOne, final ArrayList<Minion> deckTwo,
                             final ActionsInput action, final ArrayNode output) {
        switch (command){
            case "getPlayerDeck":
                if(action.getPlayerIdx() == 1) {
                    this.displayDeck(deckOne, output, action.getPlayerIdx());
                } else {
                    this.displayDeck(deckTwo, output, action.getPlayerIdx());
                }
                break;
            case "getPlayerHero":
                if(action.getPlayerIdx() == 1) {
                    this.displayHero(playerOne, output, action.getPlayerIdx());
                } else {
                    this.displayHero(playerTwo, output, action.getPlayerIdx());
                }
                break;
            case "getPlayerTurn":
                this.displayTurn(this.playerTurn, output);
                break;
            case "endPlayerTurn":
                if(this.playerTurn == 1) {
                    this.playerTurn = 2;
                } else {
                    this.playerTurn = 1;
                }
                if(this.playerTurn == this.startGame.getStartingPlayer()) {
                    if (this.round <= 10)
                        this.round++;
                    this.increaseMana(playerOne, this.round);
                    this.increaseMana(playerTwo, this.round);
                    this.resetHasAttacked(gametable);
                    this.putCardsInHand(playerOne, deckOne);
                    this.putCardsInHand(playerTwo, deckTwo);
                }
                break;
            case "placeCard":
                if(this.playerTurn == 1) {
                    this.placeCardPlayerOne(gametable, playerOne, action.getHandIdx(), output);
                }
                else {
                    this.placeCardPlayerTwo(gametable, playerTwo, action.getHandIdx(), output);
                }
                break;
            case "getCardsInHand":
                if(action.getPlayerIdx() == 1) {
                    this.displayHand(playerOne.getHand(), output, action.getPlayerIdx());
                } else {
                    this.displayHand(playerTwo.getHand(), output, action.getPlayerIdx());
                }
                break;
            case "getCardsOnTable":
                this.displayTable(gametable, output);
                break;
            case "getPlayerMana":
                if(action.getPlayerIdx() == 1) {
                    this.displayMana(playerOne, output, action.getPlayerIdx());
                } else {
                    this.displayMana(playerTwo, output, action.getPlayerIdx());
                }
                break;
            case "cardUsesAttack":
                this.attackMinion(gametable, action.getCardAttacker(), action.getCardAttacked(),
                        output, this.playerTurn);
                break;
            case "getCardAtPosition":
                this.displayCard(gametable, action.getX(), action.getY(), output);
                break;
            case "cardUsesAbility":
                this.useCardAbility(gametable, action, output);
                break;
            default:
                break;
        }
    }

    public void useCardAbility(final Gametable gametable, final ActionsInput action,
                                 final ArrayNode output) {
        int xAttacker = action.getCardAttacker().getX();
        int yAttacker = action.getCardAttacker().getY();
        Minion cardAttacker = gametable.getTable().get(xAttacker).get(yAttacker);
        int xAttacked = action.getCardAttacked().getX();
        int yAttacked = action.getCardAttacked().getY();
        Minion cardAttacked = gametable.getTable().get(xAttacked).get(yAttacked);
        if(this.checkUseAbilityIsValid(cardAttacker, action.getCardAttacked(), action.getCardAttacker(), output)) {
            switch (cardAttacker.getName()) {
                case "Sentinel":
                    this.useSentinelAbility(gametable, cardAttacker, action.getCardAttacked(), output);
                    break;
                case "Berserker":
                    this.useBerserkerAbility(gametable, cardAttacker, action.getCardAttacked(), output);
                    break;
                case "Goliath":
                    this.useGoliathAbility(gametable, cardAttacker, action.getCardAttacked(), output);
                    break;
                case "Warden":
                    this.useWardenAbility(gametable, cardAttacker, action.getCardAttacked(), output);
                    break;
                case "Disciple":
                    this.useDiscipleAbility(gametable, cardAttacker, action.getCardAttacked(), output);
                    break;
                case "Miraj":
                    this.useMirajAbility(gametable, cardAttacker, action.getCardAttacked(), output);
                    break;
                case "The Cursed One":
                    this.useTheCursedOneAbility(gametable, cardAttacker, action.getCardAttacked(), output);
                    break;
                case "The Ripper":
                    this.useTheRipperAbility(gametable, cardAttacker, action.getCardAttacked(), output);
                    break;
                default:
                    break;
            }
        }


    }

    public boolean checkUseAbilityIsValid(final Minion card, final Coordinates attacked,
                                          final Coordinates attacker, final ArrayNode output) {
        ObjectMapper objectMapper = new ObjectMapper();
        if(card.isStatusFrozen()) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("command", "cardUsesAbility");
            ObjectNode cardAttackerNode = objectMapper.createObjectNode();
            cardAttackerNode.put("x", attacker.getX());
            cardAttackerNode.put("y", attacker.getY());
            objectNode.put("cardAttacker", cardAttackerNode);
            ObjectNode cardAttackedNode = objectMapper.createObjectNode();
            cardAttackedNode.put("x", attacked.getX());
            cardAttackedNode.put("y", attacked.getY());
            objectNode.put("cardAttacked", cardAttackedNode);
            objectNode.put("error", "Attacker card is frozen.");
            output.add(objectNode);
            return false;
        }
        if(card.isHasAttacked()) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("command", "cardUsesAbility");
            ObjectNode cardAttackerNode = objectMapper.createObjectNode();
            cardAttackerNode.put("x", attacker.getX());
            cardAttackerNode.put("y", attacker.getY());
            objectNode.put("cardAttacker", cardAttackerNode);
            ObjectNode cardAttackedNode = objectMapper.createObjectNode();
            cardAttackedNode.put("x", attacked.getX());
            cardAttackedNode.put("y", attacked.getY());
            objectNode.put("cardAttacked", cardAttackedNode);
            objectNode.put("error", "Attacker card has already attacked this turn.");
            return false;
        }
        return true;
    }

    public void displayCard(final Gametable gametable, final int x, final int y,
                            final ArrayNode output) {
        if(gametable.getTable().get(x).size() >= y) {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("command", "getCardAtPosition");
            Minion card = gametable.getTable().get(x).get(y);
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
            objectNode.put("output", cardNode);
            objectNode.put("x", x);
            objectNode.put("y", y);
            output.add(objectNode);
        }
        else {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("command", "getCardAtPosition");
            objectNode.put("x", x);
            objectNode.put("y", y);
            objectNode.put("output", "No card available at that position.");
            output.add(objectNode);
        }

    }

    public void resetHasAttacked(final Gametable gametable) {
        for (ArrayList<Minion> row : gametable.getTable()) {
            for (Minion card : row) {
                card.setHasAttacked(false);
            }
        }
    }

    public void attackMinion(final Gametable gametable, final Coordinates attacker,
                             final Coordinates attacked, final ArrayNode output,
                             final int playerTurn) {
        int xAttacker = attacker.getX();
        int yAttacker = attacker.getY();
        int xAttacked = attacked.getX();
        int yAttacked = attacked.getY();
        Minion cardAttacker = gametable.getTable().get(xAttacker).get(yAttacker);
        Minion cardAttacked = gametable.getTable().get(xAttacked).get(yAttacked);
        if(this.checkAttackIsValid(playerTurn, cardAttacked, cardAttacker,
                xAttacked, yAttacked, output, attacker, attacked, gametable)) {
            cardAttacker.setHasAttacked(true);
            cardAttacked.setHealth(cardAttacked.getHealth() - cardAttacker.getAttackDamage());
            if(cardAttacked.getHealth() <= 0) {
                gametable.getTable().get(xAttacked).remove(yAttacked);
            }
        }
    }

    public boolean checkAttackIsValid(final int playerTurn, final Minion cardAttacked,
                                        final Minion cardAttacker, final int xAttacked,
                                        final int yAttacked, final ArrayNode output,
                                      final Coordinates attacker, final Coordinates attacked,
                                      final Gametable gametable) {
        ObjectMapper objectMapper = new ObjectMapper();
        if(!this.checkAttackRightEnemy(playerTurn, xAttacked)) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("command", "cardUsesAttack");
            ObjectNode cardAttackerNode = objectMapper.createObjectNode();
            cardAttackerNode.put("x", attacker.getX());
            cardAttackerNode.put("y", attacker.getY());
            objectNode.put("cardAttacker", cardAttackerNode);
            ObjectNode cardAttackedNode = objectMapper.createObjectNode();
            cardAttackedNode.put("x", attacked.getX());
            cardAttackedNode.put("y", attacked.getY());
            objectNode.put("cardAttacked", cardAttackedNode);
            objectNode.put("error", "Attacked card does not belong to the enemy.");
            output.add(objectNode);
            return false;
        }
        if(cardAttacker.isHasAttacked()) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("command", "cardUsesAttack");
            ObjectNode cardAttackerNode = objectMapper.createObjectNode();
            cardAttackerNode.put("x", attacker.getX());
            cardAttackerNode.put("y", attacker.getY());
            objectNode.put("cardAttacker", cardAttackerNode);
            ObjectNode cardAttackedNode = objectMapper.createObjectNode();
            cardAttackedNode.put("x", attacked.getX());
            cardAttackedNode.put("y", attacked.getY());
            objectNode.put("cardAttacked", cardAttackedNode);
            objectNode.put("error", "Attacker card has already attacked this turn.");
            output.add(objectNode);
            return false;
        }
        if(cardAttacker.isStatusFrozen()) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("command", "cardUsesAttack");
            ObjectNode cardAttackerNode = objectMapper.createObjectNode();
            cardAttackerNode.put("x", attacker.getX());
            cardAttackerNode.put("y", attacker.getY());
            objectNode.put("cardAttacker", cardAttackerNode);
            ObjectNode cardAttackedNode = objectMapper.createObjectNode();
            cardAttackedNode.put("x", attacked.getX());
            cardAttackedNode.put("y", attacked.getY());
            objectNode.put("cardAttacked", cardAttackedNode);
            objectNode.put("error", "Attacker card is frozen.");
            output.add(objectNode);
            return false;
        }
        if(this.existsTank(cardAttacked, playerTurn, gametable)) {
            if (!this.cardIsTank(cardAttacked, playerTurn, gametable)) {
                ObjectNode objectNode = objectMapper.createObjectNode();
                objectNode.put("command", "cardUsesAttack");
                ObjectNode cardAttackerNode = objectMapper.createObjectNode();
                cardAttackerNode.put("x", attacker.getX());
                cardAttackerNode.put("y", attacker.getY());
                objectNode.put("cardAttacker", cardAttackerNode);
                ObjectNode cardAttackedNode = objectMapper.createObjectNode();
                cardAttackedNode.put("x", attacked.getX());
                cardAttackedNode.put("y", attacked.getY());
                objectNode.put("cardAttacked", cardAttackedNode);
                objectNode.put("error", "Attacked card is not of type 'Tank'.");
                output.add(objectNode);
                return false;
            }
        }
        return true;
    }

    public boolean existsTank(final Minion card, final int playerTurn, final Gametable gametable) {
        if(playerTurn == 1) {
            for (Minion minion : gametable.getTable().get(2)) {
                if(this.cardIsTank(minion, playerTurn, gametable)) {
                    return true;
                }
            }
        }
        else {
            for (Minion minion : gametable.getTable().get(1)) {
                if(this.cardIsTank(minion, playerTurn, gametable)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean cardIsTank(final Minion card, final int playerTurn, final Gametable gametable) {
        switch (card.getName()) {
            case "Goliath":
            case "Warden":
                return true;
            default:
                return false;
        }
    }

    public boolean checkAttackRightEnemy(final int playerTurn, final int x) {
        if(playerTurn == 1) {
            return x == 0 || x == 1;
        }
        else {
            return x == 2 || x == 3;
        }
    }

    public void placeCardPlayerOne(final Gametable gametable, final Player playerOne,
                                   final int handIdx, final ArrayNode output) {
        ArrayList<Minion> hand = playerOne.getHand();
        if(handIdx < hand.size()) {
            Minion card = hand.get(handIdx);
            if(playerOne.getManaToUse() >= card.getMana()) {
                playerOne.setManaToUse(playerOne.getManaToUse() - card.getMana());
                gametable.placeCardPlayerOne(card);
                hand.remove(handIdx);
            }
            else
            {
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode objectNode = objectMapper.createObjectNode();
                objectNode.put("command", "placeCard");
                objectNode.put("handIdx", handIdx);
                objectNode.put("error", "Not enough mana to place card on table.");
                output.add(objectNode);
            }
        }
    }

    public void placeCardPlayerTwo(final Gametable gametable, final Player playerTwo,
                                   final int handIdx, final ArrayNode output) {
        ArrayList<Minion> hand = playerTwo.getHand();
        if(handIdx < hand.size()) {
            Minion card = hand.get(handIdx);
            if(playerTwo.getManaToUse() >= card.getMana()) {
                playerTwo.setManaToUse(playerTwo.getManaToUse() - card.getMana());
                gametable.placeCardPlayerTwo(card);
                hand.remove(handIdx);
            }
            else
            {
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode objectNode = objectMapper.createObjectNode();
                objectNode.put("command", "placeCard");
                objectNode.put("handIdx", handIdx);
                objectNode.put("error", "Not enough mana to place card on table.");
                output.add(objectNode);
            }
        }
    }

    public void displayDeck(final ArrayList<Minion> deck, final ArrayNode output,
                            final int playerIdx) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode preObjectNode = objectMapper.createObjectNode();
        preObjectNode.put("command", "getPlayerDeck");
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

    public void displayHero(final Player player, final ArrayNode output, final int playerIdx) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "getPlayerHero");
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

    public void displayTurn(final int playerTurn, final ArrayNode output) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "getPlayerTurn");
        objectNode.put("output", playerTurn);
        output.add(objectNode);
    }

    public void displayHand(final ArrayList<Minion> hand, final ArrayNode output,
                            final int playerIdx) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "getCardsInHand");
        objectNode.put("playerIdx", playerIdx);
        ArrayNode arrayNode = objectMapper.createArrayNode();
        for (Minion card : hand) {
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
        objectNode.put("output", arrayNode);
        output.add(objectNode);
    }

    public void displayTable(final Gametable gametable, final ArrayNode output) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "getCardsOnTable");
        ArrayNode arrayNode = objectMapper.createArrayNode();
        for (ArrayList<Minion> row : gametable.getTable()) {
            if(!row.isEmpty()){
                ArrayNode rowNode = objectMapper.createArrayNode();
                for (Minion card : row) {
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
                    rowNode.add(cardNode);
                }
                arrayNode.add(rowNode);
            }
            else
            {
                ArrayNode rowNode = objectMapper.createArrayNode();
                arrayNode.add(rowNode);
            }
        }
        objectNode.put("output", arrayNode);
        output.add(objectNode);
    }

    public void increaseMana(final Player player, final int round) {
        player.setManaToUse(player.getManaToUse() + round);
    }

    public void displayMana(final Player player, final ArrayNode output, final int playerIdx) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "getPlayerMana");
        objectNode.put("output", player.getManaToUse());
        objectNode.put("playerIdx", playerIdx);
        output.add(objectNode);
    }
}

