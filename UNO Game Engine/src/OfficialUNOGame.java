import java.util.*;
import java.util.List;

public class OfficialUNOGame extends Game {

    @Override
    protected Card playCard(Player currentPlayer) {
        System.out.println("Enter a valid card to play.");
        String cardNotation = scanner.nextLine();
        cardNotation = checkForUNO(currentPlayer, cardNotation);
        while (!currentPlayer.hasCard(cardNotation)) {
            System.out.println("Player does not have this card or card is invalid. Enter another card.");
            cardNotation = scanner.nextLine();
        }
        Card card = currentPlayer.pickCardFromHand(cardNotation);
        while (!colorIsValid(card) && !symbolIsValid(card) || !currentPlayer.hasCard(cardNotation)) {
            System.out.println("Player does not have this card or card is invalid. Enter another card.");
            cardNotation = scanner.nextLine();
            if (currentPlayer.hasCard(cardNotation)) {
                card = currentPlayer.pickCardFromHand(cardNotation);
            }
        }
        card = currentPlayer.pickCardFromHand(cardNotation);
        return card;
    }

    @Override
    protected String checkForUNO(Player currentPlayer, String cardNotation) {
        if (currentPlayer.getCardsInHand().size() == 2) {
            playerCalledUNO(currentPlayer, cardNotation);
            if (hasPlayerCalledUNO(currentPlayer)) {
                System.out.println("You called UNO. Now enter a valid card to play.");
                cardNotation = scanner.nextLine();
            }
        }
        return cardNotation;
    }

    @Override
    protected void playerCalledUNO(Player currentPlayer, String cardNotation) {
        if (cardNotation.equals("UNO")) {
            currentPlayer.setUNO();
            System.out.println("Player called UNO");
        }
    }

    @Override
    protected boolean hasPlayerCalledUNO(Player currentPlayer) {
        return currentPlayer.hasCalledUNO();
    }

    @Override
    protected void showWinnerOfGameAndScore() {
        String winnerPlayer = "";
        int winningScore = 0;
        for (Player player : players) {
            if (player.getScore() > winningScore) {
                winningScore = player.getScore();
                winnerPlayer = player.getName();
            }
        }
        System.out.println("The winner of the game is " + winnerPlayer + " with a score of " + winningScore + "\n");
    }

    @Override
    protected void showWinnerOfRoundAndScore() {
        String winnerPlayer = "";
        int winningScore = 0;
        for (Player player : players) {
            if (player.getScore() > winningScore) {
                winningScore = player.getScore();
                winnerPlayer = player.getName();
            }
        }
        System.out.println("The winner of the round is " + winnerPlayer + " with a score of " + winningScore + "\n");
    }

    @Override
    protected void showTopCardInDiscardPile() {
        System.out.println("Top card in discard pile is " + deck.getTopCard());
    }

