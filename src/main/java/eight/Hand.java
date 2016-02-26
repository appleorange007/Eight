package eight;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Hand {

    // click detection box for cards in hand
    Rectangle cardBox[];

    // click detection box for cards on table
    Rectangle tableBox[] = new Rectangle[3];

    private List<Card> handCards;
    private List<Card[]> frontCards;
    private List<Card> abandonCards;

    MainInterface main;
    Image back;
    Graphics g;

    public Hand(MainInterface mainI, Image back, Graphics g) {
        this.main = mainI;
        this.back = back;
        this.g = g;

        tableBox[0] = new Rectangle(103, 350, 71, 96);
        tableBox[1] = new Rectangle(188, 350, 71, 96);
        tableBox[2] = new Rectangle(276, 350, 71, 96);
    }

    public int mouseClick(int mouseX, int mouseY)
    {
        // Checking for click inside card
        for (int n = 0; n < Constant.PLAY_CARD_COUNT + 1; n++) {
            if (cardBox[n].contains(mouseX, mouseY)) {
                return n;
            }
        }
        return -1;
    }

    public int length()
    {
        return handCards.size();
    }

    public void initCard(Card[] cards) {
        this.handCards = Arrays.asList(cards);

        cardBox = new Rectangle[cards.length];
        for (int i = 0; i < cards.length; i++) {
            int x = 10 + i * Constant.WIDTH;
            int y = 20;
            cardBox[i] = new Rectangle(x, y, Constant.WIDTH, Constant.HEIGHT);
        }

        Collections.sort(handCards);
    }

    public void addCard(Card card) {
        if (null == handCards) {
            handCards = new ArrayList<Card>();
        }
        handCards.add(card);
    }

    public String displayAllHandCardStr() {
        String cardStr = "";
        for (Card card : handCards) {
            cardStr = cardStr + card.getNumber() + " ";
        }
        return cardStr;
    }

    public void showHand() {
        displayAllHandCardStr();

        for (int i = 0; i < handCards.size(); i++) {
            Card card = handCards.get(i);
            // System.out.println(cardBox[i].getX());
            card.drawCard((int) cardBox[i].getX(), (int) cardBox[i].getY());
        }

    }
}
