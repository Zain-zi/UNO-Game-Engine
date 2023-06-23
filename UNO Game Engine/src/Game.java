import java.util.*;

public abstract class Game {
    protected final Scanner scanner = new Scanner(System.in);
    protected final LinkedList<Player> players = new LinkedList<>();
    private boolean endOfRound;
    private boolean endOfGame;
    protected Deck deck;

    public void play() {
        initializeGame();
        while (!isEndOfGame()) {
            initializeRound();
            checkEndOfGame();
        }
        showWinnerOfGameAndScore();
    }

    protected void initializeRound() {
        sortPlayers();
        startRound();
        createDeck();
        shuffleCards();
        dealCardsToPlayers();
        drawFaceUpCard();
        while (!isEndOfRound()) {
            turn();
            checkEndOfRound();
        }
        calculatePlayerScore();
        showWinnerOfRoundAndScore();
    }

    protected abstract void sortPlayers();

    protected void initializeGame() {
        definePlayers(getNumberOfPlayers());
    }

    protected int getNumberOfPlayers() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of players: ");
        int numberOfPlayers = scanner.nextInt();
        while (numberOfPlayers < 2 || numberOfPlayers > 10) { // throw exception
            System.out.println("Number of players must be at least 2 and no more than 10." + "\nPlease enter a valid number: ");
            numberOfPlayers = scanner.nextInt();
        }
        return numberOfPlayers;
    }

    protected void definePlayers(int numberOfPlayers) {
        for (int i = numberOfPlayers; i > 0; i--) {
            players.addFirst(new Player("P" + i));
        }
    }

    protected void turn() {
        Player currentPlayer = getCurrentPlayer();
        showPlayerAndCards(currentPlayer);
        showTopCardInDiscardPile();
        if (noMatchingCardsInPlayerHand(currentPlayer)) {
            try {
                drawCardFromDrawPile(currentPlayer);
            } catch (RuntimeException e) {
                System.out.println("Drawn card does not match. Ending turn. \n");
                endTurn();
                return;
            }
        }
        Card card = playCard(currentPlayer);
        discardPlayedCardIntoDiscardPile(card, currentPlayer);
        playerDidNotCallUNO(currentPlayer);
        applyRules(card);
        endTurn();
        System.out.println();
    }

    protected abstract Player getCurrentPlayer();

    protected abstract void showWinnerOfRoundAndScore();

    protected abstract String checkForUNO(Player currentPlayer, String cardNotation);

    protected abstract void playerCalledUNO(Player currentPlayer, String cardNotation);

    protected abstract boolean hasPlayerCalledUNO(Player currentPlayer);

    protected abstract void showWinnerOfGameAndScore();

    protected abstract void showTopCardInDiscardPile();

    protected abstract void playerDidNotCallUNO(Player currentPlayer);

    protected abstract void discardPlayedCardIntoDiscardPile(Card card, Player player);

    protected abstract void drawCardFromDrawPile(Player player);

    protected abstract boolean noMatchingCardsInPlayerHand(Player player);

    protected abstract void applyRules(Card card);

    protected abstract boolean isActionCard(Card card);

    protected abstract void applyActionCardRules(Card card);

    protected abstract void drawTwoPenalty();

    protected abstract void reverseTurnsPenalty();

    protected abstract void skipPlayerPenalty();

    protected abstract void endTurn();

    protected abstract void applyWildCardRules(Card card);

    protected abstract void showPlayerAndCards(Player player);

    protected abstract Card playCard(Player player);

    protected abstract void validateCard(Card card);

    protected abstract boolean symbolIsValid(Card card);

    protected abstract boolean colorIsValid(Card card);

    protected abstract void changeColor();

    protected abstract boolean isValidInputColor(String inputColor);

    protected abstract void drawFourPenalty();

    protected abstract Player getNextPlayer();

    protected abstract void createDeck();

    protected abstract List<Card> createNumberedCards();

    protected abstract List<Card> createNumberedCardsBasedOnColor(List<CardColor> color);

    protected abstract List<Card> createActionCards();

    protected abstract List<Card> createCards(String symbol);

    protected abstract List<Card> createTwoCards(List<CardColor> color, String symbol);

    protected abstract List<Card> createWildCards();

    protected abstract List<Card> createFourWildCards(String symbol);

    protected abstract List<CardColor> createWildCardsColorList();

    protected abstract void afterDeckCreationHook(List<Card> deck);

    protected abstract void shuffleCards();

    protected abstract void dealCardsToPlayers();

    protected abstract int getNumberOfDealtCards();

    protected abstract void drawFaceUpCard();

    protected abstract void calculatePlayerScore();

    protected abstract void checkEndOfRound();

    protected abstract boolean isWildCard(Card card);

    protected abstract int getWinningScore();

    protected abstract void checkEndOfGame();

    protected abstract Player getWinnerPlayer();

    protected abstract int valueOfCard(Card card);

    protected void startRound() {
        endOfRound = false;
    }

    protected void endRound() {
        endOfRound = true;
    }

    protected void endGame() {
        endOfGame = true;
    }

    protected boolean isEndOfRound() {
        return endOfRound;
    }

    protected boolean isEndOfGame() {
        return endOfGame;
    }
}
