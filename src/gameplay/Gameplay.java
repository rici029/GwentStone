package gameplay;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CardInput;
import fileio.Coordinates;
import fileio.DecksInput;
import fileio.GameInput;
import fileio.StartGameInput;
import fileio.ActionsInput;
import gamestats.Gamestats;
import gametable.Gametable;
import heroes.EmpressThorina;
import heroes.GeneralKocioraw;
import heroes.Hero;
import heroes.KingMudface;
import heroes.LordRoyce;
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
import java.util.Objects;
import java.util.Random;

@Getter
@Setter
public class Gameplay {
    private static final int MAX_MANA = 10;
    private static final int BACKROWOFPLAYERONEIDX = 3;
    private StartGameInput startGame;
    private ArrayList<ActionsInput> actions;
    private DecksInput playerOneDecks;
    private DecksInput playerTwoDecks;
    private int playerTurn;
    private int round;
    private boolean gameEnded;
    private Gamestats gamestats;

    public Gameplay(final GameInput game, final DecksInput playerOneDecks,
                    final DecksInput playerTwoDecks, final Gamestats gamestats) {
        this.startGame = game.getStartGame();
        this.actions = game.getActions();
        this.playerOneDecks = playerOneDecks;
        this.playerTwoDecks = playerTwoDecks;
        this.playerTurn = this.startGame.getStartingPlayer();
        this.round = 1;
        this.gameEnded = false;
        this.gamestats = gamestats;
    }

    /**
     *
     * @param output the output array
     */
    public void startGame(final ArrayNode output) {
        Gametable gametable = new Gametable();
        Player playerOne = this.setPlayerOne();
        Player playerTwo = this.setPlayerTwo();
        ArrayList<Minion> deckOne = this.getChosenDeck(this.playerOneDecks,
                this.startGame.getPlayerOneDeckIdx(), this.startGame.getShuffleSeed());
        ArrayList<Minion> deckTwo = this.getChosenDeck(this.playerTwoDecks,
                this.startGame.getPlayerTwoDeckIdx(), this.startGame.getShuffleSeed());
        this.putCardsInHand(playerOne, deckOne);
        this.putCardsInHand(playerTwo, deckTwo);
        this.gamestats.setNrOfGames(gamestats.getNrOfGames() + 1);
        for (ActionsInput action : actions) {
            String command = action.getCommand();
            this.checkCommand(command, gametable, playerOne, playerTwo,
                    deckOne, deckTwo, action, output);
        }
    }

    /**
     *
     * @return player one
     */
    public Player setPlayerOne() {
        CardInput card = this.startGame.getPlayerOneHero();
        Hero hero = this.createHero(card);
        return new Player(1, hero);
    }

    /**
     *
     * @return player two
     */
    public Player setPlayerTwo() {
        CardInput card = this.startGame.getPlayerTwoHero();
        Hero hero = this.createHero(card);
        return new Player(1, hero);
    }

    /**
     *
     * @param card card to be created
     * @return hero
     */
    public Hero createHero(final CardInput card) {
        switch (card.getName()) {
            case "Empress Thorina":
                return new EmpressThorina(card.getMana(), card.getDescription(),
                        card.getColors(), card.getName());
            case "General Kocioraw":
                return new GeneralKocioraw(card.getMana(), card.getDescription(),
                        card.getColors(), card.getName());
            case "King Mudface":
                return new KingMudface(card.getMana(), card.getDescription(),
                        card.getColors(), card.getName());
            case "Lord Royce":
                return new LordRoyce(card.getMana(), card.getDescription(),
                        card.getColors(), card.getName());
            default:
                return null;
        }
    }

    /**
     *
     * @param decks decks from which the deck is chosen
     * @param idx index of the deck
     * @param shuffleSeed seed for shuffling
     * @return chosen deck
     */
    public ArrayList<Minion> getChosenDeck(final DecksInput decks, final int idx,
                                           final int shuffleSeed) {
        ArrayList<ArrayList<CardInput>> decksList = decks.getDecks();
        ArrayList<CardInput> deck = decksList.get(idx);
        ArrayList<Minion> minions = new ArrayList<>();
        for (CardInput card : deck) {
            Minion minion = this.createMinion(card);
            minions.add(minion);
        }
        Collections.shuffle(minions, new Random(shuffleSeed));
        return minions;
    }

