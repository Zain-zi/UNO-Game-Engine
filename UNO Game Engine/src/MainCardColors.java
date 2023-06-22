import java.util.ArrayList;
import java.util.List;

public enum MainCardColors implements CardColor {
    RED, BLUE, GREEN, YELLOW;

    public static List<CardColor> getAllMainColors() {
        List<CardColor> allMainColors = new ArrayList<>();
        allMainColors.add(MainCardColors.RED);
        allMainColors.add(MainCardColors.BLUE);
        allMainColors.add(MainCardColors.GREEN);
        allMainColors.add(MainCardColors.YELLOW);
        return allMainColors;
    }

    public static List<CardColor> getRedColor() {
        List<CardColor> redColor = new ArrayList<>();
        redColor.add(MainCardColors.RED);
        return redColor;
    }

    public static List<CardColor> getBlueColor() {
        List<CardColor> blueColor = new ArrayList<>();
        blueColor.add(MainCardColors.BLUE);
        return blueColor;
    }

    public static List<CardColor> getGreenColor() {
        List<CardColor> greenColor = new ArrayList<>();
        greenColor.add(MainCardColors.GREEN);
        return greenColor;
    }

    public static List<CardColor> getYellowColor() {
        List<CardColor> yellowColor = new ArrayList<>();
        yellowColor.add(MainCardColors.YELLOW);
        return yellowColor;
    }
}
