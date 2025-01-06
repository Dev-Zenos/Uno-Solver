import java.util.ArrayList;

public class UnoElo {

    public ArrayList<Card> getValidCards(Card mainCard, ArrayList<Card> deck) {
        ArrayList<Card> validCards = new ArrayList<Card>();
        for (Card card : deck) {
            if (card.getColor().equals(mainCard.getColor()) || card.getNumber() == mainCard.getNumber()
                    || card.getColor().equals("Black")) {
                validCards.add(card);
            }
        }
        return validCards;
    }

}
