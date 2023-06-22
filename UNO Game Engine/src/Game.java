import java.util.*;

public abstract class Game {
    private boolean endOfRound;

    private boolean endOfGame;

    protected final LinkedList<Player> players = new LinkedList<>();

    protected Deck deck;

    public void play() {
        initializeGame();
        while (!isEndOfGame()) {
            initializeRound();
            checkEndOfGame();
        }

    }

    protected void initializeRound() {
        createDeck();
        shuffleCards();
        dealCardsToPlayers();
        drawFaceUpCard();
        while (!isEndOfRound()) {
            turn();
            checkEndOfRound();
        }
        calculatePlayerScore();
    }

    protected void initializeGame() {
        definePlayers(getNumberOfPlayers());
    }

    protected int getNumberOfPlayers() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of players: ");
        int numberOfPlayers = scanner.nextInt();
        while (numberOfPlayers < 2 || numberOfPlayers > 10) { // throw exception
            System.out.println("Number of players must be at least 2 and no more than 10." +
                    "\nPlease enter a valid number: ");
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
        Player currentPlayer = players.getFirst();
        showPlayerAndCards(currentPlayer);
        if (noMatchingCardsInPlayerHand(currentPlayer)) {
            try {
                drawCardFromDrawPile(currentPlayer);
            } catch (RuntimeException e) {
                endTurn();
                return;
            }
        }
        Card card = playCard(currentPlayer);
        discardPlayedCardIntoDiscardPile(card, currentPlayer);
        playerDidNotCallUNO(currentPlayer);
        endTurn();
        applyRules(card);
    }

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

    protected abstract boolean validateInputColor(String inputColor);

    protected abstract void drawFourPenalty();

    protected abstract void createDeck();

    protected abstract void afterDeckCreationHook(List<Card> deck);

    protected abstract void shuffleCards();

    protected abstract void dealCardsToPlayers();

    protected abstract int getNumberOfDealtCards();

    protected abstract void drawFaceUpCard();

    protected abstract void calculatePlayerScore();

    protected abstract void checkEndOfRound();

    protected abstract int getWinningScore();

    protected abstract void checkEndOfGame();

    protected abstract int valueOfCard(Card card);

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

    protected Deque<Player> getPlayers() {

        return players;
    }

    protected Deck getDeck() {
        return deck;
    }
}
