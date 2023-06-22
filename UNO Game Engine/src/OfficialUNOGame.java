import java.util.*;
import java.util.List;

public class OfficialUNOGame extends Game {

    private final Scanner scanner = new Scanner(System.in);

    @Override
    protected Card playCard(Player currentPlayer) {
        System.out.println("Enter a valid card to play.");
        String cardNotation = scanner.nextLine();
        if (currentPlayer.getCardsInHand().size() == 2) {
            playerCalledUNO(currentPlayer, cardNotation);
        }
        Card card = currentPlayer.pickCardFromHand(cardNotation);
        while (!colorIsValid(card) && !symbolIsValid(card)) {
            System.out.println("Enter a playable card.");
            cardNotation = scanner.nextLine();
            card = currentPlayer.pickCardFromHand(cardNotation);
        }
        return card;
    }

    private static void playerCalledUNO(Player currentPlayer, String cardNotation) {
        if (cardNotation.equals("UNO")) {
            currentPlayer.calledUNO();
            System.out.println("Player called UNO");
        }
    }

    @Override
    protected void playerDidNotCallUNO(Player currentPlayer) {
        if (!currentPlayer.isUNO() && currentPlayer.getCardsInHand().size() == 1) {
            currentPlayer.addCard(deck.drawCard());
            currentPlayer.addCard(deck.drawCard());
        }
    }

    @Override
    protected void validateCard(Card card) {
        if (!colorIsValid(card) && !symbolIsValid(card)) {
            throw new RuntimeException("Card does not match.");
        }
    }

    @Override
    protected boolean symbolIsValid(Card card) {
        return card.getSymbol().equals(deck.getTopCard().getSymbol());
    }

