import java.util.List;

public class Player {
    private List<Card> cardsInHand;
    private final String name;
    private boolean UNO;
    private int score;

    public Player(String name) {
        this.name = name;
    }

    public List<Card> getCardsInHand() {
        return cardsInHand;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }


    public void setScore(int score) {
        this.score = score;
    }

    public void addCard(Card card) {
        if (isUNO()) { // move it to implementation, in case developer wants to change the calling UNO rule
            UNO = false;
        }
        cardsInHand.add(card);
    }

    public Card pickCardFromHand(String cardNotation) {
        for (Card card : cardsInHand) {
            if (card.getNotation().equals(cardNotation)) {
                return card;
            }
        }
        throw new RuntimeException("Player doesn't have this card.");
    }

    public void removeCard(Card card) {
        if (hasCard(card)) {
            cardsInHand.remove(card);
        } else {
            throw new RuntimeException("Player doesn't have this card.");
        }
    }

    public boolean hasCard(Card card) {
        return cardsInHand.contains(card);
    }

    public boolean hasNoCards() {
        return cardsInHand.isEmpty();
    }

    public void calledUNO() {
        UNO = true;
    }

    public boolean isUNO() {
        return UNO;
    }

}
