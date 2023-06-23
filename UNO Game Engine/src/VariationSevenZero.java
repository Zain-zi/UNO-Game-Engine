import java.util.LinkedList;
import java.util.List;

public class VariationSevenZero extends OfficialUNOGame {
    @Override
    protected int getNumberOfDealtCards() {
        return 3;
    }
    @Override
    protected int getWinningScore() {
        return 50;
    }
    @Override
    protected void applyRules(Card card) {
        if (isZeroCard(card)) {
            applyZeroCardRule();
        } else if (isSevenCard(card)) {
            applySevenCardRule();
        } else {
            super.applyRules(card);
        }
    }

    protected void applySevenCardRule() {
        System.out.println("A seven card was played. \nChoose a valid player to swap hands with: ");
        String playerToSwapWith = scanner.nextLine();
        for (Player player : players) {
            if (player.getName().equals(playerToSwapWith) && !player.getName().equals(getCurrentPlayer().getName())) {
                List<Card> tempHand = player.getCardsInHand();
                player.setCardsInHand(getCurrentPlayer().getCardsInHand());
                getCurrentPlayer().setCardsInHand(tempHand);
                return;
            }
        }
        System.out.println("Player name is not valid. Try again.");
        applySevenCardRule();
    }

    protected boolean isSevenCard(Card card) {
        return (card.getSymbol().equals("7"));
    }

    protected void applyZeroCardRule() {
        System.out.println("A zero card was played. \nAll cards will be swapped.");
        LinkedList<List<Card>> allHands = new LinkedList<>();
        for (Player player : players) {
            allHands.add(player.getCardsInHand());
        }
        allHands.addFirst(allHands.removeLast());
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setCardsInHand(allHands.get(i));
        }
    }

    protected boolean isZeroCard(Card card) {
        return (card.getSymbol().equals("0"));
    }
}
