import java.util.ArrayList;
import java.util.List;

public class Player {
    private final List<Card> cardsInHand = new ArrayList<>();
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
        UNO = false;
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

    public boolean hasCard(String cardNotation) {
        for (Card card : cardsInHand) {
            if (card.getNotation().equals(cardNotation)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasNoCards() {
        return cardsInHand.isEmpty();
    }

    public void setUNO() {
        UNO = true;
    }

    public boolean hasCalledUNO() {
        return UNO;
    }

}
