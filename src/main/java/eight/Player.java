package eight;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Player {
    private static final Logger logger = LoggerFactory.getLogger(MainInterface.class);

    Image cardspic;
    MainInterface main;
    Graphics g;
    Hand hand;

    // click detection box for cards in hand
    Rectangle cardBox[];

    Player(MainInterface mi, Image cardspic, Graphics g) {
        this.cardspic = cardspic;
        this.main = mi;
        this.g = g;
        this.hand = mi.getMyHand();
    }

    public void initHandCard(Card[] cards) {
        hand.initCard(cards);
    }

    public void addHandCard(Card card) {
        hand.addCard(card);

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
        logger.info("display table 0 ");
        hand.showHand();
        main.repaint();
    }

    public void cardSelection(int selection) {

    }

}
