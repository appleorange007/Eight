package eight;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Player {
    private static final Logger logger = LoggerFactory.getLogger(MainInterface.class);
    private List<Card> handCards;
    private List<Card[]> frontCards;
    private List<Card> abandonCards;
    Image cardspic;
    MainInterface main;
    Graphics g;

    Player(Image cardspic, MainInterface mi, Graphics g) {
        this.cardspic = cardspic;
        this.main = mi;
        this.g = g;

    }

    public void initHandCard(Card[] cards) {
        handCards = Arrays.asList(cards);
    }

    public void addHandCard(Card card) {
        if (null == handCards) {
            handCards = new ArrayList<Card>();
        }
        handCards.add(card);
    }

    public void displayTable() {
        g.setColor(Color.black);
        g.fillRect(0, 0, 450, 550);
        g.setColor(Color.white);
        g.drawLine(0, 450, 450, 450);
        g.setColor(Color.red);
        g.drawRoundRect(355, 5, 90, 40, 15, 15);
        g.setColor(Color.white);
        g.drawRoundRect(5, 360, 90, 40, 15, 15);
        g.drawRoundRect(5, 5, 90, 40, 15, 15);
        g.drawRoundRect(355, 360, 90, 40, 15, 15);
        logger.info("display Card 0 ");
        handCards.get(0).drawCard(100, 100);
        main.repaint();
    }

    public String displayAllHandCardStr() {
        String cardStr = "";
        for (Card card : handCards) {
            cardStr = cardStr + card.getNumber() + " ";
        }
        return cardStr;
    }

}
