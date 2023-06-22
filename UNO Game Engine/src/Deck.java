import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final List<Card> drawPile;
    private final List<Card> discardPile = new ArrayList<>();

    public Deck(List<Card> drawPile) {
        this.drawPile = drawPile;
    }

    public Card drawCard() {
        if (drawPile.isEmpty()) {
            updateDrawPile();
        }
        return drawPile.remove(0);
    }

    public void updateDrawPile() {
        Card topCard = discardPile.remove(discardPile.size() - 1);
        while(!discardPile.isEmpty()) {
            if(getTopCard().getSymbol().equals(Card.UNIQUE)) {
                discardPile.remove(discardPile.size() - 1);
            } else {
                drawPile.add(discardPile.remove(discardPile.size() - 1));
            }
        }
        discardPile.add(topCard);
        Collections.shuffle(drawPile);
    }
    public void addToDiscardPile(Card card) {
        discardPile.add(card);
    }

    public List<Card> getDrawPile() {
        return drawPile;
    }

    public List<Card> getDiscardPile() {
        return discardPile;
    }

    public Card getTopCard() {
        return discardPile.get(discardPile.size() - 1);
    }
}