    @Override
    protected boolean colorIsValid(Card card) {
        for (CardColor cardColor : card.getColor()) {
            for (CardColor topCardColor : deck.getTopCard().getColor()) {
                if (cardColor.equals(topCardColor)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void discardPlayedCardIntoDiscardPile(Card card, Player currentPlayer) {
        deck.addToDiscardPile(card);
        currentPlayer.removeCard(card);
    }

    @Override
    protected void drawCardFromDrawPile(Player currentPlayer) {
        System.out.println("Player has no matching cards. Draw from draw pile.");
        Card drawnCard = deck.drawCard();
        currentPlayer.addCard(drawnCard);
        showPlayerAndCards(currentPlayer);
        try {
            validateCard(drawnCard);
        } catch (RuntimeException e) {
            throw new RuntimeException("Drawn card does not match");
        }
    }

    @Override
    protected boolean noMatchingCardsInPlayerHand(Player currentPlayer) {
        for (Card card : currentPlayer.getCardsInHand()) {
            if (colorIsValid(card) || symbolIsValid(card)) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void showPlayerAndCards(Player currentPlayer) {
        System.out.println(currentPlayer + "Cards in hand: ");
        System.out.println(currentPlayer.getCardsInHand());
    }

    @Override
    protected void applyRules(Card card) {
        if (isActionCard(card)) {
            applyActionCardRules(card);
        } else if (isWildCard(card)) {
            applyWildCardRules(card);
        }
    }

    @Override
    protected void applyActionCardRules(Card card) {
        switch (card.getSymbol()) {
            case "SKIP" -> skipPlayerPenalty();
            case "REVERSE" -> reverseTurnsPenalty();
            case "DRAW TWO" -> drawTwoPenalty();
        }
    }

    @Override
    protected void drawTwoPenalty() {
        players.getFirst().addCard(deck.drawCard());
        players.getFirst().addCard(deck.drawCard());
    }

    @Override
    protected void reverseTurnsPenalty() {
        players.addFirst(players.getLast());
        Collections.reverse(players);
    }

    @Override
    protected void skipPlayerPenalty() {
        endTurn();
    }

    @Override
    protected void endTurn() {
        players.addLast(players.remove());
    }

    @Override
    protected void applyWildCardRules(Card card) {
        switch (card.getSymbol()) {
            case "WILD" -> changeColor();
            case "WILD DRAW FOUR" -> {
                changeColor();
                drawFourPenalty();
            }
        }
    }

    @Override
    protected void changeColor() {
        System.out.println("Enter chosen color: ");
        String color = scanner.nextLine();
        while (validateInputColor(color)) {
            System.out.println("Invalid color. Enter another: ");
            color = scanner.nextLine();
        }
        MainCardColors validColor = MainCardColors.valueOf(color);
        List<CardColor> validListOfColors = new ArrayList<>();
        validListOfColors.add(validColor);
        Card card = new Card(validListOfColors, Card.UNIQUE);
        deck.getDiscardPile().add(card);
    }

    @Override
    protected boolean validateInputColor(String inputColor) {
        try {
            MainCardColors.valueOf(inputColor);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    protected void drawFourPenalty() {
        for (int i = 0; i < 4; i++) {
            players.getFirst().addCard(deck.drawCard());
        }
    }

    @Override
    protected void createDeck() {
        List<Card> drawPile = new ArrayList<>(createNumberedCards());
        drawPile.addAll(createActionCards());
        drawPile.addAll(createWildCards());
        afterDeckCreationHook(drawPile);
        deck = new Deck(drawPile);
    }

    protected List<Card> createNumberedCards() {
        List<Card> drawPile = new ArrayList<>(createNumberedCardsBasedOnColor(MainCardColors.getRedColor()));
        drawPile.addAll(createNumberedCardsBasedOnColor(MainCardColors.getBlueColor()));
        drawPile.addAll(createNumberedCardsBasedOnColor(MainCardColors.getGreenColor()));
        drawPile.addAll(createNumberedCardsBasedOnColor(MainCardColors.getYellowColor()));
        return drawPile;
    }

    protected List<Card> createNumberedCardsBasedOnColor(List<CardColor> color) {
        List<Card> drawPile = new ArrayList<>();
        drawPile.add(new Card(color, String.valueOf(0)));
        for (int i = 1; i < 10; i++) {
            drawPile.add(new Card(color, String.valueOf(i)));
            drawPile.add(new Card(color, String.valueOf(i)));
        }
        return drawPile;
    }

    protected List<Card> createActionCards() {
        List<Card> drawPile = new ArrayList<>(createCards("SKIP"));
        drawPile.addAll(createCards("REVERSE"));
        drawPile.addAll(createCards("DRAW TWO"));
        return drawPile;
    }

    protected List<Card> createCards(String symbol) {
        List<Card> drawPile = new ArrayList<>(createTwoCards(MainCardColors.getRedColor(), symbol));
        drawPile.addAll(createTwoCards(MainCardColors.getBlueColor(), symbol));
        drawPile.addAll(createTwoCards(MainCardColors.getGreenColor(), symbol));
        drawPile.addAll(createTwoCards(MainCardColors.getYellowColor(), symbol));
        return drawPile;
    }

    protected List<Card> createTwoCards(List<CardColor> color, String symbol) {
        List<Card> drawPile = new ArrayList<>();
        drawPile.add(new Card(color, symbol));
        drawPile.add(new Card(color, symbol));
        return drawPile;
    }

    protected List<Card> createWildCards() {
        List<Card> drawPile = new ArrayList<>(createFourWildCards("WILD"));
        drawPile.addAll(createFourWildCards("WILD DRAW FOUR"));
        return drawPile;
    }

    protected List<Card> createFourWildCards(String symbol) {
        List<CardColor> colorList = new ArrayList<>(createWildCardsColorList());
        List<Card> drawPile = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            drawPile.add(new Card(colorList, symbol));
        }
        return drawPile;
    }

    protected List<CardColor> createWildCardsColorList() {
        return MainCardColors.getAllMainColors();
    }

    @Override
    protected void afterDeckCreationHook(List<Card> deck) {
        // Do nothing
    }

    @Override
    protected void shuffleCards() {
        Collections.shuffle(deck.getDrawPile());
    }

    @Override
    protected void dealCardsToPlayers() {
        int cards = getNumberOfDealtCards();
        for (int i = 0; i < cards; i++) {
            for (Player player : players) {
                player.addCard(deck.drawCard());
            }
        }
    }

    @Override
    protected int getNumberOfDealtCards() {
        return 7;
    }

    @Override
    protected void drawFaceUpCard() {
        deck.addToDiscardPile(deck.drawCard());
    }

    @Override
    protected void calculatePlayerScore() {
        if (isEndOfRound()) {
            for (Player player : players) {
                List<Card> cardsInHand = player.getCardsInHand();
                for (Card card : cardsInHand) {
                    player.setScore(player.getScore() + valueOfCard(card));
                }
            }
        }
    }

    @Override
    protected int valueOfCard(Card card) {
        if (isActionCard(card)) {
            return 20;
        } else if (isWildCard(card)) {
            return 50;
        } else {
            return Integer.parseInt(card.getSymbol());
        }
    }

    protected boolean isActionCard(Card card) {
        return (card.getSymbol().equals("SKIP") || card.getSymbol().equals("REVERSE") || card.getSymbol().equals("DRAW 2"));
    }

    protected boolean isWildCard(Card card) {
        return (card.getSymbol().equals("WILD") || card.getSymbol().equals("WILD DRAW FOUR"));
    }

    @Override
    protected void checkEndOfRound() {
        for (Player player : players) {
            if (player.hasNoCards()) {
                endRound();
            }
        }
    }

    @Override
    protected int getWinningScore() {
        return 500;
    }

    @Override
    protected void checkEndOfGame() {
        for (Player player : players) {
            if (player.getScore() >= getWinningScore()) {
                endGame();
            }
        }
    }
}
