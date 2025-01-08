import java.util.ArrayList;

public class UnoElo {

    private ArrayList<Card> redCards = new ArrayList<Card>();
    private ArrayList<Card> blueCards = new ArrayList<Card>();
    private ArrayList<Card> greenCards = new ArrayList<Card>();
    private ArrayList<Card> yellowCards = new ArrayList<Card>();
    private ArrayList<Card> blackCards = new ArrayList<Card>();

    private int deckElo = 0;

    public void sortCards(ArrayList<Card> deck) {
        for (Card card : deck) {
            if (card.getColor().equals("Red")) {
                redCards.add(card);
            } else if (card.getColor().equals("Blue")) {
                blueCards.add(card);
            } else if (card.getColor().equals("Green")) {
                greenCards.add(card);
            } else if (card.getColor().equals("Yellow")) {
                yellowCards.add(card);
            } else if (card.getColor().equals("Black")) {
                blackCards.add(card);
            }
        }
    }

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

    public boolean isValidCard(Card mainCard, Card card) {
        if (card.getColor().equals(mainCard.getColor()) || card.getNumber() == mainCard.getNumber()
                || card.getColor().equals("Black")) {
            return true;
        }
        return false;
    }

    public ArrayList<Card> isSameNumber(ArrayList<Card> deck, Card card) {
        ArrayList<Card> sameNumber = new ArrayList<Card>();
        for (Card c : deck) {
            if (c.getNumber() == card.getNumber()) {
                sameNumber.add(c);
            }
        }
        return sameNumber;
    }

    public void calculateElo(Card mainCard, ArrayList<Card> deck) {
        deckElo = 0;
        for (Card card : deck) {
            if (isValidCard(mainCard, card)) {
                if (card.getNumber() < 10) {
                    deckElo += card.getNumber();
                }
            }
        }
        ArrayList<Card> sameNumber = isSameNumber(deck, new Card(mainCard.getColor(), 10, 0)); // Reverse
        deckElo += sameNumber.size() * 10;

        sameNumber = isSameNumber(deck, new Card(mainCard.getColor(), 11, 0)); // Skip
        deckElo += sameNumber.size() * 11;

        sameNumber = isSameNumber(deck, new Card(mainCard.getColor(), 12, 0)); // +2
        deckElo += sameNumber.size() * 12;

    }

}
