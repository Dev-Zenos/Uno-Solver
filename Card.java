import java.lang.String;

public class Card {
    private String color;
    private String name;
    private int number;
    private int elo;

    public Card(String color, int number, int elo) {
        this.color = color;
        this.number = number;
        this.elo = elo;
        name = color + " " + toCard(number);

    }

    public void setElo(int elo) {
        this.elo = elo;
    }

    public String getColor() {
        return color;
    }

    public int getNumber() {
        return number;
    }

    public int getElo() {
        return elo;
    }

    public String toString() {
        String str = String.format("Color: %s, Name: %s, Number: %s, Elo: %s\n", color, name, number, elo);
        return str;
    }

    private String toCard(float num) {
        if (num == 0) {
            return "0";
        } else if (num == 1) {
            return "1";
        } else if (num == 2) {
            return "2";
        } else if (num == 3) {
            return "3";
        } else if (num == 4) {
            return "4";
        } else if (num == 5) {
            return "5";
        } else if (num == 6) {
            return "6";
        } else if (num == 7) {
            return "7";
        } else if (num == 8) {
            return "8";
        } else if (num == 9) {
            return "9";
        } else if (num == 10) {
            return "Reverse";
        } else if (num == 11) {
            return "Skip";
        } else if (num == 12) {
            return "Draw 2";
        } else if (num == 13) {
            return "Wild";
        } else if (num == 14) {
            return "Wild Draw 4";
        } else {
            return "Invalid";
        }
    }
}
