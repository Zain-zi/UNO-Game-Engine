import java.util.List;

public class Card {
    public static final String UNIQUE = "UNIQUE";
    private final List<CardColor> cardColor;
    private final String cardSymbol;

    public Card(List<CardColor> cardColor, String cardSymbol) {
        this.cardColor = cardColor;
        this.cardSymbol = cardSymbol;
    }

    public List<CardColor> getColor() {
        return cardColor;
    }

    public String getSymbol() {
        return cardSymbol;
    }

    public String getNotation() {
        return reformatColorNotation() + cardSymbol;
    }

    private String reformatColorNotation() {
        if (cardColor.size() >= 4) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (CardColor color : cardColor) {
            stringBuilder.append(color.toString()).append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.append(" ").toString();
    }

    @Override
    public String toString() {
        return getNotation();
    }
}