    @Override
    protected void playerDidNotCallUNO(Player currentPlayer) {
        if (!currentPlayer.hasCalledUNO() && currentPlayer.getCardsInHand().size() == 1) {
            currentPlayer.addCard(deck.drawCard());
            currentPlayer.addCard(deck.drawCard());
            System.out.println("Player had two cards and did not call UNO. Player received a two card penalty.");
            System.out.println(currentPlayer.getCardsInHand() + "\n");
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
        System.out.println(currentPlayer.getName() + " Cards in hand: ");
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
        System.out.println("\nNext player has two new cards as penalty.\n");
        getNextPlayer().addCard(deck.drawCard());
        getNextPlayer().addCard(deck.drawCard());
        skipPlayerPenalty();
    }

    @Override
    protected void reverseTurnsPenalty() {
        System.out.println("\nReversing turns.\n");
        Collections.reverse(players);
        players.addFirst(players.removeLast());
    }

    @Override
    protected void skipPlayerPenalty() {
        System.out.println("\nSkipping next player.\n");
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
        while (!isValidInputColor(color)) {
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
    protected boolean isValidInputColor(String inputColor) {
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
            getNextPlayer().addCard(deck.drawCard());
        }
        skipPlayerPenalty();
    }

    private Player getNextPlayer() {
        return players.get(1);
    }

    @Override
    protected void createDeck() {
        List<Card> drawPile = new ArrayList<>(createNumberedCards());
        drawPile.addAll(createActionCards());
        drawPile.addAll(createWildCards());
        afterDeckCreationHook(drawPile);
        deck = new Deck(drawPile);
    }

    @Override
    protected List<Card> createNumberedCards() {
        List<Card> drawPile = new ArrayList<>(createNumberedCardsBasedOnColor(MainCardColors.getRedColor()));
        drawPile.addAll(createNumberedCardsBasedOnColor(MainCardColors.getBlueColor()));
        drawPile.addAll(createNumberedCardsBasedOnColor(MainCardColors.getGreenColor()));
        drawPile.addAll(createNumberedCardsBasedOnColor(MainCardColors.getYellowColor()));
        return drawPile;
    }

    @Override
    protected List<Card> createNumberedCardsBasedOnColor(List<CardColor> color) {
        List<Card> drawPile = new ArrayList<>();
        drawPile.add(new Card(color, String.valueOf(0)));
        for (int i = 1; i < 10; i++) {
            drawPile.add(new Card(color, String.valueOf(i)));
            drawPile.add(new Card(color, String.valueOf(i)));
        }
        return drawPile;
    }

    @Override
    protected List<Card> createActionCards() {
        List<Card> drawPile = new ArrayList<>(createCards("SKIP"));
        drawPile.addAll(createCards("REVERSE"));
        drawPile.addAll(createCards("DRAW TWO"));
        return drawPile;
    }

    @Override
    protected List<Card> createCards(String symbol) {
        List<Card> drawPile = new ArrayList<>(createTwoCards(MainCardColors.getRedColor(), symbol));
        drawPile.addAll(createTwoCards(MainCardColors.getBlueColor(), symbol));
        drawPile.addAll(createTwoCards(MainCardColors.getGreenColor(), symbol));
        drawPile.addAll(createTwoCards(MainCardColors.getYellowColor(), symbol));
        return drawPile;
    }

    @Override
    protected List<Card> createTwoCards(List<CardColor> color, String symbol) {
        List<Card> drawPile = new ArrayList<>();
        drawPile.add(new Card(color, symbol));
        drawPile.add(new Card(color, symbol));
        return drawPile;
    }

    @Override
    protected List<Card> createWildCards() {
        List<Card> drawPile = new ArrayList<>(createFourWildCards("WILD"));
        drawPile.addAll(createFourWildCards("WILD DRAW FOUR"));
        return drawPile;
    }

    @Override
    protected List<Card> createFourWildCards(String symbol) {
        List<CardColor> colorList = new ArrayList<>(createWildCardsColorList());
        List<Card> drawPile = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            drawPile.add(new Card(colorList, symbol));
        }
        return drawPile;
    }

    @Override
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
        for (Player player : players) {
            player.getCardsInHand().clear();
        }
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
        Player winnerPlayer = getWinnerPlayer();
        if (isEndOfRound()) {
            for (Player player : players) {
                List<Card> cardsInHand = player.getCardsInHand();
                for (Card card : cardsInHand) {
                    winnerPlayer.setScore(winnerPlayer.getScore() + valueOfCard(card));
                }
            }
        }
    }

    @Override
    protected Player getWinnerPlayer() {
        for (Player player : players) {
            if (player.hasNoCards()) {
                return player;
            }
        }
        throw new RuntimeException("No winning player found.");
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

    @Override
    protected boolean isActionCard(Card card) {
        return (card.getSymbol().equals("SKIP") || card.getSymbol().equals("REVERSE") || card.getSymbol().equals("DRAW TWO"));
    }

    @Override
    protected boolean isWildCard(Card card) {
        return (card.getSymbol().equals("WILD") || card.getSymbol().equals("WILD DRAW FOUR"));
    }

    @Override
    protected void sortPlayers() {
        players.sort(Comparator.comparing(Player::getName));
    }
    @Override
    protected Player getCurrentPlayer() {
        return players.getFirst();
    }

    @Override
    protected int getWinningScore() {
        return 500;
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
    protected void checkEndOfGame() {
        for (Player player : players) {
            if (player.getScore() >= getWinningScore()) {
                endGame();
            }
        }
    }
}