    /**
     *
     * @param card card to be created
     * @return created card
     */
    public Minion createMinion(final CardInput card) {
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

    /**
     *
     * @param player player who gets the cards
     * @param deck the deck from which the cards are taken
     */
    public void putCardsInHand(final Player player, final ArrayList<Minion> deck) {
        ArrayList<Minion> hand = player.getHand();
        if (!deck.isEmpty()) {
            hand.add(deck.get(0));
            deck.remove(0);
        }
    }

    /**
     *
     * @param command command to be checked
     * @param gametable the game table
     * @param playerOne player one
     * @param playerTwo player two
     * @param deckOne player one's deck
     * @param deckTwo player two's deck
     * @param action the action to be performed
     * @param output the output array
     */
    public void checkCommand(final String command, final Gametable gametable,
                             final Player playerOne, final Player playerTwo,
                             final ArrayList<Minion> deckOne, final ArrayList<Minion> deckTwo,
                             final ActionsInput action, final ArrayNode output) {
        switch (command) {
            case "getPlayerDeck":
                if (action.getPlayerIdx() == 1) {
                    this.displayDeck(deckOne, output, action.getPlayerIdx());
                } else {
                    this.displayDeck(deckTwo, output, action.getPlayerIdx());
                }
                break;
            case "getPlayerHero":
                if (action.getPlayerIdx() == 1) {
                    this.displayHero(playerOne, output, action.getPlayerIdx());
                } else {
                    this.displayHero(playerTwo, output, action.getPlayerIdx());
                }
                break;
            case "getPlayerTurn":
                this.displayTurn(output);
                break;
            case "endPlayerTurn":
                if (this.gameEnded) {
                    break;
                }
                if (this.playerTurn == 1) {
                    this.resetFrozenCards(gametable);
                    this.playerTurn = 2;
                } else {
                    this.resetFrozenCards(gametable);
                    this.playerTurn = 1;
                }
                if (this.playerTurn == this.startGame.getStartingPlayer()) {
                    if (this.round < MAX_MANA) {
                        this.round++;
                    }
                    this.increaseMana(playerOne);
                    this.increaseMana(playerTwo);
                    this.resetHasAttacked(gametable, playerOne, playerTwo);
                    this.putCardsInHand(playerOne, deckOne);
                    this.putCardsInHand(playerTwo, deckTwo);
                }
                break;
            case "placeCard":
                if (this.gameEnded) {
                    break;
                }
                if (this.playerTurn == 1) {
                    this.placeCardPlayerOne(gametable, playerOne, action.getHandIdx(), output);
                }  else {
                    this.placeCardPlayerTwo(gametable, playerTwo, action.getHandIdx(), output);
                }
                break;
            case "getCardsInHand":
                if (action.getPlayerIdx() == 1) {
                    this.displayHand(playerOne.getHand(), output, action.getPlayerIdx());
                } else {
                    this.displayHand(playerTwo.getHand(), output, action.getPlayerIdx());
                }
                break;
            case "getCardsOnTable":
                this.displayTable(gametable, output);
                break;
            case "getPlayerMana":
                if (action.getPlayerIdx() == 1) {
                    this.displayMana(playerOne, output, action.getPlayerIdx());
                } else {
                    this.displayMana(playerTwo, output, action.getPlayerIdx());
                }
                break;
            case "cardUsesAttack":
                if (this.gameEnded) {
                    break;
                }
                this.attackMinion(gametable, action.getCardAttacker(),
                        action.getCardAttacked(), output);
                break;
            case "getCardAtPosition":
                this.displayCard(gametable, action.getX(), action.getY(), output);
                break;
            case "cardUsesAbility":
                if (this.gameEnded) {
                    break;
                }
                this.useCardAbility(gametable, action, output);
                break;
            case "useAttackHero":
                if (this.gameEnded) {
                    break;
                }
                this.attackHero(gametable, action.getCardAttacker(), output, playerOne, playerTwo);
                break;
            case "useHeroAbility":
                if (this.gameEnded) {
                    break;
                }
                this.useHeroAbility(action, output, playerOne, playerTwo, gametable);
                break;
            case "getFrozenCardsOnTable":
                this.displayFrozenCards(gametable, output);
                break;
            case "getPlayerOneWins":
                this.displayPlayerOneWins(output);
                break;
            case "getPlayerTwoWins":
                this.displayPlayerTwoWins(output);
                break;
            case "getTotalGamesPlayed":
                this.displayTotalGames(output);
                break;
            default:
                break;
        }
    }

    /**
     *
     * @param output the output array
     */
    public void displayPlayerOneWins(final ArrayNode output) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "getPlayerOneWins");
        objectNode.put("output", this.gamestats.getNrPlayerOneWins());
        output.add(objectNode);
    }

    /**
     *
     * @param output the output array
     */
    public void displayPlayerTwoWins(final ArrayNode output) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "getPlayerTwoWins");
        objectNode.put("output", this.gamestats.getNrPlayerTwoWins());
        output.add(objectNode);
    }

    /**
     *
     * @param output the output array
     */
    public void displayTotalGames(final ArrayNode output) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "getTotalGamesPlayed");
        objectNode.put("output", this.gamestats.getNrOfGames());
        output.add(objectNode);
    }

    /**
     *
     * @param gametable the game table
     */
    public void resetFrozenCards(final Gametable gametable) {
        if (this.playerTurn == 1) {
            for (Minion card : gametable.getTable().get(2)) {
                card.setStatusFrozen(false);
            }
            for (Minion card : gametable.getTable().get(BACKROWOFPLAYERONEIDX)) {
                card.setStatusFrozen(false);
            }
        } else {
            for (Minion card : gametable.getTable().get(0)) {
                card.setStatusFrozen(false);
            }
            for (Minion card : gametable.getTable().get(1)) {
                card.setStatusFrozen(false);
            }
        }
    }

    /**
     *
     * @param g the game table
     * @param output the output array
     */
    public void displayFrozenCards(final Gametable g, final ArrayNode output) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "getFrozenCardsOnTable");
        ArrayNode arrayNode = objectMapper.createArrayNode();
        for (ArrayList<Minion> row : g.getTable()) {
            for (Minion card : row) {
                if (card.isStatusFrozen()) {
                    ObjectNode cardNode = objectMapper.createObjectNode();
                    cardNode.put("mana", card.getMana());
                    cardNode.put("attackDamage", card.getAttackDamage());
                    cardNode.put("health", card.getHealth());
                    cardNode.put("description", card.getDescription());
                    ArrayNode colors = objectMapper.createArrayNode();
                    for (String color : card.getColors()) {
                        colors.add(color);
                    }
                    cardNode.put("colors", colors);
                    cardNode.put("name", card.getName());
                    arrayNode.add(cardNode);
                }
            }
        }
        objectNode.put("output", arrayNode);
        output.add(objectNode);
    }

    /**
     *
     * @param action the action to be performed
     * @param output the output array
     * @param playerOne player one
     * @param playerTwo player two
     * @param gametable the game table
     */
    public void useHeroAbility(final ActionsInput action, final ArrayNode output,
                               final Player playerOne, final Player playerTwo,
                               final Gametable gametable) {
        if (this.playerTurn == 1) {
            usePlayerAbility(output, playerOne, gametable, action.getAffectedRow());
        } else {
            usePlayerAbility(output, playerTwo, gametable, action.getAffectedRow());
        }
    }

    /**
     *
     * @param output the output array
     * @param attacker the player who uses the ability
     * @param gametable the game table
     * @param affectedRow the row affected by the ability
     */
    public void usePlayerAbility(final ArrayNode output, final Player attacker,
                                 final Gametable gametable, final int affectedRow) {
        Hero hero = attacker.getHero();
        if (attacker.getManaToUse() >= hero.getMana()) {
            if (!hero.isHasAttacked()) {
                if (Objects.equals(hero.getName(), "Empress Thorina")
                        || Objects.equals(hero.getName(), "Lord Royce")) {
                    this.caseOneHeroAbility(hero, output, gametable, affectedRow, attacker);
                } else {
                    this.caseTwoHeroAbility(hero, output, gametable, affectedRow, attacker);
                }
            } else {
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode objectNode = objectMapper.createObjectNode();
                objectNode.put("command", "useHeroAbility");
                objectNode.put("affectedRow", affectedRow);
                objectNode.put("error", "Hero has already attacked this turn.");
                output.add(objectNode);
            }
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("command", "useHeroAbility");
            objectNode.put("affectedRow", affectedRow);
            objectNode.put("error", "Not enough mana to use hero's ability.");
            output.add(objectNode);
        }
    }

    /**
     *
     * @param hero the hero who uses the ability
     * @param output the output array
     * @param gametable the game table
     * @param affectedRow the row affected by the ability
     * @param attacker the player who uses the ability
     */
    public void caseOneHeroAbility(final Hero hero, final ArrayNode output,
                                   final Gametable gametable, final int affectedRow,
                                   final Player attacker) {
        if (checkHeroRowCaseOne(affectedRow)) {
            attacker.setManaToUse(attacker.getManaToUse() - hero.getMana());
            hero.specialAbility(gametable, affectedRow);
            hero.setHasAttacked(true);
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("command", "useHeroAbility");
            objectNode.put("affectedRow", affectedRow);
            objectNode.put("error", "Selected row does not belong to the enemy.");
            output.add(objectNode);
        }
    }

    /**
     *
     * @param affectedRow the row affected by the ability
     * @return true if the row is valid, false otherwise
     */
    public boolean checkHeroRowCaseOne(final int affectedRow) {
        if (this.playerTurn == 1) {
            return affectedRow == 0 || affectedRow == 1;
        }  else {
            return affectedRow == 2 || affectedRow == BACKROWOFPLAYERONEIDX;
        }
    }

    /**
     *
     * @param hero the hero who uses the ability
     * @param output the output array
     * @param gametable the game table
     * @param affectedRow the row affected by the ability
     * @param attacker the player who uses the ability
     */
    public void caseTwoHeroAbility(final Hero hero, final ArrayNode output,
                                   final Gametable gametable, final int affectedRow,
                                   final Player attacker) {
        if (checkHeroRowCaseTwo(affectedRow)) {
            attacker.setManaToUse(attacker.getManaToUse() - hero.getMana());
            hero.specialAbility(gametable, affectedRow);
            hero.setHasAttacked(true);
        }  else {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("command", "useHeroAbility");
            objectNode.put("affectedRow", affectedRow);
            objectNode.put("error", "Selected row does not belong to the current player.");
            output.add(objectNode);
        }
    }

    /**
     *
     * @param affectedRow the row affected by the ability
     * @return true if the row is valid, false otherwise
     */
    public boolean checkHeroRowCaseTwo(final int affectedRow) {
        if (this.playerTurn == 1) {
            return affectedRow == 2 || affectedRow == BACKROWOFPLAYERONEIDX;
        } else {
            return affectedRow == 0 || affectedRow == 1;
        }
    }

    /**
     *
     * @param gametable the game table
     * @param attacker the player who attacks
     * @param output the output array
     * @param playerOne player one
     * @param playerTwo player two
     */
    public void attackHero(final Gametable gametable, final Coordinates attacker,
                           final ArrayNode output, final Player playerOne,
                           final Player playerTwo) {
        int xAttacker = attacker.getX();
        int yAttacker = attacker.getY();
        Minion cardAttacker = gametable.getTable().get(xAttacker).get(yAttacker);
        ObjectMapper objectMapper = new ObjectMapper();
        if (!cardAttacker.isStatusFrozen()) {
            if (!cardAttacker.isHasAttacked()) {
                if (existsTank(gametable)) {
                    ObjectNode objectNode = objectMapper.createObjectNode();
                    objectNode.put("command", "useAttackHero");
                    ObjectNode cardAttackerNode = objectMapper.createObjectNode();
                    cardAttackerNode.put("x", attacker.getX());
                    cardAttackerNode.put("y", attacker.getY());
                    objectNode.put("cardAttacker", cardAttackerNode);
                    objectNode.put("error", "Attacked card is not of type 'Tank'.");
                    output.add(objectNode);
                } else {
                    damageHero(cardAttacker, playerOne, playerTwo, output);
                }
            } else {
                ObjectNode objectNode = objectMapper.createObjectNode();
                objectNode.put("command", "useAttackHero");
                ObjectNode cardAttackerNode = objectMapper.createObjectNode();
                cardAttackerNode.put("x", attacker.getX());
                cardAttackerNode.put("y", attacker.getY());
                objectNode.put("cardAttacker", cardAttackerNode);
                objectNode.put("error", "Attacker card has already attacked this turn.");
                output.add(objectNode);
            }
        } else {
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("command", "useAttackHero");
            ObjectNode cardAttackerNode = objectMapper.createObjectNode();
            cardAttackerNode.put("x", attacker.getX());
            cardAttackerNode.put("y", attacker.getY());
            objectNode.put("cardAttacker", cardAttackerNode);
            objectNode.put("error", "Attacker card is frozen.");
            output.add(objectNode);
        }
    }

    /**
     *
     * @param card the card to be checked
     * @param playerOne player one
     * @param playerTwo player two
     * @param output the output array
     */
    public void damageHero(final Minion card, final Player playerOne,
                           final Player playerTwo, final ArrayNode output) {
        if (playerTurn == 1) {
            playerTwo.getHero().setHealth(playerTwo.getHero().getHealth() - card.getAttackDamage());
            card.setHasAttacked(true);
            if (playerTwo.getHero().getHealth() <= 0) {
                this.gameEnded = true;
                gameEnd(1, output);
            }
        }  else {
            playerOne.getHero().setHealth(playerOne.getHero().getHealth() - card.getAttackDamage());
            card.setHasAttacked(true);
            if (playerOne.getHero().getHealth() <= 0) {
                this.gameEnded = true;
                gameEnd(2, output);
            }
        }
    }

    /**
     *
     * @param playerIdx the player who wins
     * @param output the output array
     */
    public void gameEnd(final int playerIdx, final ArrayNode output) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        if (playerIdx == 1) {
            objectNode.put("gameEnded", "Player one killed the enemy hero.");
            this.gamestats.setNrPlayerOneWins(gamestats.getNrPlayerOneWins() + 1);
        }  else {
            objectNode.put("gameEnded", "Player two killed the enemy hero.");
            this.gamestats.setNrPlayerTwoWins(gamestats.getNrPlayerTwoWins() + 1);
        }
        output.add(objectNode);
    }

    /**
     *
     * @param gametable the game table
     * @param action the action to be performed
     * @param output the output array
     */
    public void useCardAbility(final Gametable gametable, final ActionsInput action,
                                 final ArrayNode output) {
        int xAttacker = action.getCardAttacker().getX();
        int yAttacker = action.getCardAttacker().getY();
        Minion cardAttacker = gametable.getTable().get(xAttacker).get(yAttacker);
        int xAttacked = action.getCardAttacked().getX();
        int yAttacked = action.getCardAttacked().getY();
        Minion cardAttacked = gametable.getTable().get(xAttacked).get(yAttacked);
        if (this.checkUseAbilityIsValid(cardAttacker, action.getCardAttacked(),
                action.getCardAttacker(), output)) {
            switch (cardAttacker.getName()) {
                case "Disciple":
                    this.discipleAbility(cardAttacker, cardAttacked,
                            output, action);
                    break;
                case "Miraj":
                    this.mirajAbility(cardAttacker, cardAttacked,
                            gametable, output, action);
                    break;
                case "The Cursed One":
                    this.theCursedOneAbility(cardAttacker, cardAttacked, gametable, output, action);
                    break;
                case "The Ripper":
                    this.theRipperAbility(cardAttacker, cardAttacked, gametable, output, action);
                    break;
                default:
                    break;
            }
        }


    }

    /**
     *
     * @param card card which uses the ability
     * @param cardAttacked the card that is attacked
     * @param gametable the game table
     * @param output the output array
     * @param action the action to be performed
     */
    public void theRipperAbility(final Minion card, final Minion cardAttacked,
                                 final Gametable gametable, final ArrayNode output,
                                 final ActionsInput action) {
        if (checkAttackRightEnemy(action.getCardAttacked().getX())) {
            if (existsTank(gametable)) {
                if (cardIsTank(cardAttacked)) {
                    card.specialAbility(cardAttacked);
                    card.setHasAttacked(true);
                } else {
                    cardAttackedIsNotTank(output, action.getCardAttacker(),
                            action.getCardAttacked(), "cardUsesAbility");
                }
            } else {
                card.specialAbility(cardAttacked);
                card.setHasAttacked(true);
            }
        } else {
            Coordinates attacker = action.getCardAttacker();
            Coordinates attacked = action.getCardAttacked();
            attackedOtherError(output, attacker, attacked, "cardUsesAbility",
                    "Attacked card does not belong to the enemy.");
        }
    }

    /**
     *
     * @param card card which uses the ability
     * @param cardAttacked the card that is attacked
     * @param gametable the game table
     * @param output the output array
     * @param action the action to be performed
     */
    public void theCursedOneAbility(final Minion card, final Minion cardAttacked,
                                    final Gametable gametable, final ArrayNode output,
                                    final ActionsInput action) {
        if (checkAttackRightEnemy(action.getCardAttacked().getX())) {
            if (existsTank(gametable)) {
                if (cardIsTank(cardAttacked)) {
                    card.specialAbility(cardAttacked);
                    card.setHasAttacked(true);
                } else {
                    cardAttackedIsNotTank(output, action.getCardAttacker(),
                            action.getCardAttacked(), "cardUsesAbility");
                }
            } else {
                card.specialAbility(cardAttacked);
                card.setHasAttacked(true);
            }
        } else {
            Coordinates attacker = action.getCardAttacker();
            Coordinates attacked = action.getCardAttacked();
            attackedOtherError(output, attacker, attacked, "cardUsesAbility",
                    "Attacked card does not belong to the enemy.");
        }
    }

    /**
     *
     * @param card the card which uses the ability
     * @param cardAttacked the card that is attacked
     * @param output output array
     * @param action the action to be performed
     */
    public void discipleAbility(final Minion card, final Minion cardAttacked,
                                final ArrayNode output,
                                final ActionsInput action) {
        if (!checkAttackRightEnemy(action.getCardAttacked().getX())) {
            cardAttacked.setHealth(cardAttacked.getHealth() + 2);
            card.setHasAttacked(true);
        } else {
            Coordinates attacker = action.getCardAttacker();
            Coordinates attacked = action.getCardAttacked();
            attackedOtherError(output, attacker, attacked, "cardUsesAbility",
                    "Attacked card does not belong to the current player.");
        }
    }

    /**
     *
     * @param card the card which uses the ability
     * @param cardAttacked the card that is attacked
     * @param gametable the game table
     * @param output the output array
     * @param action the action to be performed
     */
    public void mirajAbility(final Minion card, final Minion cardAttacked,
                             final Gametable gametable, final ArrayNode output,
                             final ActionsInput action) {
        if (this.checkAttackRightEnemy(action.getCardAttacked().getX())) {
            if (this.existsTank(gametable)) {
                if (cardIsTank(cardAttacked)) {
                    card.specialAbility(cardAttacked);
                    card.setHasAttacked(true);
                } else {
                    cardAttackedIsNotTank(output, action.getCardAttacker(),
                            action.getCardAttacked(), "cardUsesAbility");
                }
            } else {
                card.specialAbility(cardAttacked);
                card.setHasAttacked(true);
            }
        } else {
            Coordinates attacker = action.getCardAttacker();
            Coordinates attacked = action.getCardAttacked();
            attackedOtherError(output, attacker, attacked, "cardUsesAbility",
                    "Attacked card does not belong to the enemy.");
        }
    }

    /**
     *
     * @param output the output array
     * @param attacker the player who attacks
     * @param attacked the player who is attacked
     * @param command the command to be performed
     * @param error the error message
     */
    private static void attackedOtherError(final ArrayNode output, final Coordinates attacker,
                                           final Coordinates attacked, final String command,
                                           final String error) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", command);
        ObjectNode cardAttackerNode = objectMapper.createObjectNode();
        cardAttackerNode.put("x", attacker.getX());
        cardAttackerNode.put("y", attacker.getY());
        objectNode.put("cardAttacker", cardAttackerNode);
        ObjectNode cardAttackedNode = objectMapper.createObjectNode();
        cardAttackedNode.put("x", attacked.getX());
        cardAttackedNode.put("y", attacked.getY());
        objectNode.put("cardAttacked", cardAttackedNode);
        objectNode.put("error", error);
        output.add(objectNode);
    }

    /**
     *
     * @param card the card to be checked
     * @param attacked the card that is attacked
     * @param attacker the card that attacks
     * @param output the output array
     * @return true if the card can attack, false otherwise
     */
    public boolean checkUseAbilityIsValid(final Minion card, final Coordinates attacked,
                                          final Coordinates attacker, final ArrayNode output) {
        if (card.isStatusFrozen()) {
            usedFrozenCardError(attacked, attacker, output, "cardUsesAbility");
            return false;
        }
        if (card.isHasAttacked()) {
            cardAlreadyAttacked(attacked, attacker, "cardUsesAbility", output);
            return false;
        }
        return true;
    }

    /**
     *
     * @param attacked the card that is attacked
     * @param attacker the card that attacks
     * @param command the command to be performed
     * @param output the output array
     */
    private static void cardAlreadyAttacked(final Coordinates attacked, final Coordinates attacker,
                                            final String command, final ArrayNode output) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", command);
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
    }

    /**
     *
     * @param attacked the card that is attacked
     * @param attacker the card that attacks
     * @param output the output array
     * @param command the command to be performed
     */
    private static void usedFrozenCardError(final Coordinates attacked, final Coordinates attacker,
                                            final ArrayNode output, final String command) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", command);
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
    }

    /**
     *
     * @param gametable the game table
     * @param x x coordinate
     * @param y y coordinate
     * @param output the output array
     */
    public void displayCard(final Gametable gametable, final int x, final int y,
                            final ArrayNode output) {
        if (gametable.getTable().get(x).size() >= y) {
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
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("command", "getCardAtPosition");
            objectNode.put("x", x);
            objectNode.put("y", y);
            objectNode.put("output", "No card available at that position.");
            output.add(objectNode);
        }

    }

    /**
     *
     * @param gametable the game table
     * @param playerOne player one
     * @param playerTwo player two
     */
    public void resetHasAttacked(final Gametable gametable, final Player playerOne,
                                 final Player playerTwo) {
        for (ArrayList<Minion> row : gametable.getTable()) {
            for (Minion card : row) {
                card.setHasAttacked(false);
            }
        }

        playerOne.getHero().setHasAttacked(false);
        playerTwo.getHero().setHasAttacked(false);
    }

    /**
     *
     * @param gametable the game table
     * @param attacker the card that attacks
     * @param attacked the card that is attacked
     * @param output the output arra
     */
    public void attackMinion(final Gametable gametable, final Coordinates attacker,
                             final Coordinates attacked, final ArrayNode output) {
        int xAttacker = attacker.getX();
        int yAttacker = attacker.getY();
        int xAttacked = attacked.getX();
        int yAttacked = attacked.getY();
        Minion cardAttacker = gametable.getTable().get(xAttacker).get(yAttacker);
        Minion cardAttacked = gametable.getTable().get(xAttacked).get(yAttacked);
        if (this.checkAttackIsValid(cardAttacked, cardAttacker,
                xAttacked, output, attacker, attacked, gametable)) {
            cardAttacker.setHasAttacked(true);
            cardAttacked.setHealth(cardAttacked.getHealth() - cardAttacker.getAttackDamage());
            if (cardAttacked.getHealth() <= 0) {
                gametable.getTable().get(xAttacked).remove(yAttacked);
            }
        }
    }

    /**
     *
     * @param cardAttacked the card that is attacked
     * @param cardAttacker the card that attacks
     * @param xAttacked the row coordinate of the card that is attacked
     * @param output the output array
     * @param attacker the coordinates of the card that attacks
     * @param attacked the coordinates of the card that is attacked
     * @param gametable the game table
     * @return true if the attack is valid, false otherwise
     */
    public boolean checkAttackIsValid(final Minion cardAttacked, final Minion cardAttacker,
                                      final int xAttacked, final ArrayNode output,
                                      final Coordinates attacker, final Coordinates attacked,
                                      final Gametable gametable) {
        if (!this.checkAttackRightEnemy(xAttacked)) {
            attackedOtherError(output, attacker, attacked, "cardUsesAttack",
                    "Attacked card does not belong to the enemy.");
            return false;
        }
        if (cardAttacker.isHasAttacked()) {
            cardAlreadyAttacked(attacked, attacker, "cardUsesAttack", output);
            return false;
        }
        if (cardAttacker.isStatusFrozen()) {
            usedFrozenCardError(attacked, attacker, output, "cardUsesAttack");
            return false;
        }
        if (this.existsTank(gametable)) {
            if (!this.cardIsTank(cardAttacked)) {
                cardAttackedIsNotTank(output, attacker, attacked, "cardUsesAttack");
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param output the output array
     * @param attacker the player who attacks
     * @param attacked the player who is attacked
     * @param command the command to be performed
     */
    private static void cardAttackedIsNotTank(final ArrayNode output, final Coordinates attacker,
                                              final Coordinates attacked, final String command) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", command);
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
    }

    /**
     *
     * @param gametable the game table
     * @return true if a tank exists, false otherwise
     */
    public boolean existsTank(final Gametable gametable) {
        if (playerTurn == 2) {
            for (Minion minion : gametable.getTable().get(2)) {
                if (this.cardIsTank(minion)) {
                    return true;
                }
            }
        } else {
            for (Minion minion : gametable.getTable().get(1)) {
                if (this.cardIsTank(minion)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *
     * @param card the card to be checked
     * @return true if the card is a tank, false otherwise
     */
    public boolean cardIsTank(final Minion card) {
        switch (card.getName()) {
            case "Goliath":
                return true;
            case "Warden":
                return true;
            default:
                return false;
        }
    }

    /**
     *
     * @param x the row of the attacked card
     * @return true if the card belongs to the enemy, false otherwise
     */
    public boolean checkAttackRightEnemy(final int x) {
        if (playerTurn == 1) {
            return x == 0 || x == 1;
        } else {
            return x == 2 || x == BACKROWOFPLAYERONEIDX;
        }
    }

    /**
     *
     * @param gametable the game table
     * @param playerOne player one
     * @param handIdx the index of the card in the hand
     * @param output the output array
     */
    public void placeCardPlayerOne(final Gametable gametable, final Player playerOne,
                                   final int handIdx, final ArrayNode output) {
        ArrayList<Minion> hand = playerOne.getHand();
        if (handIdx < hand.size()) {
            Minion card = hand.get(handIdx);
            if (playerOne.getManaToUse() >= card.getMana()) {
                playerOne.setManaToUse(playerOne.getManaToUse() - card.getMana());
                gametable.placeCardPlayerOne(card);
                hand.remove(handIdx);
            } else {
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode objectNode = objectMapper.createObjectNode();
                objectNode.put("command", "placeCard");
                objectNode.put("handIdx", handIdx);
                objectNode.put("error", "Not enough mana to place card on table.");
                output.add(objectNode);
            }
        }
    }

    /**
     *
     * @param gametable the game table
     * @param playerTwo player two
     * @param handIdx the index of the card in the hand
     * @param output the output array
     */
    public void placeCardPlayerTwo(final Gametable gametable, final Player playerTwo,
                                   final int handIdx, final ArrayNode output) {
        ArrayList<Minion> hand = playerTwo.getHand();
        if (handIdx < hand.size()) {
            Minion card = hand.get(handIdx);
            if (playerTwo.getManaToUse() >= card.getMana()) {
                playerTwo.setManaToUse(playerTwo.getManaToUse() - card.getMana());
                gametable.placeCardPlayerTwo(card);
                hand.remove(handIdx);
            } else {
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode objectNode = objectMapper.createObjectNode();
                objectNode.put("command", "placeCard");
                objectNode.put("handIdx", handIdx);
                objectNode.put("error", "Not enough mana to place card on table.");
                output.add(objectNode);
            }
        }
    }

    /**
     *
     * @param deck the deck to be displayed
     * @param output the output array
     * @param playerIdx the player index
     */
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

    /**
     *
     * @param player the player to be displayed
     * @param output the output array
     * @param playerIdx the player index
     */
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

    /**
     *
     * @param output the output array
     */
    public void displayTurn(final ArrayNode output) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "getPlayerTurn");
        objectNode.put("output", playerTurn);
        output.add(objectNode);
    }

    /**
     *
     * @param hand the hand to be displayed
     * @param output the output array
     * @param playerIdx the player index
     */
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

    /**
     *
     * @param gametable the game table
     * @param output the output array
     */
    public void displayTable(final Gametable gametable, final ArrayNode output) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "getCardsOnTable");
        ArrayNode arrayNode = objectMapper.createArrayNode();
        for (ArrayList<Minion> row : gametable.getTable()) {
            if (!row.isEmpty()) {
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
            } else {
                ArrayNode rowNode = objectMapper.createArrayNode();
                arrayNode.add(rowNode);
            }
        }
        objectNode.put("output", arrayNode);
        output.add(objectNode);
    }

    /**
     *
     * @param player the player whose mana is increase
     */
    public void increaseMana(final Player player) {
        player.setManaToUse(player.getManaToUse() + round);
    }

    /**
     *
     * @param player the player whose mana is displayed
     * @param output the output array
     * @param playerIdx the player index
     */
    public void displayMana(final Player player, final ArrayNode output, final int playerIdx) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "getPlayerMana");
        objectNode.put("output", player.getManaToUse());
        objectNode.put("playerIdx", playerIdx);
        output.add(objectNode);
    }
}
